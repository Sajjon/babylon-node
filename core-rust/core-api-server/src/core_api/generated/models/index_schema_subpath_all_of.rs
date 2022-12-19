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
pub struct IndexSchemaSubpathAllOf {
    /// A decimal-string-encoded integer between 0 and 2^64 - 1, which captures the index of the schema sub-path
    #[serde(rename = "index")]
    pub index: String,
}

impl IndexSchemaSubpathAllOf {
    pub fn new(index: String) -> IndexSchemaSubpathAllOf {
        IndexSchemaSubpathAllOf {
            index,
        }
    }
}


