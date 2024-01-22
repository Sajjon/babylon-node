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

use prometheus::Registry;

use crate::traits::QueryableProofStore;
use radix_engine::blueprints::consensus_manager::{
    ConsensusManagerConfigurationFieldPayload, ConsensusManagerField,
};
use radix_engine::prelude::dec;
use radix_engine::system::system_substates::FieldSubstate;
use radix_engine::utils::generate_validator_fee_fix_state_updates;

use radix_engine_common::network::NetworkDefinition;
use radix_engine_common::prelude::{Epoch, CONSENSUS_MANAGER};
use radix_engine_interface::prelude::MAIN_BASE_PARTITION;
use radix_engine_store_interface::db_key_mapper::{MappedSubstateDatabase, SpreadPrefixKeyMapper};

use sbor::HasLatestVersion;

use node_common::locks::LockFactory;
use node_common::scheduler::Scheduler;

use crate::ProtocolUpdateEnactmentCondition::EnactUnconditionallyAtEpoch;
use crate::{
    FixedFlashProtocolUpdater, NoStateUpdatesProtocolUpdater, ProtocolConfig, ProtocolUpdate,
    ProtocolUpdater, ProtocolUpdaterFactory, StateManager, StateManagerConfig,
};
use radix_engine::track::StateUpdates;
use std::ops::Deref;

use crate::test::prepare_and_commit_round_update;
use crate::transaction::FlashTransactionV1;

const GENESIS_PROTOCOL_VERSION: &str = "testing-genesis";
const V2_PROTOCOL_VERSION: &str = "testing-v2";

struct TestProtocolUpdaterFactory {
    v2_flash: FlashTransactionV1,
}

impl ProtocolUpdaterFactory for TestProtocolUpdaterFactory {
    fn updater_for(&self, protocol_version_name: &str) -> Option<Box<dyn ProtocolUpdater>> {
        match protocol_version_name {
            GENESIS_PROTOCOL_VERSION => Some(Box::new(NoStateUpdatesProtocolUpdater::default(
                NetworkDefinition::simulator(),
            ))),
            V2_PROTOCOL_VERSION => Some(Box::new(
                FixedFlashProtocolUpdater::new_with_default_configurator(
                    V2_PROTOCOL_VERSION.to_string(),
                    NetworkDefinition::simulator(),
                    vec![self.v2_flash.clone()],
                ),
            )),
            _ => None,
        }
    }
}

#[test]
fn flash_protocol_update_test() {
    let mut state_manager_config =
        StateManagerConfig::new_for_testing(tempfile::tempdir().unwrap().path().to_str().unwrap());

    // We're enacting an update after another transaction commit
    let protocol_update_epoch = Epoch::of(3);

    // Updating to "testing-v2" at post_genesis_state_version + 1
    state_manager_config.protocol_config = ProtocolConfig {
        genesis_protocol_version: GENESIS_PROTOCOL_VERSION.to_string(),
        protocol_updates: vec![ProtocolUpdate {
            next_protocol_version: V2_PROTOCOL_VERSION.to_string(),
            enactment_condition: EnactUnconditionallyAtEpoch(protocol_update_epoch),
        }],
    };

    // This is a bit of a hack to be able to use fixed flash protocol update
    let consensus_manager_state_updates = {
        // Run the genesis first
        let tmp_state_manager = create_state_manager(
            state_manager_config.clone(),
            // Fake updater, unused
            Box::new(TestProtocolUpdaterFactory {
                v2_flash: FlashTransactionV1 {
                    name: "unused".to_string(),
                    state_updates: StateUpdates::default(),
                },
            }),
        );
        tmp_state_manager
            .state_computer
            .execute_genesis_for_unit_tests_with_default_config();
        // Now we can prepare the state updates based on the initialized database
        let state_updates = generate_validator_fee_fix_state_updates(
            tmp_state_manager.database.read_current().deref(),
        );
        state_updates
    };

    let state_manager = create_state_manager(
        state_manager_config,
        Box::new(TestProtocolUpdaterFactory {
            v2_flash: FlashTransactionV1 {
                name: "testing-v2-flash".to_string(),
                state_updates: consensus_manager_state_updates,
            },
        }),
    );

    // Commit 3 round updates to get us to the next epoch (3).
    let _ = prepare_and_commit_round_update(&state_manager);
    let _ = prepare_and_commit_round_update(&state_manager);
    let (prepare_result, _commit_summary) = prepare_and_commit_round_update(&state_manager);

    assert_eq!(
        prepare_result.next_epoch.unwrap().epoch,
        protocol_update_epoch
    );

    assert_eq!(
        prepare_result.next_protocol_version,
        Some(V2_PROTOCOL_VERSION.to_string())
    );

    let read_db = state_manager.database.read_current();
    let pre_protocol_update_state_version = read_db.max_state_version();
    drop(read_db);

    // Now let's apply the protocol update (this would normally be called by Java)
    state_manager.apply_protocol_update(V2_PROTOCOL_VERSION);

    // Verify that a new consensus manager config has been flashed
    let read_db = state_manager.database.read_current();
    let config_substate = read_db.get_mapped::<SpreadPrefixKeyMapper, FieldSubstate<ConsensusManagerConfigurationFieldPayload>>(
        CONSENSUS_MANAGER.as_node_id(),
        MAIN_BASE_PARTITION,
        &ConsensusManagerField::Configuration.into()
    ).unwrap();

    assert_eq!(
        config_substate
            .into_payload()
            .into_latest()
            .config
            .validator_creation_usd_cost,
        dec!("100")
    );

    assert_eq!(
        read_db.max_state_version(),
        pre_protocol_update_state_version.next().unwrap()
    );
}

fn create_state_manager(
    config: StateManagerConfig,
    protocol_updater_factory: Box<dyn ProtocolUpdaterFactory + Sync + Send>,
) -> StateManager {
    StateManager::new(
        config,
        None,
        &LockFactory::new("testing"),
        protocol_updater_factory,
        &Registry::new(),
        &Scheduler::new("testing"),
    )
}
