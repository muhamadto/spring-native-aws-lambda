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

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.MapAssert;

@SuppressWarnings("unchecked")
public class LambdaEventInvokeConfigAssert extends AbstractCDKResourcesAssert<LambdaEventInvokeConfigAssert, Map<String, Object>> {

  private LambdaEventInvokeConfigAssert(final Map<String, Object> actual) {
    super(actual, LambdaEventInvokeConfigAssert.class);
  }

  public static LambdaEventInvokeConfigAssert assertThat(final Map<String, Object> actual) {
    return new LambdaEventInvokeConfigAssert(actual);
  }

  public LambdaEventInvokeConfigAssert hasLambdaEventInvokeConfig(final String expectedFunctionName,
      final String expectedSuccessEventDestination,
      final String expectedFailureEventDestination) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> destinationConfig =
        (Map<String, Object>) properties.get("DestinationConfig");
    final String actualFunctionName =
        ((Map<String, String>) properties.get("FunctionName")).get("Ref");
    final String actualFailureDestinationConfig = getEventFailureDestination(destinationConfig);
    final String actualSuccessDestinationConfig = getEventSuccessDestination(destinationConfig);

    Assertions.assertThat(actualFunctionName)
        .isInstanceOf(String.class)
        .matches(actual -> actual.matches(expectedFunctionName));

    Assertions.assertThat(actualFailureDestinationConfig)
        .isInstanceOf(String.class)
        .matches(expectedFailureEventDestination);

    Assertions.assertThat(actualSuccessDestinationConfig)
        .isInstanceOf(String.class)
        .matches(expectedSuccessEventDestination);

    return this;
  }

  public LambdaEventInvokeConfigAssert hasLambdaEventInvokeConfigMaximumRetryAttempts(final Integer expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    Assertions.assertThat((Integer) properties.get("MaximumRetryAttempts"))
        .isInstanceOf(Integer.class)
        .isEqualTo(expected);

    return this;
  }

  public LambdaEventInvokeConfigAssert hasLambdaEventInvokeConfigQualifier(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    Assertions.assertThat((String) properties.get("Qualifier"))
        .isInstanceOf(String.class)
        .matches(actual -> actual.equals(expected));

    return this;
  }

  private String getEventFailureDestination(final Map<String, Object> destinationConfig) {
    return getLambdaEventDestination(destinationConfig, "OnFailure");
  }

  private String getEventSuccessDestination(final Map<String, Object> destinationConfig) {
    return getLambdaEventDestination(destinationConfig, "OnSuccess");
  }

  private String getLambdaEventDestination(final Map<String, Object> destinationConfig,
      final String successFailureKey) {
    final Map<String, Object> successFailureDestinationConfig =
        (Map<String, Object>) destinationConfig.get(successFailureKey);
    final Map<String, String> destination =
        (Map<String, String>) successFailureDestinationConfig.get("Destination");
    return destination.get("Ref");
  }
}
