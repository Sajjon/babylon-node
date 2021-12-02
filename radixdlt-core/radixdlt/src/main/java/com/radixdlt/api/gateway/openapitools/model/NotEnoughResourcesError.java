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
 * NotEnoughResourcesError
 */
@JsonPropertyOrder({
  NotEnoughResourcesError.JSON_PROPERTY_REQUESTED_AMOUNT,
  NotEnoughResourcesError.JSON_PROPERTY_AVAILABLE_AMOUNT
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-12-01T18:41:04.998487-06:00[America/Chicago]")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = BelowMinimumStakeError.class, name = "BelowMinimumStakeError"),
  @JsonSubTypes.Type(value = CouldNotConstructFeesError.class, name = "CouldNotConstructFeesError"),
  @JsonSubTypes.Type(value = InvalidAccountAddressError.class, name = "InvalidAccountAddress"),
  @JsonSubTypes.Type(value = InvalidPublicKeyError.class, name = "InvalidPublicKey"),
  @JsonSubTypes.Type(value = InvalidTokenRRIError.class, name = "InvalidTokenRRI"),
  @JsonSubTypes.Type(value = InvalidTokenSymbolError.class, name = "InvalidTokenSymbol"),
  @JsonSubTypes.Type(value = InvalidValidatorAddressError.class, name = "InvalidValidatorAddress"),
  @JsonSubTypes.Type(value = MessageTooLongError.class, name = "MessageTooLongError"),
  @JsonSubTypes.Type(value = NotEnoughResourcesError.class, name = "NotEnoughResourcesError"),
  @JsonSubTypes.Type(value = NotValidatorOwnerError.class, name = "NotValidatorOwnerError"),
  @JsonSubTypes.Type(value = TokenNotFoundError.class, name = "TokenNotFound"),
})

public class NotEnoughResourcesError extends Error {
  public static final String JSON_PROPERTY_REQUESTED_AMOUNT = "requested_amount";
  private String requestedAmount;

  public static final String JSON_PROPERTY_AVAILABLE_AMOUNT = "available_amount";
  private String availableAmount;


  public NotEnoughResourcesError requestedAmount(String requestedAmount) {
    this.requestedAmount = requestedAmount;
    return this;
  }

   /**
   * Get requestedAmount
   * @return requestedAmount
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_REQUESTED_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getRequestedAmount() {
    return requestedAmount;
  }


  @JsonProperty(JSON_PROPERTY_REQUESTED_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRequestedAmount(String requestedAmount) {
    this.requestedAmount = requestedAmount;
  }


  public NotEnoughResourcesError availableAmount(String availableAmount) {
    this.availableAmount = availableAmount;
    return this;
  }

   /**
   * Get availableAmount
   * @return availableAmount
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_AVAILABLE_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getAvailableAmount() {
    return availableAmount;
  }


  @JsonProperty(JSON_PROPERTY_AVAILABLE_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAvailableAmount(String availableAmount) {
    this.availableAmount = availableAmount;
  }


  /**
   * Return true if this NotEnoughResourcesError object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotEnoughResourcesError notEnoughResourcesError = (NotEnoughResourcesError) o;
    return Objects.equals(this.requestedAmount, notEnoughResourcesError.requestedAmount) &&
        Objects.equals(this.availableAmount, notEnoughResourcesError.availableAmount) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestedAmount, availableAmount, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotEnoughResourcesError {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    requestedAmount: ").append(toIndentedString(requestedAmount)).append("\n");
    sb.append("    availableAmount: ").append(toIndentedString(availableAmount)).append("\n");
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
  mappings.put("BelowMinimumStakeError", BelowMinimumStakeError.class);
  mappings.put("CouldNotConstructFeesError", CouldNotConstructFeesError.class);
  mappings.put("InvalidAccountAddress", InvalidAccountAddressError.class);
  mappings.put("InvalidPublicKey", InvalidPublicKeyError.class);
  mappings.put("InvalidTokenRRI", InvalidTokenRRIError.class);
  mappings.put("InvalidTokenSymbol", InvalidTokenSymbolError.class);
  mappings.put("InvalidValidatorAddress", InvalidValidatorAddressError.class);
  mappings.put("MessageTooLongError", MessageTooLongError.class);
  mappings.put("NotEnoughResourcesError", NotEnoughResourcesError.class);
  mappings.put("NotValidatorOwnerError", NotValidatorOwnerError.class);
  mappings.put("TokenNotFound", TokenNotFoundError.class);
  mappings.put("NotEnoughResourcesError", NotEnoughResourcesError.class);
  JSON.registerDiscriminator(NotEnoughResourcesError.class, "type", mappings);
}
}

