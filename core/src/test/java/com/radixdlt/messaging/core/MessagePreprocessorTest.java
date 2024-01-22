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

package com.radixdlt.messaging.core;

import static com.radixdlt.lang.Tuple.tuple;
import static com.radixdlt.utils.SerializerTestDataGenerator.randomProposal;
import static com.radixdlt.utils.SerializerTestDataGenerator.randomVote;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Streams;
import com.google.common.hash.HashCode;
import com.radixdlt.addressing.Addressing;
import com.radixdlt.consensus.HighQC;
import com.radixdlt.crypto.ECKeyPair;
import com.radixdlt.lang.Tuple.Tuple2;
import com.radixdlt.messaging.consensus.ConsensusEventMessage;
import com.radixdlt.messaging.consensus.GetVerticesErrorResponseMessage;
import com.radixdlt.messaging.consensus.GetVerticesRequestMessage;
import com.radixdlt.messaging.consensus.GetVerticesResponseMessage;
import com.radixdlt.messaging.ledgersync.LedgerStatusUpdateMessage;
import com.radixdlt.messaging.ledgersync.StatusResponseMessage;
import com.radixdlt.messaging.ledgersync.SyncRequestMessage;
import com.radixdlt.messaging.ledgersync.SyncResponseMessage;
import com.radixdlt.messaging.mempool.MempoolAddMessage;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.monitoring.MetricsInitializer;
import com.radixdlt.networks.Network;
import com.radixdlt.p2p.NodeId;
import com.radixdlt.p2p.PeerControl;
import com.radixdlt.p2p.capability.Capabilities;
import com.radixdlt.serialization.DefaultSerialization;
import com.radixdlt.serialization.DsonOutput;
import com.radixdlt.serialization.Serialization;
import com.radixdlt.sync.LedgerExtensionSyncDto;
import com.radixdlt.sync.LedgerProofSyncDto;
import com.radixdlt.sync.LedgerProofSyncStatusDto;
import com.radixdlt.utils.Compress;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MessagePreprocessorTest {
  // Classes which have no zero-parameter constructors are excluded:
  // GetPeersMessage PeerPingMessage PeerPongMessage StatusRequestMessage

  // Classes which are valid regardless from constructor parameters are excluded
  // PeersResponseMessage

  @SuppressWarnings("unchecked")
  private static final List<Tuple2<Message, String>> TEST_VECTORS =
      List.of(
          tuple(
              new GetVerticesErrorResponseMessage(
                  mock(HighQC.class), mock(GetVerticesRequestMessage.class)),
              "highQC"),
          tuple(
              new GetVerticesErrorResponseMessage(
                  mock(HighQC.class), mock(GetVerticesRequestMessage.class)),
              "request"),
          tuple(new GetVerticesRequestMessage(mock(HashCode.class), 1), "vertexId"),
          tuple(new GetVerticesResponseMessage(mock(List.class)), "vertices"),
          tuple(new LedgerStatusUpdateMessage(mock(LedgerProofSyncStatusDto.class)), "proof"),
          tuple(new MempoolAddMessage(mock(List.class)), "txns"),
          tuple(new StatusResponseMessage(mock(LedgerProofSyncStatusDto.class)), "proof"),
          tuple(new SyncRequestMessage(mock(LedgerProofSyncDto.class)), "startProofExclusive"),
          tuple(new SyncResponseMessage(mock(LedgerExtensionSyncDto.class)), "ledgerExtension"));

  private static final Serialization SERIALIZATION = DefaultSerialization.getInstance();

  private final Metrics metrics = new MetricsInitializer().initialize();
  private final MessageCentralConfiguration config = mock(MessageCentralConfiguration.class);
  private final PeerControl peerControl = mock(PeerControl.class);
  private final MessagePreprocessor messagePreprocessor =
      new MessagePreprocessor(
          metrics,
          config,
          System::currentTimeMillis,
          SERIALIZATION,
          () -> peerControl,
          Addressing.ofNetwork(Network.INTEGRATIONTESTNET),
          Capabilities.testingDefault());

  private final InboundMessage inboundMessage;

  public MessagePreprocessorTest(InboundMessage inboundMessage) {
    this.inboundMessage = inboundMessage;
  }

  @Parameters
  public static Collection<InboundMessage> testParameters() {
    var consensusMessages =
        Stream.of(
            prepareTestMessage(new ConsensusEventMessage(randomProposal()), "proposal"),
            prepareTestMessage(new ConsensusEventMessage(randomProposal()), "vote", randomVote()),
            prepareTestMessage(new ConsensusEventMessage(randomVote()), "vote"),
            prepareTestMessage(
                new ConsensusEventMessage(randomVote()), "proposal", randomProposal()));

    return Streams.concat(
            TEST_VECTORS.stream()
                .map(tuple -> tuple.map(MessagePreprocessorTest::prepareTestMessage)),
            consensusMessages)
        .toList();
  }

  private static InboundMessage prepareTestMessage(Message message, String field) {
    return prepareTestMessage(message, field, null);
  }

  private static InboundMessage prepareTestMessage(Message message, String field, Object value) {
    var source = NodeId.fromPublicKey(ECKeyPair.generateNew().getPublicKey());
    var inboundMessage = generateMessage(source, message, field, value);

    return inboundMessage;
  }

  private static InboundMessage generateMessage(
      NodeId source, Message message, String field, Object value) {
    try {
      setField(message, field, value);
    } catch (Exception e) {
      fail(
          "Unable to set field %s for message of %s because of %s"
              .formatted(field, message.getClass(), e));

      throw new RuntimeException("unreachable"); // tame compiler
    }

    return new InboundMessage(1L, source, serialize(message));
  }

  private static byte[] serialize(Message message) {
    try {
      return Compress.compress(SERIALIZATION.toDson(message, DsonOutput.Output.WIRE));
    } catch (IOException e) {
      fail(
          "Unable to serialize message of type "
              + message.getClass()
              + " because of "
              + e.getMessage());
      throw new RuntimeException("unreachable"); // tame compiler
    }
  }

  private static void setField(Object instance, String fieldName, Object toSet) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);

    // In Java 17, the security manager is deprecated and by default disabled,
    // so we can just change the field accessibility.
    field.setAccessible(true);

    field.set(instance, toSet);
  }

  @Test
  public void invalid_message_is_not_accepted_and_peer_is_banned() {
    var result = messagePreprocessor.process(inboundMessage);

    assertFalse(result.isSuccess());

    verify(peerControl)
        .banPeer(eq(inboundMessage.source()), eq(Duration.ofMinutes(5)), anyString());
  }
}
