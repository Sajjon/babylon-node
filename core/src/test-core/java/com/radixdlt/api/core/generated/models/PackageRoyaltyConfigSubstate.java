/*
 * Babylon Core API
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node. It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against historical ledger state, you may also wish to consider using the [Gateway API](https://betanet-gateway.redoc.ly/). 
 *
 * The version of the OpenAPI document: 0.1.0
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
import com.radixdlt.api.core.generated.models.BlueprintRoyaltyConfig;
import com.radixdlt.api.core.generated.models.PackageRoyaltyConfigSubstateAllOf;
import com.radixdlt.api.core.generated.models.SubstateBase;
import com.radixdlt.api.core.generated.models.SubstateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * PackageRoyaltyConfigSubstate
 */
@JsonPropertyOrder({
  PackageRoyaltyConfigSubstate.JSON_PROPERTY_SUBSTATE_TYPE,
  PackageRoyaltyConfigSubstate.JSON_PROPERTY_BLUEPRINT_ROYALTIES
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class PackageRoyaltyConfigSubstate {
  public static final String JSON_PROPERTY_SUBSTATE_TYPE = "substate_type";
  private SubstateType substateType;

  public static final String JSON_PROPERTY_BLUEPRINT_ROYALTIES = "blueprint_royalties";
  private List<BlueprintRoyaltyConfig> blueprintRoyalties = new ArrayList<>();

  public PackageRoyaltyConfigSubstate() { 
  }

  public PackageRoyaltyConfigSubstate substateType(SubstateType substateType) {
    this.substateType = substateType;
    return this;
  }

   /**
   * Get substateType
   * @return substateType
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_SUBSTATE_TYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public SubstateType getSubstateType() {
    return substateType;
  }


  @JsonProperty(JSON_PROPERTY_SUBSTATE_TYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSubstateType(SubstateType substateType) {
    this.substateType = substateType;
  }


  public PackageRoyaltyConfigSubstate blueprintRoyalties(List<BlueprintRoyaltyConfig> blueprintRoyalties) {
    this.blueprintRoyalties = blueprintRoyalties;
    return this;
  }

  public PackageRoyaltyConfigSubstate addBlueprintRoyaltiesItem(BlueprintRoyaltyConfig blueprintRoyaltiesItem) {
    this.blueprintRoyalties.add(blueprintRoyaltiesItem);
    return this;
  }

   /**
   * Get blueprintRoyalties
   * @return blueprintRoyalties
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_BLUEPRINT_ROYALTIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<BlueprintRoyaltyConfig> getBlueprintRoyalties() {
    return blueprintRoyalties;
  }


  @JsonProperty(JSON_PROPERTY_BLUEPRINT_ROYALTIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBlueprintRoyalties(List<BlueprintRoyaltyConfig> blueprintRoyalties) {
    this.blueprintRoyalties = blueprintRoyalties;
  }


  /**
   * Return true if this PackageRoyaltyConfigSubstate object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PackageRoyaltyConfigSubstate packageRoyaltyConfigSubstate = (PackageRoyaltyConfigSubstate) o;
    return Objects.equals(this.substateType, packageRoyaltyConfigSubstate.substateType) &&
        Objects.equals(this.blueprintRoyalties, packageRoyaltyConfigSubstate.blueprintRoyalties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(substateType, blueprintRoyalties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PackageRoyaltyConfigSubstate {\n");
    sb.append("    substateType: ").append(toIndentedString(substateType)).append("\n");
    sb.append("    blueprintRoyalties: ").append(toIndentedString(blueprintRoyalties)).append("\n");
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

