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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_RESOURCE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * This record is used to represent a ApiGateway rest api resource.
 *
 * <pre>
 *       final IntrinsicFunctionArn parentId = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("RootResourceId"))
 *         .build();
 *
 *     final ResourceReference restApiId = ResourceReference.builder()
 *         .reference(stringLikeRegexp("identifier(.*)"))
 *         .build();
 *
 *     final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder()
 *         .parentId(parentId)
 *         .pathPart("{proxy+}")
 *         .restApiId(restApiId)
 *         .build();
 *
 *     final RestApiResource restApiResource = RestApiResource.builder()
 *         .properties(restApiResourceProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiResource(@JsonProperty("Properties") RestApiResourceProperties properties) {

  static String type = APIGATEWAY_RESTAPI_RESOURCE.getValue();

  /**
   * This record is used to represent a ApiGateway rest api resource properties.
   *
   * <pre>
   *       final IntrinsicFunctionArn parentId = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("RootResourceId")
   *         .build();
   *
   *     final ResourceReference restApiId = ResourceReference.builder()
   *         .reference(stringLikeRegexp("identifier(.*)"))
   *         .build();
   *
   *     final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder()
   *         .parentId(parentId)
   *         .pathPart("{proxy+}")
   *         .restApiId(restApiId)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RestApiResourceProperties(
      @JsonProperty("ParentId") IntrinsicFunctionArn parentId,
      @JsonProperty("PathPart") String pathPart,
      @JsonProperty("RestApiId") ResourceReference restApiId) {

  }
}
