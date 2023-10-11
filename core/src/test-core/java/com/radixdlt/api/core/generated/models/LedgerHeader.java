/*
 * Radix Core API - Babylon
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  The default configuration is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function. The node exposes a configuration flag which allows disabling certain endpoints which may be problematic, but monitoring is advised. This configuration parameter is `api.core.flags.enable_unbounded_endpoints` / `RADIXDLT_CORE_API_FLAGS_ENABLE_UNBOUNDED_ENDPOINTS`.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` have high guarantees of forward compatibility in future node versions. We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code.  Other endpoints may be changed with new node versions carrying protocol-updates, although any breaking changes will be flagged clearly in the corresponding release notes.  All responses may have additional fields added, so clients are advised to use JSON parsers which ignore unknown fields on JSON objects. 
 *
 * The version of the OpenAPI document: v1.0.4
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
import com.radixdlt.api.core.generated.models.LedgerHashes;
import com.radixdlt.api.core.generated.models.NextEpoch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * LedgerHeader
 */
@JsonPropertyOrder({
  LedgerHeader.JSON_PROPERTY_EPOCH,
  LedgerHeader.JSON_PROPERTY_ROUND,
  LedgerHeader.JSON_PROPERTY_STATE_VERSION,
  LedgerHeader.JSON_PROPERTY_HASHES,
  LedgerHeader.JSON_PROPERTY_CONSENSUS_PARENT_ROUND_TIMESTAMP_MS,
  LedgerHeader.JSON_PROPERTY_PROPOSER_TIMESTAMP_MS,
  LedgerHeader.JSON_PROPERTY_NEXT_EPOCH
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class LedgerHeader {
  public static final String JSON_PROPERTY_EPOCH = "epoch";
  private Long epoch;

  public static final String JSON_PROPERTY_ROUND = "round";
  private Long round;

  public static final String JSON_PROPERTY_STATE_VERSION = "state_version";
  private Long stateVersion;

  public static final String JSON_PROPERTY_HASHES = "hashes";
  private LedgerHashes hashes;

  public static final String JSON_PROPERTY_CONSENSUS_PARENT_ROUND_TIMESTAMP_MS = "consensus_parent_round_timestamp_ms";
  private Long consensusParentRoundTimestampMs;

  public static final String JSON_PROPERTY_PROPOSER_TIMESTAMP_MS = "proposer_timestamp_ms";
  private Long proposerTimestampMs;

  public static final String JSON_PROPERTY_NEXT_EPOCH = "next_epoch";
  private NextEpoch nextEpoch;

  public LedgerHeader() { 
  }

  public LedgerHeader epoch(Long epoch) {
    this.epoch = epoch;
    return this;
  }

   /**
   * An integer between &#x60;0&#x60; and &#x60;10^10&#x60;, marking the epoch.
   * minimum: 0
   * maximum: 10000000000
   * @return epoch
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "An integer between `0` and `10^10`, marking the epoch.")
  @JsonProperty(JSON_PROPERTY_EPOCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getEpoch() {
    return epoch;
  }


  @JsonProperty(JSON_PROPERTY_EPOCH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEpoch(Long epoch) {
    this.epoch = epoch;
  }


  public LedgerHeader round(Long round) {
    this.round = round;
    return this;
  }

   /**
   * An integer between &#x60;0&#x60; and &#x60;10^10&#x60;, marking the current round in an epoch
   * minimum: 0
   * maximum: 10000000000
   * @return round
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "An integer between `0` and `10^10`, marking the current round in an epoch")
  @JsonProperty(JSON_PROPERTY_ROUND)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getRound() {
    return round;
  }


  @JsonProperty(JSON_PROPERTY_ROUND)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRound(Long round) {
    this.round = round;
  }


  public LedgerHeader stateVersion(Long stateVersion) {
    this.stateVersion = stateVersion;
    return this;
  }

   /**
   * Get stateVersion
   * minimum: 1
   * maximum: 100000000000000
   * @return stateVersion
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_STATE_VERSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getStateVersion() {
    return stateVersion;
  }


  @JsonProperty(JSON_PROPERTY_STATE_VERSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setStateVersion(Long stateVersion) {
    this.stateVersion = stateVersion;
  }


  public LedgerHeader hashes(LedgerHashes hashes) {
    this.hashes = hashes;
    return this;
  }

   /**
   * Get hashes
   * @return hashes
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_HASHES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LedgerHashes getHashes() {
    return hashes;
  }


  @JsonProperty(JSON_PROPERTY_HASHES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setHashes(LedgerHashes hashes) {
    this.hashes = hashes;
  }


  public LedgerHeader consensusParentRoundTimestampMs(Long consensusParentRoundTimestampMs) {
    this.consensusParentRoundTimestampMs = consensusParentRoundTimestampMs;
    return this;
  }

   /**
   * An integer between &#x60;0&#x60; and &#x60;10^14&#x60;, marking the consensus parent round timestamp in ms.
   * minimum: 0
   * maximum: 100000000000000
   * @return consensusParentRoundTimestampMs
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "An integer between `0` and `10^14`, marking the consensus parent round timestamp in ms.")
  @JsonProperty(JSON_PROPERTY_CONSENSUS_PARENT_ROUND_TIMESTAMP_MS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getConsensusParentRoundTimestampMs() {
    return consensusParentRoundTimestampMs;
  }


  @JsonProperty(JSON_PROPERTY_CONSENSUS_PARENT_ROUND_TIMESTAMP_MS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setConsensusParentRoundTimestampMs(Long consensusParentRoundTimestampMs) {
    this.consensusParentRoundTimestampMs = consensusParentRoundTimestampMs;
  }


  public LedgerHeader proposerTimestampMs(Long proposerTimestampMs) {
    this.proposerTimestampMs = proposerTimestampMs;
    return this;
  }

   /**
   * An integer between &#x60;0&#x60; and &#x60;10^14&#x60;, marking the proposer timestamp in ms.
   * minimum: 0
   * maximum: 100000000000000
   * @return proposerTimestampMs
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "An integer between `0` and `10^14`, marking the proposer timestamp in ms.")
  @JsonProperty(JSON_PROPERTY_PROPOSER_TIMESTAMP_MS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Long getProposerTimestampMs() {
    return proposerTimestampMs;
  }


  @JsonProperty(JSON_PROPERTY_PROPOSER_TIMESTAMP_MS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setProposerTimestampMs(Long proposerTimestampMs) {
    this.proposerTimestampMs = proposerTimestampMs;
  }


  public LedgerHeader nextEpoch(NextEpoch nextEpoch) {
    this.nextEpoch = nextEpoch;
    return this;
  }

   /**
   * Get nextEpoch
   * @return nextEpoch
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_NEXT_EPOCH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public NextEpoch getNextEpoch() {
    return nextEpoch;
  }


  @JsonProperty(JSON_PROPERTY_NEXT_EPOCH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNextEpoch(NextEpoch nextEpoch) {
    this.nextEpoch = nextEpoch;
  }


  /**
   * Return true if this LedgerHeader object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LedgerHeader ledgerHeader = (LedgerHeader) o;
    return Objects.equals(this.epoch, ledgerHeader.epoch) &&
        Objects.equals(this.round, ledgerHeader.round) &&
        Objects.equals(this.stateVersion, ledgerHeader.stateVersion) &&
        Objects.equals(this.hashes, ledgerHeader.hashes) &&
        Objects.equals(this.consensusParentRoundTimestampMs, ledgerHeader.consensusParentRoundTimestampMs) &&
        Objects.equals(this.proposerTimestampMs, ledgerHeader.proposerTimestampMs) &&
        Objects.equals(this.nextEpoch, ledgerHeader.nextEpoch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(epoch, round, stateVersion, hashes, consensusParentRoundTimestampMs, proposerTimestampMs, nextEpoch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LedgerHeader {\n");
    sb.append("    epoch: ").append(toIndentedString(epoch)).append("\n");
    sb.append("    round: ").append(toIndentedString(round)).append("\n");
    sb.append("    stateVersion: ").append(toIndentedString(stateVersion)).append("\n");
    sb.append("    hashes: ").append(toIndentedString(hashes)).append("\n");
    sb.append("    consensusParentRoundTimestampMs: ").append(toIndentedString(consensusParentRoundTimestampMs)).append("\n");
    sb.append("    proposerTimestampMs: ").append(toIndentedString(proposerTimestampMs)).append("\n");
    sb.append("    nextEpoch: ").append(toIndentedString(nextEpoch)).append("\n");
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

