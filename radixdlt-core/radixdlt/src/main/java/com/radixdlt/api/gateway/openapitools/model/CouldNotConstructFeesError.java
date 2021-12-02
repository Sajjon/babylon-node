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
 * CouldNotConstructFeesError
 */
@JsonPropertyOrder({
  CouldNotConstructFeesError.JSON_PROPERTY_ATTEMPTS
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

public class CouldNotConstructFeesError extends Error {
  public static final String JSON_PROPERTY_ATTEMPTS = "attempts";
  private Integer attempts;


  public CouldNotConstructFeesError attempts(Integer attempts) {
    this.attempts = attempts;
    return this;
  }

   /**
   * Get attempts
   * @return attempts
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_ATTEMPTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAttempts() {
    return attempts;
  }


  @JsonProperty(JSON_PROPERTY_ATTEMPTS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAttempts(Integer attempts) {
    this.attempts = attempts;
  }


  /**
   * Return true if this CouldNotConstructFeesError object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CouldNotConstructFeesError couldNotConstructFeesError = (CouldNotConstructFeesError) o;
    return Objects.equals(this.attempts, couldNotConstructFeesError.attempts) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attempts, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CouldNotConstructFeesError {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    attempts: ").append(toIndentedString(attempts)).append("\n");
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
  mappings.put("CouldNotConstructFeesError", CouldNotConstructFeesError.class);
  JSON.registerDiscriminator(CouldNotConstructFeesError.class, "type", mappings);
}
}

