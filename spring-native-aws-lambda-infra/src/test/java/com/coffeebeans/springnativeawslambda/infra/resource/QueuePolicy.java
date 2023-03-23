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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.QUEUE_POLICY;

import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

/**
 * This record represents SQS QueuePolicy.
 * <pre>
 *       final ResourceReference queue = ResourceReference.builder()
 *         .reference(stringLikeRegexp("identifier(.*)"))
 *         .build();
 *
 *     final IntrinsicFunctionArn resource = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final PolicyStatementCondition policyStatementCondition = PolicyStatementCondition.builder()
 *         .arnEquals(Map.of("aws:SourceArn", ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build()))
 *         .build();
 *
 *     final PolicyStatement policyStatement = PolicyStatement.builder()
 *         .effect(ALLOW)
 *         .principal(PolicyPrincipal.builder().service("sns.amazonaws.com").build())
 *         .resource(resource)
 *         .action("sqs:SendMessage")
 *         .condition(policyStatementCondition)
 *         .build();
 *
 *     final PolicyDocument policyDocument = PolicyDocument.builder()
 *         .statement(policyStatement)
 *         .build();
 *
 *     final QueuePolicyProperties queuePolicyProperties = QueuePolicyProperties.builder()
 *         .queue(queue)
 *         .policyDocument(policyDocument)
 *         .build();
 *
 *     final QueuePolicy queuePolicy = QueuePolicy.builder()
 *         .properties(queuePolicyProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record QueuePolicy(@JsonProperty("Properties") QueuePolicyProperties properties) {

  @JsonProperty("Type")
  static String type = QUEUE_POLICY.getValue();

  /**
   * This record represents SQS QueuePolicy properties.
   *
   * <pre>
   *       final ResourceReference queue = ResourceReference.builder()
   *         .reference(stringLikeRegexp("identifier(.*)"))
   *         .build();
   *
   *     final IntrinsicFunctionArn resource = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final PolicyStatementCondition policyStatementCondition = PolicyStatementCondition.builder()
   *         .arnEquals(Map.of("aws:SourceArn", ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build()))
   *         .build();
   *
   *     final PolicyStatement policyStatement = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .principal(PolicyPrincipal.builder().service("sns.amazonaws.com").build())
   *         .resource(resource)
   *         .action("sqs:SendMessage")
   *         .condition(policyStatementCondition)
   *         .build();
   *
   *     final PolicyDocument policyDocument = PolicyDocument.builder()
   *         .statement(policyStatement)
   *         .build();
   *
   *     final QueuePolicyProperties queuePolicyProperties = QueuePolicyProperties.builder()
   *         .queue(queue)
   *         .policyDocument(policyDocument)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record QueuePolicyProperties(@Singular @JsonProperty("Queues") List<ResourceReference> queues,
                                      @JsonProperty("PolicyDocument") PolicyDocument policyDocument) {

  }
}
