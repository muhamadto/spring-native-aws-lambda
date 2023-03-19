package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.POLICY;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.ROLE;

import com.coffeebeans.springnativeawslambda.infra.resource.Policy;
import com.coffeebeans.springnativeawslambda.infra.resource.Role;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

public class IamAssert extends AbstractAssert<IamAssert, Template> {

  private IamAssert(final Template template) {
    super(template, IamAssert.class);
  }

  public static IamAssert assertThat(final Template template) {
    return new IamAssert(template);
  }

  public IamAssert hasRole(final Role expected) {
    final Map<String, Map<String, Object>> roles = actual.findResources(ROLE.getValue(), expected);

    Assertions.assertThat(roles)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public IamAssert hasPolicy(final Policy expected) {
    final Map<String, Map<String, Object>> policies = actual.findResources(POLICY.getValue(), expected);

    Assertions.assertThat(policies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }
}
