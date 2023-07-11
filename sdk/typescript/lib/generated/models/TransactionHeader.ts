/* tslint:disable */
/* eslint-disable */
/**
 * Babylon Core API - RCnet V2
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node\'s function.  This API exposes queries against the node\'s current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the first release candidate of the Radix Babylon network (\"RCnet-V1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code.  We give no guarantees that other endpoints will not change before Babylon mainnet launch, although changes are expected to be minimal. 
 *
 * The version of the OpenAPI document: 0.4.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { exists, mapValues } from '../runtime';
import type { PublicKey } from './PublicKey';
import {
    PublicKeyFromJSON,
    PublicKeyFromJSONTyped,
    PublicKeyToJSON,
} from './PublicKey';

/**
 * 
 * @export
 * @interface TransactionHeader
 */
export interface TransactionHeader {
    /**
     * The logical id of the network
     * @type {number}
     * @memberof TransactionHeader
     */
    network_id: number;
    /**
     * An integer between `0` and `10^10`, marking the epoch from which the transaction can be submitted.
     * In the case of uncommitted transactions, a value of `10^10` indicates that the epoch was >= `10^10`.
     * @type {number}
     * @memberof TransactionHeader
     */
    start_epoch_inclusive: number;
    /**
     * An integer between `0` and `10^10`, marking the epoch from which the transaction will no longer be valid, and be rejected.
     * In the case of uncommitted transactions, a value of `10^10` indicates that the epoch was >= `10^10`.
     * @type {number}
     * @memberof TransactionHeader
     */
    end_epoch_exclusive: number;
    /**
     * An integer between `0` and `2^32 - 1`, chosen to allow a unique intent to be created (to enable submitting an otherwise identical/duplicate intent).
     * @type {number}
     * @memberof TransactionHeader
     */
    nonce: number;
    /**
     * 
     * @type {PublicKey}
     * @memberof TransactionHeader
     */
    notary_public_key: PublicKey;
    /**
     * Specifies whether the notary public key should be included in the transaction signers list
     * @type {boolean}
     * @memberof TransactionHeader
     */
    notary_is_signatory: boolean;
    /**
     * An integer between `0` and `255`, giving the validator tip as a percentage amount. A value of `1` corresponds to 1% of the fee.
     * @type {number}
     * @memberof TransactionHeader
     */
    tip_percentage: number;
}

/**
 * Check if a given object implements the TransactionHeader interface.
 */
export function instanceOfTransactionHeader(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "network_id" in value;
    isInstance = isInstance && "start_epoch_inclusive" in value;
    isInstance = isInstance && "end_epoch_exclusive" in value;
    isInstance = isInstance && "nonce" in value;
    isInstance = isInstance && "notary_public_key" in value;
    isInstance = isInstance && "notary_is_signatory" in value;
    isInstance = isInstance && "tip_percentage" in value;

    return isInstance;
}

export function TransactionHeaderFromJSON(json: any): TransactionHeader {
    return TransactionHeaderFromJSONTyped(json, false);
}

export function TransactionHeaderFromJSONTyped(json: any, ignoreDiscriminator: boolean): TransactionHeader {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'network_id': json['network_id'],
        'start_epoch_inclusive': json['start_epoch_inclusive'],
        'end_epoch_exclusive': json['end_epoch_exclusive'],
        'nonce': json['nonce'],
        'notary_public_key': PublicKeyFromJSON(json['notary_public_key']),
        'notary_is_signatory': json['notary_is_signatory'],
        'tip_percentage': json['tip_percentage'],
    };
}

export function TransactionHeaderToJSON(value?: TransactionHeader | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'network_id': value.network_id,
        'start_epoch_inclusive': value.start_epoch_inclusive,
        'end_epoch_exclusive': value.end_epoch_exclusive,
        'nonce': value.nonce,
        'notary_public_key': PublicKeyToJSON(value.notary_public_key),
        'notary_is_signatory': value.notary_is_signatory,
        'tip_percentage': value.tip_percentage,
    };
}

