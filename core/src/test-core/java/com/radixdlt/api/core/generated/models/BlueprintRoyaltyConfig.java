/*
 * Babylon Core API - RCnet v3.1
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the fourth release candidate of the Radix Babylon network (\"RCnet v3.1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code. 
 *
 * The version of the OpenAPI document: 0.5.1
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
import com.radixdlt.api.core.generated.models.BlueprintMethodRoyalty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * BlueprintRoyaltyConfig
 */
@JsonPropertyOrder({
  BlueprintRoyaltyConfig.JSON_PROPERTY_IS_ENABLED,
  BlueprintRoyaltyConfig.JSON_PROPERTY_METHOD_RULES
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class BlueprintRoyaltyConfig {
  public static final String JSON_PROPERTY_IS_ENABLED = "is_enabled";
  private Boolean isEnabled;

  public static final String JSON_PROPERTY_METHOD_RULES = "method_rules";
  private List<BlueprintMethodRoyalty> methodRules = null;

  public BlueprintRoyaltyConfig() { 
  }

  public BlueprintRoyaltyConfig isEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

   /**
   * Get isEnabled
   * @return isEnabled
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_IS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getIsEnabled() {
    return isEnabled;
  }


  @JsonProperty(JSON_PROPERTY_IS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }


  public BlueprintRoyaltyConfig methodRules(List<BlueprintMethodRoyalty> methodRules) {
    this.methodRules = methodRules;
    return this;
  }

  public BlueprintRoyaltyConfig addMethodRulesItem(BlueprintMethodRoyalty methodRulesItem) {
    if (this.methodRules == null) {
      this.methodRules = new ArrayList<>();
    }
    this.methodRules.add(methodRulesItem);
    return this;
  }

   /**
   * The royalty rules by method. The array is only present if royalties are enabled.
   * @return methodRules
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The royalty rules by method. The array is only present if royalties are enabled.")
  @JsonProperty(JSON_PROPERTY_METHOD_RULES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<BlueprintMethodRoyalty> getMethodRules() {
    return methodRules;
  }


  @JsonProperty(JSON_PROPERTY_METHOD_RULES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMethodRules(List<BlueprintMethodRoyalty> methodRules) {
    this.methodRules = methodRules;
  }


  /**
   * Return true if this BlueprintRoyaltyConfig object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlueprintRoyaltyConfig blueprintRoyaltyConfig = (BlueprintRoyaltyConfig) o;
    return Objects.equals(this.isEnabled, blueprintRoyaltyConfig.isEnabled) &&
        Objects.equals(this.methodRules, blueprintRoyaltyConfig.methodRules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isEnabled, methodRules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlueprintRoyaltyConfig {\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    methodRules: ").append(toIndentedString(methodRules)).append("\n");
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

