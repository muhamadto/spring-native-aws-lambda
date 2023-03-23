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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

/**
 * This record is used to represent a ApiGateway rest api account.
 * <pre>
 *       final IntrinsicFunctionArn cloudWatchRoleArn = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final RestApiAccountProperties restApiAccountProperties = RestApiAccountProperties.builder()
 *         .cloudWatchRoleArn(cloudWatchRoleArn)
 *         .build();
 *
 *     final RestApiAccount restApiAccount = RestApiAccount.builder()
 *         .properties(restApiAccountProperties)
 *         .dependency(stringLikeRegexp("identifier(.*)"))
 *         .updateReplacePolicy(stringLikeRegexp("Retain"))
 *         .deletionPolicy(stringLikeRegexp("Retain"))
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiAccount(@JsonProperty("Properties") RestApiAccountProperties properties,
                             @Singular @JsonProperty("DependsOn") List<Matcher> dependencies,
                             @JsonProperty("UpdateReplacePolicy") String updateReplacePolicy,
                             @JsonProperty("DeletionPolicy") String deletionPolicy) {

  @JsonProperty("Type")
  static String type = APIGATEWAY_RESTAPI_ACCOUNT.getValue();

  /**
   * This record is used to represent a ApiGateway rest api account properties.
   * <pre>
   *       final IntrinsicFunctionArn cloudWatchRoleArn = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final RestApiAccountProperties restApiAccountProperties = RestApiAccountProperties.builder()
   *         .cloudWatchRoleArn(cloudWatchRoleArn)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RestApiAccountProperties(@JsonProperty("CloudWatchRoleArn") IntrinsicFunctionArn cloudWatchRoleArn) {

  }
}
