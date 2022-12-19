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
pub struct NetworkConfigurationResponse {
    #[serde(rename = "version")]
    pub version: Box<crate::core_api::generated::models::NetworkConfigurationResponseVersion>,
    /// The logical name of the network
    #[serde(rename = "network")]
    pub network: String,
    /// The network suffix used for Bech32m HRPs used for addressing.
    #[serde(rename = "network_hrp_suffix")]
    pub network_hrp_suffix: String,
    #[serde(rename = "address_types")]
    pub address_types: Vec<crate::core_api::generated::models::AddressType>,
    #[serde(rename = "well_known_addresses")]
    pub well_known_addresses: Box<crate::core_api::generated::models::NetworkConfigurationResponseWellKnownAddresses>,
}

impl NetworkConfigurationResponse {
    pub fn new(version: crate::core_api::generated::models::NetworkConfigurationResponseVersion, network: String, network_hrp_suffix: String, address_types: Vec<crate::core_api::generated::models::AddressType>, well_known_addresses: crate::core_api::generated::models::NetworkConfigurationResponseWellKnownAddresses) -> NetworkConfigurationResponse {
        NetworkConfigurationResponse {
            version: Box::new(version),
            network,
            network_hrp_suffix,
            address_types,
            well_known_addresses: Box::new(well_known_addresses),
        }
    }
}


