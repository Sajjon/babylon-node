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
import type { PackageCodeKey } from './PackageCodeKey';
import {
    PackageCodeKeyFromJSON,
    PackageCodeKeyFromJSONTyped,
    PackageCodeKeyToJSON,
} from './PackageCodeKey';
import type { PackageCodeOriginalCodeEntryValue } from './PackageCodeOriginalCodeEntryValue';
import {
    PackageCodeOriginalCodeEntryValueFromJSON,
    PackageCodeOriginalCodeEntryValueFromJSONTyped,
    PackageCodeOriginalCodeEntryValueToJSON,
} from './PackageCodeOriginalCodeEntryValue';

/**
 * 
 * @export
 * @interface PackageCodeOriginalCodeEntrySubstate
 */
export interface PackageCodeOriginalCodeEntrySubstate {
    /**
     * 
     * @type {string}
     * @memberof PackageCodeOriginalCodeEntrySubstate
     */
    substate_type: PackageCodeOriginalCodeEntrySubstateSubstateTypeEnum;
    /**
     * 
     * @type {boolean}
     * @memberof PackageCodeOriginalCodeEntrySubstate
     */
    is_locked: boolean;
    /**
     * 
     * @type {PackageCodeKey}
     * @memberof PackageCodeOriginalCodeEntrySubstate
     */
    key: PackageCodeKey;
    /**
     * 
     * @type {PackageCodeOriginalCodeEntryValue}
     * @memberof PackageCodeOriginalCodeEntrySubstate
     */
    value: PackageCodeOriginalCodeEntryValue;
}


/**
 * @export
 */
export const PackageCodeOriginalCodeEntrySubstateSubstateTypeEnum = {
    PackageCodeOriginalCodeEntry: 'PackageCodeOriginalCodeEntry'
} as const;
export type PackageCodeOriginalCodeEntrySubstateSubstateTypeEnum = typeof PackageCodeOriginalCodeEntrySubstateSubstateTypeEnum[keyof typeof PackageCodeOriginalCodeEntrySubstateSubstateTypeEnum];


/**
 * Check if a given object implements the PackageCodeOriginalCodeEntrySubstate interface.
 */
export function instanceOfPackageCodeOriginalCodeEntrySubstate(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "substate_type" in value;
    isInstance = isInstance && "is_locked" in value;
    isInstance = isInstance && "key" in value;
    isInstance = isInstance && "value" in value;

    return isInstance;
}

export function PackageCodeOriginalCodeEntrySubstateFromJSON(json: any): PackageCodeOriginalCodeEntrySubstate {
    return PackageCodeOriginalCodeEntrySubstateFromJSONTyped(json, false);
}

export function PackageCodeOriginalCodeEntrySubstateFromJSONTyped(json: any, ignoreDiscriminator: boolean): PackageCodeOriginalCodeEntrySubstate {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'substate_type': json['substate_type'],
        'is_locked': json['is_locked'],
        'key': PackageCodeKeyFromJSON(json['key']),
        'value': PackageCodeOriginalCodeEntryValueFromJSON(json['value']),
    };
}

export function PackageCodeOriginalCodeEntrySubstateToJSON(value?: PackageCodeOriginalCodeEntrySubstate | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'substate_type': value.substate_type,
        'is_locked': value.is_locked,
        'key': PackageCodeKeyToJSON(value.key),
        'value': PackageCodeOriginalCodeEntryValueToJSON(value.value),
    };
}

