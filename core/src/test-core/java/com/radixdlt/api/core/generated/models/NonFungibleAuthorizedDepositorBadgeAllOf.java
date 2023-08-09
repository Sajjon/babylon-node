/*
 * Babylon Core API - RCnet v3
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the second release candidate of the Radix Babylon network (\"RCnet v3\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code. 
 *
 * The version of the OpenAPI document: 0.5.0
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
import com.radixdlt.api.core.generated.models.NonFungibleGlobalId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * NonFungibleAuthorizedDepositorBadgeAllOf
 */
@JsonPropertyOrder({
  NonFungibleAuthorizedDepositorBadgeAllOf.JSON_PROPERTY_NON_FUNGIBLE_GLOBAL_ID
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class NonFungibleAuthorizedDepositorBadgeAllOf {
  public static final String JSON_PROPERTY_NON_FUNGIBLE_GLOBAL_ID = "non_fungible_global_id";
  private NonFungibleGlobalId nonFungibleGlobalId;

  public NonFungibleAuthorizedDepositorBadgeAllOf() { 
  }

  public NonFungibleAuthorizedDepositorBadgeAllOf nonFungibleGlobalId(NonFungibleGlobalId nonFungibleGlobalId) {
    this.nonFungibleGlobalId = nonFungibleGlobalId;
    return this;
  }

   /**
   * Get nonFungibleGlobalId
   * @return nonFungibleGlobalId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_GLOBAL_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public NonFungibleGlobalId getNonFungibleGlobalId() {
    return nonFungibleGlobalId;
  }


  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_GLOBAL_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNonFungibleGlobalId(NonFungibleGlobalId nonFungibleGlobalId) {
    this.nonFungibleGlobalId = nonFungibleGlobalId;
  }


  /**
   * Return true if this NonFungibleAuthorizedDepositorBadge_allOf object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NonFungibleAuthorizedDepositorBadgeAllOf nonFungibleAuthorizedDepositorBadgeAllOf = (NonFungibleAuthorizedDepositorBadgeAllOf) o;
    return Objects.equals(this.nonFungibleGlobalId, nonFungibleAuthorizedDepositorBadgeAllOf.nonFungibleGlobalId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nonFungibleGlobalId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NonFungibleAuthorizedDepositorBadgeAllOf {\n");
    sb.append("    nonFungibleGlobalId: ").append(toIndentedString(nonFungibleGlobalId)).append("\n");
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

