/*
 * Babylon Core API
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node. It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Heavy load may impact the node's function.  If you require queries against historical ledger state, you may also wish to consider using the [Gateway API](https://betanet-gateway.redoc.ly/). 
 *
 * The version of the OpenAPI document: 0.2.0
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.radixdlt.api.core.generated.models.EpochUpdateValidatorTransaction;
import com.radixdlt.api.core.generated.models.Instant;
import com.radixdlt.api.core.generated.models.TimeUpdateValidatorTransaction;
import com.radixdlt.api.core.generated.models.TimeUpdateValidatorTransactionAllOf;
import com.radixdlt.api.core.generated.models.ValidatorTransaction;
import com.radixdlt.api.core.generated.models.ValidatorTransactionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import com.radixdlt.api.core.generated.client.JSON;
/**
 * TimeUpdateValidatorTransaction
 */
@JsonPropertyOrder({
  TimeUpdateValidatorTransaction.JSON_PROPERTY_PROPOSER_TIMESTAMP,
  TimeUpdateValidatorTransaction.JSON_PROPERTY_CONSENSUS_EPOCH,
  TimeUpdateValidatorTransaction.JSON_PROPERTY_ROUND_IN_EPOCH
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
@JsonIgnoreProperties(
  value = "type", // ignore manually set type, it will be automatically generated by Jackson during serialization
  allowSetters = true // allows the type to be set during deserialization
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = EpochUpdateValidatorTransaction.class, name = "EpochUpdate"),
  @JsonSubTypes.Type(value = TimeUpdateValidatorTransaction.class, name = "TimeUpdate"),
})

public class TimeUpdateValidatorTransaction extends ValidatorTransaction {
  public static final String JSON_PROPERTY_PROPOSER_TIMESTAMP = "proposer_timestamp";
  private Instant proposerTimestamp;

  public static final String JSON_PROPERTY_CONSENSUS_EPOCH = "consensus_epoch";
  private Long consensusEpoch;

  public static final String JSON_PROPERTY_ROUND_IN_EPOCH = "round_in_epoch";
  private Long roundInEpoch;

  public TimeUpdateValidatorTransaction() { 
  }

  public TimeUpdateValidatorTransaction proposerTimestamp(Instant proposerTimestamp) {
    this.proposerTimestamp = proposerTimestamp;
    return this;
  }

   /**
   * Get proposerTimestamp
   * @return proposerTimestamp
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PROPOSER_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Instant getProposerTimestamp() {
    return proposerTimestamp;
  }


  @JsonProperty(JSON_PROPERTY_PROPOSER_TIMESTAMP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setProposerTimestamp(Instant proposerTimestamp) {
    this.proposerTimestamp = proposerTimestamp;
  }


  public TimeUpdateValidatorTransaction consensusEpoch(Long consensusEpoch) {
    this.consensusEpoch = consensusEpoch;
    return this;
  }

   /**
   * An integer between &#x60;0&#x60; and &#x60;10^10&#x60;, marking the consensus epoch. Note that currently this is not the same as &#x60;scrypto_epoch&#x60;, but eventually will be. 
   * minimum: 0
   * maximum: 10000000000
   * @return consensusEpoch
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "An integer between `0` and `10^10`, marking the consensus epoch. Note that currently this is not the same as `scrypto_epoch`, but eventually will be. ")
  @JsonProperty(JSON_PROPERTY_CONSENSUS_EPOCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getConsensusEpoch() {
    return consensusEpoch;
  }


  @JsonProperty(JSON_PROPERTY_CONSENSUS_EPOCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setConsensusEpoch(Long consensusEpoch) {
    this.consensusEpoch = consensusEpoch;
  }


  public TimeUpdateValidatorTransaction roundInEpoch(Long roundInEpoch) {
    this.roundInEpoch = roundInEpoch;
    return this;
  }

   /**
   * An integer between &#x60;0&#x60; and &#x60;10^10&#x60;, marking the consensus round in the epoch
   * minimum: 0
   * maximum: 10000000000
   * @return roundInEpoch
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "An integer between `0` and `10^10`, marking the consensus round in the epoch")
  @JsonProperty(JSON_PROPERTY_ROUND_IN_EPOCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getRoundInEpoch() {
    return roundInEpoch;
  }


  @JsonProperty(JSON_PROPERTY_ROUND_IN_EPOCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRoundInEpoch(Long roundInEpoch) {
    this.roundInEpoch = roundInEpoch;
  }


  /**
   * Return true if this TimeUpdateValidatorTransaction object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeUpdateValidatorTransaction timeUpdateValidatorTransaction = (TimeUpdateValidatorTransaction) o;
    return Objects.equals(this.proposerTimestamp, timeUpdateValidatorTransaction.proposerTimestamp) &&
        Objects.equals(this.consensusEpoch, timeUpdateValidatorTransaction.consensusEpoch) &&
        Objects.equals(this.roundInEpoch, timeUpdateValidatorTransaction.roundInEpoch) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(proposerTimestamp, consensusEpoch, roundInEpoch, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeUpdateValidatorTransaction {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    proposerTimestamp: ").append(toIndentedString(proposerTimestamp)).append("\n");
    sb.append("    consensusEpoch: ").append(toIndentedString(consensusEpoch)).append("\n");
    sb.append("    roundInEpoch: ").append(toIndentedString(roundInEpoch)).append("\n");
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
  mappings.put("EpochUpdate", EpochUpdateValidatorTransaction.class);
  mappings.put("TimeUpdate", TimeUpdateValidatorTransaction.class);
  mappings.put("TimeUpdateValidatorTransaction", TimeUpdateValidatorTransaction.class);
  JSON.registerDiscriminator(TimeUpdateValidatorTransaction.class, "type", mappings);
}
}

