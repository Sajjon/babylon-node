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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.radixdlt.api.core.generated.models.BlueprintSchemaCollectionPartition;
import com.radixdlt.api.core.generated.models.BlueprintSchemaFieldPartition;
import com.radixdlt.api.core.generated.models.FunctionSchema;
import com.radixdlt.api.core.generated.models.LocalTypeIndex;
import com.radixdlt.api.core.generated.models.ScryptoSchema;
import com.radixdlt.api.core.generated.models.VirtualLazyLoadSchema;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * BlueprintSchema
 */
@JsonPropertyOrder({
  BlueprintSchema.JSON_PROPERTY_OUTER_BLUEPRINT,
  BlueprintSchema.JSON_PROPERTY_SCHEMA,
  BlueprintSchema.JSON_PROPERTY_FUNCTION_SCHEMAS,
  BlueprintSchema.JSON_PROPERTY_VIRTUAL_LAZY_LOAD_FUNCTION_SCHEMAS,
  BlueprintSchema.JSON_PROPERTY_EVENT_SCHEMAS,
  BlueprintSchema.JSON_PROPERTY_FIELD_PARTITION,
  BlueprintSchema.JSON_PROPERTY_COLLECTION_PARTITIONS,
  BlueprintSchema.JSON_PROPERTY_DEPENDENCIES,
  BlueprintSchema.JSON_PROPERTY_FEATURES
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class BlueprintSchema {
  public static final String JSON_PROPERTY_OUTER_BLUEPRINT = "outer_blueprint";
  private String outerBlueprint;

  public static final String JSON_PROPERTY_SCHEMA = "schema";
  private ScryptoSchema schema;

  public static final String JSON_PROPERTY_FUNCTION_SCHEMAS = "function_schemas";
  private Map<String, FunctionSchema> functionSchemas = new HashMap<>();

  public static final String JSON_PROPERTY_VIRTUAL_LAZY_LOAD_FUNCTION_SCHEMAS = "virtual_lazy_load_function_schemas";
  private Map<String, VirtualLazyLoadSchema> virtualLazyLoadFunctionSchemas = new HashMap<>();

  public static final String JSON_PROPERTY_EVENT_SCHEMAS = "event_schemas";
  private Map<String, LocalTypeIndex> eventSchemas = new HashMap<>();

  public static final String JSON_PROPERTY_FIELD_PARTITION = "field_partition";
  private BlueprintSchemaFieldPartition fieldPartition;

  public static final String JSON_PROPERTY_COLLECTION_PARTITIONS = "collection_partitions";
  private List<BlueprintSchemaCollectionPartition> collectionPartitions = new ArrayList<>();

  public static final String JSON_PROPERTY_DEPENDENCIES = "dependencies";
  private List<String> dependencies = new ArrayList<>();

  public static final String JSON_PROPERTY_FEATURES = "features";
  private List<String> features = new ArrayList<>();

  public BlueprintSchema() { 
  }

  public BlueprintSchema outerBlueprint(String outerBlueprint) {
    this.outerBlueprint = outerBlueprint;
    return this;
  }

   /**
   * Get outerBlueprint
   * @return outerBlueprint
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_OUTER_BLUEPRINT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOuterBlueprint() {
    return outerBlueprint;
  }


  @JsonProperty(JSON_PROPERTY_OUTER_BLUEPRINT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOuterBlueprint(String outerBlueprint) {
    this.outerBlueprint = outerBlueprint;
  }


  public BlueprintSchema schema(ScryptoSchema schema) {
    this.schema = schema;
    return this;
  }

   /**
   * Get schema
   * @return schema
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_SCHEMA)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public ScryptoSchema getSchema() {
    return schema;
  }


  @JsonProperty(JSON_PROPERTY_SCHEMA)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSchema(ScryptoSchema schema) {
    this.schema = schema;
  }


  public BlueprintSchema functionSchemas(Map<String, FunctionSchema> functionSchemas) {
    this.functionSchemas = functionSchemas;
    return this;
  }

  public BlueprintSchema putFunctionSchemasItem(String key, FunctionSchema functionSchemasItem) {
    this.functionSchemas.put(key, functionSchemasItem);
    return this;
  }

   /**
   * A map from the function name to the FunctionSchema
   * @return functionSchemas
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "A map from the function name to the FunctionSchema")
  @JsonProperty(JSON_PROPERTY_FUNCTION_SCHEMAS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Map<String, FunctionSchema> getFunctionSchemas() {
    return functionSchemas;
  }


  @JsonProperty(JSON_PROPERTY_FUNCTION_SCHEMAS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFunctionSchemas(Map<String, FunctionSchema> functionSchemas) {
    this.functionSchemas = functionSchemas;
  }


  public BlueprintSchema virtualLazyLoadFunctionSchemas(Map<String, VirtualLazyLoadSchema> virtualLazyLoadFunctionSchemas) {
    this.virtualLazyLoadFunctionSchemas = virtualLazyLoadFunctionSchemas;
    return this;
  }

  public BlueprintSchema putVirtualLazyLoadFunctionSchemasItem(String key, VirtualLazyLoadSchema virtualLazyLoadFunctionSchemasItem) {
    this.virtualLazyLoadFunctionSchemas.put(key, virtualLazyLoadFunctionSchemasItem);
    return this;
  }

   /**
   * A map from the system function ID to the VirtualLazyLoadSchema
   * @return virtualLazyLoadFunctionSchemas
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "A map from the system function ID to the VirtualLazyLoadSchema")
  @JsonProperty(JSON_PROPERTY_VIRTUAL_LAZY_LOAD_FUNCTION_SCHEMAS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Map<String, VirtualLazyLoadSchema> getVirtualLazyLoadFunctionSchemas() {
    return virtualLazyLoadFunctionSchemas;
  }


  @JsonProperty(JSON_PROPERTY_VIRTUAL_LAZY_LOAD_FUNCTION_SCHEMAS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVirtualLazyLoadFunctionSchemas(Map<String, VirtualLazyLoadSchema> virtualLazyLoadFunctionSchemas) {
    this.virtualLazyLoadFunctionSchemas = virtualLazyLoadFunctionSchemas;
  }


  public BlueprintSchema eventSchemas(Map<String, LocalTypeIndex> eventSchemas) {
    this.eventSchemas = eventSchemas;
    return this;
  }

  public BlueprintSchema putEventSchemasItem(String key, LocalTypeIndex eventSchemasItem) {
    this.eventSchemas.put(key, eventSchemasItem);
    return this;
  }

   /**
   * A map from the event name to the local type index for the event payload under the blueprint schema.
   * @return eventSchemas
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "A map from the event name to the local type index for the event payload under the blueprint schema.")
  @JsonProperty(JSON_PROPERTY_EVENT_SCHEMAS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Map<String, LocalTypeIndex> getEventSchemas() {
    return eventSchemas;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SCHEMAS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEventSchemas(Map<String, LocalTypeIndex> eventSchemas) {
    this.eventSchemas = eventSchemas;
  }


  public BlueprintSchema fieldPartition(BlueprintSchemaFieldPartition fieldPartition) {
    this.fieldPartition = fieldPartition;
    return this;
  }

   /**
   * Get fieldPartition
   * @return fieldPartition
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_FIELD_PARTITION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BlueprintSchemaFieldPartition getFieldPartition() {
    return fieldPartition;
  }


  @JsonProperty(JSON_PROPERTY_FIELD_PARTITION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFieldPartition(BlueprintSchemaFieldPartition fieldPartition) {
    this.fieldPartition = fieldPartition;
  }


  public BlueprintSchema collectionPartitions(List<BlueprintSchemaCollectionPartition> collectionPartitions) {
    this.collectionPartitions = collectionPartitions;
    return this;
  }

  public BlueprintSchema addCollectionPartitionsItem(BlueprintSchemaCollectionPartition collectionPartitionsItem) {
    this.collectionPartitions.add(collectionPartitionsItem);
    return this;
  }

   /**
   * The collection partitions for this blueprint.
   * @return collectionPartitions
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "The collection partitions for this blueprint.")
  @JsonProperty(JSON_PROPERTY_COLLECTION_PARTITIONS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<BlueprintSchemaCollectionPartition> getCollectionPartitions() {
    return collectionPartitions;
  }


  @JsonProperty(JSON_PROPERTY_COLLECTION_PARTITIONS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCollectionPartitions(List<BlueprintSchemaCollectionPartition> collectionPartitions) {
    this.collectionPartitions = collectionPartitions;
  }


  public BlueprintSchema dependencies(List<String> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  public BlueprintSchema addDependenciesItem(String dependenciesItem) {
    this.dependencies.add(dependenciesItem);
    return this;
  }

   /**
   * Get dependencies
   * @return dependencies
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_DEPENDENCIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<String> getDependencies() {
    return dependencies;
  }


  @JsonProperty(JSON_PROPERTY_DEPENDENCIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }


  public BlueprintSchema features(List<String> features) {
    this.features = features;
    return this;
  }

  public BlueprintSchema addFeaturesItem(String featuresItem) {
    this.features.add(featuresItem);
    return this;
  }

   /**
   * Get features
   * @return features
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_FEATURES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<String> getFeatures() {
    return features;
  }


  @JsonProperty(JSON_PROPERTY_FEATURES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFeatures(List<String> features) {
    this.features = features;
  }


  /**
   * Return true if this BlueprintSchema object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlueprintSchema blueprintSchema = (BlueprintSchema) o;
    return Objects.equals(this.outerBlueprint, blueprintSchema.outerBlueprint) &&
        Objects.equals(this.schema, blueprintSchema.schema) &&
        Objects.equals(this.functionSchemas, blueprintSchema.functionSchemas) &&
        Objects.equals(this.virtualLazyLoadFunctionSchemas, blueprintSchema.virtualLazyLoadFunctionSchemas) &&
        Objects.equals(this.eventSchemas, blueprintSchema.eventSchemas) &&
        Objects.equals(this.fieldPartition, blueprintSchema.fieldPartition) &&
        Objects.equals(this.collectionPartitions, blueprintSchema.collectionPartitions) &&
        Objects.equals(this.dependencies, blueprintSchema.dependencies) &&
        Objects.equals(this.features, blueprintSchema.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outerBlueprint, schema, functionSchemas, virtualLazyLoadFunctionSchemas, eventSchemas, fieldPartition, collectionPartitions, dependencies, features);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlueprintSchema {\n");
    sb.append("    outerBlueprint: ").append(toIndentedString(outerBlueprint)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    functionSchemas: ").append(toIndentedString(functionSchemas)).append("\n");
    sb.append("    virtualLazyLoadFunctionSchemas: ").append(toIndentedString(virtualLazyLoadFunctionSchemas)).append("\n");
    sb.append("    eventSchemas: ").append(toIndentedString(eventSchemas)).append("\n");
    sb.append("    fieldPartition: ").append(toIndentedString(fieldPartition)).append("\n");
    sb.append("    collectionPartitions: ").append(toIndentedString(collectionPartitions)).append("\n");
    sb.append("    dependencies: ").append(toIndentedString(dependencies)).append("\n");
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
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

