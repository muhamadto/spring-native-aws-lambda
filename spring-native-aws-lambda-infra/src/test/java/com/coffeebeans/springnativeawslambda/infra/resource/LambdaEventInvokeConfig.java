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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;

import com.coffeebeans.springnativeawslambda.infra.resource.Lambda.LambdaDestinationReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import software.amazon.awscdk.assertions.Matcher;

/**
 * This record is used to represent a Lambda EventInvokeConfig.
 * <pre>
 *   final LambdaDestinationReference onFailure = LambdaDestinationReference.builder()
 *         .destination(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
 *         .build();
 *
 *     final LambdaDestinationReference onSuccess = LambdaDestinationReference.builder()
 *         .destination(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
 *         .build();
 *
 *     final LambdaEventInvokeConfigProperties lambdaEventInvokeConfigProperties = LambdaEventInvokeConfigProperties.builder()
 *         .functionName(ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build())
 *         .maximumRetryAttempts(2)
 *         .qualifier(exact("$LATEST"))
 *         .lambdaDestinationConfig(LambdaDestinationConfig.builder()
 *             .onFailure(onFailure)
 *             .onSuccess(onSuccess)
 *             .build())
 *         .build();
 *
 *     final LambdaEventInvokeConfig lambdaEventInvokeConfig = LambdaEventInvokeConfig.builder()
 *         .properties(lambdaEventInvokeConfigProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LambdaEventInvokeConfig(@JsonProperty("Properties") LambdaEventInvokeConfigProperties properties) {

  @JsonProperty("Type")
  static String type = LAMBDA_EVENT_INVOKE_CONFIG.getValue();

  /**
   * This record is used to represent a Lambda EventInvokeConfig properties.
   * <pre>
   *   final LambdaDestinationReference onFailure = LambdaDestinationReference.builder()
   *         .destination(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   *
   *     final LambdaDestinationReference onSuccess = LambdaDestinationReference.builder()
   *         .destination(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   *
   *     final LambdaEventInvokeConfigProperties lambdaEventInvokeConfigProperties = LambdaEventInvokeConfigProperties.builder()
   *         .functionName(ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build())
   *         .maximumRetryAttempts(2)
   *         .qualifier(exact("$LATEST"))
   *         .lambdaDestinationConfig(LambdaDestinationConfig.builder()
   *             .onFailure(onFailure)
   *             .onSuccess(onSuccess)
   *             .build())
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaEventInvokeConfigProperties(@JsonProperty("DestinationConfig") LambdaDestinationConfig lambdaDestinationConfig,
                                                  @JsonProperty("FunctionName") ResourceReference functionName,
                                                  @JsonProperty("MaximumRetryAttempts") Integer maximumRetryAttempts,
                                                  @JsonProperty("Qualifier") Matcher qualifier) {

  }

  /**
   * This record represents lambda destination config.
   * <pre>
   *   final LambdaDestinationConfig lambdaDestinationConfig = LambdaDestinationConfig.builder()
   *       .onFailure(LambdaDestinationReference.builder().destination(ResourceReference.builder().reference(stringLikeRegexp(failureDestinationPattern)).build()).build())
   *       .OnSuccess(LambdaDestinationReference.builder().destination(ResourceReference.builder().reference(stringLikeRegexp(successDestinationPattern)).build()).build())
   *       .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaDestinationConfig(@JsonProperty("OnFailure") LambdaDestinationReference onFailure,
                                        @JsonProperty("OnSuccess") LambdaDestinationReference onSuccess) {

  }
}
