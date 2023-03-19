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
