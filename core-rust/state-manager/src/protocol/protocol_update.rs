// This file contains the protocol update logic for specific protocol versions

use node_common::locks::{LockFactory, RwLock, StateLock};
use radix_engine::track::StateUpdates;
use radix_engine::transaction::CostingParameters;
use radix_engine_common::network::NetworkDefinition;
use radix_engine_common::prelude::Decimal;
use std::ops::Deref;
use std::sync::Arc;

use transaction::prelude::TransactionPayloadPreparable;
use transaction::validation::{NotarizedTransactionValidator, ValidationConfig};
use utils::btreemap;

use crate::epoch_handling::EpochAwareAccuTreeFactory;
use crate::mainnet_updates::ProductionProtocolUpdaterFactory;
use crate::traits::{
    CommitBundle, CommitStore, CommittedTransactionBundle, HashTreeUpdate, QueryableProofStore,
    ReceiptAccuTreeSliceV1, SubstateStoreUpdate, TransactionAccuTreeSliceV1,
};
use crate::transaction::{
    ExecutionConfigurator, FlashTransactionV1, LedgerTransaction, LedgerTransactionValidator,
    PreparedLedgerTransaction, TransactionSeriesExecutor,
};
use crate::{
    CommittedTransactionIdentifiers, ExecutionCache, LedgerHeader, LedgerProof, LedgerProofOrigin,
    LoggingConfig, ProtocolState, StateManagerDatabase,
};

pub trait ProtocolUpdaterFactory {
    fn updater_for(&self, protocol_version_name: &str) -> Option<Box<dyn ProtocolUpdater>>;
}

/// Protocol update consists of two events:
/// 1) Updating the current (state computer) configuration ("transaction processing rules").
///    This includes: transaction validation, execution configuration, etc
/// 2) Executing arbitrary state updates against the current database state.
///    While the abstraction is quite flexible, the only concrete implementation at the moment
///    only modifies the state through committing system transactions (e.g. substate flash).
pub trait ProtocolUpdater {
    /// Returns the new configuration that the state computer
    /// should use after enacting the given protocol version.
    fn updatable_config(&self) -> UpdatableStateComputerConfig;

    /// Executes these state updates associated with the given protocol version
    /// that haven't yet been applied
    /// (hence "remaining", e.g. if node is restarted mid-protocol update).
    fn execute_remaining_state_updates(&self, store: Arc<StateLock<StateManagerDatabase>>);
}

#[derive(Clone, Debug)]
pub struct UpdatableStateComputerConfig {
    pub network: NetworkDefinition,
    pub logging_config: LoggingConfig,
    pub validation_config: ValidationConfig,
    pub costing_parameters: CostingParameters,
}

impl UpdatableStateComputerConfig {
    pub fn default(network: NetworkDefinition) -> UpdatableStateComputerConfig {
        let network_id = network.id;
        UpdatableStateComputerConfig {
            network,
            logging_config: LoggingConfig::default(),
            validation_config: ValidationConfig::default(network_id),
            costing_parameters: CostingParameters::default(),
        }
    }
}

impl UpdatableStateComputerConfig {
    pub fn ledger_transaction_validator(&self) -> LedgerTransactionValidator {
        LedgerTransactionValidator::default_from_validation_config(self.validation_config)
    }

    pub fn user_transaction_validator(&self) -> NotarizedTransactionValidator {
        NotarizedTransactionValidator::new(self.validation_config)
    }

    pub fn validation_config(&self) -> ValidationConfig {
        self.validation_config
    }

    pub fn execution_configurator(&self, no_fees: bool) -> ExecutionConfigurator {
        let mut costing_parameters = self.costing_parameters;
        if no_fees {
            costing_parameters.execution_cost_unit_price = Decimal::ZERO;
            costing_parameters.finalization_cost_unit_price = Decimal::ZERO;
            costing_parameters.state_storage_price = Decimal::ZERO;
            costing_parameters.archive_storage_price = Decimal::ZERO;
        }
        ExecutionConfigurator::new(&self.network, &self.logging_config, costing_parameters)
    }
}

/// A protocol updater implementation that only changes the configuration
/// and does not commit any state updates.
pub struct NoStateUpdatesProtocolUpdater {
    updatable_config: UpdatableStateComputerConfig,
}

impl NoStateUpdatesProtocolUpdater {
    pub fn default(network: NetworkDefinition) -> Self {
        Self {
            updatable_config: UpdatableStateComputerConfig::default(network),
        }
    }
}

impl ProtocolUpdater for NoStateUpdatesProtocolUpdater {
    fn updatable_config(&self) -> UpdatableStateComputerConfig {
        self.updatable_config.clone()
    }

    fn execute_remaining_state_updates(&self, _store: Arc<StateLock<StateManagerDatabase>>) {
        // no-op
    }
}

pub struct FixedFlashProtocolUpdater {
    protocol_version_name: String,
    updatable_config: UpdatableStateComputerConfig,
    flash_txns: Vec<FlashTransactionV1>,
}

impl FixedFlashProtocolUpdater {
    pub fn new_with_default_configurator(
        protocol_version_name: String,
        network: NetworkDefinition,
        flash_txns: Vec<FlashTransactionV1>,
    ) -> Self {
        Self {
            protocol_version_name,
            updatable_config: UpdatableStateComputerConfig::default(network),
            flash_txns,
        }
    }
}

impl ProtocolUpdater for FixedFlashProtocolUpdater {
    fn updatable_config(&self) -> UpdatableStateComputerConfig {
        self.updatable_config.clone()
    }

    fn execute_remaining_state_updates(&self, store: Arc<StateLock<StateManagerDatabase>>) {
        // We're using the new configuration to execute the protocol update
        // transactions (although it's not a requirement).
        let updatable_config = self.updatable_config();
        let mut txn_committer = ProtocolUpdateFlashTxnCommitter::new(
            self.protocol_version_name.clone(),
            store,
            updatable_config.execution_configurator(true), /* No fees for protocol updates */
            updatable_config.ledger_transaction_validator(),
        );

        while let Some(next_batch_idx) = txn_committer.next_committable_batch_idx() {
            let maybe_next_flash_txn = self.flash_txns.get(next_batch_idx as usize);
            if let Some(next_flash_txn) = maybe_next_flash_txn {
                txn_committer.commit_flash(next_flash_txn.clone());
            } else {
                // Nothing more to commit
                break;
            }
        }
    }
}

enum ProtocolUpdateProgress {
    UpdateInitiatedButNothingCommitted {
        protocol_version_name: String,
    },
    UpdateInProgress {
        protocol_version_name: String,
        last_batch_idx: u32,
    },
    /// This means that the last proof contains no notion of a protocol update,
    /// which in practice almost always means that it has already executed in full.
    /// But we leave this interpretation to the caller,
    /// so here we just call it "not updating".
    NotUpdating,
}

/// A helper that manages committing flash transactions state updates.
/// It handles the logic to fulfill the resumability contract of "execute_remaining_state_updates"
/// by storing the index of a previously committed transaction batch in the ledger proof.
pub struct ProtocolUpdateFlashTxnCommitter {
    protocol_version_name: String,
    store: Arc<StateLock<StateManagerDatabase>>,
    execution_configurator: RwLock<ExecutionConfigurator>,
    ledger_transaction_validator: LedgerTransactionValidator,
}

impl ProtocolUpdateFlashTxnCommitter {
    pub fn new(
        protocol_version_name: String,
        store: Arc<StateLock<StateManagerDatabase>>,
        execution_configurator: ExecutionConfigurator,
        ledger_transaction_validator: LedgerTransactionValidator,
    ) -> Self {
        Self {
            protocol_version_name,
            store,
            execution_configurator: LockFactory::new("protocol_update")
                .new_rwlock(execution_configurator),
            ledger_transaction_validator,
        }
    }

    fn read_protocol_update_progress(&self) -> ProtocolUpdateProgress {
        let Some(latest_proof) = self.store.read_current().get_latest_proof() else {
            return ProtocolUpdateProgress::NotUpdating;
        };

        match &latest_proof.origin {
            LedgerProofOrigin::Genesis { .. } => ProtocolUpdateProgress::NotUpdating,
            LedgerProofOrigin::Consensus { .. } => {
                if let Some(latest_proof_protocol_version) =
                    latest_proof.ledger_header.next_protocol_version
                {
                    ProtocolUpdateProgress::UpdateInitiatedButNothingCommitted {
                        protocol_version_name: latest_proof_protocol_version,
                    }
                } else {
                    ProtocolUpdateProgress::NotUpdating
                }
            }
            LedgerProofOrigin::ProtocolUpdate {
                protocol_version_name,
                batch_idx,
            } => ProtocolUpdateProgress::UpdateInProgress {
                protocol_version_name: protocol_version_name.to_string(),
                last_batch_idx: *batch_idx,
            },
        }
    }

    pub fn next_committable_batch_idx(&self) -> Option<u32> {
        match self.read_protocol_update_progress() {
            ProtocolUpdateProgress::UpdateInitiatedButNothingCommitted {
                protocol_version_name: state_protocol_version_name,
            } => {
                if self.protocol_version_name == state_protocol_version_name {
                    Some(0)
                } else {
                    None
                }
            }
            ProtocolUpdateProgress::UpdateInProgress {
                protocol_version_name: state_protocol_version_name,
                last_batch_idx,
            } => {
                if self.protocol_version_name == state_protocol_version_name {
                    Some(last_batch_idx.checked_add(1).unwrap())
                } else {
                    None
                }
            }
            ProtocolUpdateProgress::NotUpdating => None,
        }
    }

    pub fn commit_flash(&mut self, flash_txn: FlashTransactionV1) {
        self.commit_flash_batch(vec![flash_txn]);
    }

    /// Commits a batch of flash transactions, followed by a single
    /// proof (of protocol update origin).
    pub fn commit_flash_batch(&mut self, flash_txns: Vec<FlashTransactionV1>) {
        let flash_ledger_txns = flash_txns
            .into_iter()
            .map(|flash_txn| LedgerTransaction::FlashV1(Box::new(flash_txn)))
            .collect();
        self.commit_txn_batch(flash_ledger_txns);
    }

    fn commit_txn_batch(&mut self, transactions: Vec<LedgerTransaction>) {
        let batch_idx = self
            .next_committable_batch_idx()
            .expect("Can't commit next protocol update batch");

        let read_store = self.store.read_current();
        let latest_proof: LedgerProof = read_store
            .get_latest_proof()
            .expect("Pre-genesis protocol updates are currently not supported");

        let lock_factory = LockFactory::new("protocol_update");
        let execution_cache = lock_factory.new_mutex(ExecutionCache::new(
            latest_proof.ledger_header.hashes.transaction_root,
        ));
        // For the purpose of executing protocol update transactions we're just going to use
        // a dummy protocol state with no configured updates and the name of this (in progress)
        // protocol update as the current version (although that could really be any string,
        // it doesn't matter here).
        let dummy_protocol_state = ProtocolState {
            current_epoch: None,
            current_protocol_version: self.protocol_version_name.clone(),
            enacted_protocol_updates: btreemap!(),
            pending_protocol_updates: vec![],
        };

        let mut series_executor = TransactionSeriesExecutor::new(
            read_store.deref(),
            &execution_cache,
            &self.execution_configurator,
            dummy_protocol_state,
        );

        // TODO: extract common code from here and StateComputer::commit (also see the comment there)
        let mut committed_transaction_bundles = Vec::new();
        let mut substate_store_update = SubstateStoreUpdate::new();
        let mut state_tree_update = HashTreeUpdate::new();
        let mut new_node_ancestry_records = Vec::new();
        let epoch_accu_trees = EpochAwareAccuTreeFactory::new(
            series_executor.epoch_identifiers().state_version,
            series_executor.latest_state_version(),
        );
        let mut transaction_tree_slice_merger = epoch_accu_trees.create_merger();
        let mut receipt_tree_slice_merger = epoch_accu_trees.create_merger();

        for transaction in transactions {
            let raw = transaction.to_raw().unwrap();
            let prepared = PreparedLedgerTransaction::prepare_from_raw(&raw).unwrap();
            let validated = self.ledger_transaction_validator.validate_flash(prepared);

            let commit = series_executor
                .execute_and_update_state(&validated, "flash protocol update")
                .expect("protocol update not committable")
                .expect_success("protocol update");

            substate_store_update.apply(commit.database_updates);
            let hash_structures_diff = commit.hash_structures_diff;
            state_tree_update.add(
                series_executor.latest_state_version(),
                hash_structures_diff.state_hash_tree_diff,
            );
            new_node_ancestry_records.extend(commit.new_substate_node_ancestry_records);
            transaction_tree_slice_merger.append(hash_structures_diff.transaction_tree_diff.slice);
            receipt_tree_slice_merger.append(hash_structures_diff.receipt_tree_diff.slice);

            let proposer_timestamp_ms = latest_proof.ledger_header.proposer_timestamp_ms;
            committed_transaction_bundles.push(CommittedTransactionBundle {
                state_version: series_executor.latest_state_version(),
                raw,
                receipt: commit.local_receipt,
                identifiers: CommittedTransactionIdentifiers {
                    payload: validated.create_identifiers(),
                    resultant_ledger_hashes: *series_executor.latest_ledger_hashes(),
                    proposer_timestamp_ms,
                },
            });
        }

        let resultant_state_version = series_executor.latest_state_version();
        let resultant_ledger_hashes = *series_executor.latest_ledger_hashes();
        let proof = LedgerProof {
            ledger_header: LedgerHeader {
                epoch: latest_proof.ledger_header.epoch,
                round: latest_proof.ledger_header.round,
                state_version: resultant_state_version,
                hashes: resultant_ledger_hashes,
                consensus_parent_round_timestamp_ms: latest_proof
                    .ledger_header
                    .consensus_parent_round_timestamp_ms,
                proposer_timestamp_ms: latest_proof.ledger_header.proposer_timestamp_ms,
                next_epoch: series_executor.next_epoch().cloned(),
                next_protocol_version: None,
            },
            origin: LedgerProofOrigin::ProtocolUpdate {
                protocol_version_name: self.protocol_version_name.clone(),
                batch_idx,
            },
        };

        let commit_bundle = CommitBundle {
            transactions: committed_transaction_bundles,
            proof,
            substate_store_update,
            vertex_store: None,
            state_tree_update,
            transaction_tree_slice: TransactionAccuTreeSliceV1(
                transaction_tree_slice_merger.into_slice(),
            ),
            receipt_tree_slice: ReceiptAccuTreeSliceV1(receipt_tree_slice_merger.into_slice()),
            new_substate_node_ancestry_records: new_node_ancestry_records,
        };

        drop(read_store);

        self.store.write_current().commit(commit_bundle);
    }
}

pub struct TestingDefaultProtocolUpdaterFactory {
    network: NetworkDefinition,
    mainnet_protocol_updater_factory: ProductionProtocolUpdaterFactory,
}

impl TestingDefaultProtocolUpdaterFactory {
    pub fn new(network: NetworkDefinition) -> TestingDefaultProtocolUpdaterFactory {
        TestingDefaultProtocolUpdaterFactory {
            network: network.clone(),
            mainnet_protocol_updater_factory: ProductionProtocolUpdaterFactory::new(network),
        }
    }
}

impl ProtocolUpdaterFactory for TestingDefaultProtocolUpdaterFactory {
    fn updater_for(&self, protocol_version_name: &str) -> Option<Box<dyn ProtocolUpdater>> {
        // Default testing updater delegates to mainnet updater if protocol update matches or,
        // if not, returns a default updater with a single empty flash transaction.
        self.mainnet_protocol_updater_factory
            .updater_for(protocol_version_name)
            .or(Some(Box::new(
                FixedFlashProtocolUpdater::new_with_default_configurator(
                    protocol_version_name.to_owned(),
                    self.network.clone(),
                    vec![FlashTransactionV1 {
                        name: format!("{}-txn", protocol_version_name),
                        state_updates: StateUpdates::default(),
                    }],
                ),
            )))
    }
}
