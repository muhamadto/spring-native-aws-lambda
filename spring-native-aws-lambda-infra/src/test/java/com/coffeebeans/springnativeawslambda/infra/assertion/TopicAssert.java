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
