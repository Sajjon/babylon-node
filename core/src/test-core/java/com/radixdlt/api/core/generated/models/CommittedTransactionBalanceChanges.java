/*
 * Radix Core API - Babylon
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  The default configuration is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function. The node exposes a configuration flag which allows disabling certain endpoints which may be problematic, but monitoring is advised. This configuration parameter is `api.core.flags.enable_unbounded_endpoints` / `RADIXDLT_CORE_API_FLAGS_ENABLE_UNBOUNDED_ENDPOINTS`.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` have high guarantees of forward compatibility in future node versions. We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code.  Other endpoints may be changed with new node versions carrying protocol-updates, although any breaking changes will be flagged clearly in the corresponding release notes.  All responses may have additional fields added, so clients are advised to use JSON parsers which ignore unknown fields on JSON objects. 
 *
 * The version of the OpenAPI document: v1.0.4
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.radixdlt.api.core.generated.models;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.radixdlt.api.core.generated.models.LtsEntityFungibleBalanceChanges;
import com.radixdlt.api.core.generated.models.LtsEntityNonFungibleBalanceChanges;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * The fee-related and non-fee-related balance changes of a transaction, aggregated by resource and global entity  within which the change occurred. 
 */
@ApiModel(description = "The fee-related and non-fee-related balance changes of a transaction, aggregated by resource and global entity  within which the change occurred. ")
@JsonPropertyOrder({
  CommittedTransactionBalanceChanges.JSON_PROPERTY_FUNGIBLE_ENTITY_BALANCE_CHANGES,
  CommittedTransactionBalanceChanges.JSON_PROPERTY_NON_FUNGIBLE_ENTITY_BALANCE_CHANGES
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class CommittedTransactionBalanceChanges {
  public static final String JSON_PROPERTY_FUNGIBLE_ENTITY_BALANCE_CHANGES = "fungible_entity_balance_changes";
  private List<LtsEntityFungibleBalanceChanges> fungibleEntityBalanceChanges = new ArrayList<>();

  public static final String JSON_PROPERTY_NON_FUNGIBLE_ENTITY_BALANCE_CHANGES = "non_fungible_entity_balance_changes";
  private List<LtsEntityNonFungibleBalanceChanges> nonFungibleEntityBalanceChanges = new ArrayList<>();

  public CommittedTransactionBalanceChanges() { 
  }

  public CommittedTransactionBalanceChanges fungibleEntityBalanceChanges(List<LtsEntityFungibleBalanceChanges> fungibleEntityBalanceChanges) {
    this.fungibleEntityBalanceChanges = fungibleEntityBalanceChanges;
    return this;
  }

  public CommittedTransactionBalanceChanges addFungibleEntityBalanceChangesItem(LtsEntityFungibleBalanceChanges fungibleEntityBalanceChangesItem) {
    this.fungibleEntityBalanceChanges.add(fungibleEntityBalanceChangesItem);
    return this;
  }

   /**
   * A list of all fungible balance updates which occurred in this transaction, aggregated by the global entity (such as account) which owns the vaults which were updated. 
   * @return fungibleEntityBalanceChanges
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "A list of all fungible balance updates which occurred in this transaction, aggregated by the global entity (such as account) which owns the vaults which were updated. ")
  @JsonProperty(JSON_PROPERTY_FUNGIBLE_ENTITY_BALANCE_CHANGES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<LtsEntityFungibleBalanceChanges> getFungibleEntityBalanceChanges() {
    return fungibleEntityBalanceChanges;
  }


  @JsonProperty(JSON_PROPERTY_FUNGIBLE_ENTITY_BALANCE_CHANGES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFungibleEntityBalanceChanges(List<LtsEntityFungibleBalanceChanges> fungibleEntityBalanceChanges) {
    this.fungibleEntityBalanceChanges = fungibleEntityBalanceChanges;
  }


  public CommittedTransactionBalanceChanges nonFungibleEntityBalanceChanges(List<LtsEntityNonFungibleBalanceChanges> nonFungibleEntityBalanceChanges) {
    this.nonFungibleEntityBalanceChanges = nonFungibleEntityBalanceChanges;
    return this;
  }

  public CommittedTransactionBalanceChanges addNonFungibleEntityBalanceChangesItem(LtsEntityNonFungibleBalanceChanges nonFungibleEntityBalanceChangesItem) {
    this.nonFungibleEntityBalanceChanges.add(nonFungibleEntityBalanceChangesItem);
    return this;
  }

   /**
   * Non fungible changes per entity and resource 
   * @return nonFungibleEntityBalanceChanges
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "Non fungible changes per entity and resource ")
  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_ENTITY_BALANCE_CHANGES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<LtsEntityNonFungibleBalanceChanges> getNonFungibleEntityBalanceChanges() {
    return nonFungibleEntityBalanceChanges;
  }


  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_ENTITY_BALANCE_CHANGES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNonFungibleEntityBalanceChanges(List<LtsEntityNonFungibleBalanceChanges> nonFungibleEntityBalanceChanges) {
    this.nonFungibleEntityBalanceChanges = nonFungibleEntityBalanceChanges;
  }


  /**
   * Return true if this CommittedTransactionBalanceChanges object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommittedTransactionBalanceChanges committedTransactionBalanceChanges = (CommittedTransactionBalanceChanges) o;
    return Objects.equals(this.fungibleEntityBalanceChanges, committedTransactionBalanceChanges.fungibleEntityBalanceChanges) &&
        Objects.equals(this.nonFungibleEntityBalanceChanges, committedTransactionBalanceChanges.nonFungibleEntityBalanceChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fungibleEntityBalanceChanges, nonFungibleEntityBalanceChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommittedTransactionBalanceChanges {\n");
    sb.append("    fungibleEntityBalanceChanges: ").append(toIndentedString(fungibleEntityBalanceChanges)).append("\n");
    sb.append("    nonFungibleEntityBalanceChanges: ").append(toIndentedString(nonFungibleEntityBalanceChanges)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

