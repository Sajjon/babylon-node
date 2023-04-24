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
/**
 * 
 * @export
 * @interface TransactionPayloadStatus
 */
export interface TransactionPayloadStatus {
    /**
     * The hex-encoded notarized transaction hash. This is known as the Notarized Transaction Hash, Payload Hash or User Payload Hash. This hash is `Blake2b-256(compiled_notarized_transaction)`
     * @type {string}
     * @memberof TransactionPayloadStatus
     */
    payload_hash: string;
    /**
     * The status of the transaction payload, as per this node.
     * A NotInMempool status means that it wasn't rejected at last execution attempt, but it's not currently in the mempool either.
     * @type {string}
     * @memberof TransactionPayloadStatus
     */
    status: TransactionPayloadStatusStatusEnum;
    /**
     * An explanation for the error, if failed or rejected
     * @type {string}
     * @memberof TransactionPayloadStatus
     */
    error_message?: string;
}


/**
 * @export
 */
export const TransactionPayloadStatusStatusEnum = {
    CommittedSuccess: 'CommittedSuccess',
    CommittedFailure: 'CommittedFailure',
    PermanentlyRejected: 'PermanentlyRejected',
    TransientlyRejected: 'TransientlyRejected',
    InMempool: 'InMempool',
    NotInMempool: 'NotInMempool'
} as const;
export type TransactionPayloadStatusStatusEnum = typeof TransactionPayloadStatusStatusEnum[keyof typeof TransactionPayloadStatusStatusEnum];


/**
 * Check if a given object implements the TransactionPayloadStatus interface.
 */
export function instanceOfTransactionPayloadStatus(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "payload_hash" in value;
    isInstance = isInstance && "status" in value;

    return isInstance;
}

export function TransactionPayloadStatusFromJSON(json: any): TransactionPayloadStatus {
    return TransactionPayloadStatusFromJSONTyped(json, false);
}

export function TransactionPayloadStatusFromJSONTyped(json: any, ignoreDiscriminator: boolean): TransactionPayloadStatus {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'payload_hash': json['payload_hash'],
        'status': json['status'],
        'error_message': !exists(json, 'error_message') ? undefined : json['error_message'],
    };
}

export function TransactionPayloadStatusToJSON(value?: TransactionPayloadStatus | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'payload_hash': value.payload_hash,
        'status': value.status,
        'error_message': value.error_message,
    };
}

