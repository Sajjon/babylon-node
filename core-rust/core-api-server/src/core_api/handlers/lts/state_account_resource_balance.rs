use crate::core_api::*;
use radix_engine::types::{ComponentAddress, Decimal, ResourceAddress};
use state_manager::{
    query::{dump_component_state, VaultData},
    store::traits::QueryableProofStore,
};
use std::ops::Deref;

#[tracing::instrument(skip(state))]
pub(crate) async fn handle_lts_state_account_fungible_resource_balance(
    state: State<CoreApiState>,
    Json(request): Json<models::LtsStateAccountFungibleResourceBalanceRequest>,
) -> Result<Json<models::LtsStateAccountFungibleResourceBalanceResponse>, ResponseError<()>> {
    assert_matching_network(&request.network, &state.network)?;

    if !request.account_address.starts_with("account_") {
        return Err(client_error(
            "Only component addresses starting with account_ currently work with this endpoint.",
        ));
    }

    let mapping_context = MappingContext::new(&state.network);
    let extraction_context = ExtractionContext::new(&state.network);

    let fungible_resource_address =
        extract_resource_address(&extraction_context, &request.resource_address)
            .map_err(|err| err.into_response_error("resource_address"))?;

    if let ResourceAddress::NonFungible(_) = fungible_resource_address {
        return Err(client_error(
            "The provided resource address is not a fungible resource.",
        ));
    }

    let component_address =
        extract_component_address(&extraction_context, &request.account_address)
            .map_err(|err| err.into_response_error("account_address"))?;

    // TODO: super-inefficient implementation - change before mainnet
    let database = state.database.read();
    let component_dump = match dump_component_state(database.deref(), component_address) {
        Ok(component_dump) => component_dump,
        Err(err) => match component_address {
            ComponentAddress::Account(_) => return Err(not_found_error("Account not found")),
            ComponentAddress::EcdsaSecp256k1VirtualAccount(_)
            | ComponentAddress::EddsaEd25519VirtualAccount(_) => {
                return Ok(models::LtsStateAccountFungibleResourceBalanceResponse {
                    state_version: to_api_state_version(database.max_state_version())?,
                    account_address: to_api_component_address(&mapping_context, &component_address),
                    fungible_resource_balance: Box::new(models::LtsFungibleResourceBalance {
                        fungible_resource_address: to_api_resource_address(
                            &mapping_context,
                            &fungible_resource_address,
                        ),
                        amount: to_api_decimal(&Decimal::zero()),
                    }),
                })
                .map(Json)
            }
            _ => {
                return Err(server_error(format!(
                    "Error traversing component state: {err:?}"
                )))
            }
        },
    };

    let fungible_resource_balance_amount = component_dump
        .vaults
        .into_iter()
        .filter_map(|vault| match vault {
            VaultData::NonFungible { .. } => None,
            VaultData::Fungible {
                resource_address,
                amount,
            } => {
                if resource_address != fungible_resource_address {
                    return None;
                }
                Some(amount)
            }
        })
        .fold(Decimal::zero(), |total, amount| total + amount);

    Ok(models::LtsStateAccountFungibleResourceBalanceResponse {
        state_version: to_api_state_version(database.max_state_version())?,
        account_address: to_api_component_address(&mapping_context, &component_address),
        fungible_resource_balance: Box::new(models::LtsFungibleResourceBalance {
            fungible_resource_address: to_api_resource_address(
                &mapping_context,
                &fungible_resource_address,
            ),
            amount: to_api_decimal(&fungible_resource_balance_amount),
        }),
    })
    .map(Json)
}
