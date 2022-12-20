/*
 * Babylon Core API
 *
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node. It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against historical ledger state, you may also wish to consider using the [Gateway API](https://betanet-gateway.redoc.ly/). 
 *
 * The version of the OpenAPI document: 0.1.0
 * 
 * Generated by: https://openapi-generator.tech
 */




#[derive(Clone, Debug, PartialEq, Default, serde::Serialize, serde::Deserialize)]
pub struct MempoolTransactionResponse {
    #[serde(rename = "notarized_transaction")]
    pub notarized_transaction: Box<crate::core_api::generated::models::NotarizedTransaction>,
}

impl MempoolTransactionResponse {
    pub fn new(notarized_transaction: crate::core_api::generated::models::NotarizedTransaction) -> MempoolTransactionResponse {
        MempoolTransactionResponse {
            notarized_transaction: Box::new(notarized_transaction),
        }
    }
}


