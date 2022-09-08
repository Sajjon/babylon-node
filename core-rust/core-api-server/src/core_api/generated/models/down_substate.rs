/*
 * Babylon Core API
 *
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.1.0
 * 
 * Generated by: https://openapi-generator.tech
 */




#[derive(Clone, Debug, PartialEq, Default, serde::Serialize, serde::Deserialize)]
pub struct DownSubstate {
    /// SBOR-encoded and then hex-encoded substate ID.
    #[serde(rename = "substate_id")]
    pub substate_id: String,
    /// Substate hash.
    #[serde(rename = "substate_hash")]
    pub substate_hash: String,
    /// A decimal 32-bit unsigned integer
    #[serde(rename = "version")]
    pub version: String,
}

impl DownSubstate {
    pub fn new(substate_id: String, substate_hash: String, version: String) -> DownSubstate {
        DownSubstate {
            substate_id,
            substate_hash,
            version,
        }
    }
}


