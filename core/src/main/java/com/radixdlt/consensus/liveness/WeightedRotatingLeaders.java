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

package com.radixdlt.consensus.liveness;

import com.radixdlt.consensus.bft.BFTValidator;
import com.radixdlt.consensus.bft.BFTValidatorId;
import com.radixdlt.consensus.bft.BFTValidatorSet;
import com.radixdlt.consensus.bft.Round;
import com.radixdlt.utils.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Rotates leaders with those having more power being proposed more often in proportion to the
 * amount of power they have.
 *
 * <p>Calculation of the next leader is dependent on the weight state of the previous round and thus
 * computing the leader for an arbitrary round can be quite expensive.
 *
 * <p>We resolve this by keeping a cache of some given size of the previous rounds closest to the
 * highest round calculated.
 *
 * <p>This class is stateful and is NOT thread-safe.
 */
public final class WeightedRotatingLeaders implements ProposerElection {
  private static final UInt384 POW_2_256 = UInt384.from(UInt256.MAX_VALUE).increment();

  private final BFTValidatorSet validatorSet;
  private final Comparator<Entry<BFTValidator, UInt384>> weightsComparator;
  private final CachingNextLeaderComputer nextLeaderComputer;

  public WeightedRotatingLeaders(BFTValidatorSet validatorSet, int cacheSize) {
    this.validatorSet = validatorSet;
    this.weightsComparator =
        Comparator.comparing(Entry<BFTValidator, UInt384>::getValue)
            .thenComparing(
                v -> v.getKey().getValidatorId().getKey(), KeyComparator.instance().reversed());
    this.nextLeaderComputer =
        new CachingNextLeaderComputer(validatorSet, weightsComparator, cacheSize);
  }

  private static class CachingNextLeaderComputer {
    private final BFTValidatorSet validatorSet;
    private final Comparator<Entry<BFTValidator, UInt384>> weightsComparator;
    private final Map<BFTValidator, UInt384> weights;
    private final BFTValidator[] cache;
    private final Long lcm;
    private Round curRound;

    private CachingNextLeaderComputer(
        BFTValidatorSet validatorSet,
        Comparator<Entry<BFTValidator, UInt384>> weightsComparator,
        int cacheSize) {
      this.validatorSet = validatorSet;
      this.weightsComparator = weightsComparator;
      this.weights = new HashMap<>();
      this.cache = new BFTValidator[cacheSize];

      UInt192[] powerArray =
          validatorSet.getValidators().stream().map(BFTValidator::getPower).toArray(UInt192[]::new);
      // after cappedLCM is executed, the following invariant will be true:
      // (lcm > 0 && lcm < 2^63 -1 ) || lcm == null
      // This is due to use of 2^63 - 1 cap and also the invariant from ValidatorSet
      // that powerArray will always be non-zero
      UInt192 lcm192 = UInt192.cappedLCM(UInt192.from(Long.MAX_VALUE), powerArray);
      this.lcm = lcm192 == null ? null : lcm192.getLow().getLow();

      this.resetToRound(Round.of(0));
    }

    private BFTValidator computeHeaviest() {
      final Entry<BFTValidator, UInt384> max =
          weights.entrySet().stream()
              .max(weightsComparator)
              .orElseThrow(() -> new IllegalStateException("Weights cannot be empty"));
      return max.getKey();
    }

    private void computeNext() {
      // Reset current leader by subtracting total power
      final int curIndex = (int) (this.curRound.number() % cache.length);
      final BFTValidator curLeader = cache[curIndex];
      weights.merge(curLeader, UInt384.from(validatorSet.getTotalPower()), UInt384::subtract);

      // Add weights relative to each validator's power
      for (BFTValidator validator : validatorSet.getValidators()) {
        weights.merge(validator, UInt384.from(validator.getPower()), UInt384::add);
      }

      // Compute next leader by getting heaviest validator
      this.curRound = this.curRound.next();
      int index = (int) (this.curRound.number() % cache.length);
      cache[index] = computeHeaviest();
    }

    private BFTValidator checkCacheForProposer(Round round) {
      if (round.compareTo(curRound) <= 0 && round.number() > curRound.number() - cache.length) {
        final int index = (int) (round.number() % cache.length);
        return cache[index];
      }

      return null;
    }

    private void computeToRound(Round round) {
      while (round.compareTo(curRound) > 0) {
        computeNext();
      }
    }

    private BFTValidator resetToRound(Round round) {
      // reset if round isn't in cache
      if (curRound == null || round.number() < curRound.number() - cache.length) {
        if (lcm == null || lcm > round.number()) {
          curRound = Round.epochInitial();
        } else {
          long multipleOfLCM = round.number() / lcm;
          curRound = Round.of(multipleOfLCM * lcm);
        }

        for (BFTValidator validator : validatorSet.getValidators()) {
          weights.put(validator, POW_2_256.subtract(validator.getPower()));
        }
        cache[0] = computeHeaviest();
      }

      // compute to round
      computeToRound(round);

      // guaranteed to return non-null;
      return cache[(int) (round.number() % cache.length)];
    }

    @Override
    public String toString() {
      return String.format("%s %s %s", this.curRound, Arrays.toString(this.cache), this.weights);
    }
  }

  @Override
  public BFTValidatorId getProposer(Round round) {
    nextLeaderComputer.computeToRound(round);

    // validator will only be null if the round supplied is before the cache
    // window
    BFTValidator validator = nextLeaderComputer.checkCacheForProposer(round);
    if (validator != null) {
      // dynamic program cache successful
      return validator.getValidatorId();
    } else {
      // cache doesn't have value, do the expensive operation
      CachingNextLeaderComputer computer =
          new CachingNextLeaderComputer(validatorSet, weightsComparator, 1);
      return computer.resetToRound(round).getValidatorId();
    }
  }

  @Override
  public String toString() {
    return String.format("%s %s", this.getClass().getSimpleName(), this.nextLeaderComputer);
  }
}
