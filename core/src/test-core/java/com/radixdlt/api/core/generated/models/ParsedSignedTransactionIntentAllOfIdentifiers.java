/*
 * Babylon Core API - RCnet V1
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the first release candidate of the Radix Babylon network (\"RCnet-V1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code.  We give no guarantees that other endpoints will not change before Babylon mainnet launch, although changes are expected to be minimal. 
 *
 * The version of the OpenAPI document: 0.3.0
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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * ParsedSignedTransactionIntentAllOfIdentifiers
 */
@JsonPropertyOrder({
  ParsedSignedTransactionIntentAllOfIdentifiers.JSON_PROPERTY_INTENT_HASH,
  ParsedSignedTransactionIntentAllOfIdentifiers.JSON_PROPERTY_SIGNATURES_HASH
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ParsedSignedTransactionIntentAllOfIdentifiers {
  public static final String JSON_PROPERTY_INTENT_HASH = "intent_hash";
  private String intentHash;

  public static final String JSON_PROPERTY_SIGNATURES_HASH = "signatures_hash";
  private String signaturesHash;

  public ParsedSignedTransactionIntentAllOfIdentifiers() { 
  }

  public ParsedSignedTransactionIntentAllOfIdentifiers intentHash(String intentHash) {
    this.intentHash = intentHash;
    return this;
  }

   /**
   * The hex-encoded transaction intent hash. This is known as the Intent Hash, Transaction ID or Transaction Identifier for user transactions. This hash is &#x60;Blake2b-256(compiled_intent)&#x60;
   * @return intentHash
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The hex-encoded transaction intent hash. This is known as the Intent Hash, Transaction ID or Transaction Identifier for user transactions. This hash is `Blake2b-256(compiled_intent)`")
  @JsonProperty(JSON_PROPERTY_INTENT_HASH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getIntentHash() {
    return intentHash;
  }


  @JsonProperty(JSON_PROPERTY_INTENT_HASH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIntentHash(String intentHash) {
    this.intentHash = intentHash;
  }


  public ParsedSignedTransactionIntentAllOfIdentifiers signaturesHash(String signaturesHash) {
    this.signaturesHash = signaturesHash;
    return this;
  }

   /**
   * The hex-encoded signed transaction hash. This is known as the Signed Transaction Hash or Signatures Hash. This is the hash which is signed as part of notarization. This hash is &#x60;Blake2b-256(compiled_signed_transaction)&#x60;
   * @return signaturesHash
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The hex-encoded signed transaction hash. This is known as the Signed Transaction Hash or Signatures Hash. This is the hash which is signed as part of notarization. This hash is `Blake2b-256(compiled_signed_transaction)`")
  @JsonProperty(JSON_PROPERTY_SIGNATURES_HASH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getSignaturesHash() {
    return signaturesHash;
  }


  @JsonProperty(JSON_PROPERTY_SIGNATURES_HASH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSignaturesHash(String signaturesHash) {
    this.signaturesHash = signaturesHash;
  }


  /**
   * Return true if this ParsedSignedTransactionIntent_allOf_identifiers object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParsedSignedTransactionIntentAllOfIdentifiers parsedSignedTransactionIntentAllOfIdentifiers = (ParsedSignedTransactionIntentAllOfIdentifiers) o;
    return Objects.equals(this.intentHash, parsedSignedTransactionIntentAllOfIdentifiers.intentHash) &&
        Objects.equals(this.signaturesHash, parsedSignedTransactionIntentAllOfIdentifiers.signaturesHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(intentHash, signaturesHash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParsedSignedTransactionIntentAllOfIdentifiers {\n");
    sb.append("    intentHash: ").append(toIndentedString(intentHash)).append("\n");
    sb.append("    signaturesHash: ").append(toIndentedString(signaturesHash)).append("\n");
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

