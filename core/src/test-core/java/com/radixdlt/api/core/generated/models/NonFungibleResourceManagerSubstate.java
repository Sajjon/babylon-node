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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.radixdlt.api.core.generated.models.AccessControllerSubstate;
import com.radixdlt.api.core.generated.models.AccessRulesSubstate;
import com.radixdlt.api.core.generated.models.AccountSubstate;
import com.radixdlt.api.core.generated.models.ClockSubstate;
import com.radixdlt.api.core.generated.models.ComponentRoyaltyAccumulatorSubstate;
import com.radixdlt.api.core.generated.models.ComponentRoyaltyConfigSubstate;
import com.radixdlt.api.core.generated.models.ComponentStateSubstate;
import com.radixdlt.api.core.generated.models.EntityReference;
import com.radixdlt.api.core.generated.models.EpochManagerSubstate;
import com.radixdlt.api.core.generated.models.FungibleResourceManagerSubstate;
import com.radixdlt.api.core.generated.models.KeyValueStoreEntrySubstate;
import com.radixdlt.api.core.generated.models.LocalTypeIndex;
import com.radixdlt.api.core.generated.models.MetadataEntrySubstate;
import com.radixdlt.api.core.generated.models.NonFungibleIdType;
import com.radixdlt.api.core.generated.models.NonFungibleResourceManagerSubstate;
import com.radixdlt.api.core.generated.models.NonFungibleResourceManagerSubstateAllOf;
import com.radixdlt.api.core.generated.models.PackageCodeSubstate;
import com.radixdlt.api.core.generated.models.PackageCodeTypeSubstate;
import com.radixdlt.api.core.generated.models.PackageFunctionAccessRulesSubstate;
import com.radixdlt.api.core.generated.models.PackageInfoSubstate;
import com.radixdlt.api.core.generated.models.PackageRoyaltySubstate;
import com.radixdlt.api.core.generated.models.Substate;
import com.radixdlt.api.core.generated.models.SubstateType;
import com.radixdlt.api.core.generated.models.TypeInfoSubstate;
import com.radixdlt.api.core.generated.models.ValidatorSetSubstate;
import com.radixdlt.api.core.generated.models.ValidatorSubstate;
import com.radixdlt.api.core.generated.models.VaultFungibleSubstate;
import com.radixdlt.api.core.generated.models.VaultInfoSubstate;
import com.radixdlt.api.core.generated.models.VaultNonFungibleSubstate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import com.radixdlt.api.core.generated.client.JSON;
/**
 * NonFungibleResourceManagerSubstate
 */
@JsonPropertyOrder({
  NonFungibleResourceManagerSubstate.JSON_PROPERTY_NON_FUNGIBLE_ID_TYPE,
  NonFungibleResourceManagerSubstate.JSON_PROPERTY_TOTAL_SUPPLY,
  NonFungibleResourceManagerSubstate.JSON_PROPERTY_NON_FUNGIBLE_DATA_TABLE,
  NonFungibleResourceManagerSubstate.JSON_PROPERTY_NON_FUNGIBLE_DATA_TYPE_INDEX,
  NonFungibleResourceManagerSubstate.JSON_PROPERTY_NON_FUNGIBLE_DATA_MUTABLE_FIELDS
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
@JsonIgnoreProperties(
  value = "substate_type", // ignore manually set substate_type, it will be automatically generated by Jackson during serialization
  allowSetters = true // allows the substate_type to be set during deserialization
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "substate_type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = AccessControllerSubstate.class, name = "AccessController"),
  @JsonSubTypes.Type(value = AccessRulesSubstate.class, name = "AccessRules"),
  @JsonSubTypes.Type(value = AccountSubstate.class, name = "Account"),
  @JsonSubTypes.Type(value = ClockSubstate.class, name = "Clock"),
  @JsonSubTypes.Type(value = ComponentRoyaltyAccumulatorSubstate.class, name = "ComponentRoyaltyAccumulator"),
  @JsonSubTypes.Type(value = ComponentRoyaltyConfigSubstate.class, name = "ComponentRoyaltyConfig"),
  @JsonSubTypes.Type(value = ComponentStateSubstate.class, name = "ComponentState"),
  @JsonSubTypes.Type(value = EpochManagerSubstate.class, name = "EpochManager"),
  @JsonSubTypes.Type(value = FungibleResourceManagerSubstate.class, name = "FungibleResourceManager"),
  @JsonSubTypes.Type(value = KeyValueStoreEntrySubstate.class, name = "KeyValueStoreEntry"),
  @JsonSubTypes.Type(value = MetadataEntrySubstate.class, name = "MetadataEntry"),
  @JsonSubTypes.Type(value = NonFungibleResourceManagerSubstate.class, name = "NonFungibleResourceManager"),
  @JsonSubTypes.Type(value = PackageCodeSubstate.class, name = "PackageCode"),
  @JsonSubTypes.Type(value = PackageCodeTypeSubstate.class, name = "PackageCodeType"),
  @JsonSubTypes.Type(value = PackageFunctionAccessRulesSubstate.class, name = "PackageFunctionAccessRules"),
  @JsonSubTypes.Type(value = PackageInfoSubstate.class, name = "PackageInfo"),
  @JsonSubTypes.Type(value = PackageRoyaltySubstate.class, name = "PackageRoyalty"),
  @JsonSubTypes.Type(value = TypeInfoSubstate.class, name = "TypeInfo"),
  @JsonSubTypes.Type(value = ValidatorSubstate.class, name = "Validator"),
  @JsonSubTypes.Type(value = ValidatorSetSubstate.class, name = "ValidatorSet"),
  @JsonSubTypes.Type(value = VaultFungibleSubstate.class, name = "VaultFungible"),
  @JsonSubTypes.Type(value = VaultInfoSubstate.class, name = "VaultInfo"),
  @JsonSubTypes.Type(value = VaultNonFungibleSubstate.class, name = "VaultNonFungible"),
})

public class NonFungibleResourceManagerSubstate extends Substate {
  public static final String JSON_PROPERTY_NON_FUNGIBLE_ID_TYPE = "non_fungible_id_type";
  private NonFungibleIdType nonFungibleIdType;

  public static final String JSON_PROPERTY_TOTAL_SUPPLY = "total_supply";
  private String totalSupply;

  public static final String JSON_PROPERTY_NON_FUNGIBLE_DATA_TABLE = "non_fungible_data_table";
  private EntityReference nonFungibleDataTable;

  public static final String JSON_PROPERTY_NON_FUNGIBLE_DATA_TYPE_INDEX = "non_fungible_data_type_index";
  private LocalTypeIndex nonFungibleDataTypeIndex;

  public static final String JSON_PROPERTY_NON_FUNGIBLE_DATA_MUTABLE_FIELDS = "non_fungible_data_mutable_fields";
  private List<String> nonFungibleDataMutableFields = new ArrayList<>();

  public NonFungibleResourceManagerSubstate() { 
  }

  public NonFungibleResourceManagerSubstate nonFungibleIdType(NonFungibleIdType nonFungibleIdType) {
    this.nonFungibleIdType = nonFungibleIdType;
    return this;
  }

   /**
   * Get nonFungibleIdType
   * @return nonFungibleIdType
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_ID_TYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public NonFungibleIdType getNonFungibleIdType() {
    return nonFungibleIdType;
  }


  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_ID_TYPE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNonFungibleIdType(NonFungibleIdType nonFungibleIdType) {
    this.nonFungibleIdType = nonFungibleIdType;
  }


  public NonFungibleResourceManagerSubstate totalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
    return this;
  }

   /**
   * The string-encoded decimal representing the total supply of this resource. A decimal is formed of some signed integer &#x60;m&#x60; of attos (&#x60;10^(-18)&#x60;) units, where &#x60;-2^(256 - 1) &lt;&#x3D; m &lt; 2^(256 - 1)&#x60;. 
   * @return totalSupply
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The string-encoded decimal representing the total supply of this resource. A decimal is formed of some signed integer `m` of attos (`10^(-18)`) units, where `-2^(256 - 1) <= m < 2^(256 - 1)`. ")
  @JsonProperty(JSON_PROPERTY_TOTAL_SUPPLY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getTotalSupply() {
    return totalSupply;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_SUPPLY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTotalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
  }


  public NonFungibleResourceManagerSubstate nonFungibleDataTable(EntityReference nonFungibleDataTable) {
    this.nonFungibleDataTable = nonFungibleDataTable;
    return this;
  }

   /**
   * Get nonFungibleDataTable
   * @return nonFungibleDataTable
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_DATA_TABLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public EntityReference getNonFungibleDataTable() {
    return nonFungibleDataTable;
  }


  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_DATA_TABLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNonFungibleDataTable(EntityReference nonFungibleDataTable) {
    this.nonFungibleDataTable = nonFungibleDataTable;
  }


  public NonFungibleResourceManagerSubstate nonFungibleDataTypeIndex(LocalTypeIndex nonFungibleDataTypeIndex) {
    this.nonFungibleDataTypeIndex = nonFungibleDataTypeIndex;
    return this;
  }

   /**
   * Get nonFungibleDataTypeIndex
   * @return nonFungibleDataTypeIndex
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_DATA_TYPE_INDEX)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalTypeIndex getNonFungibleDataTypeIndex() {
    return nonFungibleDataTypeIndex;
  }


  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_DATA_TYPE_INDEX)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNonFungibleDataTypeIndex(LocalTypeIndex nonFungibleDataTypeIndex) {
    this.nonFungibleDataTypeIndex = nonFungibleDataTypeIndex;
  }


  public NonFungibleResourceManagerSubstate nonFungibleDataMutableFields(List<String> nonFungibleDataMutableFields) {
    this.nonFungibleDataMutableFields = nonFungibleDataMutableFields;
    return this;
  }

  public NonFungibleResourceManagerSubstate addNonFungibleDataMutableFieldsItem(String nonFungibleDataMutableFieldsItem) {
    this.nonFungibleDataMutableFields.add(nonFungibleDataMutableFieldsItem);
    return this;
  }

   /**
   * The field names of the NF Metadata which are mutable. 
   * @return nonFungibleDataMutableFields
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The field names of the NF Metadata which are mutable. ")
  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_DATA_MUTABLE_FIELDS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<String> getNonFungibleDataMutableFields() {
    return nonFungibleDataMutableFields;
  }


  @JsonProperty(JSON_PROPERTY_NON_FUNGIBLE_DATA_MUTABLE_FIELDS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNonFungibleDataMutableFields(List<String> nonFungibleDataMutableFields) {
    this.nonFungibleDataMutableFields = nonFungibleDataMutableFields;
  }


  /**
   * Return true if this NonFungibleResourceManagerSubstate object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NonFungibleResourceManagerSubstate nonFungibleResourceManagerSubstate = (NonFungibleResourceManagerSubstate) o;
    return Objects.equals(this.nonFungibleIdType, nonFungibleResourceManagerSubstate.nonFungibleIdType) &&
        Objects.equals(this.totalSupply, nonFungibleResourceManagerSubstate.totalSupply) &&
        Objects.equals(this.nonFungibleDataTable, nonFungibleResourceManagerSubstate.nonFungibleDataTable) &&
        Objects.equals(this.nonFungibleDataTypeIndex, nonFungibleResourceManagerSubstate.nonFungibleDataTypeIndex) &&
        Objects.equals(this.nonFungibleDataMutableFields, nonFungibleResourceManagerSubstate.nonFungibleDataMutableFields) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nonFungibleIdType, totalSupply, nonFungibleDataTable, nonFungibleDataTypeIndex, nonFungibleDataMutableFields, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NonFungibleResourceManagerSubstate {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    nonFungibleIdType: ").append(toIndentedString(nonFungibleIdType)).append("\n");
    sb.append("    totalSupply: ").append(toIndentedString(totalSupply)).append("\n");
    sb.append("    nonFungibleDataTable: ").append(toIndentedString(nonFungibleDataTable)).append("\n");
    sb.append("    nonFungibleDataTypeIndex: ").append(toIndentedString(nonFungibleDataTypeIndex)).append("\n");
    sb.append("    nonFungibleDataMutableFields: ").append(toIndentedString(nonFungibleDataMutableFields)).append("\n");
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
  mappings.put("AccessController", AccessControllerSubstate.class);
  mappings.put("AccessRules", AccessRulesSubstate.class);
  mappings.put("Account", AccountSubstate.class);
  mappings.put("Clock", ClockSubstate.class);
  mappings.put("ComponentRoyaltyAccumulator", ComponentRoyaltyAccumulatorSubstate.class);
  mappings.put("ComponentRoyaltyConfig", ComponentRoyaltyConfigSubstate.class);
  mappings.put("ComponentState", ComponentStateSubstate.class);
  mappings.put("EpochManager", EpochManagerSubstate.class);
  mappings.put("FungibleResourceManager", FungibleResourceManagerSubstate.class);
  mappings.put("KeyValueStoreEntry", KeyValueStoreEntrySubstate.class);
  mappings.put("MetadataEntry", MetadataEntrySubstate.class);
  mappings.put("NonFungibleResourceManager", NonFungibleResourceManagerSubstate.class);
  mappings.put("PackageCode", PackageCodeSubstate.class);
  mappings.put("PackageCodeType", PackageCodeTypeSubstate.class);
  mappings.put("PackageFunctionAccessRules", PackageFunctionAccessRulesSubstate.class);
  mappings.put("PackageInfo", PackageInfoSubstate.class);
  mappings.put("PackageRoyalty", PackageRoyaltySubstate.class);
  mappings.put("TypeInfo", TypeInfoSubstate.class);
  mappings.put("Validator", ValidatorSubstate.class);
  mappings.put("ValidatorSet", ValidatorSetSubstate.class);
  mappings.put("VaultFungible", VaultFungibleSubstate.class);
  mappings.put("VaultInfo", VaultInfoSubstate.class);
  mappings.put("VaultNonFungible", VaultNonFungibleSubstate.class);
  mappings.put("NonFungibleResourceManagerSubstate", NonFungibleResourceManagerSubstate.class);
  JSON.registerDiscriminator(NonFungibleResourceManagerSubstate.class, "substate_type", mappings);
}
}

