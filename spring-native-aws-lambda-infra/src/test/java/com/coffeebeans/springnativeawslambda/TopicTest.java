package com.coffeebeans.springnativeawslambda.function.infra;

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.function.infra.resource.CdkResourceType;
import com.coffeebeans.springnativeawslambda.function.infra.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.springnativeawslambda.function.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.function.infra.resource.Tag;
import com.coffeebeans.springnativeawslambda.function.infra.resource.Topic;
import com.coffeebeans.springnativeawslambda.function.infra.resource.TopicProperties;
import com.coffeebeans.springnativeawslambda.function.infra.resource.TopicSubscription;
import com.coffeebeans.springnativeawslambda.function.infra.resource.TopicSubscriptionProperties;
import java.util.Map;
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

    final Topic topic = Topic.builder()
        .properties(topicProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(CdkResourceType.SNS.getValue(), topic);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_failure_topic() {

    final TopicProperties topicProperties = TopicProperties.builder()
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .topicName(exact("spring-native-aws-lambda-function-failure-topic"))
        .build();

    final Topic topic = Topic.builder()
        .properties(topicProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(CdkResourceType.SNS.getValue(), topic);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
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

    final TopicSubscription topicSubscription = TopicSubscription.builder()
        .properties(topicSubscriptionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(CdkResourceType.SNS_SUBSCRIPTION.getValue(), topicSubscription);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
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

    final TopicSubscription topicSubscription = TopicSubscription.builder()
        .properties(topicSubscriptionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(CdkResourceType.SNS_SUBSCRIPTION.getValue(), topicSubscription);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }
}