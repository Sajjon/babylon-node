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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * TransactionManifest
 */
@JsonPropertyOrder({
  TransactionManifest.JSON_PROPERTY_INSTRUCTIONS,
  TransactionManifest.JSON_PROPERTY_BLOBS_HEX
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class TransactionManifest {
  public static final String JSON_PROPERTY_INSTRUCTIONS = "instructions";
  private String instructions;

  public static final String JSON_PROPERTY_BLOBS_HEX = "blobs_hex";
  private Map<String, String> blobsHex = null;

  public TransactionManifest() { 
  }

  public TransactionManifest instructions(String instructions) {
    this.instructions = instructions;
    return this;
  }

   /**
   * The decompiled transaction manifest instructions. Only returned if enabled in TransactionFormatOptions on your request.
   * @return instructions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The decompiled transaction manifest instructions. Only returned if enabled in TransactionFormatOptions on your request.")
  @JsonProperty(JSON_PROPERTY_INSTRUCTIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getInstructions() {
    return instructions;
  }


  @JsonProperty(JSON_PROPERTY_INSTRUCTIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }


  public TransactionManifest blobsHex(Map<String, String> blobsHex) {
    this.blobsHex = blobsHex;
    return this;
  }

  public TransactionManifest putBlobsHexItem(String key, String blobsHexItem) {
    if (this.blobsHex == null) {
      this.blobsHex = new HashMap<>();
    }
    this.blobsHex.put(key, blobsHexItem);
    return this;
  }

   /**
   * A map of the hex-encoded blob hash, to hex-encoded blob content. Only returned if enabled in TransactionFormatOptions on your request.
   * @return blobsHex
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A map of the hex-encoded blob hash, to hex-encoded blob content. Only returned if enabled in TransactionFormatOptions on your request.")
  @JsonProperty(JSON_PROPERTY_BLOBS_HEX)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Map<String, String> getBlobsHex() {
    return blobsHex;
  }


  @JsonProperty(JSON_PROPERTY_BLOBS_HEX)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBlobsHex(Map<String, String> blobsHex) {
    this.blobsHex = blobsHex;
  }


  /**
   * Return true if this TransactionManifest object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionManifest transactionManifest = (TransactionManifest) o;
    return Objects.equals(this.instructions, transactionManifest.instructions) &&
        Objects.equals(this.blobsHex, transactionManifest.blobsHex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instructions, blobsHex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionManifest {\n");
    sb.append("    instructions: ").append(toIndentedString(instructions)).append("\n");
    sb.append("    blobsHex: ").append(toIndentedString(blobsHex)).append("\n");
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

