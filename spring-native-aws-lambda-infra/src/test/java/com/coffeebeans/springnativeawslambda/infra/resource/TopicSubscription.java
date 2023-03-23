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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.TOPIC_SUBSCRIPTION;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Builder;

/**
 * This record represents SNS Topic subscription.
 *
 * <pre>
 *       final ResourceReference topicArn = ResourceReference.builder()
 *         .reference(stringLikeRegexp("identifier(.*)"))
 *         .build();
 *
 *     final IntrinsicFunctionArn endpoint = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final TopicSubscriptionProperties topicSubscriptionProperties = TopicSubscriptionProperties.builder()
 *         .protocol("sqs")
 *         .topicArn(topicArn)
 *         .endpoint(endpoint)
 *         .build();
 *
 *     final TopicSubscription topicSubscription = TopicSubscription.builder()
 *         .properties(topicSubscriptionProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TopicSubscription(@JsonProperty("Properties") TopicSubscriptionProperties properties) {

  @JsonProperty("Type")
  static String type = TOPIC_SUBSCRIPTION.getValue();

  /**
   * This record represents SNS Topic subscription properties.
   *
   * <pre>
   *       final ResourceReference topicArn = ResourceReference.builder()
   *         .reference(stringLikeRegexp("identifier(.*)"))
   *         .build();
   *
   *     final IntrinsicFunctionArn endpoint = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final TopicSubscriptionProperties topicSubscriptionProperties = TopicSubscriptionProperties.builder()
   *         .protocol("sqs")
   *         .topicArn(topicArn)
   *         .endpoint(endpoint)
   *         .build();
   *
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record TopicSubscriptionProperties(@JsonProperty("TopicArn") ResourceReference topicArn,
                                            @JsonProperty("Protocol") String protocol,
                                            @JsonProperty("Endpoint") IntrinsicFunctionArn endpoint,
                                            @JsonProperty("FilterPolicy") Map<String, Object> filterPolicy) {

  }
}

