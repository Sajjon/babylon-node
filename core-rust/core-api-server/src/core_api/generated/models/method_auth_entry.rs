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
pub struct MethodAuthEntry {
    #[serde(rename = "method")]
    pub method: Option<crate::core_api::generated::models::LocalMethodReference>, // Using Option permits Default trait; Will always be Some in normal use
    #[serde(rename = "access_rule_reference")]
    pub access_rule_reference: Option<crate::core_api::generated::models::AccessRuleReference>, // Using Option permits Default trait; Will always be Some in normal use
}

impl MethodAuthEntry {
    pub fn new(method: crate::core_api::generated::models::LocalMethodReference, access_rule_reference: crate::core_api::generated::models::AccessRuleReference) -> MethodAuthEntry {
        MethodAuthEntry {
            method: Option::Some(method),
            access_rule_reference: Option::Some(access_rule_reference),
        }
    }
}


