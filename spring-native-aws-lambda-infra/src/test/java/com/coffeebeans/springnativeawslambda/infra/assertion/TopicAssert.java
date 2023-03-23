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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.TOPIC;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.TOPIC_SUBSCRIPTION;

import com.coffeebeans.springnativeawslambda.infra.resource.Topic;
import com.coffeebeans.springnativeawslambda.infra.resource.TopicSubscription;
import org.assertj.core.api.AbstractAssert;
import software.amazon.awscdk.assertions.Template;

public class TopicAssert extends AbstractAssert<TopicAssert, Template> {

  private TopicAssert(final Template actual) {
    super(actual, TopicAssert.class);
  }

  public static TopicAssert assertThat(final Template actual) {
    return new TopicAssert(actual);
  }

  public TopicAssert hasTopic(final Topic expected) {

    actual.hasResource(TOPIC.getValue(), expected);

    return this;
  }

  public TopicAssert hasTopicSubscription(final TopicSubscription expected) {
    actual.hasResource(TOPIC_SUBSCRIPTION.getValue(), expected);

    return this;
  }
}
