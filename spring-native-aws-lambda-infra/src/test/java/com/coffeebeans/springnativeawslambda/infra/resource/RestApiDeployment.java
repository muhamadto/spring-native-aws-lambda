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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

/**
 * This record is used to represent a ApiGateway rest api deployment.
 *
 * <pre>
 *       final RestApiDeploymentProperties restApiDeploymentProperties = RestApiDeploymentProperties.builder()
 *         .restApiId(ResourceReference.builder()
 *         .reference(stringLikeRegexp("identifier(.*)")).build())
 *         .description("Automatically created by the RestApi construct")
 *         .build();
 *
 *     final RestApiDeployment restApiDeployment = RestApiDeployment.builder().properties(restApiDeploymentProperties)
 *         .dependency(stringLikeRegexp("identifier(.*)"))
 *         .dependency(stringLikeRegexp("identifier(.*)"))
 *         .dependency(stringLikeRegexp("identifier(.*)"))
 *         .dependency(stringLikeRegexp("identifier(.*)"))
 *         .dependency(stringLikeRegexp("identifier(.*)"))
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiDeployment(@JsonProperty("Properties") RestApiDeploymentProperties properties,
                                @Singular @JsonProperty("DependsOn") List<Matcher> dependencies) {

  @JsonProperty("Type")
  static String type = APIGATEWAY_RESTAPI_DEPLOYMENT.getValue();

  /**
   * This record is used to represent a ApiGateway rest api deployment properties.
   *
   * <pre>
   *       final RestApiDeploymentProperties restApiDeploymentProperties = RestApiDeploymentProperties.builder()
   *         .restApiId(ResourceReference.builder()
   *         .reference(stringLikeRegexp("identifier(.*)")).build())
   *         .description("Automatically created by the RestApi construct")
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RestApiDeploymentProperties(@JsonProperty("RestApiId") ResourceReference restApiId,
                                            @JsonProperty("Description") String description) {

  }
}
