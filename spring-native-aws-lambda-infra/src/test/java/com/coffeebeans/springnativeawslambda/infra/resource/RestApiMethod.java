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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_METHOD;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * This record is used to represent a ApiGateway rest api method.
 *
 * <pre>
 *       final IntrinsicFunctionArn uri = IntrinsicFunctionArn.builder()
 *         .joinArn(EMPTY)
 *         .joinArn(List.of(
 *             "arn:",
 *             ResourceReference.builder().reference(exact("AWS::Partition")).build(),
 *             ":apigateway:",
 *             ResourceReference.builder().reference(exact("AWS::Region")).build(),
 *             ":lambda:path/2015-03-31/functions/",
 *             IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("identifier(.*)")).attributesArn("Arn").build(),
 *             "/invocations"))
 *         .build();
 *
 *     final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder()
 *         .type("AWS_PROXY")
 *         .integrationHttpMethod("POST")
 *         .uri(uri)
 *         .build();
 *
 *     final RestApiMethodProperties restApiMethodProperties = RestApiMethodProperties.builder()
 *         .httpMethod("POST")
 *         .nonRootResourceId(ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build())
 *         .restApiId(ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build())
 *         .authorizationType("NONE")
 *         .integration(restApiMethodIntegration)
 *         .build();
 *
 *     final RestApiMethod restApiMethod = RestApiMethod.builder()
 *         .properties(restApiMethodProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiMethod(@JsonProperty("Properties") RestApiMethodProperties properties) {

  @JsonProperty("Type")
  static String type = APIGATEWAY_RESTAPI_METHOD.getValue();

  /**
   * This record is used to represent a ApiGateway rest api method properties.
   *
   * <pre>
   *       final IntrinsicFunctionArn uri = IntrinsicFunctionArn.builder()
   *         .joinArn(EMPTY)
   *         .joinArn(List.of(
   *             "arn:",
   *             ResourceReference.builder().reference(exact("AWS::Partition")).build(),
   *             ":apigateway:",
   *             ResourceReference.builder().reference(exact("AWS::Region")).build(),
   *             ":lambda:path/2015-03-31/functions/",
   *             IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("identifier(.*)")).attributesArn("Arn").build(),
   *             "/invocations"))
   *         .build();
   *
   *     final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type("AWS_PROXY")
   *         .integrationHttpMethod("POST")
   *         .uri(uri)
   *         .build();
   *
   *     final RestApiMethodProperties restApiMethodProperties = RestApiMethodProperties.builder()
   *         .httpMethod("POST")
   *         .nonRootResourceId(ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build())
   *         .restApiId(ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build())
   *         .authorizationType("NONE")
   *         .integration(restApiMethodIntegration)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RestApiMethodProperties(@JsonProperty("HttpMethod") String httpMethod,
                                        @JsonProperty("RestApiId") ResourceReference restApiId,
                                        @JsonProperty("Integration") RestApiMethodIntegration integration,
                                        @JsonProperty("AuthorizationType") String authorizationType,
                                        @JsonProperty("ResourceId") Object resourceId) {

  }

  /**
   * This record is used to represent a ApiGateway rest api method integration.
   *
   * <pre>
   *       final IntrinsicFunctionArn uri = IntrinsicFunctionArn.builder()
   *         .joinArn(EMPTY)
   *         .joinArn(List.of(
   *             "arn:",
   *             ResourceReference.builder().reference(exact("AWS::Partition")).build(),
   *             ":apigateway:",
   *             ResourceReference.builder().reference(exact("AWS::Region")).build(),
   *             ":lambda:path/2015-03-31/functions/",
   *             IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("identifier(.*)")).attributesArn("Arn").build(),
   *             "/invocations"))
   *         .build();
   *
   *     final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type("AWS_PROXY")
   *         .integrationHttpMethod("POST")
   *         .uri(uri)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RestApiMethodIntegration(@JsonProperty("IntegrationHttpMethod") String integrationHttpMethod,
                                         @JsonProperty("Type") String type,
                                         @JsonProperty("Uri") IntrinsicFunctionArn uri) {

  }
}
