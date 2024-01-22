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

package com.radixdlt.sync;

import static com.radixdlt.utils.TypedMocks.rmock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableList;
import com.radixdlt.consensus.LedgerHashes;
import com.radixdlt.consensus.LedgerHeader;
import com.radixdlt.consensus.TimestampedECDSASignatures;
import com.radixdlt.consensus.bft.Round;
import com.radixdlt.crypto.HashUtils;
import com.radixdlt.environment.RemoteEventDispatcher;
import com.radixdlt.ledger.LedgerExtension;
import com.radixdlt.ledger.LedgerProofBundle;
import com.radixdlt.ledger.LedgerUpdate;
import com.radixdlt.monitoring.MetricsInitializer;
import com.radixdlt.p2p.NodeId;
import com.radixdlt.p2p.PeersView;
import com.radixdlt.rev2.REv2ToConsensus;
import com.radixdlt.statecomputer.commit.LedgerProof;
import com.radixdlt.statecomputer.commit.LedgerProofOrigin;
import com.radixdlt.sync.messages.remote.LedgerStatusUpdate;
import com.radixdlt.sync.messages.remote.StatusResponse;
import com.radixdlt.sync.messages.remote.SyncRequest;
import com.radixdlt.sync.messages.remote.SyncResponse;
import com.radixdlt.utils.PrivateKeys;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class RemoteSyncServiceTest {

  private RemoteSyncService processor;
  private PeersView peersView;
  private LocalSyncService localSyncService;
  private TransactionsAndProofReader reader;
  private RemoteEventDispatcher<NodeId, StatusResponse> statusResponseDispatcher;
  private RemoteEventDispatcher<NodeId, SyncResponse> syncResponseDispatcher;
  private RemoteEventDispatcher<NodeId, LedgerStatusUpdate> statusUpdateDispatcher;

  @Before
  public void setUp() {
    this.peersView = mock(PeersView.class);
    this.localSyncService = mock(LocalSyncService.class);
    this.reader = mock(TransactionsAndProofReader.class);
    this.statusResponseDispatcher = rmock(RemoteEventDispatcher.class);
    this.syncResponseDispatcher = rmock(RemoteEventDispatcher.class);
    this.statusUpdateDispatcher = rmock(RemoteEventDispatcher.class);

    final var initialLatestProof = mock(LedgerProofBundle.class);
    when(initialLatestProof.resultantStateVersion()).thenReturn(1L);

    this.processor =
        new RemoteSyncService(
            peersView,
            localSyncService,
            reader,
            statusResponseDispatcher,
            syncResponseDispatcher,
            statusUpdateDispatcher,
            new SyncRelayConfig(5000L, 10, 5000L, 10, 50),
            new MetricsInitializer().initialize(),
            initialLatestProof);
  }

  @Test
  public void when_remote_sync_request__then_process_it() {
    SyncRequest request = mock(SyncRequest.class);
    LedgerProofSyncDto proofDto = mock(LedgerProofSyncDto.class);
    LedgerHeader header = mock(LedgerHeader.class);
    when(header.getStateVersion()).thenReturn(2L);
    when(proofDto.getLedgerHeader()).thenReturn(header);
    when(request.getStartProofExclusive()).thenReturn(proofDto);
    NodeId node = mock(NodeId.class);
    LedgerExtension ledgerExtension = mock(LedgerExtension.class);
    when(ledgerExtension.proof())
        .thenReturn(
            new LedgerProof(
                REv2ToConsensus.ledgerHeader(
                    LedgerHeader.create(1L, Round.epochInitial(), 1L, LedgerHashes.zero(), 0L, 0L)),
                new LedgerProofOrigin.Consensus(HashUtils.zero256(), List.of())));
    when(reader.getTransactions(anyLong())).thenReturn(ledgerExtension);
    processor.syncRequestEventProcessor().process(node, SyncRequest.create(proofDto));
    verify(syncResponseDispatcher, times(1)).dispatch(eq(node), any());
  }

  @Test(expected = NullPointerException.class)
  public void when_bad_remote_sync_request__then_throw_NPE() {
    var node = mock(NodeId.class);
    var ledgerExtension = mock(LedgerExtension.class);
    var verifiedHeader = mock(LedgerProof.class);
    when(ledgerExtension.proof()).thenReturn(verifiedHeader);
    when(reader.getTransactions(anyLong())).thenReturn(ledgerExtension);

    processor.syncRequestEventProcessor().process(node, SyncRequest.create(null));
    verify(syncResponseDispatcher, times(1)).dispatch(eq(node), any());
  }

  @Test
  public void when_remote_sync_request_and_unable__then_dont_do_anything() {
    SyncRequest request = mock(SyncRequest.class);
    LedgerProofSyncDto header = mock(LedgerProofSyncDto.class);
    when(header.getOpaque()).thenReturn(HashUtils.zero256());
    when(header.getLedgerHeader()).thenReturn(mock(LedgerHeader.class));
    when(header.getSignatures()).thenReturn(mock(TimestampedECDSASignatures.class));
    when(request.getStartProofExclusive()).thenReturn(header);
    processor
        .syncRequestEventProcessor()
        .process(
            NodeId.fromPublicKey(PrivateKeys.ofNumeric(1).getPublicKey()),
            SyncRequest.create(header));
    verify(syncResponseDispatcher, never()).dispatch(any(NodeId.class), any());
  }

  @Test
  public void when_remote_sync_request_and_null_return__then_dont_do_anything() {
    LedgerProofSyncDto header = mock(LedgerProofSyncDto.class);
    when(header.getOpaque()).thenReturn(HashUtils.zero256());
    when(header.getLedgerHeader()).thenReturn(mock(LedgerHeader.class));
    when(header.getSignatures()).thenReturn(mock(TimestampedECDSASignatures.class));
    processor
        .syncRequestEventProcessor()
        .process(
            NodeId.fromPublicKey(PrivateKeys.ofNumeric(1).getPublicKey()),
            SyncRequest.create(header));
    when(reader.getTransactions(anyLong())).thenReturn(null);
    verify(syncResponseDispatcher, never()).dispatch(any(NodeId.class), any());
  }

  @Test
  public void when_ledger_update_but_syncing__then_dont_send_status_update() {
    final var proof = mock(LedgerProof.class);
    final var ledgerUpdate = mock(LedgerUpdate.class);
    when(proof.stateVersion()).thenReturn(2L);
    final LedgerProofBundle latestProofs = mock(LedgerProofBundle.class);
    when(latestProofs.primaryProof()).thenReturn(proof);
    when(ledgerUpdate.committedProof()).thenReturn(latestProofs);

    when(this.localSyncService.getSyncState())
        .thenReturn(
            SyncState.SyncingState.init(
                mock(LedgerProof.class),
                ImmutableList.of(),
                mock(com.radixdlt.statecomputer.commit.LedgerHeader.class)));

    this.processor.ledgerUpdateEventProcessor().process(ledgerUpdate);

    verifyNoMoreInteractions(peersView);
    verifyNoMoreInteractions(statusUpdateDispatcher);
  }
}
