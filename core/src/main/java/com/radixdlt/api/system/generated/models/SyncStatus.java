/*
 * Babylon System API - RCnet V2
 * This API is exposed by the Babylon Radix node to give clients access to information about the node itself, its configuration, status and subsystems.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against ledger state, you may also wish to consider using the [Core API or Gateway API instead](https://docs-babylon.radixdlt.com/main/apis/api-specification.html).  ## Integration and forward compatibility guarantees  We give no guarantees that endpoints will not change before Babylon mainnet launch, although changes are expected to be minimal. 
 *
 * The version of the OpenAPI document: 0.4.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.radixdlt.api.system.generated.models;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Information on how synced the node is to the rest of the network.
 */
@ApiModel(description = "Information on how synced the node is to the rest of the network.")
@JsonPropertyOrder({
  SyncStatus.JSON_PROPERTY_CURRENT_STATE_VERSION,
  SyncStatus.JSON_PROPERTY_TARGET_STATE_VERSION
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class SyncStatus {
  public static final String JSON_PROPERTY_CURRENT_STATE_VERSION = "current_state_version";
  private Long currentStateVersion;

  public static final String JSON_PROPERTY_TARGET_STATE_VERSION = "target_state_version";
  private Long targetStateVersion;


  public SyncStatus currentStateVersion(Long currentStateVersion) {
    this.currentStateVersion = currentStateVersion;
    return this;
  }

   /**
   * Get currentStateVersion
   * @return currentStateVersion
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CURRENT_STATE_VERSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getCurrentStateVersion() {
    return currentStateVersion;
  }


  @JsonProperty(JSON_PROPERTY_CURRENT_STATE_VERSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCurrentStateVersion(Long currentStateVersion) {
    this.currentStateVersion = currentStateVersion;
  }


  public SyncStatus targetStateVersion(Long targetStateVersion) {
    this.targetStateVersion = targetStateVersion;
    return this;
  }

   /**
   * Get targetStateVersion
   * @return targetStateVersion
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_TARGET_STATE_VERSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getTargetStateVersion() {
    return targetStateVersion;
  }


  @JsonProperty(JSON_PROPERTY_TARGET_STATE_VERSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTargetStateVersion(Long targetStateVersion) {
    this.targetStateVersion = targetStateVersion;
  }


  /**
   * Return true if this SyncStatus object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SyncStatus syncStatus = (SyncStatus) o;
    return Objects.equals(this.currentStateVersion, syncStatus.currentStateVersion) &&
        Objects.equals(this.targetStateVersion, syncStatus.targetStateVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentStateVersion, targetStateVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SyncStatus {\n");
    sb.append("    currentStateVersion: ").append(toIndentedString(currentStateVersion)).append("\n");
    sb.append("    targetStateVersion: ").append(toIndentedString(targetStateVersion)).append("\n");
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

