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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_PERMISSION;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * This record is used to represent a Lambda Permission.
 * <pre>
 *       final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final List<Object> joinArn = List.of(
 *         "arn:",
 *         ResourceReference.builder().reference(exact("AWS::Partition")).build(),
 *         ":execute-api:",
 *         ResourceReference.builder().reference(exact("AWS::Region")).build(),
 *         ":",
 *         ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
 *         ":",
 *         ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build(),
 *         "/",
 *         ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build());
 *
 *     final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
 *         .joinArns(List.of(EMPTY, joinArn))
 *         .build();
 *
 *     final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
 *         .action("lambda:InvokeFunction")
 *         .principal("apigateway.amazonaws.com")
 *         .functionName(functionName)
 *         .sourceArn(sourceArn)
 *         .build();
 *
 *     final LambdaPermission lambdaPermission = LambdaPermission.builder()
 *         .properties(lambdaPermissionProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LambdaPermission(@JsonProperty("Properties") LambdaPermissionProperties properties) {

  @JsonProperty("Type")
  static String type = LAMBDA_PERMISSION.getValue();

  /**
   * This record is used to represent a Lambda Permission properties.
   * <pre>
   *       final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final List<Object> joinArn = List.of(
   *         "arn:",
   *         ResourceReference.builder().reference(exact("AWS::Partition")).build(),
   *         ":execute-api:",
   *         ResourceReference.builder().reference(exact("AWS::Region")).build(),
   *         ":",
   *         ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
   *         ":",
   *         ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build(),
   *         "/",
   *         ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build());
   *
   *     final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
   *         .joinArns(List.of(EMPTY, joinArn))
   *         .build();
   *
   *     final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
   *         .action("lambda:InvokeFunction")
   *         .principal("apigateway.amazonaws.com")
   *         .functionName(functionName)
   *         .sourceArn(sourceArn)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaPermissionProperties(@JsonProperty("Action") String action,
                                           @JsonProperty("FunctionName") IntrinsicFunctionArn functionName,
                                           @JsonProperty("Principal") String principal,
                                           @JsonProperty("SourceArn") IntrinsicFunctionArn sourceArn) {

  }
}