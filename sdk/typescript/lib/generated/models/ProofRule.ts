/* tslint:disable */
/* eslint-disable */
/**
 * Babylon Core API - RCnet v3.1
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node\'s function.  This API exposes queries against the node\'s current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the fourth release candidate of the Radix Babylon network (\"RCnet v3.1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code. 
 *
 * The version of the OpenAPI document: 0.5.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import {
    AllOfProofRule,
    instanceOfAllOfProofRule,
    AllOfProofRuleFromJSON,
    AllOfProofRuleFromJSONTyped,
    AllOfProofRuleToJSON,
} from './AllOfProofRule';
import {
    AmountOfProofRule,
    instanceOfAmountOfProofRule,
    AmountOfProofRuleFromJSON,
    AmountOfProofRuleFromJSONTyped,
    AmountOfProofRuleToJSON,
} from './AmountOfProofRule';
import {
    AnyOfProofRule,
    instanceOfAnyOfProofRule,
    AnyOfProofRuleFromJSON,
    AnyOfProofRuleFromJSONTyped,
    AnyOfProofRuleToJSON,
} from './AnyOfProofRule';
import {
    CountOfProofRule,
    instanceOfCountOfProofRule,
    CountOfProofRuleFromJSON,
    CountOfProofRuleFromJSONTyped,
    CountOfProofRuleToJSON,
} from './CountOfProofRule';
import {
    RequireProofRule,
    instanceOfRequireProofRule,
    RequireProofRuleFromJSON,
    RequireProofRuleFromJSONTyped,
    RequireProofRuleToJSON,
} from './RequireProofRule';

/**
 * @type ProofRule
 * 
 * @export
 */
export type ProofRule = { type: 'AllOf' } & AllOfProofRule | { type: 'AmountOf' } & AmountOfProofRule | { type: 'AnyOf' } & AnyOfProofRule | { type: 'CountOf' } & CountOfProofRule | { type: 'Require' } & RequireProofRule;

export function ProofRuleFromJSON(json: any): ProofRule {
    return ProofRuleFromJSONTyped(json, false);
}

export function ProofRuleFromJSONTyped(json: any, ignoreDiscriminator: boolean): ProofRule {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    switch (json['type']) {
        case 'AllOf':
            return {...AllOfProofRuleFromJSONTyped(json, true), type: 'AllOf'};
        case 'AmountOf':
            return {...AmountOfProofRuleFromJSONTyped(json, true), type: 'AmountOf'};
        case 'AnyOf':
            return {...AnyOfProofRuleFromJSONTyped(json, true), type: 'AnyOf'};
        case 'CountOf':
            return {...CountOfProofRuleFromJSONTyped(json, true), type: 'CountOf'};
        case 'Require':
            return {...RequireProofRuleFromJSONTyped(json, true), type: 'Require'};
        default:
            throw new Error(`No variant of ProofRule exists with 'type=${json['type']}'`);
    }
}

export function ProofRuleToJSON(value?: ProofRule | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    switch (value['type']) {
        case 'AllOf':
            return AllOfProofRuleToJSON(value);
        case 'AmountOf':
            return AmountOfProofRuleToJSON(value);
        case 'AnyOf':
            return AnyOfProofRuleToJSON(value);
        case 'CountOf':
            return CountOfProofRuleToJSON(value);
        case 'Require':
            return RequireProofRuleToJSON(value);
        default:
            throw new Error(`No variant of ProofRule exists with 'type=${value['type']}'`);
    }

}

