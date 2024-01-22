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

use crate::jni::node_rust_environment::JNINodeRustEnvironment;
use crate::jni::LedgerSyncLimitsConfig;
use crate::store::traits::*;
use crate::{epoch_change_iter, LedgerProof, StateVersion};
use jni::objects::{JClass, JObject};
use jni::sys::jbyteArray;
use jni::JNIEnv;
use node_common::java::*;
use radix_engine::types::*;
use std::ops::Deref;

#[derive(Debug, ScryptoCategorize, ScryptoEncode, ScryptoDecode)]
struct TxnsAndProofRequest {
    start_state_version_inclusive: u64,
    limits_config: LedgerSyncLimitsConfig,
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getSyncableTxnsAndProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |request: TxnsAndProofRequest| -> Result<TxnsAndProof, GetSyncableTxnsAndProofError> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            // Note: even though we read a strictly historical state here, we cannot use the
            // "historical, non-locked" DB access - please see the TODO note at `LedgerProofsGc`.
            let res = database.read_current().get_syncable_txns_and_proof(
                StateVersion::of(request.start_state_version_inclusive),
                request
                    .limits_config
                    .max_txns_for_responses_spanning_more_than_one_proof,
                request.limits_config.max_txn_bytes_for_single_response,
            );
            res
        },
    )
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getLatestProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |_no_args: ()| -> Option<LedgerProof> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            let proof = database.read_current().get_latest_proof();
            proof
        },
    )
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getLatestEpochProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |_no_args: ()| -> Option<LedgerProof> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            let proof = database.read_current().get_latest_epoch_proof();
            proof
        },
    )
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getLatestProtocolUpdateInitProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |_no_args: ()| -> Option<LedgerProof> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            let proof = database
                .read_current()
                .get_latest_protocol_update_init_proof();
            proof
        },
    )
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getLatestProtocolUpdateExecutionProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |_no_args: ()| -> Option<LedgerProof> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            let proof = database
                .read_current()
                .get_latest_protocol_update_execution_proof();
            proof
        },
    )
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getPostGenesisEpochProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(&env, request_payload, |_: ()| -> Option<LedgerProof> {
        let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
        let proof = database
            .access_non_locked_historical()
            .get_post_genesis_epoch_proof();
        proof
    })
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getEpochProof(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |epoch: Epoch| -> Option<LedgerProof> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            let proof = database
                .access_non_locked_historical()
                .get_epoch_proof(epoch);
            proof
        },
    )
}

#[no_mangle]
extern "system" fn Java_com_radixdlt_transaction_REv2TransactionAndProofStore_getSignificantProtocolUpdateReadinessForEpoch(
    env: JNIEnv,
    _class: JClass,
    j_rust_global_context: JObject,
    request_payload: jbyteArray,
) -> jbyteArray {
    jni_sbor_coded_call(
        &env,
        request_payload,
        |epoch: Epoch| -> Option<IndexMap<String, Decimal>> {
            let database = JNINodeRustEnvironment::get_database(&env, j_rust_global_context);
            let non_locked_db = database.access_non_locked_historical();
            let mut epoch_change_event_iter = epoch_change_iter(non_locked_db.deref(), epoch);
            let maybe_epoch_change = epoch_change_event_iter.next();
            maybe_epoch_change
                .filter(|(_, epoch_change_event)| epoch_change_event.epoch == epoch)
                .map(|(_, epoch_change_event)| {
                    epoch_change_event.significant_protocol_update_readiness
                })
        },
    )
}

pub fn export_extern_functions() {}
