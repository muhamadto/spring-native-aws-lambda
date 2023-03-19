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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class TagUtilsTest {

  @Test
  void should_create_and_return_tag_map() {
    // given
    final String env = "test";
    final String costCentre = "coffeeBeans-core";

    // when
    final Map<String, String> tags = TagUtils.createTags(env, costCentre);

    assertThat(tags)
        .containsEntry(TagUtils.TAG_KEY_ENV, env)
        .containsEntry(TagUtils.TAG_KEY_COST_CENTRE, costCentre);
  }
}