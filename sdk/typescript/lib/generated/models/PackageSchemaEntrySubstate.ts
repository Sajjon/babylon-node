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
import type { PackageSchemaEntryValue } from './PackageSchemaEntryValue';
import {
    PackageSchemaEntryValueFromJSON,
    PackageSchemaEntryValueFromJSONTyped,
    PackageSchemaEntryValueToJSON,
} from './PackageSchemaEntryValue';
import type { SchemaKey } from './SchemaKey';
import {
    SchemaKeyFromJSON,
    SchemaKeyFromJSONTyped,
    SchemaKeyToJSON,
} from './SchemaKey';

/**
 * 
 * @export
 * @interface PackageSchemaEntrySubstate
 */
export interface PackageSchemaEntrySubstate {
    /**
     * 
     * @type {string}
     * @memberof PackageSchemaEntrySubstate
     */
    substate_type: PackageSchemaEntrySubstateSubstateTypeEnum;
    /**
     * 
     * @type {boolean}
     * @memberof PackageSchemaEntrySubstate
     */
    is_locked: boolean;
    /**
     * 
     * @type {SchemaKey}
     * @memberof PackageSchemaEntrySubstate
     */
    key: SchemaKey;
    /**
     * 
     * @type {PackageSchemaEntryValue}
     * @memberof PackageSchemaEntrySubstate
     */
    value: PackageSchemaEntryValue;
}


/**
 * @export
 */
export const PackageSchemaEntrySubstateSubstateTypeEnum = {
    PackageSchemaEntry: 'PackageSchemaEntry'
} as const;
export type PackageSchemaEntrySubstateSubstateTypeEnum = typeof PackageSchemaEntrySubstateSubstateTypeEnum[keyof typeof PackageSchemaEntrySubstateSubstateTypeEnum];


/**
 * Check if a given object implements the PackageSchemaEntrySubstate interface.
 */
export function instanceOfPackageSchemaEntrySubstate(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "substate_type" in value;
    isInstance = isInstance && "is_locked" in value;
    isInstance = isInstance && "key" in value;
    isInstance = isInstance && "value" in value;

    return isInstance;
}

export function PackageSchemaEntrySubstateFromJSON(json: any): PackageSchemaEntrySubstate {
    return PackageSchemaEntrySubstateFromJSONTyped(json, false);
}

export function PackageSchemaEntrySubstateFromJSONTyped(json: any, ignoreDiscriminator: boolean): PackageSchemaEntrySubstate {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'substate_type': json['substate_type'],
        'is_locked': json['is_locked'],
        'key': SchemaKeyFromJSON(json['key']),
        'value': PackageSchemaEntryValueFromJSON(json['value']),
    };
}

export function PackageSchemaEntrySubstateToJSON(value?: PackageSchemaEntrySubstate | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'substate_type': value.substate_type,
        'is_locked': value.is_locked,
        'key': SchemaKeyToJSON(value.key),
        'value': PackageSchemaEntryValueToJSON(value.value),
    };
}

