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
import com.radixdlt.api.core.generated.models.LedgerStateSummary;
import com.radixdlt.api.core.generated.models.ProtocolVersionReadiness;
import com.radixdlt.api.core.generated.models.Substate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * StateConsensusManagerResponse
 */
@JsonPropertyOrder({
  StateConsensusManagerResponse.JSON_PROPERTY_AT_LEDGER_STATE,
  StateConsensusManagerResponse.JSON_PROPERTY_CONFIG,
  StateConsensusManagerResponse.JSON_PROPERTY_STATE,
  StateConsensusManagerResponse.JSON_PROPERTY_CURRENT_PROPOSAL_STATISTIC,
  StateConsensusManagerResponse.JSON_PROPERTY_CURRENT_VALIDATOR_SET,
  StateConsensusManagerResponse.JSON_PROPERTY_CURRENT_TIME,
  StateConsensusManagerResponse.JSON_PROPERTY_CURRENT_TIME_ROUNDED_TO_MINUTES,
  StateConsensusManagerResponse.JSON_PROPERTY_CURRENT_VALIDATOR_READINESS_SIGNALS
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class StateConsensusManagerResponse {
  public static final String JSON_PROPERTY_AT_LEDGER_STATE = "at_ledger_state";
  private LedgerStateSummary atLedgerState;

  public static final String JSON_PROPERTY_CONFIG = "config";
  private Substate config;

  public static final String JSON_PROPERTY_STATE = "state";
  private Substate state;

  public static final String JSON_PROPERTY_CURRENT_PROPOSAL_STATISTIC = "current_proposal_statistic";
  private Substate currentProposalStatistic;

  public static final String JSON_PROPERTY_CURRENT_VALIDATOR_SET = "current_validator_set";
  private Substate currentValidatorSet;

  public static final String JSON_PROPERTY_CURRENT_TIME = "current_time";
  private Substate currentTime;

  public static final String JSON_PROPERTY_CURRENT_TIME_ROUNDED_TO_MINUTES = "current_time_rounded_to_minutes";
  private Substate currentTimeRoundedToMinutes;

  public static final String JSON_PROPERTY_CURRENT_VALIDATOR_READINESS_SIGNALS = "current_validator_readiness_signals";
  private List<ProtocolVersionReadiness> currentValidatorReadinessSignals = null;

  public StateConsensusManagerResponse() { 
  }

  public StateConsensusManagerResponse atLedgerState(LedgerStateSummary atLedgerState) {
    this.atLedgerState = atLedgerState;
    return this;
  }

   /**
   * Get atLedgerState
   * @return atLedgerState
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_AT_LEDGER_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LedgerStateSummary getAtLedgerState() {
    return atLedgerState;
  }


  @JsonProperty(JSON_PROPERTY_AT_LEDGER_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAtLedgerState(LedgerStateSummary atLedgerState) {
    this.atLedgerState = atLedgerState;
  }


  public StateConsensusManagerResponse config(Substate config) {
    this.config = config;
    return this;
  }

   /**
   * Get config
   * @return config
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CONFIG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Substate getConfig() {
    return config;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setConfig(Substate config) {
    this.config = config;
  }


  public StateConsensusManagerResponse state(Substate state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Substate getState() {
    return state;
  }


  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setState(Substate state) {
    this.state = state;
  }


  public StateConsensusManagerResponse currentProposalStatistic(Substate currentProposalStatistic) {
    this.currentProposalStatistic = currentProposalStatistic;
    return this;
  }

   /**
   * Get currentProposalStatistic
   * @return currentProposalStatistic
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CURRENT_PROPOSAL_STATISTIC)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Substate getCurrentProposalStatistic() {
    return currentProposalStatistic;
  }


  @JsonProperty(JSON_PROPERTY_CURRENT_PROPOSAL_STATISTIC)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCurrentProposalStatistic(Substate currentProposalStatistic) {
    this.currentProposalStatistic = currentProposalStatistic;
  }


  public StateConsensusManagerResponse currentValidatorSet(Substate currentValidatorSet) {
    this.currentValidatorSet = currentValidatorSet;
    return this;
  }

   /**
   * Get currentValidatorSet
   * @return currentValidatorSet
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CURRENT_VALIDATOR_SET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Substate getCurrentValidatorSet() {
    return currentValidatorSet;
  }


  @JsonProperty(JSON_PROPERTY_CURRENT_VALIDATOR_SET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCurrentValidatorSet(Substate currentValidatorSet) {
    this.currentValidatorSet = currentValidatorSet;
  }


  public StateConsensusManagerResponse currentTime(Substate currentTime) {
    this.currentTime = currentTime;
    return this;
  }

   /**
   * Get currentTime
   * @return currentTime
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CURRENT_TIME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Substate getCurrentTime() {
    return currentTime;
  }


  @JsonProperty(JSON_PROPERTY_CURRENT_TIME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCurrentTime(Substate currentTime) {
    this.currentTime = currentTime;
  }


  public StateConsensusManagerResponse currentTimeRoundedToMinutes(Substate currentTimeRoundedToMinutes) {
    this.currentTimeRoundedToMinutes = currentTimeRoundedToMinutes;
    return this;
  }

   /**
   * Get currentTimeRoundedToMinutes
   * @return currentTimeRoundedToMinutes
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CURRENT_TIME_ROUNDED_TO_MINUTES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Substate getCurrentTimeRoundedToMinutes() {
    return currentTimeRoundedToMinutes;
  }


  @JsonProperty(JSON_PROPERTY_CURRENT_TIME_ROUNDED_TO_MINUTES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCurrentTimeRoundedToMinutes(Substate currentTimeRoundedToMinutes) {
    this.currentTimeRoundedToMinutes = currentTimeRoundedToMinutes;
  }


  public StateConsensusManagerResponse currentValidatorReadinessSignals(List<ProtocolVersionReadiness> currentValidatorReadinessSignals) {
    this.currentValidatorReadinessSignals = currentValidatorReadinessSignals;
    return this;
  }

  public StateConsensusManagerResponse addCurrentValidatorReadinessSignalsItem(ProtocolVersionReadiness currentValidatorReadinessSignalsItem) {
    if (this.currentValidatorReadinessSignals == null) {
      this.currentValidatorReadinessSignals = new ArrayList<>();
    }
    this.currentValidatorReadinessSignals.add(currentValidatorReadinessSignalsItem);
    return this;
  }

   /**
   * Protocol versions signalled by current Validator set. Only returned if enabled by &#x60;include_readiness_signals&#x60; on your request. 
   * @return currentValidatorReadinessSignals
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Protocol versions signalled by current Validator set. Only returned if enabled by `include_readiness_signals` on your request. ")
  @JsonProperty(JSON_PROPERTY_CURRENT_VALIDATOR_READINESS_SIGNALS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<ProtocolVersionReadiness> getCurrentValidatorReadinessSignals() {
    return currentValidatorReadinessSignals;
  }


  @JsonProperty(JSON_PROPERTY_CURRENT_VALIDATOR_READINESS_SIGNALS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCurrentValidatorReadinessSignals(List<ProtocolVersionReadiness> currentValidatorReadinessSignals) {
    this.currentValidatorReadinessSignals = currentValidatorReadinessSignals;
  }


  /**
   * Return true if this StateConsensusManagerResponse object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateConsensusManagerResponse stateConsensusManagerResponse = (StateConsensusManagerResponse) o;
    return Objects.equals(this.atLedgerState, stateConsensusManagerResponse.atLedgerState) &&
        Objects.equals(this.config, stateConsensusManagerResponse.config) &&
        Objects.equals(this.state, stateConsensusManagerResponse.state) &&
        Objects.equals(this.currentProposalStatistic, stateConsensusManagerResponse.currentProposalStatistic) &&
        Objects.equals(this.currentValidatorSet, stateConsensusManagerResponse.currentValidatorSet) &&
        Objects.equals(this.currentTime, stateConsensusManagerResponse.currentTime) &&
        Objects.equals(this.currentTimeRoundedToMinutes, stateConsensusManagerResponse.currentTimeRoundedToMinutes) &&
        Objects.equals(this.currentValidatorReadinessSignals, stateConsensusManagerResponse.currentValidatorReadinessSignals);
  }

  @Override
  public int hashCode() {
    return Objects.hash(atLedgerState, config, state, currentProposalStatistic, currentValidatorSet, currentTime, currentTimeRoundedToMinutes, currentValidatorReadinessSignals);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateConsensusManagerResponse {\n");
    sb.append("    atLedgerState: ").append(toIndentedString(atLedgerState)).append("\n");
    sb.append("    config: ").append(toIndentedString(config)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    currentProposalStatistic: ").append(toIndentedString(currentProposalStatistic)).append("\n");
    sb.append("    currentValidatorSet: ").append(toIndentedString(currentValidatorSet)).append("\n");
    sb.append("    currentTime: ").append(toIndentedString(currentTime)).append("\n");
    sb.append("    currentTimeRoundedToMinutes: ").append(toIndentedString(currentTimeRoundedToMinutes)).append("\n");
    sb.append("    currentValidatorReadinessSignals: ").append(toIndentedString(currentValidatorReadinessSignals)).append("\n");
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

