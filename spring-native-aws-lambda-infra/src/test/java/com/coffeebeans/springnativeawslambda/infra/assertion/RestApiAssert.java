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

import java.util.Map;
import org.assertj.core.api.Assertions;

@SuppressWarnings("unchecked")
public class RestApiAssert extends AbstractCDKResourcesAssert<RestApiAssert, Map<String, Object>> {

  private RestApiAssert(final Map<String, Object> actual) {
    super(actual, RestApiAssert.class);
  }

  public static RestApiAssert assertThat(final Map<String, Object> actual) {
    return new RestApiAssert(actual);
  }

  public RestApiAssert hasRestApi(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String name = (String) properties.get("Name");

    Assertions.assertThat(name)
        .isNotBlank()
        .matches(expected);

    return this;
  }
}
