/* tslint:disable */
/* eslint-disable */
/**
 * Babylon Core API - RCnet V1
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node\'s function.  This API exposes queries against the node\'s current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the first release candidate of the Radix Babylon network (\"RCnet-V1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code.  We give no guarantees that other endpoints will not change before Babylon mainnet launch, although changes are expected to be minimal. 
 *
 * The version of the OpenAPI document: 0.3.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { exists, mapValues } from '../runtime';
import type { ErrorResponseType } from './ErrorResponseType';
import {
    ErrorResponseTypeFromJSON,
    ErrorResponseTypeFromJSONTyped,
    ErrorResponseTypeToJSON,
} from './ErrorResponseType';

/**
 * 
 * @export
 * @interface ErrorResponseBase
 */
export interface ErrorResponseBase {
    /**
     * 
     * @type {ErrorResponseType}
     * @memberof ErrorResponseBase
     */
    error_type: ErrorResponseType;
    /**
     * A numeric code corresponding to the given HTTP error code.
     * @type {number}
     * @memberof ErrorResponseBase
     */
    code: number;
    /**
     * A human-readable error message.
     * @type {string}
     * @memberof ErrorResponseBase
     */
    message: string;
    /**
     * A GUID to be used when reporting errors, to allow correlation with the Core API's error logs, in the case where the Core API details are hidden.
     * @type {string}
     * @memberof ErrorResponseBase
     */
    trace_id?: string;
}

/**
 * Check if a given object implements the ErrorResponseBase interface.
 */
export function instanceOfErrorResponseBase(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "error_type" in value;
    isInstance = isInstance && "code" in value;
    isInstance = isInstance && "message" in value;

    return isInstance;
}

export function ErrorResponseBaseFromJSON(json: any): ErrorResponseBase {
    return ErrorResponseBaseFromJSONTyped(json, false);
}

export function ErrorResponseBaseFromJSONTyped(json: any, ignoreDiscriminator: boolean): ErrorResponseBase {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'error_type': ErrorResponseTypeFromJSON(json['error_type']),
        'code': json['code'],
        'message': json['message'],
        'trace_id': !exists(json, 'trace_id') ? undefined : json['trace_id'],
    };
}

export function ErrorResponseBaseToJSON(value?: ErrorResponseBase | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'error_type': ErrorResponseTypeToJSON(value.error_type),
        'code': value.code,
        'message': value.message,
        'trace_id': value.trace_id,
    };
}

