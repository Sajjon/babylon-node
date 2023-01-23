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
import com.radixdlt.api.core.generated.models.DynamicResourceDescriptor;
import com.radixdlt.api.core.generated.models.DynamicResourceDescriptorType;
import com.radixdlt.api.core.generated.models.NonFungibleDynamicResourceDescriptor;
import com.radixdlt.api.core.generated.models.ResourceDynamicResourceDescriptor;
import com.radixdlt.api.core.generated.models.SchemaPathDynamicAmountAllOf;
import com.radixdlt.api.core.generated.models.SchemaPathDynamicResourceDescriptor;
import com.radixdlt.api.core.generated.models.SchemaSubpath;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import com.radixdlt.api.core.generated.client.JSON;
/**
 * SchemaPathDynamicResourceDescriptor
 */
@JsonPropertyOrder({
  SchemaPathDynamicResourceDescriptor.JSON_PROPERTY_SCHEMA_PATH
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
@JsonIgnoreProperties(
  value = "type", // ignore manually set type, it will be automatically generated by Jackson during serialization
  allowSetters = true // allows the type to be set during deserialization
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = NonFungibleDynamicResourceDescriptor.class, name = "NonFungible"),
  @JsonSubTypes.Type(value = ResourceDynamicResourceDescriptor.class, name = "Resource"),
  @JsonSubTypes.Type(value = SchemaPathDynamicResourceDescriptor.class, name = "SchemaPath"),
})

public class SchemaPathDynamicResourceDescriptor extends DynamicResourceDescriptor {
  public static final String JSON_PROPERTY_SCHEMA_PATH = "schema_path";
  private List<SchemaSubpath> schemaPath = new ArrayList<>();

  public SchemaPathDynamicResourceDescriptor() { 
  }

  public SchemaPathDynamicResourceDescriptor schemaPath(List<SchemaSubpath> schemaPath) {
    this.schemaPath = schemaPath;
    return this;
  }

  public SchemaPathDynamicResourceDescriptor addSchemaPathItem(SchemaSubpath schemaPathItem) {
    this.schemaPath.add(schemaPathItem);
    return this;
  }

   /**
   * Get schemaPath
   * @return schemaPath
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_SCHEMA_PATH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<SchemaSubpath> getSchemaPath() {
    return schemaPath;
  }


  @JsonProperty(JSON_PROPERTY_SCHEMA_PATH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSchemaPath(List<SchemaSubpath> schemaPath) {
    this.schemaPath = schemaPath;
  }


  /**
   * Return true if this SchemaPathDynamicResourceDescriptor object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SchemaPathDynamicResourceDescriptor schemaPathDynamicResourceDescriptor = (SchemaPathDynamicResourceDescriptor) o;
    return Objects.equals(this.schemaPath, schemaPathDynamicResourceDescriptor.schemaPath) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schemaPath, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SchemaPathDynamicResourceDescriptor {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    schemaPath: ").append(toIndentedString(schemaPath)).append("\n");
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
  mappings.put("NonFungible", NonFungibleDynamicResourceDescriptor.class);
  mappings.put("Resource", ResourceDynamicResourceDescriptor.class);
  mappings.put("SchemaPath", SchemaPathDynamicResourceDescriptor.class);
  mappings.put("SchemaPathDynamicResourceDescriptor", SchemaPathDynamicResourceDescriptor.class);
  JSON.registerDiscriminator(SchemaPathDynamicResourceDescriptor.class, "type", mappings);
}
}

