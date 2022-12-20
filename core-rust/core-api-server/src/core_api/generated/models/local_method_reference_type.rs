/*
 * Babylon Core API
 *
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node. It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against historical ledger state, you may also wish to consider using the [Gateway API](https://betanet-gateway.redoc.ly/). 
 *
 * The version of the OpenAPI document: 0.1.0
 * 
 * Generated by: https://openapi-generator.tech
 */


/// 
#[derive(Clone, Copy, Debug, Eq, PartialEq, Ord, PartialOrd, Hash, serde::Serialize, serde::Deserialize)]
pub enum LocalMethodReferenceType {
    #[serde(rename = "NativeMethod")]
    NativeMethod,
    #[serde(rename = "NativeFunction")]
    NativeFunction,
    #[serde(rename = "ScryptoMethod")]
    ScryptoMethod,

}

impl ToString for LocalMethodReferenceType {
    fn to_string(&self) -> String {
        match self {
            Self::NativeMethod => String::from("NativeMethod"),
            Self::NativeFunction => String::from("NativeFunction"),
            Self::ScryptoMethod => String::from("ScryptoMethod"),
        }
    }
}

impl Default for LocalMethodReferenceType {
    fn default() -> LocalMethodReferenceType {
        Self::NativeMethod
    }
}




