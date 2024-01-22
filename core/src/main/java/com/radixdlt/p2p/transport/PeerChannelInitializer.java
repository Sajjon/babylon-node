/* Copyright 2021 Radix Publishing Ltd incorporated in Jersey (Channel Islands).
 *
 * Licensed under the Radix License, Version 1.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at:
 *
 * radixfoundation.org/licenses/LICENSE-v1
 *
 * The Licensor hereby grants permission for the Canonical version of the Work to be
 * published, distributed and used under or by reference to the Licensor’s trademark
 * Radix ® and use of any unregistered trade names, logos or get-up.
 *
 * The Licensor provides the Work (and each Contributor provides its Contributions) on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
 * including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT,
 * MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Whilst the Work is capable of being deployed, used and adopted (instantiated) to create
 * a distributed ledger it is your responsibility to test and validate the code, together
 * with all logic and performance of that code under all foreseeable scenarios.
 *
 * The Licensor does not make or purport to make and hereby excludes liability for all
 * and any representation, warranty or undertaking in any form whatsoever, whether express
 * or implied, to any entity or person, including any representation, warranty or
 * undertaking, as to the functionality security use, value or other characteristics of
 * any distributed ledger nor in respect the functioning or value of any tokens which may
 * be created stored or transferred using the Work. The Licensor does not warrant that the
 * Work or any use of the Work complies with any law or regulation in any territory where
 * it may be implemented or used or that it will be appropriate for any specific purpose.
 *
 * Neither the licensor nor any current or former employees, officers, directors, partners,
 * trustees, representatives, agents, advisors, contractors, or volunteers of the Licensor
 * shall be liable for any direct or indirect, special, incidental, consequential or other
 * losses of any kind, in tort, contract or otherwise (including but not limited to loss
 * of revenue, income or profits, or loss of use or data, or loss of reputation, or loss
 * of any economic or other opportunity of whatsoever nature or howsoever arising), arising
 * out of or in connection with (without limitation of any use, misuse, of any ledger system
 * or use made or its functionality or any performance or operation of any code or protocol
 * caused by bugs or programming or logic errors or otherwise);
 *
 * A. any offer, purchase, holding, use, sale, exchange or transmission of any
 * cryptographic keys, tokens or assets created, exchanged, stored or arising from any
 * interaction with the Work;
 *
 * B. any failure in a transmission or loss of any token or assets keys or other digital
 * artefacts due to errors in transmission;
 *
 * C. bugs, hacks, logic errors or faults in the Work or any communication;
 *
 * D. system software or apparatus including but not limited to losses caused by errors
 * in holding or transmitting tokens by any third-party;
 *
 * E. breaches or failure of security including hacker attacks, loss or disclosure of
 * password, loss of private key, unauthorised use or misuse of such passwords or keys;
 *
 * F. any losses including loss of anticipated savings or other benefits resulting from
 * use of the Work or any changes to the Work (however implemented).
 *
 * You are solely responsible for; testing, validating and evaluation of all operation
 * logic, functionality, security and appropriateness of using the Work for any commercial
 * or non-commercial purpose and for any reproduction or redistribution by You of the
 * Work. You assume all risks associated with Your use of the Work and the exercise of
 * permissions under this License.
 */

package com.radixdlt.p2p.transport;

import com.radixdlt.addressing.Addressing;
import com.radixdlt.crypto.ECKeyOps;
import com.radixdlt.environment.EventDispatcher;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.networks.Network;
import com.radixdlt.p2p.P2PConfig;
import com.radixdlt.p2p.PeerEvent;
import com.radixdlt.p2p.RadixNodeUri;
import com.radixdlt.p2p.capability.Capabilities;
import com.radixdlt.p2p.transport.logging.LogSink;
import com.radixdlt.p2p.transport.logging.LoggingHandler;
import com.radixdlt.serialization.Serialization;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PeerChannelInitializer extends ChannelInitializer<SocketChannel> {
  private static final Logger log = LogManager.getLogger();

  // Send/receive buffers for each open socket
  private static final int PER_SOCKET_RECEIVE_BUFFER_SIZE = 1024 * 1024;
  private static final int PER_SOCKET_SEND_BUFFER_SIZE = 1024 * 1024;

  private static final int SOCKET_BACKLOG_SIZE = 100;

  private static final int FRAME_HEADER_LENGTH = 4;

  private final P2PConfig config;
  private final Addressing addressing;
  private final Network network;
  private final String newestProtocolVersion;
  private final Metrics metrics;
  private final Serialization serialization;
  private final SecureRandom secureRandom;
  private final ECKeyOps ecKeyOps;
  private final EventDispatcher<PeerEvent> peerEventDispatcher;
  private final Optional<RadixNodeUri> uri;
  private final Capabilities capabilities;
  private final int maxMessageSize;

  public PeerChannelInitializer(
      P2PConfig config,
      Addressing addressing,
      Network network,
      String newestProtocolVersion,
      Metrics metrics,
      Serialization serialization,
      SecureRandom secureRandom,
      ECKeyOps ecKeyOps,
      EventDispatcher<PeerEvent> peerEventDispatcher,
      Optional<RadixNodeUri> uri,
      Capabilities capabilities,
      int maxMessageSize) {
    this.config = Objects.requireNonNull(config);
    this.addressing = Objects.requireNonNull(addressing);
    this.network = network;
    this.newestProtocolVersion = newestProtocolVersion;
    this.metrics = Objects.requireNonNull(metrics);
    this.serialization = Objects.requireNonNull(serialization);
    this.secureRandom = Objects.requireNonNull(secureRandom);
    this.ecKeyOps = Objects.requireNonNull(ecKeyOps);
    this.peerEventDispatcher = Objects.requireNonNull(peerEventDispatcher);
    this.uri = Objects.requireNonNull(uri);
    this.capabilities = capabilities;
    this.maxMessageSize = maxMessageSize;
  }

  @Override
  protected void initChannel(SocketChannel socketChannel) {
    metrics.networking().channelsInitialized().inc();

    final var socketChannelConfig = socketChannel.config();
    socketChannelConfig.setReceiveBufferSize(PER_SOCKET_RECEIVE_BUFFER_SIZE);
    socketChannelConfig.setSendBufferSize(PER_SOCKET_SEND_BUFFER_SIZE);
    socketChannelConfig.setOption(ChannelOption.SO_BACKLOG, SOCKET_BACKLOG_SIZE);

    if (log.isDebugEnabled()) {
      socketChannel.pipeline().addLast(new LoggingHandler(LogSink.using(log), false));
    }

    uri.ifPresent(u -> log.trace("Initializing peer channel to {}", u));

    if (uri.isEmpty() && this.config.useProxyProtocol()) {
      /* If the node is configured to support the PROXY protocol, we add a dedicated
      pipeline that decodes a single header packet (with host's real ip address and port), and then
      replaces this one-time pipeline with the main one (which forwards to PeerChannel). */
      createProxyProtocolPipeline(socketChannel);
    } else {
      createPeerChannelPipeline(socketChannel, socketChannel.remoteAddress());
    }
  }

  private void createProxyProtocolPipeline(SocketChannel socketChannel) {
    socketChannel
        .pipeline()
        .addLast("decode_proxy_header_line", new LineBasedFrameDecoder(255, true, true))
        .addLast("decode_proxy_header_bytes", new ByteArrayDecoder())
        .addLast("handle_proxy_header", new ProxyHeaderHandler(socketChannel))
        .addLast("exception_handler", new ExceptionHandler(metrics, Optional.empty()));
  }

  private final class ProxyHeaderHandler extends SimpleChannelInboundHandler<byte[]> {
    private final SocketChannel socketChannel;

    ProxyHeaderHandler(SocketChannel socketChannel) {
      this.socketChannel = socketChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws IOException {
      final var clientAddress = parseProxyHeader(new String(msg));

      // remove the proxy pipeline
      ctx.pipeline().remove(LineBasedFrameDecoder.class);
      ctx.pipeline().remove(ByteArrayDecoder.class);
      ctx.pipeline().remove(ProxyHeaderHandler.class);
      ctx.pipeline().remove(ExceptionHandler.class);

      // and create a regular peer channel pipeline
      createPeerChannelPipeline(socketChannel, clientAddress);
    }

    private InetSocketAddress parseProxyHeader(String line) throws IOException {
      /* The proxy protocol line is a single line that ends with a carriage return
      and line feed ("\r\n"), and has the following form:
      PROXY_STRING + single space + INET_PROTOCOL + single space + CLIENT_IP + single space
      + PROXY_IP + single space + CLIENT_PORT + single space + PROXY_PORT + "\r\n" */
      final var components = line.split(" ");

      if (!components[0].equals("PROXY") || !components[1].startsWith("TCP")) {
        log.warn("Received invalid PROXY protocol header line: {}", line);
        socketChannel.close();
        throw new IOException("Invalid PROXY header");
      }

      return InetSocketAddress.createUnresolved(components[2], Integer.parseInt(components[4]));
    }
  }

  private void createPeerChannelPipeline(
      SocketChannel socketChannel, InetSocketAddress remoteAddress) {
    final var peerChannel =
        new PeerChannel(
            config,
            addressing,
            network,
            newestProtocolVersion,
            metrics,
            serialization,
            secureRandom,
            ecKeyOps,
            peerEventDispatcher,
            uri,
            socketChannel,
            Optional.ofNullable(remoteAddress),
            capabilities);

    final var maxFrameLength = maxMessageSize + FrameCodec.FRAME_OVERHEAD;

    socketChannel
        .pipeline()
        .addLast(
            "unpack",
            new LengthFieldBasedFrameDecoder(
                maxFrameLength,
                0, // Frames begin with length prefix, so no offset
                FRAME_HEADER_LENGTH,
                0, // Length field doesn't include the length of itself, so no need to adjust for
                // that
                FRAME_HEADER_LENGTH,
                true /* Fail fast */))
        .addLast("handler", peerChannel)
        .addLast("pack", new LengthFieldPrepender(FRAME_HEADER_LENGTH))
        .addLast("exception_handler", new ExceptionHandler(metrics, Optional.of(peerChannel)));
  }
}
