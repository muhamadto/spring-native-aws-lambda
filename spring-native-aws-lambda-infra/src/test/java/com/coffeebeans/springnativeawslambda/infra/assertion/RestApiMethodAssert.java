package com.coffeebeans.springnativeawslambda.infra.assertion;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class RestApiMethodAssert extends
    AbstractCDKResourcesAssert<RestApiMethodAssert, Map<String, Object>> {

  private RestApiMethodAssert(final Map<String, Object> actual) {
    super(actual, RestApiMethodAssert.class);
  }

  public static RestApiMethodAssert assertThat(final Map<String, Object> actual) {
    return new RestApiMethodAssert(actual);
  }

  public RestApiMethodAssert hasRestApiId(@NotNull final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public RestApiMethodAssert hasAuthorizationType(@NotNull final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String authorizationType = properties.get("AuthorizationType");

    Assertions.assertThat(authorizationType)
        .isNotBlank()
        .matches(e -> e.matches(expected));

    return this;
  }

  public RestApiMethodAssert hasHttpMethod(final String expected) {

    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String httpMethod = properties.get("HttpMethod");

    Assertions.assertThat(httpMethod)
        .isNotBlank()
        .matches(e -> e.matches(expected));

    return this;
  }

  public RestApiMethodAssert hasIntegration(final String method, final String type) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> integration = (Map<String, String>) properties.get("Integration");

    Assertions.assertThat(integration)
        .isNotEmpty()
        .containsEntry("IntegrationHttpMethod", method)
        .containsEntry("Type", type)
        .containsKey("Uri");

    return this;
  }

  public RestApiMethodAssert hasNonRootResourceId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> resourceId = (Map<String, String>) properties.get("ResourceId");

    Assertions.assertThat(resourceId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public RestApiMethodAssert hasRootResourceId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> resourceId = (Map<String, String>) properties.get("ResourceId");

    Assertions.assertThat(resourceId)
        .isNotEmpty()
        .extracting("Fn::GetAtt")
        .asList()
        .map(e -> (String) e)
        .anySatisfy(e -> Assertions.assertThat(e).matches(expected));

    return this;
  }
}
