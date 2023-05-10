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

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

@SuppressWarnings("unchecked")
public class IamRoleAssert extends AbstractCDKResourcesAssert<IamRoleAssert, Map<String, Object>> {

  private IamRoleAssert(final Map<String, Object> actual) {
    super(actual, IamRoleAssert.class);
  }

  public static IamRoleAssert assertThat(final Map<String, Object> actual) {
    return new IamRoleAssert(actual);
  }

  public IamRoleAssert hasManagedPolicyArn(final String managedPolicyArnString) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List<Object> managedPolicyArns = (List<Object>) properties.get("ManagedPolicyArns");

    Assertions.assertThat(managedPolicyArns)
        .isNotNull()
        .isNotEmpty()
        .satisfies(l -> Assertions.assertThat(l)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .anySatisfy(s -> Assertions.assertThat(s)
                .isInstanceOf(Map.class)
                .extracting("Fn::Join")
                .asList()
                .filteredOn(e -> e instanceof List<?>)
                .first()
                .asList()
                .anySatisfy(joinElement -> Assertions.assertThat(joinElement)
                    .matches(e -> e.toString().matches(managedPolicyArnString))
                )
            ));

    return this;
  }

  public IamRoleAssert hasAssumeRolePolicyDocument(final String principal,
      final String resource,
      final String effect,
      final String policyDocumentVersion,
      final String action) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> policyDocument =
        (Map<String, Object>) properties.get("AssumeRolePolicyDocument");

    return hasPolicy(principal, resource, effect, policyDocumentVersion, action, policyDocument);
  }
}
