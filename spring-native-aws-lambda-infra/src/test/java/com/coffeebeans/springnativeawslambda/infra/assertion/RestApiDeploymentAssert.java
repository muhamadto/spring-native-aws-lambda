package com.coffeebeans.springnativeawslambda.infra.assertion;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class RestApiDeploymentAssert extends
    AbstractCDKResourcesAssert<RestApiDeploymentAssert, Map<String, Object>> {

  private RestApiDeploymentAssert(final Map<String, Object> actual) {
    super(actual, RestApiDeploymentAssert.class);
  }

  public static RestApiDeploymentAssert assertThat(final Map<String, Object> actual) {
    return new RestApiDeploymentAssert(actual);
  }

  public RestApiDeploymentAssert hasRestApiId(@NotNull final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(arn -> arn.toString().matches(expected));

    return this;
  }
}
