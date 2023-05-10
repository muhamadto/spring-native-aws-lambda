package com.coffeebeans.springnativeawslambda.infra.assertion;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class RestApiStageAssert extends
    AbstractCDKResourcesAssert<RestApiStageAssert, Map<String, Object>> {

  private RestApiStageAssert(final Map<String, Object> actual) {
    super(actual, RestApiStageAssert.class);
  }

  public static RestApiStageAssert assertThat(final Map<String, Object> actual) {
    return new RestApiStageAssert(actual);
  }

  public RestApiStageAssert hasRestApiId(@NotNull final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public RestApiStageAssert hasDeploymentId(@NotNull final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> deploymentId = (Map<String, String>) properties.get("DeploymentId");

    Assertions.assertThat(deploymentId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public RestApiStageAssert hasStageName(final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String stageName = properties.get("StageName");

    Assertions.assertThat(stageName)
        .isNotBlank()
        .matches(sn -> sn.matches(expected));

    return this;
  }
}
