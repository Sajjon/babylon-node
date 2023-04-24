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

import {
    FungibleResourceAmount,
    instanceOfFungibleResourceAmount,
    FungibleResourceAmountFromJSON,
    FungibleResourceAmountFromJSONTyped,
    FungibleResourceAmountToJSON,
} from './FungibleResourceAmount';
import {
    NonFungibleResourceAmount,
    instanceOfNonFungibleResourceAmount,
    NonFungibleResourceAmountFromJSON,
    NonFungibleResourceAmountFromJSONTyped,
    NonFungibleResourceAmountToJSON,
} from './NonFungibleResourceAmount';

/**
 * @type ResourceAmount
 * 
 * @export
 */
export type ResourceAmount = { resource_type: 'Fungible' } & FungibleResourceAmount | { resource_type: 'NonFungible' } & NonFungibleResourceAmount;

export function ResourceAmountFromJSON(json: any): ResourceAmount {
    return ResourceAmountFromJSONTyped(json, false);
}

export function ResourceAmountFromJSONTyped(json: any, ignoreDiscriminator: boolean): ResourceAmount {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    switch (json['resource_type']) {
        case 'Fungible':
            return {...FungibleResourceAmountFromJSONTyped(json, true), resource_type: 'Fungible'};
        case 'NonFungible':
            return {...NonFungibleResourceAmountFromJSONTyped(json, true), resource_type: 'NonFungible'};
        default:
            throw new Error(`No variant of ResourceAmount exists with 'resource_type=${json['resource_type']}'`);
    }
}

export function ResourceAmountToJSON(value?: ResourceAmount | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    switch (value['resource_type']) {
        case 'Fungible':
            return FungibleResourceAmountToJSON(value);
        case 'NonFungible':
            return NonFungibleResourceAmountToJSON(value);
        default:
            throw new Error(`No variant of ResourceAmount exists with 'resource_type=${value['resource_type']}'`);
    }

}

