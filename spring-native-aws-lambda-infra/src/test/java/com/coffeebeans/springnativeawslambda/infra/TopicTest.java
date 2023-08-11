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
import static cloud.pianola.cdk.fluent.assertion.CDKStackAssert.*;

import org.junit.jupiter.api.Test;

class TopicTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_dead_letter_topic() {
    assertThat(template)
        .containsTopic("spring-native-aws-lambda-function-topic")
        .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
        .hasTag("ENV", TEST);
  }
}