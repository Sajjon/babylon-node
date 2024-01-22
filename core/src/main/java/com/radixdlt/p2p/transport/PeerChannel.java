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

import static com.radixdlt.lang.Unit.unitResult;
import static com.radixdlt.messaging.core.MessagingErrors.IO_ERROR;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.RateLimiter;
import com.radixdlt.addressing.Addressing;
import com.radixdlt.crypto.ECKeyOps;
import com.radixdlt.environment.EventDispatcher;
import com.radixdlt.lang.Cause;
import com.radixdlt.lang.Result;
import com.radixdlt.lang.Unit;
import com.radixdlt.messaging.core.InboundMessage;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.networks.Network;
import com.radixdlt.p2p.NodeId;
import com.radixdlt.p2p.P2PConfig;
import com.radixdlt.p2p.PeerEvent;
import com.radixdlt.p2p.PeerEvent.PeerConnected;
import com.radixdlt.p2p.PeerEvent.PeerDisconnected;
import com.radixdlt.p2p.PeerEvent.PeerHandshakeFailed;
import com.radixdlt.p2p.RadixNodeUri;
import com.radixdlt.p2p.capability.Capabilities;
import com.radixdlt.p2p.capability.RemotePeerCapability;
import com.radixdlt.p2p.transport.handshake.AuthHandshakeResult;
import com.radixdlt.p2p.transport.handshake.AuthHandshakeResult.AuthHandshakeError;
import com.radixdlt.p2p.transport.handshake.AuthHandshakeResult.AuthHandshakeSuccess;
import com.radixdlt.p2p.transport.handshake.AuthHandshaker;
import com.radixdlt.serialization.Serialization;
import com.radixdlt.utils.RateCalculator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.reactivex.rxjava3.core.BackpressureOverflowStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that manages TCP connection channel. It takes care of the initial handshake, creating the
 * frame and message codec and forwarding the messages to MessageCentral.
 */
@SuppressWarnings({"UnstableApiUsage", "OptionalUsedAsFieldOrParameterType"})
public final class PeerChannel extends SimpleChannelInboundHandler<ByteBuf> {
  private static final Logger log = LogManager.getLogger();

  private static final byte NETWORK_VERSION = 0x02; // Babylon, the 2nd network version

  enum ChannelState {
    INACTIVE,
    AUTH_HANDSHAKE,
    ACTIVE
  }

  private final Object lock = new Object();
  private final RateLimiter droppedMessagesRateLimiter = RateLimiter.create(1.0);
  private final PublishProcessor<InboundMessage> inboundMessageSink = PublishProcessor.create();
  private final Flowable<InboundMessage> inboundMessages;
  private final Metrics metrics;
  private final Addressing addressing;
  private final EventDispatcher<PeerEvent> peerEventDispatcher;
  private final Optional<RadixNodeUri> uri;
  private final AuthHandshaker authHandshaker;
  private final boolean isInitiator;
  private final SocketChannel nettyChannel;
  private Optional<InetSocketAddress> remoteAddress;

  private ChannelState state = ChannelState.INACTIVE;
  private NodeId remoteNodeId;
  private FrameCodec frameCodec;
  private Optional<String> remoteNewestForkName = Optional.empty();

  private final RateCalculator outMessagesStats = new RateCalculator(Duration.ofSeconds(10), 128);

  private Set<RemotePeerCapability> remotePeerCapabilities = Set.of();

  public PeerChannel(
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
      SocketChannel nettyChannel,
      Optional<InetSocketAddress> remoteAddress,
      Capabilities capabilities) {
    this.metrics = requireNonNull(metrics);
    this.addressing = requireNonNull(addressing);
    this.peerEventDispatcher = requireNonNull(peerEventDispatcher);
    this.uri = requireNonNull(uri);

    uri.map(RadixNodeUri::getNodeId).ifPresent(nodeId -> this.remoteNodeId = nodeId);

    this.authHandshaker =
        new AuthHandshaker(
            serialization,
            secureRandom,
            ecKeyOps,
            network,
            NETWORK_VERSION,
            newestProtocolVersion,
            capabilities);
    this.nettyChannel = requireNonNull(nettyChannel);
    this.remoteAddress = requireNonNull(remoteAddress);

    this.isInitiator = uri.isPresent();

    this.inboundMessages =
        inboundMessageSink.onBackpressureBuffer(
            config.channelBufferSize(),
            this::onInboundMessageBufferOverflow,
            BackpressureOverflowStrategy.DROP_LATEST);

    if (this.nettyChannel.isActive()) {
      this.init();
    }
  }

  private void onInboundMessageBufferOverflow() {
    this.metrics.networking().messagesDropped().inc();
    final var logLevel = droppedMessagesRateLimiter.tryAcquire() ? Level.WARN : Level.TRACE;
    if (log.isEnabled(logLevel)) {
      log.log(logLevel, "TCP msg buffer overflow, dropping msg on {}", this);
    }
  }

  private void initHandshake(NodeId remoteNodeId) {
    final var initiatePacketOrError = authHandshaker.initiate(remoteNodeId.getPublicKey());
    if (initiatePacketOrError.isSuccess()) {
      if (log.isTraceEnabled()) {
        log.trace("Sending auth initiate to {}", this);
      }
      this.write(Unpooled.wrappedBuffer(initiatePacketOrError.unwrap()));
    } else {
      finalizeFailedHandshake(initiatePacketOrError.unwrapError());
    }
  }

  public Flowable<InboundMessage> inboundMessages() {
    return inboundMessages;
  }

  private void handleHandshakeData(ByteBuf data) throws IOException {
    if (log.isTraceEnabled()) {
      log.trace("Auth {} from {}", this.isInitiator ? "response" : "initiate", this);
    }

    if (this.isInitiator) {
      final var handshakeResult = this.authHandshaker.handleResponseMessage(data);
      this.finalizeHandshake(handshakeResult);
    } else {
      final var result = this.authHandshaker.handleInitialMessage(data);
      this.write(Unpooled.wrappedBuffer(result.getFirst()));
      this.finalizeHandshake(result.getSecond());
    }
  }

  private void finalizeHandshake(AuthHandshakeResult handshakeResult) {
    switch (handshakeResult) {
      case AuthHandshakeSuccess successResult -> finalizeSuccessfulHandshake(successResult);
      case final AuthHandshakeError errorResult -> finalizeFailedHandshake(errorResult);
    }
  }

  private void finalizeSuccessfulHandshake(AuthHandshakeSuccess successResult) {
    this.remoteNodeId = successResult.remoteNodeId();
    this.frameCodec = new FrameCodec(successResult.secrets());
    this.remoteNewestForkName = successResult.newestForkName();
    this.state = ChannelState.ACTIVE;
    this.remotePeerCapabilities = successResult.remotePeerCapabilities();

    if (log.isTraceEnabled()) {
      log.trace("Successful auth handshake: {}", this);
    }

    peerEventDispatcher.dispatch(new PeerConnected(this));
  }

  private void finalizeFailedHandshake(AuthHandshakeError errorResult) {
    log.warn("Auth handshake failed on {}: {}", this, errorResult.msg());
    peerEventDispatcher.dispatch(new PeerHandshakeFailed(this));
    this.disconnect();
  }

  private void handleMessage(ByteBuf buf) throws IOException {
    final var receiveTime = System.nanoTime();

    synchronized (this.lock) {
      final var maybeFrame = this.frameCodec.tryReadSingleFrame(buf);
      maybeFrame.ifPresentOrElse(
          frame -> inboundMessageSink.onNext(new InboundMessage(receiveTime, remoteNodeId, frame)),
          () -> log.error("Failed to read a complete frame: {}", this));
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    // if we weren't able to determine peer's address earlier, it should be available now
    if (this.remoteAddress.isEmpty()) {
      this.remoteAddress = Optional.ofNullable(this.nettyChannel.remoteAddress());
    }

    if (this.state == ChannelState.INACTIVE) {
      this.init();
    }
  }

  private void init() {
    if (log.isTraceEnabled()) {
      log.trace("Init: {}", this);
    }
    this.state = ChannelState.AUTH_HANDSHAKE;
    if (this.isInitiator) {
      this.initHandshake(this.remoteNodeId);
    }
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
    switch (this.state) {
      case ACTIVE -> this.handleMessage(buf);
      case AUTH_HANDSHAKE -> this.handleHandshakeData(buf);
      case INACTIVE -> throw new IllegalStateException("Unexpected read on inactive channel");
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    if (log.isTraceEnabled()) {
      log.trace("Closed: {}", this);
    }
    final var prevState = this.state;
    this.state = ChannelState.INACTIVE;
    this.inboundMessageSink.onComplete();

    if (prevState == ChannelState.ACTIVE) {
      // only send out event if peer was previously active
      this.peerEventDispatcher.dispatch(new PeerDisconnected(this));
    }
  }

  private void write(ByteBuf data) {
    this.nettyChannel.writeAndFlush(data);
  }

  public Result<Unit, Cause> send(byte[] data) {
    synchronized (this.lock) {
      if (this.state != ChannelState.ACTIVE) {
        return IO_ERROR.result();
      } else {
        try {
          // we don't need to release the buffer manually as this is done by Netty (in
          // writeAndFlush)
          final var buf = PooledByteBufAllocator.DEFAULT.buffer(data.length);
          try (var out = new ByteBufOutputStream(buf)) {
            this.frameCodec.writeFrame(data, out);
          }
          this.write(buf);
          this.outMessagesStats.tick();
          return unitResult();
        } catch (IOException e) {
          return IO_ERROR.result();
        }
      }
    }
  }

  public long sentMessagesRate() {
    return this.outMessagesStats.currentRate();
  }

  public void disconnect() {
    synchronized (this.lock) {
      this.nettyChannel.close();
    }
  }

  /* Do NOT use this method for any production code.
   * It's only intended to be used for low-level p2p testing. */
  @VisibleForTesting
  public void unsafeRawWriteToChannel(byte[] data) throws IOException {
    final var buf = PooledByteBufAllocator.DEFAULT.buffer(data.length);
    try (var out = new ByteBufOutputStream(buf)) {
      out.write(data);
    }
    synchronized (lock) {
      this.write(buf);
    }
  }

  public NodeId getRemoteNodeId() {
    return this.remoteNodeId;
  }

  public boolean isInbound() {
    return !this.isInitiator;
  }

  public boolean isOutbound() {
    return this.isInitiator;
  }

  public Optional<RadixNodeUri> getUri() {
    return this.uri;
  }

  public String getHost() {
    return remoteAddress.map(InetSocketAddress::getHostString).orElse("?");
  }

  public int getPort() {
    return remoteAddress.map(InetSocketAddress::getPort).orElse(0);
  }

  public Optional<String> getRemoteNewestForkName() {
    return remoteNewestForkName;
  }

  public Set<RemotePeerCapability> getRemotePeerCapabilities() {
    return remotePeerCapabilities;
  }

  @Override
  public String toString() {
    return String.format(
        "{%s %s@%s:%s | %s}",
        isInitiator ? "<-" : "->",
        remoteNodeId != null ? addressing.encodeNodeAddress(this.remoteNodeId.getPublicKey()) : "?",
        getHost(),
        getPort(),
        state);
  }
}
