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
import com.radixdlt.api.core.generated.models.ParsedTransaction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * TransactionParseResponse
 */
@JsonPropertyOrder({
  TransactionParseResponse.JSON_PROPERTY_PARSED
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class TransactionParseResponse {
  public static final String JSON_PROPERTY_PARSED = "parsed";
  private ParsedTransaction parsed;

  public TransactionParseResponse() { 
  }

  public TransactionParseResponse parsed(ParsedTransaction parsed) {
    this.parsed = parsed;
    return this;
  }

   /**
   * Get parsed
   * @return parsed
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PARSED)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public ParsedTransaction getParsed() {
    return parsed;
  }


  @JsonProperty(JSON_PROPERTY_PARSED)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setParsed(ParsedTransaction parsed) {
    this.parsed = parsed;
  }


  /**
   * Return true if this TransactionParseResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionParseResponse transactionParseResponse = (TransactionParseResponse) o;
    return Objects.equals(this.parsed, transactionParseResponse.parsed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parsed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionParseResponse {\n");
    sb.append("    parsed: ").append(toIndentedString(parsed)).append("\n");
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

