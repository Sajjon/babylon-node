use parking_lot::RwLock;
use std::ops::{Deref, Range};
use std::sync::Arc;
use std::time::Duration;

use radix_engine::ledger::ReadableSubstateStore;
use radix_engine::transaction::{
    AbortReason, PreviewError, PreviewResult, TransactionReceipt, TransactionResult,
};

use radix_engine_common::crypto::{Hash, PublicKey};
use radix_engine_common::network::NetworkDefinition;

use crate::transaction::ledger_transaction::LedgerTransaction;
use radix_engine_interface::api::node_modules::auth::AuthAddresses;
use radix_engine_interface::data::manifest::{manifest_decode, manifest_encode};

use crate::query::StateManagerSubstateQueries;
use crate::staging::ReadableStore;
use crate::store::traits::TransactionIndex;
use crate::transaction::{ConfigType, ExecutionConfigurator, TransactionLogic};
use crate::{HasIntentHash, IntentHash, PreviewRequest, RejectionReason};
use transaction::ecdsa_secp256k1::EcdsaSecp256k1PrivateKey;
use transaction::errors::TransactionValidationError;
use transaction::model::{
    Executable, NotarizedTransaction, PreviewFlags, PreviewIntent, TransactionHeader,
    TransactionIntent,
};
use transaction::validation::{IntentHashManager, ValidationConfig};
use transaction::validation::{NotarizedTransactionValidator, TransactionValidator};
use utils::btreeset;

pub struct UserTransactionValidator {
    pub validation_config: ValidationConfig,
}

// TODO: consider use of radix-engine-constans::MAX_TRANSACTION_SIZE here
pub const OVERRIDE_MAX_PAYLOAD_SIZE: usize = 1024 * 1024;

// Add a few extra bytes for the enum disciminator at the start(!)
pub const OVERRIDE_LEDGER_MAX_PAYLOAD_SIZE: usize = OVERRIDE_MAX_PAYLOAD_SIZE + 10;

impl UserTransactionValidator {
    pub fn new(network: &NetworkDefinition) -> Self {
        Self {
            validation_config: ValidationConfig::default(network.id),
        }
    }

    /// Checks the Payload max size, and SBOR decodes to a NotarizedTransaction if the size is okay
    pub fn parse_unvalidated_user_transaction_from_slice(
        transaction_payload: &[u8],
    ) -> Result<NotarizedTransaction, TransactionValidationError> {
        if transaction_payload.len() > OVERRIDE_MAX_PAYLOAD_SIZE {
            return Err(TransactionValidationError::TransactionTooLarge);
        }

        let transaction: NotarizedTransaction = manifest_decode(transaction_payload)
            .map_err(TransactionValidationError::DeserializationError)?;

        Ok(transaction)
    }

    /// Performs static validation only
    pub fn validate_and_create_executable<'a>(
        &self,
        transaction: &'a NotarizedTransaction,
        payload_size: usize,
    ) -> Result<Executable<'a>, TransactionValidationError> {
        let validator = NotarizedTransactionValidator::new(self.validation_config);
        validator.validate(transaction, payload_size, &NoopIntentHashManager {})
    }
}

pub struct LedgerTransactionValidator {
    pub validation_config: ValidationConfig,
}

impl LedgerTransactionValidator {
    pub fn new(network: &NetworkDefinition) -> Self {
        Self {
            validation_config: ValidationConfig::default(network.id),
        }
    }

    pub fn parse_unvalidated_transaction_from_slice(
        transaction_payload: &[u8],
    ) -> Result<LedgerTransaction, TransactionValidationError> {
        if transaction_payload.len() > OVERRIDE_LEDGER_MAX_PAYLOAD_SIZE {
            return Err(TransactionValidationError::TransactionTooLarge);
        }

        let transaction: LedgerTransaction = manifest_decode(transaction_payload)
            .map_err(TransactionValidationError::DeserializationError)?;

        Ok(transaction)
    }

    pub fn validate_and_create_executable<'a>(
        &self,
        ledger_transaction: &'a LedgerTransaction,
    ) -> Result<Executable<'a>, TransactionValidationError> {
        let validator = NotarizedTransactionValidator::new(self.validation_config);
        match ledger_transaction {
            LedgerTransaction::User(notarized_transaction) => {
                // TODO: Remove
                let payload_size = manifest_encode(notarized_transaction).unwrap().len();
                validator.validate(
                    notarized_transaction,
                    payload_size,
                    &NoopIntentHashManager {},
                )
            }
            LedgerTransaction::Validator(validator_transaction) => {
                let prepared = validator_transaction.prepare();
                Ok(prepared.to_executable())
            }
            LedgerTransaction::System(system_transaction) => {
                Ok(system_transaction.get_executable(btreeset!(AuthAddresses::system_role())))
            }
        }
    }
}

const UP_TO_FEE_LOAN_RUNTIME_WARN_THRESHOLD: Duration = Duration::from_millis(100);

/// A validator for `NotarizedTransaction`, deciding whether they would rejected or not-rejected
/// (i.e. "commitable") at a specific state of the `store`.
pub struct CommitableTransactionValidator<S> {
    store: Arc<RwLock<S>>,
    execution_configurator: Arc<ExecutionConfigurator>,
    user_transaction_validator: UserTransactionValidator,
}

impl<S> CommitableTransactionValidator<S> {
    pub fn new(
        network: &NetworkDefinition,
        store: Arc<RwLock<S>>,
        execution_configurator: Arc<ExecutionConfigurator>,
    ) -> Self {
        Self {
            store,
            execution_configurator,
            user_transaction_validator: UserTransactionValidator::new(network),
        }
    }
}

impl<S> CommitableTransactionValidator<S>
where
    S: ReadableStore,
    S: for<'a> TransactionIndex<&'a IntentHash>,
{
    /// Validates the transaction (`UserTransactionValidator`) and executes it up to fee loan, to
    /// determine whether it would be rejected given the current state of the substate store.
    pub fn check_for_rejection(
        &self,
        transaction: &NotarizedTransaction,
        payload_size: usize,
    ) -> Result<(), RejectionReason> {
        let read_store = self.store.read();
        let existing = read_store.get_txn_state_version_by_identifier(&transaction.intent_hash());
        if existing.is_some() {
            return Err(RejectionReason::IntentHashCommitted);
        }

        let receipt = self.validate_and_test_execute_transaction_up_to_fee_loan(
            read_store.deref(),
            transaction,
            payload_size,
        )?;

        match receipt.result {
            TransactionResult::Reject(reject_result) => Err(RejectionReason::FromExecution(
                Box::new(reject_result.error),
            )),
            TransactionResult::Commit(..) => Ok(()),
            TransactionResult::Abort(abort_result) => {
                // The transaction aborted after the fee loan was repaid - meaning the transaction result would get committed
                match abort_result.reason {
                    AbortReason::ConfiguredAbortTriggeredOnFeeLoanRepayment => Ok(()),
                }
            }
        }
    }

    fn validate_and_test_execute_transaction_up_to_fee_loan(
        &self,
        root_store: &S,
        transaction: &NotarizedTransaction,
        payload_size: usize,
    ) -> Result<TransactionReceipt, RejectionReason> {
        let executable = self
            .user_transaction_validator
            .validate_and_create_executable(transaction, payload_size)
            .map_err(RejectionReason::ValidationError)?;
        let logged_description = format!(
            "pending intent hash {}, up to fee loan",
            transaction.intent_hash()
        );
        let transaction_logic = self
            .execution_configurator
            .wrap(executable, ConfigType::Pending)
            .warn_after(UP_TO_FEE_LOAN_RUNTIME_WARN_THRESHOLD, logged_description);
        Ok(transaction_logic.execute_on(root_store))
    }
}

const PREVIEW_RUNTIME_WARN_THRESHOLD: Duration = Duration::from_millis(500);

/// A transaction preview runner.
pub struct TransactionPreviewer<S> {
    store: Arc<RwLock<S>>,
    execution_configurator: Arc<ExecutionConfigurator>,
    validation_config: ValidationConfig,
}

impl<S> TransactionPreviewer<S> {
    pub fn new(
        network: &NetworkDefinition,
        store: Arc<RwLock<S>>,
        execution_configurator: Arc<ExecutionConfigurator>,
    ) -> Self {
        Self {
            store,
            execution_configurator,
            validation_config: ValidationConfig::default(network.id),
        }
    }
}

impl<S: ReadableSubstateStore> TransactionPreviewer<S> {
    /// Executes the transaction compiled from the given request in a preview mode.
    pub fn preview(&self, preview_request: PreviewRequest) -> Result<PreviewResult, PreviewError> {
        let read_store = self.store.read();
        let intent = self.create_intent(preview_request, read_store.deref());

        let validator = NotarizedTransactionValidator::new(self.validation_config);
        let executable = validator
            .validate_preview_intent(&intent, &NoopIntentHashManager {})
            .map_err(PreviewError::TransactionValidationError)?;
        let transaction_logic = self
            .execution_configurator
            .wrap(executable, ConfigType::Preview)
            .warn_after(PREVIEW_RUNTIME_WARN_THRESHOLD, "preview");
        let receipt = transaction_logic.execute_on(read_store.deref());

        Ok(PreviewResult { intent, receipt })
    }

    fn create_intent(&self, preview_request: PreviewRequest, read_store: &S) -> PreviewIntent {
        let notary = preview_request.notary_public_key.unwrap_or_else(|| {
            PublicKey::EcdsaSecp256k1(EcdsaSecp256k1PrivateKey::from_u64(2).unwrap().public_key())
        });
        let effective_epoch_range = preview_request.explicit_epoch_range.unwrap_or_else(|| {
            let current_epoch = read_store.get_epoch();
            Range {
                start: current_epoch,
                end: current_epoch + self.validation_config.max_epoch_range,
            }
        });
        PreviewIntent {
            intent: TransactionIntent {
                header: TransactionHeader {
                    version: 1,
                    network_id: self.validation_config.network_id,
                    start_epoch_inclusive: effective_epoch_range.start,
                    end_epoch_exclusive: effective_epoch_range.end,
                    nonce: preview_request.nonce,
                    notary_public_key: notary,
                    notary_as_signatory: preview_request.notary_as_signatory,
                    cost_unit_limit: preview_request.cost_unit_limit,
                    tip_percentage: preview_request.tip_percentage,
                },
                manifest: preview_request.manifest,
            },
            signer_public_keys: preview_request.signer_public_keys,
            flags: PreviewFlags {
                unlimited_loan: preview_request.flags.unlimited_loan,
                assume_all_signature_proofs: preview_request.flags.assume_all_signature_proofs,
                permit_duplicate_intent_hash: preview_request.flags.permit_duplicate_intent_hash,
                permit_invalid_header_epoch: preview_request.flags.permit_invalid_header_epoch,
            },
        }
    }
}

/// A no-op `IntentHashManager` (allowing all hashes).
struct NoopIntentHashManager {}

impl IntentHashManager for NoopIntentHashManager {
    fn allows(&self, _hash: &Hash) -> bool {
        true
    }
}
