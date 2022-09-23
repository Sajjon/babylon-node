use sbor::{Decode, Encode, TypeId};
use scrypto::buffer::scrypto_encode;

use crate::transaction::validator::ValidatorTransaction;
use crate::PayloadHash;
use transaction::model::NotarizedTransaction;

#[derive(Debug, Clone, TypeId, Encode, Decode, PartialEq, Eq)]
pub enum Transaction {
    User(NotarizedTransaction),
    Validator(ValidatorTransaction),
}

impl Transaction {
    pub fn get_hash(&self) -> PayloadHash {
        PayloadHash::for_payload(&scrypto_encode(self))
    }

    pub fn into_payload(self) -> Vec<u8> {
        scrypto_encode(&self)
    }
}
