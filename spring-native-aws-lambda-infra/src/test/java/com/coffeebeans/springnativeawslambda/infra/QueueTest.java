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

package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.QueueAssert.assertThat;
import static com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatement.PolicyStatementEffect.ALLOW;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyDocument;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyPrincipal;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatement;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatementCondition;
import com.coffeebeans.springnativeawslambda.infra.resource.Queue;
import com.coffeebeans.springnativeawslambda.infra.resource.Queue.QueueProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Queue.QueueRedrivePolicy;
import com.coffeebeans.springnativeawslambda.infra.resource.QueuePolicy;
import com.coffeebeans.springnativeawslambda.infra.resource.QueuePolicy.QueuePolicyProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import java.util.Map;
import org.junit.jupiter.api.Test;

class QueueTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_success_queue() {

    final IntrinsicFunctionBasedArn deadLetterTargetArn = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionsuccessqueuedlqfifo(.*)"))
        .attributesArn("Arn")
        .build();

    final QueueRedrivePolicy queueRedrivePolicy = QueueRedrivePolicy.builder()
        .deadLetterTargetArn(deadLetterTargetArn)
        .maxReceiveCount(3)
        .build();

    final QueueProperties queueProperties = QueueProperties.builder()
        .contentBasedDeduplication(true)
        .fifoQueue(true)
        .deduplicationScope(exact("messageGroup"))
        .queueName(exact("spring-native-aws-lambda-function-success-queue.fifo"))
        .redrivePolicy(queueRedrivePolicy)
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final Queue expected = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(expected);
  }

  @Test
  void should_have_success_dead_letter_queue() {

    final QueueProperties queueProperties = QueueProperties.builder()
        .contentBasedDeduplication(true)
        .fifoQueue(true)
        .deduplicationScope(exact("messageGroup"))
        .queueName(exact("spring-native-aws-lambda-function-success-queue-dlq.fifo"))
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final Queue expected = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(expected);
  }

  @Test
  void should_have_success_queue_policy() {
    final ResourceReference queue = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionsuccessqueuefifo(.*)"))
        .build();

    final IntrinsicFunctionBasedArn resource = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionsuccessqueuefifo(.*)"))
        .attributesArn("Arn")
        .build();

    final PolicyStatementCondition policyStatementCondition = PolicyStatementCondition.builder()
        .arnEquals(Map.of("aws:SourceArn", ResourceReference.builder()
            .reference(stringLikeRegexp("springnativeawslambdafunctionsuccesstopicfifo(.*)"))
            .build()))
        .build();

    final PolicyStatement policyStatement = PolicyStatement.builder()
        .effect(ALLOW)
        .principal(PolicyPrincipal.builder().service(exact("sns.amazonaws.com")).build())
        .resource(resource)
        .action("sqs:SendMessage")
        .condition(policyStatementCondition)
        .build();

    final PolicyDocument policyDocument = PolicyDocument.builder()
        .statement(policyStatement)
        .build();

    final QueuePolicyProperties queuePolicyProperties = QueuePolicyProperties.builder()
        .queue(queue)
        .policyDocument(policyDocument)
        .build();

    final QueuePolicy expected = QueuePolicy.builder()
        .properties(queuePolicyProperties)
        .build();

    assertThat(template)
        .hasQueuePolicy(expected);
  }

  @Test
  void should_have_failure_queue() {

    final IntrinsicFunctionBasedArn deadLetterTargetArn = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionfailurequeuedlq(.*)"))
        .attributesArn("Arn")
        .build();

    final QueueRedrivePolicy queueRedrivePolicy = QueueRedrivePolicy.builder()
        .deadLetterTargetArn(deadLetterTargetArn)
        .maxReceiveCount(3)
        .build();

    final QueueProperties queueProperties = QueueProperties.builder()
        .queueName(exact("spring-native-aws-lambda-function-failure-queue"))
        .redrivePolicy(queueRedrivePolicy)
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final Queue expected = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(expected);
  }

  @Test
  void should_have_failure_dead_letter_queue() {

    final QueueProperties queueProperties = QueueProperties.builder()
        .queueName(exact("spring-native-aws-lambda-function-failure-queue-dlq"))
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final Queue expected = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(expected);
  }

  @Test
  void should_have_failure_queue_policy() {
    final ResourceReference queue = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionfailurequeue(.*)"))
        .build();

    final IntrinsicFunctionBasedArn resource = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionfailurequeue(.*)"))
        .attributesArn("Arn")
        .build();

    final PolicyStatementCondition policyStatementCondition = PolicyStatementCondition.builder()
        .arnEquals(Map.of("aws:SourceArn", ResourceReference.builder()
            .reference(stringLikeRegexp("springnativeawslambdafunctionfailuretopic(.*)"))
            .build()))
        .build();

    final PolicyStatement policyStatement = PolicyStatement.builder()
        .effect(ALLOW)
        .principal(PolicyPrincipal.builder().service(exact("sns.amazonaws.com")).build())
        .resource(resource)
        .action("sqs:SendMessage")
        .condition(policyStatementCondition)
        .build();

    final PolicyDocument policyDocument = PolicyDocument.builder()
        .statement(policyStatement)
        .build();

    final QueuePolicyProperties queuePolicyProperties = QueuePolicyProperties.builder()
        .queue(queue)
        .policyDocument(policyDocument)
        .build();

    final QueuePolicy expected = QueuePolicy.builder()
        .properties(queuePolicyProperties)
        .build();

    assertThat(template)
        .hasQueuePolicy(expected);
  }
}