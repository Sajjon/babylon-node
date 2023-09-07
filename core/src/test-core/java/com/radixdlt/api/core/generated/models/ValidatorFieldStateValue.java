/*
 * Babylon Core API - RCnet v3.1
 * This API is exposed by the Babylon Radix node to give clients access to the Radix Engine, Mempool and State in the node.  It is intended for use by node-runners on a private network, and is not intended to be exposed publicly. Very heavy load may impact the node's function.  This API exposes queries against the node's current state (see `/lts/state/` or `/state/`), and streams of transaction history (under `/lts/stream/` or `/stream`).  If you require queries against snapshots of historical ledger state, you may also wish to consider using the [Gateway API](https://docs-babylon.radixdlt.com/).  ## Integration and forward compatibility guarantees  This version of the Core API belongs to the fourth release candidate of the Radix Babylon network (\"RCnet v3.1\").  Integrators (such as exchanges) are recommended to use the `/lts/` endpoints - they have been designed to be clear and simple for integrators wishing to create and monitor transactions involving fungible transfers to/from accounts.  All endpoints under `/lts/` are guaranteed to be forward compatible to Babylon mainnet launch (and beyond). We may add new fields, but existing fields will not be changed. Assuming the integrating code uses a permissive JSON parser which ignores unknown fields, any additions will not affect existing code. 
 *
 * The version of the OpenAPI document: 0.5.1
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
import com.radixdlt.api.core.generated.models.EcdsaSecp256k1PublicKey;
import com.radixdlt.api.core.generated.models.EntityReference;
import com.radixdlt.api.core.generated.models.PendingOwnerStakeWithdrawal;
import com.radixdlt.api.core.generated.models.SubstateKey;
import com.radixdlt.api.core.generated.models.ValidatorFeeChangeRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * ValidatorFieldStateValue
 */
@JsonPropertyOrder({
  ValidatorFieldStateValue.JSON_PROPERTY_SORTED_KEY,
  ValidatorFieldStateValue.JSON_PROPERTY_PUBLIC_KEY,
  ValidatorFieldStateValue.JSON_PROPERTY_IS_REGISTERED,
  ValidatorFieldStateValue.JSON_PROPERTY_ACCEPTS_DELEGATED_STAKE,
  ValidatorFieldStateValue.JSON_PROPERTY_VALIDATOR_FEE_FACTOR,
  ValidatorFieldStateValue.JSON_PROPERTY_VALIDATOR_FEE_CHANGE_REQUEST,
  ValidatorFieldStateValue.JSON_PROPERTY_STAKE_UNIT_RESOURCE_ADDRESS,
  ValidatorFieldStateValue.JSON_PROPERTY_STAKE_XRD_VAULT,
  ValidatorFieldStateValue.JSON_PROPERTY_CLAIM_TOKEN_RESOURCE_ADDRESS,
  ValidatorFieldStateValue.JSON_PROPERTY_PENDING_XRD_WITHDRAW_VAULT,
  ValidatorFieldStateValue.JSON_PROPERTY_LOCKED_OWNER_STAKE_UNIT_VAULT,
  ValidatorFieldStateValue.JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_UNLOCK_VAULT,
  ValidatorFieldStateValue.JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_WITHDRAWALS,
  ValidatorFieldStateValue.JSON_PROPERTY_ALREADY_UNLOCKED_OWNER_STAKE_UNIT_AMOUNT
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ValidatorFieldStateValue {
  public static final String JSON_PROPERTY_SORTED_KEY = "sorted_key";
  private SubstateKey sortedKey;

  public static final String JSON_PROPERTY_PUBLIC_KEY = "public_key";
  private EcdsaSecp256k1PublicKey publicKey;

  public static final String JSON_PROPERTY_IS_REGISTERED = "is_registered";
  private Boolean isRegistered;

  public static final String JSON_PROPERTY_ACCEPTS_DELEGATED_STAKE = "accepts_delegated_stake";
  private Boolean acceptsDelegatedStake;

  public static final String JSON_PROPERTY_VALIDATOR_FEE_FACTOR = "validator_fee_factor";
  private String validatorFeeFactor;

  public static final String JSON_PROPERTY_VALIDATOR_FEE_CHANGE_REQUEST = "validator_fee_change_request";
  private ValidatorFeeChangeRequest validatorFeeChangeRequest;

  public static final String JSON_PROPERTY_STAKE_UNIT_RESOURCE_ADDRESS = "stake_unit_resource_address";
  private String stakeUnitResourceAddress;

  public static final String JSON_PROPERTY_STAKE_XRD_VAULT = "stake_xrd_vault";
  private EntityReference stakeXrdVault;

  public static final String JSON_PROPERTY_CLAIM_TOKEN_RESOURCE_ADDRESS = "claim_token_resource_address";
  private String claimTokenResourceAddress;

  public static final String JSON_PROPERTY_PENDING_XRD_WITHDRAW_VAULT = "pending_xrd_withdraw_vault";
  private EntityReference pendingXrdWithdrawVault;

  public static final String JSON_PROPERTY_LOCKED_OWNER_STAKE_UNIT_VAULT = "locked_owner_stake_unit_vault";
  private EntityReference lockedOwnerStakeUnitVault;

  public static final String JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_UNLOCK_VAULT = "pending_owner_stake_unit_unlock_vault";
  private EntityReference pendingOwnerStakeUnitUnlockVault;

  public static final String JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_WITHDRAWALS = "pending_owner_stake_unit_withdrawals";
  private List<PendingOwnerStakeWithdrawal> pendingOwnerStakeUnitWithdrawals = new ArrayList<>();

  public static final String JSON_PROPERTY_ALREADY_UNLOCKED_OWNER_STAKE_UNIT_AMOUNT = "already_unlocked_owner_stake_unit_amount";
  private String alreadyUnlockedOwnerStakeUnitAmount;

  public ValidatorFieldStateValue() { 
  }

  public ValidatorFieldStateValue sortedKey(SubstateKey sortedKey) {
    this.sortedKey = sortedKey;
    return this;
  }

   /**
   * Get sortedKey
   * @return sortedKey
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_SORTED_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public SubstateKey getSortedKey() {
    return sortedKey;
  }


  @JsonProperty(JSON_PROPERTY_SORTED_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSortedKey(SubstateKey sortedKey) {
    this.sortedKey = sortedKey;
  }


  public ValidatorFieldStateValue publicKey(EcdsaSecp256k1PublicKey publicKey) {
    this.publicKey = publicKey;
    return this;
  }

   /**
   * Get publicKey
   * @return publicKey
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public EcdsaSecp256k1PublicKey getPublicKey() {
    return publicKey;
  }


  @JsonProperty(JSON_PROPERTY_PUBLIC_KEY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPublicKey(EcdsaSecp256k1PublicKey publicKey) {
    this.publicKey = publicKey;
  }


  public ValidatorFieldStateValue isRegistered(Boolean isRegistered) {
    this.isRegistered = isRegistered;
    return this;
  }

   /**
   * Get isRegistered
   * @return isRegistered
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_IS_REGISTERED)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getIsRegistered() {
    return isRegistered;
  }


  @JsonProperty(JSON_PROPERTY_IS_REGISTERED)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIsRegistered(Boolean isRegistered) {
    this.isRegistered = isRegistered;
  }


  public ValidatorFieldStateValue acceptsDelegatedStake(Boolean acceptsDelegatedStake) {
    this.acceptsDelegatedStake = acceptsDelegatedStake;
    return this;
  }

   /**
   * Get acceptsDelegatedStake
   * @return acceptsDelegatedStake
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_ACCEPTS_DELEGATED_STAKE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getAcceptsDelegatedStake() {
    return acceptsDelegatedStake;
  }


  @JsonProperty(JSON_PROPERTY_ACCEPTS_DELEGATED_STAKE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAcceptsDelegatedStake(Boolean acceptsDelegatedStake) {
    this.acceptsDelegatedStake = acceptsDelegatedStake;
  }


  public ValidatorFieldStateValue validatorFeeFactor(String validatorFeeFactor) {
    this.validatorFeeFactor = validatorFeeFactor;
    return this;
  }

   /**
   * A string-encoded fixed-precision decimal to 18 decimal places. A decimal is formed of some signed integer &#x60;m&#x60; of attos (&#x60;10^(-18)&#x60;) units, where &#x60;-2^(192 - 1) &lt;&#x3D; m &lt; 2^(192 - 1)&#x60;. 
   * @return validatorFeeFactor
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "A string-encoded fixed-precision decimal to 18 decimal places. A decimal is formed of some signed integer `m` of attos (`10^(-18)`) units, where `-2^(192 - 1) <= m < 2^(192 - 1)`. ")
  @JsonProperty(JSON_PROPERTY_VALIDATOR_FEE_FACTOR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getValidatorFeeFactor() {
    return validatorFeeFactor;
  }


  @JsonProperty(JSON_PROPERTY_VALIDATOR_FEE_FACTOR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setValidatorFeeFactor(String validatorFeeFactor) {
    this.validatorFeeFactor = validatorFeeFactor;
  }


  public ValidatorFieldStateValue validatorFeeChangeRequest(ValidatorFeeChangeRequest validatorFeeChangeRequest) {
    this.validatorFeeChangeRequest = validatorFeeChangeRequest;
    return this;
  }

   /**
   * Get validatorFeeChangeRequest
   * @return validatorFeeChangeRequest
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_VALIDATOR_FEE_CHANGE_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ValidatorFeeChangeRequest getValidatorFeeChangeRequest() {
    return validatorFeeChangeRequest;
  }


  @JsonProperty(JSON_PROPERTY_VALIDATOR_FEE_CHANGE_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setValidatorFeeChangeRequest(ValidatorFeeChangeRequest validatorFeeChangeRequest) {
    this.validatorFeeChangeRequest = validatorFeeChangeRequest;
  }


  public ValidatorFieldStateValue stakeUnitResourceAddress(String stakeUnitResourceAddress) {
    this.stakeUnitResourceAddress = stakeUnitResourceAddress;
    return this;
  }

   /**
   * The Bech32m-encoded human readable version of the resource address
   * @return stakeUnitResourceAddress
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The Bech32m-encoded human readable version of the resource address")
  @JsonProperty(JSON_PROPERTY_STAKE_UNIT_RESOURCE_ADDRESS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getStakeUnitResourceAddress() {
    return stakeUnitResourceAddress;
  }


  @JsonProperty(JSON_PROPERTY_STAKE_UNIT_RESOURCE_ADDRESS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setStakeUnitResourceAddress(String stakeUnitResourceAddress) {
    this.stakeUnitResourceAddress = stakeUnitResourceAddress;
  }


  public ValidatorFieldStateValue stakeXrdVault(EntityReference stakeXrdVault) {
    this.stakeXrdVault = stakeXrdVault;
    return this;
  }

   /**
   * Get stakeXrdVault
   * @return stakeXrdVault
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_STAKE_XRD_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public EntityReference getStakeXrdVault() {
    return stakeXrdVault;
  }


  @JsonProperty(JSON_PROPERTY_STAKE_XRD_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setStakeXrdVault(EntityReference stakeXrdVault) {
    this.stakeXrdVault = stakeXrdVault;
  }


  public ValidatorFieldStateValue claimTokenResourceAddress(String claimTokenResourceAddress) {
    this.claimTokenResourceAddress = claimTokenResourceAddress;
    return this;
  }

   /**
   * The Bech32m-encoded human readable version of the resource address
   * @return claimTokenResourceAddress
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The Bech32m-encoded human readable version of the resource address")
  @JsonProperty(JSON_PROPERTY_CLAIM_TOKEN_RESOURCE_ADDRESS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getClaimTokenResourceAddress() {
    return claimTokenResourceAddress;
  }


  @JsonProperty(JSON_PROPERTY_CLAIM_TOKEN_RESOURCE_ADDRESS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setClaimTokenResourceAddress(String claimTokenResourceAddress) {
    this.claimTokenResourceAddress = claimTokenResourceAddress;
  }


  public ValidatorFieldStateValue pendingXrdWithdrawVault(EntityReference pendingXrdWithdrawVault) {
    this.pendingXrdWithdrawVault = pendingXrdWithdrawVault;
    return this;
  }

   /**
   * Get pendingXrdWithdrawVault
   * @return pendingXrdWithdrawVault
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PENDING_XRD_WITHDRAW_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public EntityReference getPendingXrdWithdrawVault() {
    return pendingXrdWithdrawVault;
  }


  @JsonProperty(JSON_PROPERTY_PENDING_XRD_WITHDRAW_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPendingXrdWithdrawVault(EntityReference pendingXrdWithdrawVault) {
    this.pendingXrdWithdrawVault = pendingXrdWithdrawVault;
  }


  public ValidatorFieldStateValue lockedOwnerStakeUnitVault(EntityReference lockedOwnerStakeUnitVault) {
    this.lockedOwnerStakeUnitVault = lockedOwnerStakeUnitVault;
    return this;
  }

   /**
   * Get lockedOwnerStakeUnitVault
   * @return lockedOwnerStakeUnitVault
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_LOCKED_OWNER_STAKE_UNIT_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public EntityReference getLockedOwnerStakeUnitVault() {
    return lockedOwnerStakeUnitVault;
  }


  @JsonProperty(JSON_PROPERTY_LOCKED_OWNER_STAKE_UNIT_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setLockedOwnerStakeUnitVault(EntityReference lockedOwnerStakeUnitVault) {
    this.lockedOwnerStakeUnitVault = lockedOwnerStakeUnitVault;
  }


  public ValidatorFieldStateValue pendingOwnerStakeUnitUnlockVault(EntityReference pendingOwnerStakeUnitUnlockVault) {
    this.pendingOwnerStakeUnitUnlockVault = pendingOwnerStakeUnitUnlockVault;
    return this;
  }

   /**
   * Get pendingOwnerStakeUnitUnlockVault
   * @return pendingOwnerStakeUnitUnlockVault
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_UNLOCK_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public EntityReference getPendingOwnerStakeUnitUnlockVault() {
    return pendingOwnerStakeUnitUnlockVault;
  }


  @JsonProperty(JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_UNLOCK_VAULT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPendingOwnerStakeUnitUnlockVault(EntityReference pendingOwnerStakeUnitUnlockVault) {
    this.pendingOwnerStakeUnitUnlockVault = pendingOwnerStakeUnitUnlockVault;
  }


  public ValidatorFieldStateValue pendingOwnerStakeUnitWithdrawals(List<PendingOwnerStakeWithdrawal> pendingOwnerStakeUnitWithdrawals) {
    this.pendingOwnerStakeUnitWithdrawals = pendingOwnerStakeUnitWithdrawals;
    return this;
  }

  public ValidatorFieldStateValue addPendingOwnerStakeUnitWithdrawalsItem(PendingOwnerStakeWithdrawal pendingOwnerStakeUnitWithdrawalsItem) {
    this.pendingOwnerStakeUnitWithdrawals.add(pendingOwnerStakeUnitWithdrawalsItem);
    return this;
  }

   /**
   * Get pendingOwnerStakeUnitWithdrawals
   * @return pendingOwnerStakeUnitWithdrawals
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_WITHDRAWALS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<PendingOwnerStakeWithdrawal> getPendingOwnerStakeUnitWithdrawals() {
    return pendingOwnerStakeUnitWithdrawals;
  }


  @JsonProperty(JSON_PROPERTY_PENDING_OWNER_STAKE_UNIT_WITHDRAWALS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPendingOwnerStakeUnitWithdrawals(List<PendingOwnerStakeWithdrawal> pendingOwnerStakeUnitWithdrawals) {
    this.pendingOwnerStakeUnitWithdrawals = pendingOwnerStakeUnitWithdrawals;
  }


  public ValidatorFieldStateValue alreadyUnlockedOwnerStakeUnitAmount(String alreadyUnlockedOwnerStakeUnitAmount) {
    this.alreadyUnlockedOwnerStakeUnitAmount = alreadyUnlockedOwnerStakeUnitAmount;
    return this;
  }

   /**
   * A string-encoded fixed-precision decimal to 18 decimal places. A decimal is formed of some signed integer &#x60;m&#x60; of attos (&#x60;10^(-18)&#x60;) units, where &#x60;-2^(192 - 1) &lt;&#x3D; m &lt; 2^(192 - 1)&#x60;. 
   * @return alreadyUnlockedOwnerStakeUnitAmount
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "A string-encoded fixed-precision decimal to 18 decimal places. A decimal is formed of some signed integer `m` of attos (`10^(-18)`) units, where `-2^(192 - 1) <= m < 2^(192 - 1)`. ")
  @JsonProperty(JSON_PROPERTY_ALREADY_UNLOCKED_OWNER_STAKE_UNIT_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getAlreadyUnlockedOwnerStakeUnitAmount() {
    return alreadyUnlockedOwnerStakeUnitAmount;
  }


  @JsonProperty(JSON_PROPERTY_ALREADY_UNLOCKED_OWNER_STAKE_UNIT_AMOUNT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAlreadyUnlockedOwnerStakeUnitAmount(String alreadyUnlockedOwnerStakeUnitAmount) {
    this.alreadyUnlockedOwnerStakeUnitAmount = alreadyUnlockedOwnerStakeUnitAmount;
  }


  /**
   * Return true if this ValidatorFieldStateValue object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidatorFieldStateValue validatorFieldStateValue = (ValidatorFieldStateValue) o;
    return Objects.equals(this.sortedKey, validatorFieldStateValue.sortedKey) &&
        Objects.equals(this.publicKey, validatorFieldStateValue.publicKey) &&
        Objects.equals(this.isRegistered, validatorFieldStateValue.isRegistered) &&
        Objects.equals(this.acceptsDelegatedStake, validatorFieldStateValue.acceptsDelegatedStake) &&
        Objects.equals(this.validatorFeeFactor, validatorFieldStateValue.validatorFeeFactor) &&
        Objects.equals(this.validatorFeeChangeRequest, validatorFieldStateValue.validatorFeeChangeRequest) &&
        Objects.equals(this.stakeUnitResourceAddress, validatorFieldStateValue.stakeUnitResourceAddress) &&
        Objects.equals(this.stakeXrdVault, validatorFieldStateValue.stakeXrdVault) &&
        Objects.equals(this.claimTokenResourceAddress, validatorFieldStateValue.claimTokenResourceAddress) &&
        Objects.equals(this.pendingXrdWithdrawVault, validatorFieldStateValue.pendingXrdWithdrawVault) &&
        Objects.equals(this.lockedOwnerStakeUnitVault, validatorFieldStateValue.lockedOwnerStakeUnitVault) &&
        Objects.equals(this.pendingOwnerStakeUnitUnlockVault, validatorFieldStateValue.pendingOwnerStakeUnitUnlockVault) &&
        Objects.equals(this.pendingOwnerStakeUnitWithdrawals, validatorFieldStateValue.pendingOwnerStakeUnitWithdrawals) &&
        Objects.equals(this.alreadyUnlockedOwnerStakeUnitAmount, validatorFieldStateValue.alreadyUnlockedOwnerStakeUnitAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sortedKey, publicKey, isRegistered, acceptsDelegatedStake, validatorFeeFactor, validatorFeeChangeRequest, stakeUnitResourceAddress, stakeXrdVault, claimTokenResourceAddress, pendingXrdWithdrawVault, lockedOwnerStakeUnitVault, pendingOwnerStakeUnitUnlockVault, pendingOwnerStakeUnitWithdrawals, alreadyUnlockedOwnerStakeUnitAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidatorFieldStateValue {\n");
    sb.append("    sortedKey: ").append(toIndentedString(sortedKey)).append("\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
    sb.append("    isRegistered: ").append(toIndentedString(isRegistered)).append("\n");
    sb.append("    acceptsDelegatedStake: ").append(toIndentedString(acceptsDelegatedStake)).append("\n");
    sb.append("    validatorFeeFactor: ").append(toIndentedString(validatorFeeFactor)).append("\n");
    sb.append("    validatorFeeChangeRequest: ").append(toIndentedString(validatorFeeChangeRequest)).append("\n");
    sb.append("    stakeUnitResourceAddress: ").append(toIndentedString(stakeUnitResourceAddress)).append("\n");
    sb.append("    stakeXrdVault: ").append(toIndentedString(stakeXrdVault)).append("\n");
    sb.append("    claimTokenResourceAddress: ").append(toIndentedString(claimTokenResourceAddress)).append("\n");
    sb.append("    pendingXrdWithdrawVault: ").append(toIndentedString(pendingXrdWithdrawVault)).append("\n");
    sb.append("    lockedOwnerStakeUnitVault: ").append(toIndentedString(lockedOwnerStakeUnitVault)).append("\n");
    sb.append("    pendingOwnerStakeUnitUnlockVault: ").append(toIndentedString(pendingOwnerStakeUnitUnlockVault)).append("\n");
    sb.append("    pendingOwnerStakeUnitWithdrawals: ").append(toIndentedString(pendingOwnerStakeUnitWithdrawals)).append("\n");
    sb.append("    alreadyUnlockedOwnerStakeUnitAmount: ").append(toIndentedString(alreadyUnlockedOwnerStakeUnitAmount)).append("\n");
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

