package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.QueueAssert.assertThat;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyDocument;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyPrincipal;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatement;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatementCondition;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatementEffect;
import com.coffeebeans.springnativeawslambda.infra.resource.Queue;
import com.coffeebeans.springnativeawslambda.infra.resource.QueuePolicy;
import com.coffeebeans.springnativeawslambda.infra.resource.QueuePolicyProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.QueueProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.QueueRedrivePolicy;
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

    final Queue queue = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(queue);
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

    final Queue queue = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(queue);
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
        .effect(PolicyStatementEffect.ALLOW)
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

    final QueuePolicy queuePolicy = QueuePolicy.builder()
        .properties(queuePolicyProperties)
        .build();

    assertThat(template)
        .hasQueuePolicy(queuePolicy);
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

    final Queue queue = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(queue);
  }

  @Test
  void should_have_failure_dead_letter_queue() {

    final QueueProperties queueProperties = QueueProperties.builder()
        .queueName(exact("spring-native-aws-lambda-function-failure-queue-dlq"))
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final Queue queue = Queue.builder()
        .properties(queueProperties)
        .updateReplacePolicy(exact("Delete"))
        .deletionPolicy(exact("Delete"))
        .build();

    assertThat(template)
        .hasQueue(queue);
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
        .effect(PolicyStatementEffect.ALLOW)
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

    final QueuePolicy queuePolicy = QueuePolicy.builder()
        .properties(queuePolicyProperties)
        .build();

    assertThat(template)
        .hasQueuePolicy(queuePolicy);
  }
}