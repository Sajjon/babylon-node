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

package com.radixdlt.transaction;

import com.google.common.reflect.TypeToken;
import com.radixdlt.environment.NodeRustEnvironment;
import com.radixdlt.lang.Option;
import com.radixdlt.lang.Result;
import com.radixdlt.lang.Tuple;
import com.radixdlt.monitoring.LabelledTimer;
import com.radixdlt.monitoring.Metrics;
import com.radixdlt.monitoring.Metrics.MethodId;
import com.radixdlt.rev2.Decimal;
import com.radixdlt.sbor.Natives;
import com.radixdlt.statecomputer.commit.LedgerProof;
import com.radixdlt.utils.UInt64;
import java.util.Map;
import java.util.Optional;

public final class REv2TransactionAndProofStore {
  public REv2TransactionAndProofStore(Metrics metrics, NodeRustEnvironment nodeRustEnvironment) {
    LabelledTimer<MethodId> timer = metrics.stateManager().nativeCall();
    this.getSyncableTxnsAndProof =
        Natives.builder(nodeRustEnvironment, REv2TransactionAndProofStore::getSyncableTxnsAndProof)
            .measure(
                timer.label(
                    new MethodId(REv2TransactionAndProofStore.class, "getSyncableTxnsAndProof")))
            .build(new TypeToken<>() {});
    this.getLatestProofFunc =
        Natives.builder(nodeRustEnvironment, REv2TransactionAndProofStore::getLatestProof)
            .measure(
                timer.label(new MethodId(REv2TransactionAndProofStore.class, "getLatestProof")))
            .build(new TypeToken<>() {});
    this.getLatestEpochProofFunc =
        Natives.builder(nodeRustEnvironment, REv2TransactionAndProofStore::getLatestEpochProof)
            .measure(
                timer.label(
                    new MethodId(REv2TransactionAndProofStore.class, "getLatestEpochProof")))
            .build(new TypeToken<>() {});
    this.getLatestProtocolUpdateInitProofFunc =
        Natives.builder(
                nodeRustEnvironment, REv2TransactionAndProofStore::getLatestProtocolUpdateInitProof)
            .measure(
                timer.label(
                    new MethodId(
                        REv2TransactionAndProofStore.class, "getLatestProtocolUpdateInitProof")))
            .build(new TypeToken<>() {});
    this.getLatestProtocolUpdateExecutionProofFunc =
        Natives.builder(
                nodeRustEnvironment,
                REv2TransactionAndProofStore::getLatestProtocolUpdateExecutionProof)
            .measure(
                timer.label(
                    new MethodId(
                        REv2TransactionAndProofStore.class,
                        "getLatestProtocolUpdateExecutionProof")))
            .build(new TypeToken<>() {});
    this.getPostGenesisEpochProofFunc =
        Natives.builder(nodeRustEnvironment, REv2TransactionAndProofStore::getPostGenesisEpochProof)
            .measure(
                timer.label(
                    new MethodId(REv2TransactionAndProofStore.class, "getPostGenesisEpochProof")))
            .build(new TypeToken<>() {});
    this.getEpochProofFunc =
        Natives.builder(nodeRustEnvironment, REv2TransactionAndProofStore::getEpochProof)
            .measure(timer.label(new MethodId(REv2TransactionAndProofStore.class, "getEpochProof")))
            .build(new TypeToken<>() {});
    this.getSignificantProtocolUpdateReadinessForEpochFunc =
        Natives.builder(
                nodeRustEnvironment,
                REv2TransactionAndProofStore::getSignificantProtocolUpdateReadinessForEpoch)
            .measure(
                timer.label(
                    new MethodId(
                        REv2TransactionAndProofStore.class,
                        "getSignificantProtocolUpdateReadinessForEpoch")))
            .build(new TypeToken<>() {});
  }

  public Result<TxnsAndProof, GetSyncableTxnsAndProofError> getSyncableTxnsAndProof(
      long startStateVersionInclusive, LedgerSyncLimitsConfig limitsConfig) {
    return this.getSyncableTxnsAndProof.call(
        new SyncableTxnsAndProofRequest(
            UInt64.fromNonNegativeLong(startStateVersionInclusive), limitsConfig));
  }

  public Optional<LedgerProof> getLatestProof() {
    return this.getLatestProofFunc.call(Tuple.tuple()).toOptional();
  }

  public Optional<LedgerProof> getLatestEpochProof() {
    return this.getLatestEpochProofFunc.call(Tuple.tuple()).toOptional();
  }

  public Optional<LedgerProof> getLatestProtocolUpdateInitProof() {
    return this.getLatestProtocolUpdateInitProofFunc.call(Tuple.tuple()).toOptional();
  }

  public Optional<LedgerProof> getLatestProtocolUpdateExecutionProof() {
    return this.getLatestProtocolUpdateExecutionProofFunc.call(Tuple.tuple()).toOptional();
  }

  public Optional<LedgerProof> getPostGenesisEpochProof() {
    return this.getPostGenesisEpochProofFunc.call(Tuple.tuple()).toOptional();
  }

  public Optional<LedgerProof> getEpochProof(long epoch) {
    return this.getEpochProofFunc.call(UInt64.fromNonNegativeLong(epoch)).toOptional();
  }

  public Optional<Map<String, Decimal>> getSignificantProtocolUpdateReadinessForEpoch(long epoch) {
    return this.getSignificantProtocolUpdateReadinessForEpochFunc
        .call(UInt64.fromNonNegativeLong(epoch))
        .toOptional();
  }

  private final Natives.Call1<
          SyncableTxnsAndProofRequest, Result<TxnsAndProof, GetSyncableTxnsAndProofError>>
      getSyncableTxnsAndProof;

  private static native byte[] getSyncableTxnsAndProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<Tuple.Tuple0, Option<LedgerProof>> getLatestProofFunc;

  private static native byte[] getLatestProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<Tuple.Tuple0, Option<LedgerProof>> getLatestEpochProofFunc;

  private static native byte[] getLatestEpochProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<Tuple.Tuple0, Option<LedgerProof>>
      getLatestProtocolUpdateInitProofFunc;

  private static native byte[] getLatestProtocolUpdateInitProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<Tuple.Tuple0, Option<LedgerProof>>
      getLatestProtocolUpdateExecutionProofFunc;

  private static native byte[] getLatestProtocolUpdateExecutionProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<Tuple.Tuple0, Option<LedgerProof>> getPostGenesisEpochProofFunc;

  private static native byte[] getPostGenesisEpochProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<UInt64, Option<LedgerProof>> getEpochProofFunc;

  private static native byte[] getEpochProof(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);

  private final Natives.Call1<UInt64, Option<Map<String, Decimal>>>
      getSignificantProtocolUpdateReadinessForEpochFunc;

  private static native byte[] getSignificantProtocolUpdateReadinessForEpoch(
      NodeRustEnvironment nodeRustEnvironment, byte[] payload);
}
