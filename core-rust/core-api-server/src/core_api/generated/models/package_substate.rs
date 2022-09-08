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
pub struct PackageSubstate {
    /// Package code, hex-encoded.
    #[serde(rename = "code")]
    pub code: String,
}

impl PackageSubstate {
    pub fn new(code: String) -> PackageSubstate {
        PackageSubstate {
            code,
        }
    }
}


