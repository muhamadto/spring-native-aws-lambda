package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.TopicAssert.assertThat;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.springnativeawslambda.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import com.coffeebeans.springnativeawslambda.infra.resource.Topic;
import com.coffeebeans.springnativeawslambda.infra.resource.TopicProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.TopicSubscription;
import com.coffeebeans.springnativeawslambda.infra.resource.TopicSubscriptionProperties;
import org.junit.jupiter.api.Test;

class TopicTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_success_topic() {

    final TopicProperties topicProperties = TopicProperties.builder()
        .contentBasedDeduplication(true)
        .fifoTopic(true)
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .topicName(exact("spring-native-aws-lambda-function-success-topic.fifo"))
        .build();

    final Topic expected = Topic.builder()
        .properties(topicProperties)
        .build();

    assertThat(template)
        .hasTopic(expected);
  }

  @Test
  void should_have_failure_topic() {

    final TopicProperties topicProperties = TopicProperties.builder()
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .topicName(exact("spring-native-aws-lambda-function-failure-topic"))
        .build();

    final Topic expected = Topic.builder()
        .properties(topicProperties)
        .build();

    assertThat(template)
        .hasTopic(expected);
  }

  @Test
  void should_have_subscription_to_success_topic() {
    final ResourceReference topicArn = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionsuccesstopicfifo(.*)"))
        .build();

    final IntrinsicFunctionBasedArn endpoint = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionsuccessqueuefifo(.*)"))
        .attributesArn("Arn")
        .build();

    final TopicSubscriptionProperties topicSubscriptionProperties = TopicSubscriptionProperties.builder()
        .protocol(exact("sqs"))
        .topicArn(topicArn)
        .endpoint(endpoint)
        .build();

    final TopicSubscription expected = TopicSubscription.builder()
        .properties(topicSubscriptionProperties)
        .build();

    assertThat(template)
        .hasTopicSubscription(expected);
  }

  @Test
  void should_have_subscription_to_failure_topic() {
    final ResourceReference topicArn = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionfailuretopic(.*)"))
        .build();

    final IntrinsicFunctionBasedArn endpoint = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionfailurequeue(.*)"))
        .attributesArn("Arn")
        .build();

    final TopicSubscriptionProperties topicSubscriptionProperties = TopicSubscriptionProperties.builder()
        .protocol(exact("sqs"))
        .topicArn(topicArn)
        .endpoint(endpoint)
        .build();

    final TopicSubscription expected = TopicSubscription.builder()
        .properties(topicSubscriptionProperties)
        .build();

    assertThat(template)
        .hasTopicSubscription(expected);
  }
}