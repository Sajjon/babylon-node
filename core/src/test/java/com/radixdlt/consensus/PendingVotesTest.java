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

package com.radixdlt.consensus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.hash.HashCode;
import com.radixdlt.consensus.bft.*;
import com.radixdlt.consensus.bft.Round;
import com.radixdlt.consensus.bft.VoteProcessingResult.VoteRejected.VoteRejectedReason;
import com.radixdlt.crypto.ECDSASecp256k1Signature;
import com.radixdlt.crypto.HashUtils;
import com.radixdlt.crypto.Hasher;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.monitoring.MetricsInitializer;
import com.radixdlt.utils.RandomHasher;
import com.radixdlt.utils.UInt192;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

public class PendingVotesTest {
  private Hasher hasher;

  private Metrics metrics;

  @Before
  public void setup() {
    this.hasher = new RandomHasher();
    this.metrics = new MetricsInitializer().initialize();
  }

  @Test
  public void equalsContractForPreviousVote() {
    EqualsVerifier.forClass(PreviousVote.class)
        .withPrefabValues(HashCode.class, HashUtils.random256(), HashUtils.random256())
        .verify();
  }

  @Test
  public void when_inserting_valid_but_unaccepted_votes__then_no_qc_is_returned() {
    HashCode vertexId = HashUtils.random256();
    Vote vote1 = makeSignedVoteFor(mock(BFTValidatorId.class), Round.genesis(), vertexId);
    Vote vote2 = makeSignedVoteFor(mock(BFTValidatorId.class), Round.genesis(), vertexId);

    BFTValidatorSet validatorSet =
        BFTValidatorSet.from(
            Collections.singleton(BFTValidator.from(vote1.getAuthor(), UInt192.ONE)));
    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    VoteData voteData = mock(VoteData.class);
    BFTHeader proposed = vote1.getVoteData().getProposed();
    when(voteData.getProposed()).thenReturn(proposed);

    assertEquals(
        VoteProcessingResult.rejected(VoteRejectedReason.INVALID_AUTHOR),
        pendingVotes.insertVote(vote2));
  }

  @Test
  public void when_inserting_valid_and_accepted_votes__then_qc_is_formed() {
    BFTValidatorId author = mock(BFTValidatorId.class);
    Vote vote = makeSignedVoteFor(author, Round.genesis(), HashUtils.random256());

    BFTValidatorSet validatorSet = mock(BFTValidatorSet.class);
    ValidationState validationState = mock(ValidationState.class);
    TimestampedECDSASignatures signatures = mock(TimestampedECDSASignatures.class);
    when(validationState.addSignature(any(), anyLong(), any())).thenReturn(true);
    when(validationState.complete()).thenReturn(true);
    when(validationState.signatures()).thenReturn(signatures);
    when(validatorSet.newValidationState()).thenReturn(validationState);
    when(validatorSet.containsValidator(any(BFTValidatorId.class))).thenReturn(true);

    VoteData voteData = mock(VoteData.class);
    BFTHeader proposed = vote.getVoteData().getProposed();
    when(voteData.getProposed()).thenReturn(proposed);

    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    assertTrue(pendingVotes.insertVote(vote) instanceof VoteProcessingResult.QuorumReached);
  }

  @Test
  public void when_inserting_valid_timeout_votes__then_tc_is_formed() {
    HashCode vertexId1 = HashUtils.random256();
    HashCode vertexId2 = HashUtils.random256();
    Vote vote1 = makeSignedVoteFor(mock(BFTValidatorId.class), Round.genesis(), vertexId1);
    when(vote1.getTimeoutSignature()).thenReturn(Optional.of(mock(ECDSASecp256k1Signature.class)));
    when(vote1.isTimeout()).thenReturn(true);
    Vote vote2 = makeSignedVoteFor(mock(BFTValidatorId.class), Round.genesis(), vertexId2);
    when(vote2.getTimeoutSignature()).thenReturn(Optional.of(mock(ECDSASecp256k1Signature.class)));
    when(vote2.isTimeout()).thenReturn(true);

    BFTValidatorSet validatorSet =
        BFTValidatorSet.from(
            Arrays.asList(
                BFTValidator.from(vote1.getAuthor(), UInt192.ONE),
                BFTValidator.from(vote2.getAuthor(), UInt192.ONE)));

    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    assertTrue(pendingVotes.insertVote(vote1) instanceof VoteProcessingResult.VoteAccepted);

    VoteProcessingResult result2 = pendingVotes.insertVote(vote2);

    assertTrue(result2 instanceof VoteProcessingResult.QuorumReached);

    assertTrue(
        ((VoteProcessingResult.QuorumReached) result2).roundQuorum()
            instanceof RoundQuorum.TimeoutRoundQuorum);
  }

  @Test
  public void when_voting_again__previous_vote_is_removed() {
    BFTValidatorId author = mock(BFTValidatorId.class);
    Vote vote = makeSignedVoteFor(author, Round.genesis(), HashUtils.random256());

    BFTValidatorSet validatorSet = mock(BFTValidatorSet.class);
    ValidationState validationState = mock(ValidationState.class);
    TimestampedECDSASignatures signatures = mock(TimestampedECDSASignatures.class);
    when(validationState.signatures()).thenReturn(signatures);
    when(validationState.isEmpty()).thenReturn(true);
    when(validatorSet.newValidationState()).thenReturn(validationState);
    when(validatorSet.containsValidator(any(BFTValidatorId.class))).thenReturn(true);

    VoteData voteData = mock(VoteData.class);
    BFTHeader proposed = vote.getVoteData().getProposed();
    when(voteData.getProposed()).thenReturn(proposed);

    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    // Preconditions
    assertEquals(VoteProcessingResult.accepted(), pendingVotes.insertVote(vote));
    assertEquals(1, pendingVotes.voteStateSize());
    assertEquals(1, pendingVotes.previousVotesSize());

    Vote vote2 = makeSignedVoteFor(author, Round.of(1), HashUtils.random256());
    // Need a different hash for this (different) vote
    assertEquals(VoteProcessingResult.accepted(), pendingVotes.insertVote(vote2));
    assertEquals(1, pendingVotes.voteStateSize());
    assertEquals(1, pendingVotes.previousVotesSize());
  }

  @Test
  public void when_voting_again__previous_timeoutvote_is_removed() {
    BFTValidatorId author = mock(BFTValidatorId.class);
    Vote vote = makeSignedVoteFor(author, Round.genesis(), HashUtils.random256());
    when(vote.getTimeoutSignature()).thenReturn(Optional.of(mock(ECDSASecp256k1Signature.class)));
    when(vote.isTimeout()).thenReturn(true);

    BFTValidatorSet validatorSet = mock(BFTValidatorSet.class);
    ValidationState validationState = mock(ValidationState.class);
    TimestampedECDSASignatures signatures = mock(TimestampedECDSASignatures.class);
    when(validationState.signatures()).thenReturn(signatures);
    when(validationState.isEmpty()).thenReturn(true);
    when(validatorSet.newValidationState()).thenReturn(validationState);
    when(validatorSet.containsValidator(any(BFTValidatorId.class))).thenReturn(true);

    VoteData voteData = mock(VoteData.class);
    BFTHeader proposed = vote.getVoteData().getProposed();
    when(voteData.getProposed()).thenReturn(proposed);

    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    // Preconditions
    assertEquals(VoteProcessingResult.accepted(), pendingVotes.insertVote(vote));
    assertEquals(1, pendingVotes.voteStateSize());
    assertEquals(1, pendingVotes.timeoutVoteStateSize());
    assertEquals(1, pendingVotes.previousVotesSize());

    Vote vote2 = makeSignedVoteFor(author, Round.of(1), HashUtils.random256());
    // Need a different hash for this (different) vote
    assertEquals(VoteProcessingResult.accepted(), pendingVotes.insertVote(vote2));
    assertEquals(1, pendingVotes.voteStateSize());
    assertEquals(0, pendingVotes.timeoutVoteStateSize());
    assertEquals(1, pendingVotes.previousVotesSize());
  }

  @Test
  public void when_submitting_a_duplicate_vote__then_can_be_replaced_if_has_timeout() {
    final var vertexId1 = HashUtils.random256();
    final var vertexId2 = HashUtils.random256();
    final var vote1 = makeSignedVoteFor(mock(BFTValidatorId.class), Round.genesis(), vertexId1);
    when(vote1.getTimeoutSignature()).thenReturn(Optional.empty());
    when(vote1.isTimeout()).thenReturn(false);
    final var vote2 = makeSignedVoteFor(mock(BFTValidatorId.class), Round.genesis(), vertexId2);
    when(vote2.getTimeoutSignature()).thenReturn(Optional.of(mock(ECDSASecp256k1Signature.class)));
    when(vote2.isTimeout()).thenReturn(true);

    BFTValidatorSet validatorSet =
        BFTValidatorSet.from(
            Arrays.asList(
                BFTValidator.from(vote1.getAuthor(), UInt192.ONE),
                BFTValidator.from(vote2.getAuthor(), UInt192.ONE)));

    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    assertTrue(pendingVotes.insertVote(vote1) instanceof VoteProcessingResult.VoteAccepted);

    // submit duplicate vote, should fail
    assertEquals(
        VoteProcessingResult.rejected(VoteRejectedReason.DUPLICATE_VOTE),
        pendingVotes.insertVote(vote1));

    // submit again, but this time with a timeout
    when(vote1.getTimeoutSignature()).thenReturn(Optional.of(mock(ECDSASecp256k1Signature.class)));
    when(vote1.isTimeout()).thenReturn(true);

    // should be accepted
    assertEquals(VoteProcessingResult.accepted(), pendingVotes.insertVote(vote1));

    // insert another timeout vote
    final var result2 = pendingVotes.insertVote(vote2);

    // and form a TC
    assertTrue(result2 instanceof VoteProcessingResult.QuorumReached);

    assertTrue(
        ((VoteProcessingResult.QuorumReached) result2).roundQuorum()
            instanceof RoundQuorum.TimeoutRoundQuorum);
  }

  @Test
  public void divergent_vertex_execution_should_be_detected() {
    // Arrange: create two votes on the same vertexId, but having
    // different ledger headers
    final var vertexId = HashUtils.random256();

    final var ledgerHeader1 = mock(LedgerHeader.class);
    when(ledgerHeader1.getStateVersion()).thenReturn(1L);
    final var bftHeader1 = mock(BFTHeader.class);
    when(bftHeader1.getLedgerHeader()).thenReturn(ledgerHeader1);
    when(bftHeader1.getVertexId()).thenReturn(vertexId);
    final var voteData1 = mock(VoteData.class);
    when(voteData1.getProposed()).thenReturn(bftHeader1);
    final var vote1 =
        makeVoteWithoutSignatureFor(mock(BFTValidatorId.class), Round.genesis(), vertexId);
    when(vote1.getSignature()).thenReturn(ECDSASecp256k1Signature.zeroSignature());
    when(vote1.getVoteData()).thenReturn(voteData1);

    final var ledgerHeader2 = mock(LedgerHeader.class);
    when(ledgerHeader1.getStateVersion()).thenReturn(2L);
    final var bftHeader2 = mock(BFTHeader.class);
    when(bftHeader2.getLedgerHeader()).thenReturn(ledgerHeader2);
    when(bftHeader2.getVertexId()).thenReturn(vertexId);
    final var voteData2 = mock(VoteData.class);
    when(voteData2.getProposed()).thenReturn(bftHeader2);
    final var vote2 =
        makeVoteWithoutSignatureFor(mock(BFTValidatorId.class), Round.genesis(), vertexId);
    when(vote2.getSignature()).thenReturn(ECDSASecp256k1Signature.zeroSignature());
    when(vote2.getVoteData()).thenReturn(voteData2);

    final var validatorSet =
        BFTValidatorSet.from(
            Arrays.asList(
                BFTValidator.from(vote1.getAuthor(), UInt192.ONE),
                BFTValidator.from(vote2.getAuthor(), UInt192.ONE)));

    final var pendingVotes = new PendingVotes(hasher, e -> {}, validatorSet, metrics);

    // Should still accept both votes...
    assertTrue(pendingVotes.insertVote(vote1) instanceof VoteProcessingResult.VoteAccepted);
    assertTrue(pendingVotes.insertVote(vote2) instanceof VoteProcessingResult.VoteAccepted);

    // ...but produce a warning and bump the metrics
    assertEquals((int) metrics.bft().divergentVertexExecutions().get(), 1);
  }

  private Vote makeSignedVoteFor(BFTValidatorId author, Round parentRound, HashCode vertexId) {
    Vote vote = makeVoteWithoutSignatureFor(author, parentRound, vertexId);
    when(vote.getSignature()).thenReturn(ECDSASecp256k1Signature.zeroSignature());
    return vote;
  }

  private Vote makeVoteWithoutSignatureFor(
      BFTValidatorId author, Round parentRound, HashCode vertexId) {
    Vote vote = mock(Vote.class);
    BFTHeader proposed = new BFTHeader(parentRound.next(), vertexId, mock(LedgerHeader.class));
    BFTHeader parent = new BFTHeader(parentRound, HashUtils.random256(), mock(LedgerHeader.class));
    VoteData voteData = new VoteData(proposed, parent, null);
    when(vote.getHashOfData(any())).thenReturn(Vote.getHashOfData(hasher, voteData, 123456L));
    when(vote.getVoteData()).thenReturn(voteData);
    when(vote.getTimestamp()).thenReturn(123456L);
    when(vote.getAuthor()).thenReturn(author);
    when(vote.getRound()).thenReturn(parentRound);
    when(vote.getEpoch()).thenReturn(0L);
    return vote;
  }
}
