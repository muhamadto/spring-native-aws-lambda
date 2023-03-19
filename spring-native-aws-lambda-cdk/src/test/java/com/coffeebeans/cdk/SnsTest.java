package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.cdk.resource.CdkResourceType.SNS;
import static com.coffeebeans.cdk.resource.CdkResourceType.SNS_SUBSCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.cdk.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.cdk.resource.ResourceReference;
import com.coffeebeans.cdk.resource.Sns;
import com.coffeebeans.cdk.resource.SnsProperties;
import com.coffeebeans.cdk.resource.SnsSnsSubscriptionProperties;
import com.coffeebeans.cdk.resource.SnsSubscription;
import com.coffeebeans.cdk.resource.Tag;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SnsTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_success_sns() {

    final SnsProperties snsProperties = SnsProperties.builder()
        .contentBasedDeduplication(true)
        .fifoTopic(true)
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .topicName(exact("spring-native-aws-lambda-function-success-topic.fifo"))
        .build();

    final Sns sns = Sns.builder()
        .properties(snsProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(SNS.getValue(), sns);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_failure_sns() {

    final SnsProperties snsProperties = SnsProperties.builder()
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .topicName(exact("spring-native-aws-lambda-function-failure-topic"))
        .build();

    final Sns sns = Sns.builder()
        .properties(snsProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(SNS.getValue(), sns);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_subscription_to_success_sns() {
    final ResourceReference topicArn = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionsuccesstopicfifo(.*)"))
        .build();

    final IntrinsicFunctionBasedArn endpoint = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionsuccessqueuefifo(.*)"))
        .attributesArn("Arn")
        .build();

    final SnsSnsSubscriptionProperties snsSnsSubscriptionProperties = SnsSnsSubscriptionProperties.builder()
        .protocol(exact("sqs"))
        .topicArn(topicArn)
        .endpoint(endpoint)
        .build();

    final SnsSubscription snsSubscription = SnsSubscription.builder()
        .properties(snsSnsSubscriptionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(SNS_SUBSCRIPTION.getValue(), snsSubscription);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_subscription_to_failure_sns() {
    final ResourceReference topicArn = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionfailuretopic(.*)"))
        .build();

    final IntrinsicFunctionBasedArn endpoint = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionfailurequeue(.*)"))
        .attributesArn("Arn")
        .build();

    final SnsSnsSubscriptionProperties snsSnsSubscriptionProperties = SnsSnsSubscriptionProperties.builder()
        .protocol(exact("sqs"))
        .topicArn(topicArn)
        .endpoint(endpoint)
        .build();

    final SnsSubscription snsSubscription = SnsSubscription.builder()
        .properties(snsSnsSubscriptionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(SNS_SUBSCRIPTION.getValue(), snsSubscription);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }
}