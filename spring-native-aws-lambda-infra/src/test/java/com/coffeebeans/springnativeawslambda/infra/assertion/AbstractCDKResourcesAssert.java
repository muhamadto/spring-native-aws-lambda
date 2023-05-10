package com.coffeebeans.springnativeawslambda.infra.assertion;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

@SuppressWarnings("unchecked")
public abstract class AbstractCDKResourcesAssert<SELF extends AbstractCDKResourcesAssert<SELF, ACTUAL>, ACTUAL extends Map<String, Object>> extends
    AbstractAssert<SELF, ACTUAL> {

  protected AbstractCDKResourcesAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public SELF hasTag(final String key, final Object value) {

    final Map<String, Map<String, Object>> properties =
        (Map<String, Map<String, Object>>) actual.get("Properties");

    final List<Object> tags = (List<Object>) properties.get("Tags");

    Assertions.assertThat(tags)
        .isNotEmpty()
        .contains(Map.of("Key", key, "Value", value));
    return myself;
  }

  public SELF hasDependency(final String expected) {

    final List<String> actualDependency = (List<String>) actual.get("DependsOn");

    Assertions.assertThat(actualDependency)
        .isInstanceOf(List.class)
        .anyMatch(s -> s.matches(expected));

    return myself;
  }

  public SELF hasUpdateReplacePolicy(final String expected) {

    final String actualUpdateReplacePolicy = (String) actual.get("UpdateReplacePolicy");

    Assertions.assertThat(actualUpdateReplacePolicy)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasDeletionPolicy(final String expected) {

    final String actualDeletionPolicy = (String) actual.get("DeletionPolicy");

    Assertions.assertThat(actualDeletionPolicy)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasEnvironmentVariable(final String key, final String value) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> environment = (Map<String, Object>) properties.get("Environment");
    final Map<String, Object> variables = (Map<String, Object>) environment.get("Variables");

    Assertions.assertThat(variables.get(key))
        .isInstanceOf(String.class)
        .isEqualTo(value);

    return myself;
  }

  public SELF hasDescription(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String description = (String) properties.get("Description");

    Assertions.assertThat(description)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasMemorySize(final Integer expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer memorySize = (Integer) properties.get("MemorySize");

    Assertions.assertThat(memorySize)
        .isInstanceOf(Integer.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasRuntime(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String runtime = (String) properties.get("Runtime");

    Assertions.assertThat(runtime)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasTimeout(final Integer timeoutInSeconds) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer timeout = (Integer) properties.get("Timeout");

    Assertions.assertThat(timeout)
        .isInstanceOf(Integer.class)
        .isEqualTo(timeoutInSeconds);

    return myself;
  }

  public SELF hasPolicy(final String principal,
     final String resource, String effect,
     final String policyDocumentVersion,
     final String action,
     final Map<String, Object> policyDocument) {
    final var policyDocumentAssertMap = Assertions.assertThat(policyDocument)
        .isNotNull()
        .isNotEmpty()
        .containsEntry("Version", policyDocumentVersion)
        .extracting("Statement")
        .asList()
        .isNotNull()
        .isNotEmpty()
        .map(l -> (Map<String, Object>) l);

    policyDocumentAssertMap.anySatisfy(s -> Assertions.assertThat(s)
        .isNotNull()
        .isNotEmpty()
        .containsEntry("Effect", effect)
        .containsEntry("Action", action));

    if(isNotBlank(principal)) {
      policyDocumentAssertMap.anySatisfy(s -> Assertions.assertThat(s)
          .extracting("Principal")
          .extracting("Service")
          .matches(e -> e.toString().matches(principal)));
    }

    if(isNotBlank(resource)) {
      policyDocumentAssertMap.anySatisfy(s -> Assertions.assertThat(s)
          .extracting("Resource")
          .extracting("Ref")
          .matches(e -> e.toString().matches(resource)));
    }

    return myself;
  }
}
