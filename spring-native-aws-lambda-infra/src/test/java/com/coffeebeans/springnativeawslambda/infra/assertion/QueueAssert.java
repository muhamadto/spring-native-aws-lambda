package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.QUEUE_POLICY;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.SQS;

import com.coffeebeans.springnativeawslambda.infra.resource.Queue;
import com.coffeebeans.springnativeawslambda.infra.resource.QueuePolicy;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

public class QueueAssert extends AbstractAssert<QueueAssert, Template> {

  private QueueAssert(final Template template) {
    super(template, QueueAssert.class);
  }

  public static QueueAssert assertThat(final Template template) {
    return new QueueAssert(template);
  }

  public QueueAssert hasQueue(final Queue expected) {
    final Map<String, Map<String, Object>> queues = actual.findResources(SQS.getValue(), expected);

    Assertions.assertThat(queues)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public QueueAssert hasQueuePolicy(final QueuePolicy expected) {
    final Map<String, Map<String, Object>> queuePolicies = actual.findResources(QUEUE_POLICY.getValue(), expected);

    Assertions.assertThat(queuePolicies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }
}
