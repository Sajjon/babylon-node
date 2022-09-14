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
pub struct NotarizedTransaction {
    /// The hex-encoded double-SHA256 hash of the notarized transaction payload
    #[serde(rename = "hash")]
    pub hash: String,
    /// The hex-encoded full notarized transaction payload
    #[serde(rename = "payload")]
    pub payload: String,
    #[serde(rename = "signed_intent")]
    pub signed_intent: Box<crate::core_api::generated::models::SignedTransactionIntent>,
    #[serde(rename = "notary_signature")]
    pub notary_signature: Option<crate::core_api::generated::models::Signature>, // Using Option permits Default trait; Will always be Some in normal use
}

impl NotarizedTransaction {
    pub fn new(hash: String, payload: String, signed_intent: crate::core_api::generated::models::SignedTransactionIntent, notary_signature: crate::core_api::generated::models::Signature) -> NotarizedTransaction {
        NotarizedTransaction {
            hash,
            payload,
            signed_intent: Box::new(signed_intent),
            notary_signature: Option::Some(notary_signature),
        }
    }
}


