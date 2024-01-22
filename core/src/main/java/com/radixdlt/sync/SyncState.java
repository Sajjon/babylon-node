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

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.radixdlt.p2p.NodeId;
import com.radixdlt.statecomputer.commit.LedgerHeader;
import com.radixdlt.statecomputer.commit.LedgerProof;
import com.radixdlt.sync.messages.remote.StatusResponse;
import com.radixdlt.utils.Pair;
import java.util.Objects;
import java.util.Optional;

/**
 * The current state of the local sync service. There are 3 possible states: - idle: the service is
 * not waiting for any response; it only processes local ledger updates and sync requests - sync
 * check: the service is waiting for StatusResponses; it also processes local messages and timeouts
 * - syncing: the service is waiting for SyncResponse; it also processes local messages and timeouts
 */
public sealed interface SyncState {
  LedgerProof getLatestProof();

  SyncState withLatestProof(LedgerProof latestProof);

  final class IdleState implements SyncState {
    private final LedgerProof latestProof;

    public static IdleState init(LedgerProof latestProof) {
      return new IdleState(latestProof);
    }

    private IdleState(LedgerProof latestProof) {
      this.latestProof = latestProof;
    }

    @Override
    public LedgerProof getLatestProof() {
      return this.latestProof;
    }

    @Override
    public IdleState withLatestProof(LedgerProof latestProof) {
      return new IdleState(latestProof);
    }

    @Override
    public String toString() {
      return String.format(
          "%s{primaryProof=%s}", this.getClass().getSimpleName(), this.latestProof);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      IdleState idleState = (IdleState) o;
      return Objects.equals(latestProof, idleState.latestProof);
    }

    @Override
    public int hashCode() {
      return Objects.hash(latestProof);
    }
  }

  final class SyncCheckState implements SyncState {
    private final LedgerProof latestProof;
    private final ImmutableSet<NodeId> peersAskedForStatus;
    private final ImmutableMap<NodeId, StatusResponse> receivedStatusResponses;

    public static SyncCheckState init(
        LedgerProof latestProof, ImmutableSet<NodeId> peersAskedForStatus) {
      return new SyncCheckState(latestProof, peersAskedForStatus, ImmutableMap.of());
    }

    private SyncCheckState(
        LedgerProof latestProof,
        ImmutableSet<NodeId> peersAskedForStatus,
        ImmutableMap<NodeId, StatusResponse> receivedStatusResponses) {
      this.latestProof = latestProof;
      this.peersAskedForStatus = peersAskedForStatus;
      this.receivedStatusResponses = receivedStatusResponses;
    }

    public boolean hasAskedPeer(NodeId peer) {
      return this.peersAskedForStatus.contains(peer);
    }

    public boolean receivedResponseFrom(NodeId peer) {
      return this.receivedStatusResponses.containsKey(peer);
    }

    public boolean gotAllResponses() {
      return this.receivedStatusResponses.size() == this.peersAskedForStatus.size();
    }

    public ImmutableMap<NodeId, StatusResponse> responses() {
      return this.receivedStatusResponses;
    }

    public SyncCheckState withStatusResponse(NodeId peer, StatusResponse statusResponse) {
      return new SyncCheckState(
          latestProof,
          peersAskedForStatus,
          new ImmutableMap.Builder<NodeId, StatusResponse>()
              .putAll(receivedStatusResponses)
              .put(peer, statusResponse)
              .build());
    }

    @Override
    public LedgerProof getLatestProof() {
      return this.latestProof;
    }

    @Override
    public SyncCheckState withLatestProof(LedgerProof latestProof) {
      return new SyncCheckState(latestProof, peersAskedForStatus, receivedStatusResponses);
    }

    @Override
    public String toString() {
      return String.format(
          "%s{primaryProof=%s peersAskedForStatus=%s receivedStatusResponses=%s}",
          this.getClass().getSimpleName(),
          this.latestProof,
          this.peersAskedForStatus,
          this.receivedStatusResponses);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SyncCheckState that = (SyncCheckState) o;
      return Objects.equals(latestProof, that.latestProof)
          && Objects.equals(peersAskedForStatus, that.peersAskedForStatus)
          && Objects.equals(receivedStatusResponses, that.receivedStatusResponses);
    }

    @Override
    public int hashCode() {
      return Objects.hash(latestProof, peersAskedForStatus, receivedStatusResponses);
    }
  }

  final class PendingRequest {
    private final NodeId peer;
    private final long requestId;

    public static PendingRequest create(NodeId peer, long requestId) {
      return new PendingRequest(peer, requestId);
    }

    private PendingRequest(NodeId peer, long requestId) {
      this.peer = peer;
      this.requestId = requestId;
    }

    public NodeId getPeer() {
      return peer;
    }

    public long getRequestId() {
      return requestId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final var that = (PendingRequest) o;
      return requestId == that.requestId && Objects.equals(peer, that.peer);
    }

    @Override
    public int hashCode() {
      return Objects.hash(peer, requestId);
    }
  }

  final class SyncingState implements SyncState {
    private final LedgerProof latestProof;
    private final ImmutableList<NodeId> candidatePeersQueue;
    private final LedgerHeader targetHeader;
    private final Optional<PendingRequest> pendingRequest;

    public static SyncingState init(
        LedgerProof latestProof,
        ImmutableList<NodeId> candidatePeersQueue,
        LedgerHeader targetHeader) {
      return new SyncingState(latestProof, candidatePeersQueue, targetHeader, Optional.empty());
    }

    private SyncingState(
        LedgerProof latestProof,
        ImmutableList<NodeId> candidatePeersQueue,
        LedgerHeader targetHeader,
        Optional<PendingRequest> pendingRequest) {
      this.latestProof = latestProof;
      this.candidatePeersQueue = candidatePeersQueue;
      this.targetHeader = targetHeader;
      this.pendingRequest = pendingRequest;
    }

    public SyncingState withPendingRequest(NodeId peer, long requestId) {
      return new SyncingState(
          latestProof,
          candidatePeersQueue,
          targetHeader,
          Optional.of(PendingRequest.create(peer, requestId)));
    }

    public SyncingState clearPendingRequest() {
      return new SyncingState(latestProof, candidatePeersQueue, targetHeader, Optional.empty());
    }

    public SyncingState removeCandidate(NodeId peer) {
      return new SyncingState(
          latestProof,
          ImmutableList.copyOf(Collections2.filter(candidatePeersQueue, not(equalTo(peer)))),
          targetHeader,
          pendingRequest);
    }

    public SyncingState withTargetHeader(LedgerHeader newTargetHeader) {
      return new SyncingState(latestProof, candidatePeersQueue, newTargetHeader, pendingRequest);
    }

    public Pair<SyncingState, Optional<NodeId>> fetchNextCandidatePeer() {
      final var peerToUse = candidatePeersQueue.stream().findFirst();

      if (peerToUse.isPresent()) {
        final var newState =
            new SyncingState(
                latestProof,
                new ImmutableList.Builder<NodeId>()
                    .addAll(Collections2.filter(candidatePeersQueue, not(equalTo(peerToUse.get()))))
                    .add(peerToUse.get())
                    .build(),
                targetHeader,
                pendingRequest);

        return Pair.of(newState, peerToUse);
      } else {
        return Pair.of(this, Optional.empty());
      }
    }

    public SyncingState addCandidatePeers(ImmutableList<NodeId> peers) {
      return new SyncingState(
          latestProof,
          new ImmutableList.Builder<NodeId>()
              .addAll(peers)
              .addAll(Collections2.filter(candidatePeersQueue, not(peers::contains)))
              .build(),
          targetHeader,
          pendingRequest);
    }

    public boolean waitingForResponse() {
      return this.pendingRequest.isPresent();
    }

    public boolean waitingForResponseFrom(NodeId peer) {
      return this.pendingRequest.stream().anyMatch(pr -> pr.getPeer().equals(peer));
    }

    public Optional<PendingRequest> getPendingRequest() {
      return this.pendingRequest;
    }

    public LedgerHeader getTargetHeader() {
      return this.targetHeader;
    }

    public Optional<NodeId> peekNthCandidate(int n) {
      var state = this;
      Optional<NodeId> candidate = Optional.empty();
      for (int i = 0; i <= n; i++) {
        final var res = state.fetchNextCandidatePeer();
        state = res.getFirst();
        candidate = res.getSecond();
      }
      return candidate;
    }

    @Override
    public LedgerProof getLatestProof() {
      return this.latestProof;
    }

    @Override
    public SyncingState withLatestProof(LedgerProof latestProof) {
      return new SyncingState(latestProof, candidatePeersQueue, targetHeader, pendingRequest);
    }

    @Override
    public String toString() {
      return String.format(
          "%s{primaryProof=%s targetHeader=%s}",
          getClass().getSimpleName(), latestProof, targetHeader);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SyncingState that = (SyncingState) o;
      return Objects.equals(latestProof, that.latestProof)
          && Objects.equals(candidatePeersQueue, that.candidatePeersQueue)
          && Objects.equals(targetHeader, that.targetHeader)
          && Objects.equals(pendingRequest, that.pendingRequest);
    }

    @Override
    public int hashCode() {
      return Objects.hash(latestProof, candidatePeersQueue, targetHeader, pendingRequest);
    }
  }
}
