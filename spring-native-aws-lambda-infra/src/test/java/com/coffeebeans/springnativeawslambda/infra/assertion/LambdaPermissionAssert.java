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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.MapAssert;

@SuppressWarnings("unchecked")
public class LambdaPermissionAssert extends
    AbstractCDKResourcesAssert<LambdaPermissionAssert, Map<String, Object>> {

  private LambdaPermissionAssert(final Map<String, Object> actual) {
    super(actual, LambdaPermissionAssert.class);
  }

  public static LambdaPermissionAssert assertThat(final Map<String, Object> actual) {
    return new LambdaPermissionAssert(actual);
  }

  public LambdaPermissionAssert hasLambdaPermission(
      final String functionName,
      final String action,
      final String principal) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final MapAssert<String, Object> mapAssert = Assertions.assertThat(properties)
        .isNotNull()
        .isNotEmpty()
        .containsEntry("Action", action);

    mapAssert.satisfies(s -> Assertions.assertThat(s)
        .extracting("FunctionName")
        .extracting("Fn::GetAtt")
        .asList()
        .map(e -> (String) e)
        .anySatisfy(e -> Assertions.assertThat(e)
            .matches(func -> func.matches(functionName))));

    if (isNotBlank(principal)) {
      mapAssert.satisfies(s -> Assertions.assertThat(s)
          .extracting("Principal")
          .matches(e -> e.toString().matches(principal)));
    }

    return this;
  }
}
