/*
 * Radix Gateway API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.9.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.radixdlt.api.gateway.openapitools.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.radixdlt.api.gateway.openapitools.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TransactionFinalizeResponseError
 */
@JsonPropertyOrder({
  TransactionFinalizeResponseError.JSON_PROPERTY_ERROR
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-12-01T19:51:43.675505-06:00[America/Chicago]")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = TransactionFinalizeResponseError.class, name = "TransactionFinalizeResponseError"),
  @JsonSubTypes.Type(value = TransactionFinalizeResponseSuccess.class, name = "TransactionFinalizeResponseSuccess"),
})

public class TransactionFinalizeResponseError extends TransactionFinalizeResponse {
  public static final String JSON_PROPERTY_ERROR = "error";
  private Error error;


  public TransactionFinalizeResponseError error(Error error) {
    this.error = error;
    return this;
  }

   /**
   * Get error
   * @return error
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_ERROR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Error getError() {
    return error;
  }


  @JsonProperty(JSON_PROPERTY_ERROR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setError(Error error) {
    this.error = error;
  }


  /**
   * Return true if this TransactionFinalizeResponseError object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionFinalizeResponseError transactionFinalizeResponseError = (TransactionFinalizeResponseError) o;
    return Objects.equals(this.error, transactionFinalizeResponseError.error) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionFinalizeResponseError {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

static {
  // Initialize and register the discriminator mappings.
  Map<String, Class<?>> mappings = new HashMap<String, Class<?>>();
  mappings.put("TransactionFinalizeResponseError", TransactionFinalizeResponseError.class);
  mappings.put("TransactionFinalizeResponseSuccess", TransactionFinalizeResponseSuccess.class);
  mappings.put("TransactionFinalizeResponseError", TransactionFinalizeResponseError.class);
  JSON.registerDiscriminator(TransactionFinalizeResponseError.class, "type", mappings);
}
}

