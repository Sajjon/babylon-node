use radix_engine::blueprints::consensus_manager::EpochChangeEvent;
use radix_engine::prelude::{ScryptoCategorize, ScryptoDecode, ScryptoEncode};
use std::cmp::Ordering;
use std::collections::BTreeMap;

use radix_engine_common::constants::CONSENSUS_MANAGER;

use radix_engine_common::math::Decimal;
use radix_engine_common::prelude::scrypto_decode;

use radix_engine_common::types::Epoch;
use radix_engine_interface::api::ModuleId;
use radix_engine_interface::prelude::CheckedMul;
use radix_engine_interface::prelude::Emitter;
use tracing::info;

use crate::traits::{IterableProofStore, QueryableProofStore, QueryableTransactionStore};
use crate::ProtocolUpdateEnactmentCondition::{
    EnactUnconditionallyAtEpoch, EnactWhenSupportedAndWithinBounds,
};
use crate::{
    LocalTransactionReceipt, ProtocolConfig, ProtocolUpdate, SignalledReadinessThreshold,
    StateVersion,
};

// This file contains types and utilities for
// managing the (dynamic) protocol state of a running node.

#[derive(Clone, Debug, Eq, PartialEq, ScryptoCategorize, ScryptoEncode, ScryptoDecode)]
pub struct ProtocolState {
    pub current_epoch: Option<Epoch>,
    pub current_protocol_version: String,
    pub enacted_protocol_updates: BTreeMap<StateVersion, String>,
    pub pending_protocol_updates: Vec<PendingProtocolUpdate>,
}

#[derive(Clone, Debug, Eq, PartialEq, ScryptoCategorize, ScryptoEncode, ScryptoDecode)]
pub struct PendingProtocolUpdate {
    pub protocol_update: ProtocolUpdate,
    pub state: PendingProtocolUpdateState,
}

#[derive(Clone, Debug, Eq, PartialEq, ScryptoCategorize, ScryptoEncode, ScryptoDecode)]
pub enum PendingProtocolUpdateState {
    ForSignalledReadinessSupportCondition {
        thresholds_state: Vec<(
            // Thresholds here are kept just for convenience,
            // they duplicate the ones in `protocol_update.enactment_condition`.
            SignalledReadinessThreshold,
            SignalledReadinessThresholdState,
        )>,
    },
    // Empty placeholder for all other stateless conditions
    Empty,
}

#[derive(Clone, Debug, Eq, PartialEq, ScryptoCategorize, ScryptoEncode, ScryptoDecode)]
pub struct SignalledReadinessThresholdState {
    /// A number of consecutive epochs on or above the threshold,
    /// including the current (uncompleted) epoch.
    pub consecutive_started_epochs_of_support: u64,
}

#[derive(Clone, Debug, Eq, PartialEq)]
pub enum InitialProtocolUpdateStatus {
    ExpectedToHaveBeenEnactedAtStateVersion(StateVersion),
    Pending(PendingProtocolUpdateState),
    ExpiredUnenacted,
}

fn compute_initial_protocol_update_status<
    S: QueryableProofStore + IterableProofStore + QueryableTransactionStore,
>(
    store: &S,
    protocol_update: &ProtocolUpdate,
) -> InitialProtocolUpdateStatus {
    match &protocol_update.enactment_condition {
        EnactWhenSupportedAndWithinBounds {
            lower_bound,
            upper_bound,
            readiness_thresholds,
        } => compute_initial_signalled_readiness_protocol_update_status(
            store,
            protocol_update,
            lower_bound,
            upper_bound,
            readiness_thresholds,
        ),
        EnactUnconditionallyAtEpoch(epoch) => {
            compute_initial_at_epoch_protocol_update_status(store, *epoch)
        }
    }
}

fn compute_initial_signalled_readiness_protocol_update_status<
    S: QueryableProofStore + IterableProofStore + QueryableTransactionStore,
>(
    store: &S,
    protocol_update: &ProtocolUpdate,
    lower_bound: &Epoch,
    upper_bound: &Epoch,
    thresholds: &[SignalledReadinessThreshold],
) -> InitialProtocolUpdateStatus {
    // Mutable var for the initial state that we'll compute in this fn
    let mut thresholds_state: Vec<(
        SignalledReadinessThreshold,
        SignalledReadinessThresholdState,
    )> = thresholds
        .iter()
        .map(|threshold| {
            (
                threshold.clone(),
                SignalledReadinessThresholdState {
                    consecutive_started_epochs_of_support: 0,
                },
            )
        })
        .collect();

    // Highest number of required epochs of support from all thresholds
    let max_required_consecutive_epochs_of_support = thresholds
        .iter()
        .map(|threshold| threshold.required_consecutive_completed_epochs_of_support)
        .max()
        .expect("No thresholds found in signalled readiness protocol update");

    // The earliest epoch where we need to consider the readiness signals
    let earliest_relevant_epoch = Epoch::of(
        lower_bound
            .number()
            .saturating_sub(max_required_consecutive_epochs_of_support),
    );

    // Start iterating from the earliest relevant epoch
    // (or the earliest epoch we have, i.e. genesis)
    let epoch_change_event_iter = epoch_change_iter(store, earliest_relevant_epoch);

    for (state_version, epoch_change_event) in epoch_change_event_iter {
        // Update the thresholds
        update_thresholds_state_at_epoch_change(
            protocol_update,
            &epoch_change_event,
            &mut thresholds_state,
        );

        // Check if any threshold passes
        let any_threshold_passes = any_threshold_passes(&thresholds_state);
        if !any_threshold_passes {
            continue;
        }

        // Check if we're on or above the lower bound
        let on_or_above_lower_bound = epoch_change_event.epoch.number() >= lower_bound.number();
        if !on_or_above_lower_bound {
            continue;
        }

        // Check if we're on or below the upper bound
        let on_or_below_upper_bound = epoch_change_event.epoch.number() <= upper_bound.number();
        if !on_or_below_upper_bound {
            continue;
        }

        // It's a match! This protocol update enacts at exactly the state version
        // corresponding to the current epoch change.
        return InitialProtocolUpdateStatus::ExpectedToHaveBeenEnactedAtStateVersion(state_version);
    }

    // The protocol update wasn't enacted yet, so just return the latest computed state
    // as the initial state.
    InitialProtocolUpdateStatus::Pending(
        PendingProtocolUpdateState::ForSignalledReadinessSupportCondition { thresholds_state },
    )
}

fn compute_initial_at_epoch_protocol_update_status<S: QueryableProofStore>(
    store: &S,
    epoch: Epoch,
) -> InitialProtocolUpdateStatus {
    if let Some(epoch_proof) = store.get_epoch_proof(epoch) {
        InitialProtocolUpdateStatus::ExpectedToHaveBeenEnactedAtStateVersion(
            epoch_proof.ledger_header.state_version,
        )
    } else {
        InitialProtocolUpdateStatus::Pending(PendingProtocolUpdateState::Empty)
    }
}

impl ProtocolState {
    pub fn compute_initial<
        S: QueryableProofStore + IterableProofStore + QueryableTransactionStore,
    >(
        store: &S,
        protocol_config: &ProtocolConfig,
    ) -> ProtocolState {
        let current_epoch_opt = store
            .get_latest_epoch_proof()
            .map(|proof| proof.ledger_header.next_epoch.unwrap().epoch);

        // For each configured protocol update we calculate it's expected status against
        // the current state of the ledger, regardless of any information stored
        // about the protocol updates that were actually enacted.
        // This is then juxtaposed with the protocol updates that have actually been enacted,
        // to catch any inconsistencies.
        // This serves mainly to protect from misconfiguration (e.g.
        // running a node with a configured protocol update for the past state version,
        // which hasn't been executed on the local ledger at the right time).
        // This also provides the initial state for stateful (readiness-based)
        // protocol update conditions.
        let initial_statuses: Vec<_> = protocol_config
            .protocol_updates
            .iter()
            .map(|protocol_update| {
                (
                    protocol_update,
                    compute_initial_protocol_update_status(store, protocol_update),
                )
            })
            .collect();

        let expected_already_enacted_protocol_updates: BTreeMap<StateVersion, String> =
            initial_statuses
                .iter()
                .flat_map(|(protocol_update, status)| match status {
                    InitialProtocolUpdateStatus::ExpectedToHaveBeenEnactedAtStateVersion(
                        state_version,
                    ) => Some((
                        *state_version,
                        protocol_update.next_protocol_version.clone(),
                    )),
                    InitialProtocolUpdateStatus::Pending(_)
                    | InitialProtocolUpdateStatus::ExpiredUnenacted => None,
                })
                .collect();

        let actually_enacted_protocol_updates: BTreeMap<StateVersion, String> = store
            .get_protocol_update_init_proof_iter(StateVersion::pre_genesis())
            .map(|proof| {
                (
                    proof.ledger_header.state_version,
                    proof
                        .ledger_header
                        .next_protocol_version
                        .expect("next_protocol_version is missing in protocol update proof"),
                )
            })
            .collect();

        if expected_already_enacted_protocol_updates != actually_enacted_protocol_updates {
            panic!(
                "State computer couldn't be initialized, protocol misconfiguration: \
             according to the current configuration and the ledger state the following \
             protocol updates should have been enacted: {:?}, but the following \
             updates were actually enacted: {:?}.",
                expected_already_enacted_protocol_updates, actually_enacted_protocol_updates,
            );
        }

        let current_protocol_version = actually_enacted_protocol_updates
            .last_key_value()
            .map(|(_, protocol_version)| protocol_version)
            .unwrap_or(&protocol_config.genesis_protocol_version)
            .clone();

        let pending_protocol_updates = initial_statuses
            .into_iter()
            .flat_map(|(protocol_update, status)| match status {
                InitialProtocolUpdateStatus::ExpectedToHaveBeenEnactedAtStateVersion(_) => None,
                InitialProtocolUpdateStatus::Pending(state) => Some(PendingProtocolUpdate {
                    protocol_update: protocol_update.clone(),
                    state,
                }),
                InitialProtocolUpdateStatus::ExpiredUnenacted => None,
            })
            .collect();

        ProtocolState {
            current_epoch: current_epoch_opt,
            current_protocol_version,
            enacted_protocol_updates: actually_enacted_protocol_updates,
            pending_protocol_updates,
        }
    }

    /// Computes a new protocol state (after executing a transaction) and
    /// optionally returns a next protocol version if a protocol update has been enacted.
    pub fn compute_next(
        self: &ProtocolState,
        local_receipt: &LocalTransactionReceipt,
        post_execute_state_version: StateVersion,
    ) -> (ProtocolState, Option<String>) {
        let mut new_protocol_state = self.clone();

        let Some(post_execute_epoch) = local_receipt
            .local_execution
            .next_epoch
            .as_ref()
            .map(|next_epoch| next_epoch.epoch)
            .or(self.current_epoch)
        else {
            // We're pre-genesis, so just return the current state
            return (new_protocol_state, None);
        };
        new_protocol_state.current_epoch = Some(post_execute_epoch);

        let mut pending_protocol_updates = vec![];
        let mut expired_protocol_updates = vec![];
        // Only a single protocol update can be enacted at a time.
        // We collect the results into a list to verify this.
        let mut enactable_protocol_updates = vec![];
        for mut pending_protocol_update in new_protocol_state.pending_protocol_updates {
            match &pending_protocol_update.protocol_update.enactment_condition {
                EnactWhenSupportedAndWithinBounds {
                    lower_bound,
                    upper_bound,
                    ..
                } => {
                    // Note: this is not a pure boolean calculation,
                    // it also updates the state.
                    let has_sufficient_support = {
                        if let PendingProtocolUpdateState::ForSignalledReadinessSupportCondition {
                            ref mut thresholds_state,
                        } = &mut pending_protocol_update.state
                        {
                            // If this was an epoch change, update the thresholds state
                            if let Some(epoch_change_event) =
                                &local_receipt.local_execution.next_epoch
                            {
                                update_thresholds_state_at_epoch_change(
                                    &pending_protocol_update.protocol_update,
                                    epoch_change_event,
                                    thresholds_state,
                                );
                            }
                            // Regardless of whether this was an epoch change or not,
                            // check if any threshold currently passes.
                            any_threshold_passes(thresholds_state)
                        } else {
                            panic!("Invalid protocol state")
                        }
                    };

                    let on_or_above_lower_bound = post_execute_epoch >= *lower_bound;

                    let on_or_below_upper_bound = post_execute_epoch <= *upper_bound;

                    if has_sufficient_support && on_or_above_lower_bound && on_or_below_upper_bound
                    {
                        enactable_protocol_updates.push(
                            pending_protocol_update
                                .protocol_update
                                .next_protocol_version,
                        );
                    } else if on_or_below_upper_bound {
                        pending_protocol_updates.push(pending_protocol_update);
                    } else {
                        expired_protocol_updates.push(pending_protocol_update);
                    }
                }
                EnactUnconditionallyAtEpoch(enactment_epoch) => {
                    if let Some(next_epoch) = &local_receipt.local_execution.next_epoch {
                        match next_epoch.epoch.cmp(enactment_epoch) {
                            Ordering::Less => {
                                pending_protocol_updates.push(pending_protocol_update)
                            }
                            Ordering::Equal => enactable_protocol_updates.push(
                                pending_protocol_update
                                    .protocol_update
                                    .next_protocol_version,
                            ),
                            Ordering::Greater => {
                                expired_protocol_updates.push(pending_protocol_update)
                            }
                        }
                    } else {
                        // Not an epoch change
                        pending_protocol_updates.push(pending_protocol_update);
                    }
                }
            }
        }

        // This isn't really a right place for this log, but will do for now
        for expired_protocol_update in expired_protocol_updates {
            info!(
                "Protocol update {:?} expires unenacted at state version {:?}",
                expired_protocol_update
                    .protocol_update
                    .next_protocol_version,
                post_execute_state_version
            );
        }

        if enactable_protocol_updates.len() > 1 {
            panic!(
                "Invalid state: more than one protocol update is enactable at state version {:?}",
                post_execute_state_version
            )
        }
        let next_protocol_version = enactable_protocol_updates.into_iter().next();

        new_protocol_state.pending_protocol_updates = pending_protocol_updates;
        if let Some(next_protocol_version) = next_protocol_version.as_ref() {
            new_protocol_state.enacted_protocol_updates.insert(
                post_execute_state_version,
                next_protocol_version.to_string(),
            );
        }

        (new_protocol_state, next_protocol_version)
    }
}

fn any_threshold_passes(
    thresholds_state: &[(
        SignalledReadinessThreshold,
        SignalledReadinessThresholdState,
    )],
) -> bool {
    thresholds_state.iter().any(|(threshold, threshold_state)| {
        // Note: `consecutive_started_epochs_of_support` must be strictly greater because
        // it includes the current (uncompleted) epoch, while the threshold condition
        // specifies fully completed epochs.
        threshold_state.consecutive_started_epochs_of_support
            > threshold.required_consecutive_completed_epochs_of_support
    })
}

fn update_thresholds_state_at_epoch_change(
    protocol_update: &ProtocolUpdate,
    epoch_change_event: &EpochChangeEvent,
    thresholds_state: &mut Vec<(
        SignalledReadinessThreshold,
        SignalledReadinessThresholdState,
    )>,
) {
    let signalled_stake_readiness = epoch_change_event
        .significant_protocol_update_readiness
        .get(&protocol_update.readiness_signal_name())
        .cloned()
        .unwrap_or_else(Decimal::zero);

    let total_stake = epoch_change_event
        .validator_set
        .total_active_stake_xrd()
        .expect("Failed to calculate the total stake");

    // Update each threshold according to its required_percentage_stake_supported
    for (threshold, threshold_state) in &mut *thresholds_state {
        let required_stake = total_stake
            .checked_mul(threshold.required_ratio_of_stake_supported)
            .expect("Failed to calculate required stake for a protocol update");
        if signalled_stake_readiness >= required_stake {
            // Support on or above threshold: inc num of consecutive epochs
            threshold_state.consecutive_started_epochs_of_support = threshold_state
                .consecutive_started_epochs_of_support
                .saturating_add(1);
        } else {
            // Not enough support: reset to 0
            threshold_state.consecutive_started_epochs_of_support = 0;
        }
    }
}

/// A helper that iterates epoch proofs and extracts
/// EpochChangeEvents from corresponding ledger receipts.
pub fn epoch_change_iter<'s, S: IterableProofStore + QueryableTransactionStore>(
    store: &'s S,
    from_epoch: Epoch,
) -> Box<dyn Iterator<Item = (StateVersion, EpochChangeEvent)> + 's> {
    let epoch_iter = store.get_epoch_proof_iter(from_epoch);
    Box::new(epoch_iter.map(|epoch_proof| {
        let next_epoch = epoch_proof
            .ledger_header
            .epoch
            .next()
            .expect("Epoch overflow");
        let state_version = epoch_proof.ledger_header.state_version;
        let epoch_receipt = store
            .get_committed_ledger_transaction_receipt(state_version)
            .unwrap_or_else(|| {
                panic!(
                    "Missing transaction receipt for epoch change transaction \
                        (next_epoch={}, state_version={})",
                    next_epoch.number(),
                    state_version
                )
            });

        let epoch_change_event = epoch_receipt
            .application_events
            .iter()
            .filter(|ev| {
                ev.type_id.0 == Emitter::Method(CONSENSUS_MANAGER.into_node_id(), ModuleId::Main)
                    && ev.type_id.1 == "EpochChangeEvent"
            })
            .map(|ev| {
                scrypto_decode::<EpochChangeEvent>(&ev.data)
                    .expect("Could not decode EpochChangeEvent")
            })
            .next()
            .unwrap_or_else(|| {
                panic!(
                    "Epoch change transaction receipt does not contain an EpochChangeEvent \
                    (next_epoch={}, state_version={}",
                    next_epoch.number(),
                    state_version
                )
            });
        (state_version, epoch_change_event)
    }))
}
