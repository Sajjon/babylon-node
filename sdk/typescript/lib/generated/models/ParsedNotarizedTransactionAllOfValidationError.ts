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
/**
 * If the transaction is known to not be valid, this gives a reason.
 * Different levels of validation are performed, dependent on the validation mode.
 * Note that, even if validation mode is Static or Full, the transaction may
 * still be rejected or fail due to issues at runtime (eg if the loan cannot be repaid).
 * @export
 * @interface ParsedNotarizedTransactionAllOfValidationError
 */
export interface ParsedNotarizedTransactionAllOfValidationError {
    /**
     * The error message.
     * @type {string}
     * @memberof ParsedNotarizedTransactionAllOfValidationError
     */
    reason: string;
    /**
     * Whether the error is known to be permanent, or not.
     * This relates to whether the transaction would be rejected permanently or temporarily if submitted.
     * @type {boolean}
     * @memberof ParsedNotarizedTransactionAllOfValidationError
     */
    is_permanent: boolean;
}

/**
 * Check if a given object implements the ParsedNotarizedTransactionAllOfValidationError interface.
 */
export function instanceOfParsedNotarizedTransactionAllOfValidationError(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "reason" in value;
    isInstance = isInstance && "is_permanent" in value;

    return isInstance;
}

export function ParsedNotarizedTransactionAllOfValidationErrorFromJSON(json: any): ParsedNotarizedTransactionAllOfValidationError {
    return ParsedNotarizedTransactionAllOfValidationErrorFromJSONTyped(json, false);
}

export function ParsedNotarizedTransactionAllOfValidationErrorFromJSONTyped(json: any, ignoreDiscriminator: boolean): ParsedNotarizedTransactionAllOfValidationError {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'reason': json['reason'],
        'is_permanent': json['is_permanent'],
    };
}

export function ParsedNotarizedTransactionAllOfValidationErrorToJSON(value?: ParsedNotarizedTransactionAllOfValidationError | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'reason': value.reason,
        'is_permanent': value.is_permanent,
    };
}

