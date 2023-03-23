/*
 *   Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.coffeebeans.springnativeawslambda.infra.resource;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.DYNAMODB_TABLE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

/**
 * This record represents a DynamoDb Table.
 * <pre>
 *       final DynamoDBTableProvisionedThroughput dynamoDBTableProvisionedThroughput = DynamoDBTableProvisionedThroughput.builder()
 *         .readCapacityUnits(1)
 *         .writeCapacityUnits(1)
 *         .build();
 *
 *     final DynamoDBTableAttributeDefinition attributeDefinition = DynamoDBTableAttributeDefinition.builder()
 *         .attributeName("id")
 *         .attributeType("S")
 *         .build();
 *
 *     final DynamoDBTableKeySchema keySchema = DynamoDBTableKeySchema.builder()
 *         .attributeName("id")
 *         .keyType("HASH")
 *         .build();
 *
 *     final DynamoDBTableProperties dynamoDBTableProperties = DynamoDBTableProperties.builder()
 *         .tableName("test")
 *         .billingMode("mode")
 *         .provisionedThroughput(dynamoDBTableProvisionedThroughput)
 *         .attributeDefinition(attributeDefinition)
 *         .keySchema(keySchema)
 *         .build();
 *
 *     final DynamoDBTable dynamoDBTable = DynamoDBTable.builder()
 *         .deletionPolicy("Retain")
 *         .updateReplacePolicy("Retain")
 *         .properties(dynamoDBTableProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record DynamoDBTable(@JsonProperty("Properties") DynamoDBTableProperties properties,
                            @JsonProperty("DeletionPolicy") String deletionPolicy,
                            @JsonProperty("UpdateReplacePolicy") String updateReplacePolicy) {

  @JsonProperty("Type")
  static String type = DYNAMODB_TABLE.getValue();

  /**
   * This record represents a DynamoDb Table properties.
   * <pre>
   *       final DynamoDBTableProvisionedThroughput dynamoDBTableProvisionedThroughput = DynamoDBTableProvisionedThroughput.builder()
   *         .readCapacityUnits(1)
   *         .writeCapacityUnits(1)
   *         .build();
   *
   *     final DynamoDBTableAttributeDefinition attributeDefinition = DynamoDBTableAttributeDefinition.builder()
   *         .attributeName("id")
   *         .attributeType("S")
   *         .build();
   *
   *     final DynamoDBTableKeySchema keySchema = DynamoDBTableKeySchema.builder()
   *         .attributeName("id")
   *         .keyType("HASH")
   *         .build();
   *
   *     final DynamoDBTableProperties dynamoDBTableProperties = DynamoDBTableProperties.builder()
   *         .tableName("test")
   *         .billingMode("mode")
   *         .provisionedThroughput(dynamoDBTableProvisionedThroughput)
   *         .attributeDefinition(attributeDefinition)
   *         .keySchema(keySchema)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableProperties(@Singular @JsonProperty("AttributeDefinitions") List<DynamoDBTableAttributeDefinition> attributeDefinitions,
                                        @Singular @JsonProperty("KeySchema") List<DynamoDBTableKeySchema> keySchemas,
                                        @JsonProperty("TableName") String tableName,
                                        @JsonProperty("BillingMode") String billingMode,
                                        @JsonProperty("ProvisionedThroughput") DynamoDBTableProvisionedThroughput provisionedThroughput) {

  }

  /**
   * This record represents a DynamoDb Table key schema.
   * <pre>
   *     final DynamoDBTableKeySchema keySchema = DynamoDBTableKeySchema.builder()
   *         .attributeName("id")
   *         .keyType("HASH")
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableKeySchema(@JsonProperty("AttributeName") String attributeName,
                                       @JsonProperty("KeyType") String keyType) {

  }

  /**
   * This record represents a DynamoDb Table attribute definition.
   * <pre>
   *     final DynamoDBTableAttributeDefinition attributeDefinition = DynamoDBTableAttributeDefinition.builder()
   *         .attributeName("id")
   *         .attributeType("S")
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableAttributeDefinition(@JsonProperty("AttributeName") String attributeName,
                                                 @JsonProperty("AttributeType") String attributeType) {

  }

  /**
   * This record represents a DynamoDb Table provisioned throughput.
   * <pre>
   *       final DynamoDBTableProvisionedThroughput dynamoDBTableProvisionedThroughput = DynamoDBTableProvisionedThroughput.builder()
   *         .readCapacityUnits(1)
   *         .writeCapacityUnits(1)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableProvisionedThroughput(@JsonProperty("ReadCapacityUnits") Integer readCapacityUnits,
                                                   @JsonProperty("WriteCapacityUnits") Integer writeCapacityUnits) {

  }
}