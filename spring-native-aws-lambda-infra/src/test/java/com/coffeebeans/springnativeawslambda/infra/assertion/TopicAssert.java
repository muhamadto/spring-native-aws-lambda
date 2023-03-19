package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.SNS;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.SNS_SUBSCRIPTION;

import com.coffeebeans.springnativeawslambda.infra.resource.Topic;
import com.coffeebeans.springnativeawslambda.infra.resource.TopicSubscription;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

public class TopicAssert extends AbstractAssert<TopicAssert, Template> {

  private TopicAssert(final Template template) {
    super(template, TopicAssert.class);
  }

  public static TopicAssert assertThat(final Template template) {
    return new TopicAssert(template);
  }

  public TopicAssert hasTopic(final Topic expected) {

    final Map<String, Map<String, Object>> topics = actual.findResources(SNS.getValue(), expected);

    Assertions.assertThat(topics)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public TopicAssert hasTopicSubscription(final TopicSubscription expected) {
    final Map<String, Map<String, Object>> topicSubscriptions = actual.findResources(SNS_SUBSCRIPTION.getValue(), expected);

    Assertions.assertThat(topicSubscriptions)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }
}
