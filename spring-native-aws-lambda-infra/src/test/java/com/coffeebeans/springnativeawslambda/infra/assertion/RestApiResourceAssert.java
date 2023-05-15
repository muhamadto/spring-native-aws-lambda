package com.coffeebeans.springnativeawslambda.infra.assertion;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class RestApiResourceAssert extends
    AbstractCDKResourcesAssert<RestApiResourceAssert, Map<String, Object>> {

  private RestApiResourceAssert(final Map<String, Object> actual) {
    super(actual, RestApiResourceAssert.class);
  }

  public static RestApiResourceAssert assertThat(final Map<String, Object> actual) {
    return new RestApiResourceAssert(actual);
  }

  public RestApiResourceAssert hasRestApiId(@NotNull final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public RestApiResourceAssert hasPath(@NotNull final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String pathPart = properties.get("PathPart");

    Assertions.assertThat(pathPart)
        .isNotBlank()
        .matches(sn -> sn.equals(expected));

    return this;
  }

  public RestApiResourceAssert hasParentId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, List<String>> parentId = (Map<String, List<String>>) properties.get(
        "ParentId");

    Assertions.assertThat(parentId)
        .isNotEmpty()
        .extracting("Fn::GetAtt")
        .asList()
        .map(e -> (String) e)
        .anySatisfy(e -> Assertions.assertThat(e).matches(expected));

    return this;
  }
}
