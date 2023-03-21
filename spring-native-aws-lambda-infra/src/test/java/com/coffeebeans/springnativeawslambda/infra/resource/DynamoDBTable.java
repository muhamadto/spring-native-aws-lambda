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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DynamoDBTable {

  @JsonProperty("DeletionPolicy")
  private String deletionPolicy;

  @JsonProperty("Properties")
  private DynamoDBTableProperties properties;

  @JsonProperty("UpdateReplacePolicy")
  private String updateReplacePolicy;

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableKeySchema(
      @JsonProperty("AttributeName") String attributeName,
      @JsonProperty("KeyType") String keyType
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableAttributeDefinition(
      @JsonProperty("AttributeName") String attributeName,
      @JsonProperty("AttributeType") String attributeType
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableProperties(
      @JsonProperty("AttributeDefinitions") List<DynamoDBTableAttributeDefinition> attributeDefinitions,
      @JsonProperty("KeySchema") List<DynamoDBTableKeySchema> keySchema,
      @JsonProperty("TableName") String tableName,
      @JsonProperty("BillingMode") String billingMode,
      @JsonProperty("ProvisionedThroughput") DynamoDBTableProvisionedThroughput provisionedThroughput
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DynamoDBTableProvisionedThroughput(@JsonProperty("ReadCapacityUnits") Integer readCapacityUnits,
                                                   @JsonProperty("WriteCapacityUnits") Integer writeCapacityUnits) {

  }
}