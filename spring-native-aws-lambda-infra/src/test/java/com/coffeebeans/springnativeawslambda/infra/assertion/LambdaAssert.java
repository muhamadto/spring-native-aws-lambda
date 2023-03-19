package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_FUNCTION;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_PERMISSION;

import com.coffeebeans.springnativeawslambda.infra.resource.Lambda;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermission;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

public class LambdaAssert extends AbstractAssert<LambdaAssert, Template> {

  private LambdaAssert(final Template template) {
    super(template, LambdaAssert.class);
  }

  public static LambdaAssert assertThat(final Template template) {
    return new LambdaAssert(template);
  }

  public LambdaAssert hasFunction(final Lambda expected) {

    final Map<String, Map<String, Object>> functions = actual.findResources(LAMBDA_FUNCTION.getValue(), expected);

    Assertions.assertThat(functions)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public LambdaAssert hasLambdaEventInvokeConfig(final LambdaEventInvokeConfig expected) {

    final Map<String, Map<String, Object>> lambdaEventInvokeConfigs = actual.findResources(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), expected);

    Assertions.assertThat(lambdaEventInvokeConfigs)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public LambdaAssert hasLambdaPermission(final LambdaPermission expected) {

    final Map<String, Map<String, Object>> lambdaPermissions = actual.findResources(LAMBDA_PERMISSION.getValue(), expected);

    Assertions.assertThat(lambdaPermissions)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }
}
