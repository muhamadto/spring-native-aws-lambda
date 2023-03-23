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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

/**
 * This record is used to represent a ApiGateway rest api.
 * <pre>
 *      final RestApiProperties restApiProperties = RestApiProperties.builder()
 *         .name("api-gateway-rest-api")
 *         .tag(Tag.builder().key("COST_CENTRE").value("test").build())
 *         .tag(Tag.builder().key("ENV").value("test").build())
 *         .build();
 *
 *     final RestApi restApi = RestApi.builder()
 *         .properties(restApiProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApi(@JsonProperty("Properties") RestApiProperties properties,
                      @JsonProperty("Type") String type
) {

  public String type() {
    return APIGATEWAY_RESTAPI.getValue();
  }

  /**
   * This record is used to represent a ApiGateway rest api properties.
   * <pre>
   *      final RestApiProperties restApiProperties = RestApiProperties.builder()
   *         .name("api-gateway-rest-api")
   *         .tag(Tag.builder().key("COST_CENTRE").value("test").build())
   *         .tag(Tag.builder().key("ENV").value("test").build())
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RestApiProperties(@JsonProperty("Name") String name,
                                  @JsonProperty("Tags") @Singular List<Tag> tags) {

  }
}
