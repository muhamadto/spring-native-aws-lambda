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
import static com.coffeebeans.springnativeawslambda.infra.assertion.CDKStackAssert.assertThat;

import com.coffeebeans.springnativeawslambda.infra.assertion.CDKStackAssert;
import org.junit.jupiter.api.Test;

class TopicTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_success_topic() {
    assertThat(template)
        .containsTopic("spring-native-aws-lambda-function-success-topic")
        .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
        .hasTag("ENV", TEST);
  }

  @Test
  void should_have_failure_topic() {

    assertThat(template)
        .containsTopic("spring-native-aws-lambda-function-failure-topic")
        .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
        .hasTag("ENV", TEST);
  }

  @Test
  void should_have_subscription_to_success_topic() {
    final String protocol = "sqs";
    final String topicArn = "springnativeawslambdafunctionsuccesstopic(.*)";
    final String endpoint = "springnativeawslambdafunctionsuccessqueue(.*)";

    assertThat(template)
        .containsTopicSubscription(topicArn, protocol, endpoint);
  }

  @Test
  void should_have_subscription_to_failure_topic() {
    final String protocol = "sqs";
    final String topicArn = "springnativeawslambdafunctionfailuretopic(.*)";
    final String endpoint = "springnativeawslambdafunctionfailurequeue(.*)";

    assertThat(template)
        .containsTopicSubscription(topicArn, protocol, endpoint);
  }
}