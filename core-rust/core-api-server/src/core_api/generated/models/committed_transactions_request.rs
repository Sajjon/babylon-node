/*
 * Babylon Core API
 *
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node. It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against historical ledger state, you may also wish to consider using the [Gateway API](https://betanet-gateway.redoc.ly/). 
 *
 * The version of the OpenAPI document: 0.1.0
 * 
 * Generated by: https://openapi-generator.tech
 */

/// CommittedTransactionsRequest : A request to retrieve a sublist of committed transactions from the ledger. 



#[derive(Clone, Debug, PartialEq, Default, serde::Serialize, serde::Deserialize)]
pub struct CommittedTransactionsRequest {
    /// The logical name of the network
    #[serde(rename = "network")]
    pub network: String,
    /// An integer between `1` and `10^13`, giving the first (resultant) state version to be returned
    #[serde(rename = "from_state_version")]
    pub from_state_version: i64,
    /// The maximum number of transactions that will be returned.
    #[serde(rename = "limit")]
    pub limit: i32,
}

impl CommittedTransactionsRequest {
    /// A request to retrieve a sublist of committed transactions from the ledger. 
    pub fn new(network: String, from_state_version: i64, limit: i32) -> CommittedTransactionsRequest {
        CommittedTransactionsRequest {
            network,
            from_state_version,
            limit,
        }
    }
}


