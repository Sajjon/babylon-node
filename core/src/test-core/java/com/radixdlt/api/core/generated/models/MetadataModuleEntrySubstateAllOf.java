/*
 * Babylon Core API - RCnet V2
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the first release candidate of the Radix Babylon network (\"RCnet-V1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code.  We give no guarantees that other endpoints will not change before Babylon mainnet launch, although changes are expected to be minimal. 
 *
 * The version of the OpenAPI document: 0.4.0
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
import com.radixdlt.api.core.generated.models.DataStruct;
import com.radixdlt.api.core.generated.models.MetadataKey;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * MetadataModuleEntrySubstateAllOf
 */
@JsonPropertyOrder({
  MetadataModuleEntrySubstateAllOf.JSON_PROPERTY_KEY,
  MetadataModuleEntrySubstateAllOf.JSON_PROPERTY_DATA_STRUCT
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class MetadataModuleEntrySubstateAllOf {
  public static final String JSON_PROPERTY_KEY = "key";
  private MetadataKey key;

  public static final String JSON_PROPERTY_DATA_STRUCT = "data_struct";
  private DataStruct dataStruct;

  public MetadataModuleEntrySubstateAllOf() { 
  }

  public MetadataModuleEntrySubstateAllOf key(MetadataKey key) {
    this.key = key;
    return this;
  }

   /**
   * Get key
   * @return key
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_KEY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public MetadataKey getKey() {
    return key;
  }


  @JsonProperty(JSON_PROPERTY_KEY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setKey(MetadataKey key) {
    this.key = key;
  }


  public MetadataModuleEntrySubstateAllOf dataStruct(DataStruct dataStruct) {
    this.dataStruct = dataStruct;
    return this;
  }

   /**
   * Get dataStruct
   * @return dataStruct
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_DATA_STRUCT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DataStruct getDataStruct() {
    return dataStruct;
  }


  @JsonProperty(JSON_PROPERTY_DATA_STRUCT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDataStruct(DataStruct dataStruct) {
    this.dataStruct = dataStruct;
  }


  /**
   * Return true if this MetadataModuleEntrySubstate_allOf object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetadataModuleEntrySubstateAllOf metadataModuleEntrySubstateAllOf = (MetadataModuleEntrySubstateAllOf) o;
    return Objects.equals(this.key, metadataModuleEntrySubstateAllOf.key) &&
        Objects.equals(this.dataStruct, metadataModuleEntrySubstateAllOf.dataStruct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, dataStruct);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetadataModuleEntrySubstateAllOf {\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    dataStruct: ").append(toIndentedString(dataStruct)).append("\n");
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

