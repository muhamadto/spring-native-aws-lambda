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

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

@SuppressWarnings("unchecked")
public class QueuePolicyAssert extends
    AbstractCDKResourcesAssert<QueuePolicyAssert, Map<String, Object>> {

  private QueuePolicyAssert(final Map<String, Object> actual) {
    super(actual, QueuePolicyAssert.class);
  }

  public static QueuePolicyAssert assertThat(final Map<String, Object> actual) {
    return new QueuePolicyAssert(actual);
  }

  public QueuePolicyAssert hasQueuePolicy(
      final String queueReference,
      final String policyStatementAction,
      final String policyStatementResource,
      final String policyStatementPrincipalService
  ) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final List<Map<String, Object>> queues = (List<Map<String, Object>>) properties.get("Queues");
    final Map<String, Object> policyDocument =
        (Map<String, Object>) properties.get("PolicyDocument");
    final List<Map<String, Object>> statements =
        (List<Map<String, Object>>) policyDocument.get("Statement");

    Assertions.assertThat(queues)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1)
        .extracting("Ref")
        .element(0)
        .matches(e -> ((String) e).matches(queueReference));

    boolean actionFound = false;
    boolean principalServiceFound = false;
    boolean principalResourceFound = false;
    for (Map<String, Object> statement : statements) {
      actionFound = statement.get("Action").equals(policyStatementAction);

      final Map<String, Object> principal = (Map<String, Object>) statement.get("Principal");
      final String service = (String) principal.get("Service");
      principalServiceFound =
          isNoneBlank(service) && service.equals(policyStatementPrincipalService);

      final List<Object> resources = ((Map<String, List<Object>>) statement.get("Resource")).get(
          "Fn::GetAtt");

      final String resource = (String) resources.get(0);
      principalResourceFound = isNoneBlank(resource) && resource.matches(policyStatementResource);
    }

    Assertions.assertThat(policyDocument.get("Version"))
        .isInstanceOf(String.class)
        .isEqualTo("2012-10-17");

    Assertions.assertThat(actionFound)
        .isTrue();

    Assertions.assertThat(principalServiceFound)
        .isTrue();

    Assertions.assertThat(principalResourceFound)
        .isTrue();

    return this;
  }

  public QueuePolicyAssert hasEffect(final String effect) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final Map<String, Object> policyDocument =
        (Map<String, Object>) properties.get("PolicyDocument");
    final List<Map<String, Object>> statements =
        (List<Map<String, Object>>) policyDocument.get("Statement");

    boolean effectFound = false;
    for (Map<String, Object> statement : statements) {
      effectFound = statement.get("Effect").equals(effect);
    }

    Assertions.assertThat(effectFound)
        .isTrue();

    return this;
  }

  public QueuePolicyAssert hasCondition(
      final String policyStatementConditionType,
      final String policyStatementConditionSourceType,
      final String policyStatementConditionSourceArn
  ) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final Map<String, Object> policyDocument =
        (Map<String, Object>) properties.get("PolicyDocument");
    final List<Map<String, Object>> statements =
        (List<Map<String, Object>>) policyDocument.get("Statement");

    boolean conditionMatch = false;
    for (Map<String, Object> statement : statements) {
      final Map<String, Object> condition = (Map<String, Object>) statement.get("Condition");
      final Map<String, Object> conditionType =
          (Map<String, Object>) condition.get(policyStatementConditionType);

      final Map<String, Object> conditionSourceType =
          (Map<String, Object>) conditionType.get(policyStatementConditionSourceType);

       conditionMatch =
           ((String) conditionSourceType.get("Ref")).matches(policyStatementConditionSourceArn);
    }

    Assertions.assertThat(conditionMatch)
        .isTrue();

    return this;
  }

}

//  final String policyStatementConditionType,
//  final String policyStatementConditionSourceType,
//  final String policyStatementConditionSourceArn

//
//"Condition", Map.of(
//    policyStatementConditionType, Map.of(
//    policyStatementConditionSourceType, Map.of("Ref", stringLikeRegexp(policyStatementConditionSourceArn))
//    )
//    )

/**
 * final Map<String, Map<String, Object>> properties = Map.of( "Properties", Map.of("Queues",
 * List.of(Map.of("Ref", stringLikeRegexp(queueReference))), "PolicyDocument", Map.of( "Version",
 * "2012-10-17", "Statement", List.of( Map.of( "Action", policyStatementAction, "Resource",
 * Map.of("Fn::GetAtt", List.of(stringLikeRegexp(policyStatementResource), "Arn")), "Principal",
 * Map.of( "Service", policyStatementPrincipalService ) ) ) ) ) );
 */