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
pub struct NewSubstateVersion {
    #[serde(rename = "substate_id")]
    pub substate_id: Box<crate::core_api::generated::models::SubstateId>,
    /// An integer between `0` and `10^13`, counting the number of times the substate was updated
    #[serde(rename = "version")]
    pub version: i64,
    /// The hex-encoded, SBOR-encoded substate data bytes
    #[serde(rename = "substate_hex")]
    pub substate_hex: String,
    /// The hex-encoded single-SHA256 hash of the substate data bytes
    #[serde(rename = "substate_data_hash")]
    pub substate_data_hash: String,
    #[serde(rename = "substate_data")]
    pub substate_data: Option<crate::core_api::generated::models::Substate>, // Using Option permits Default trait; Will always be Some in normal use
}

impl NewSubstateVersion {
    pub fn new(substate_id: crate::core_api::generated::models::SubstateId, version: i64, substate_hex: String, substate_data_hash: String, substate_data: crate::core_api::generated::models::Substate) -> NewSubstateVersion {
        NewSubstateVersion {
            substate_id: Box::new(substate_id),
            version,
            substate_hex,
            substate_data_hash,
            substate_data: Option::Some(substate_data),
        }
    }
}


