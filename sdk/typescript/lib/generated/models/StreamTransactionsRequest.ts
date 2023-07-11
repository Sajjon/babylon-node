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
import type { SborFormatOptions } from './SborFormatOptions';
import {
    SborFormatOptionsFromJSON,
    SborFormatOptionsFromJSONTyped,
    SborFormatOptionsToJSON,
} from './SborFormatOptions';
import type { SubstateFormatOptions } from './SubstateFormatOptions';
import {
    SubstateFormatOptionsFromJSON,
    SubstateFormatOptionsFromJSONTyped,
    SubstateFormatOptionsToJSON,
} from './SubstateFormatOptions';
import type { TransactionFormatOptions } from './TransactionFormatOptions';
import {
    TransactionFormatOptionsFromJSON,
    TransactionFormatOptionsFromJSONTyped,
    TransactionFormatOptionsToJSON,
} from './TransactionFormatOptions';

/**
 * A request to retrieve a sublist of committed transactions from the ledger.
 * @export
 * @interface StreamTransactionsRequest
 */
export interface StreamTransactionsRequest {
    /**
     * The logical name of the network
     * @type {string}
     * @memberof StreamTransactionsRequest
     */
    network: string;
    /**
     * 
     * @type {number}
     * @memberof StreamTransactionsRequest
     */
    from_state_version: number;
    /**
     * The maximum number of transactions that will be returned.
     * @type {number}
     * @memberof StreamTransactionsRequest
     */
    limit: number;
    /**
     * 
     * @type {SborFormatOptions}
     * @memberof StreamTransactionsRequest
     */
    sbor_format_options?: SborFormatOptions;
    /**
     * 
     * @type {TransactionFormatOptions}
     * @memberof StreamTransactionsRequest
     */
    transaction_format_options?: TransactionFormatOptions;
    /**
     * 
     * @type {SubstateFormatOptions}
     * @memberof StreamTransactionsRequest
     */
    substate_format_options?: SubstateFormatOptions;
}

/**
 * Check if a given object implements the StreamTransactionsRequest interface.
 */
export function instanceOfStreamTransactionsRequest(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "network" in value;
    isInstance = isInstance && "from_state_version" in value;
    isInstance = isInstance && "limit" in value;

    return isInstance;
}

export function StreamTransactionsRequestFromJSON(json: any): StreamTransactionsRequest {
    return StreamTransactionsRequestFromJSONTyped(json, false);
}

export function StreamTransactionsRequestFromJSONTyped(json: any, ignoreDiscriminator: boolean): StreamTransactionsRequest {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'network': json['network'],
        'from_state_version': json['from_state_version'],
        'limit': json['limit'],
        'sbor_format_options': !exists(json, 'sbor_format_options') ? undefined : SborFormatOptionsFromJSON(json['sbor_format_options']),
        'transaction_format_options': !exists(json, 'transaction_format_options') ? undefined : TransactionFormatOptionsFromJSON(json['transaction_format_options']),
        'substate_format_options': !exists(json, 'substate_format_options') ? undefined : SubstateFormatOptionsFromJSON(json['substate_format_options']),
    };
}

export function StreamTransactionsRequestToJSON(value?: StreamTransactionsRequest | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'network': value.network,
        'from_state_version': value.from_state_version,
        'limit': value.limit,
        'sbor_format_options': SborFormatOptionsToJSON(value.sbor_format_options),
        'transaction_format_options': TransactionFormatOptionsToJSON(value.transaction_format_options),
        'substate_format_options': SubstateFormatOptionsToJSON(value.substate_format_options),
    };
}

