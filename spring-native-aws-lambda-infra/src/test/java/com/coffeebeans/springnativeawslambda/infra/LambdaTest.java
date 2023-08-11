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

package com.coffeebeans.springnativeawslambda.infra;

import static cloud.pianola.cdk.fluent.assertion.CDKStackAssert.*;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LambdaTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_lambda_function() {

    assertThat(template)
        .containsFunction("spring-native-aws-lambda-function")
        .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
        .hasCode("test-cdk-bucket", "(.*).zip")
        .hasRole("springnativeawslambdafunctionrole(.*)")
        .hasDependency("springnativeawslambdafunctionrole(.*)")
        .hasDependency("springnativeawslambdafunctionroleDefaultPolicy(.*)")
        .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
        .hasTag("ENV", TEST)
        .hasEnvironmentVariable("ENV", TEST)
        .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", TEST)
        .hasDescription("Lambda example with spring native")
        .hasMemorySize(512)
        .hasRuntime("provided.al2")
        .hasTimeout(3);
  }

  @Test
  void should_have_role_with_AWSLambdaBasicExecutionRole_policy_to_assume_by_lambda() {
    final String principal = "lambda.amazonaws.com";
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";
    final String managedPolicyArn = ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole";

    assertThat(template)
        .containsRoleWithManagedPolicyArn(managedPolicyArn)
        .hasAssumeRolePolicyDocument(principal, null, effect, policyDocumentVersion,
            "sts:AssumeRole");
  }

  @Test
  void should_have_default_policy_to_allow_lambda_publish_to_sns() throws JsonProcessingException {

    final String policyName = "springnativeawslambdafunctionroleDefaultPolicy(.*)";
    final String deadLetterTopic = "springnativeawslambdafunctiontopic(.*)";
    final String action = "sns:Publish";
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";

    assertThat(template)
        .containsPolicy(policyName)
        .isAssociatedWithRole("springnativeawslambdafunctionrole(.*)")
        .hasPolicyDocumentVersion(policyDocumentVersion)
        .hasPolicyDocumentStatement(null,
            deadLetterTopic,
            action,
            effect,
            policyDocumentVersion);
  }

  @Test
  void should_have_event_invoke_config_for_success_and_failure() {

    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaEventInvokeConfig(functionName)
        .hasLambdaEventInvokeConfigQualifier("$LATEST")
        .hasLambdaEventInvokeConfigMaximumRetryAttempts(2);
  }

  @Test
  void should_have_permission_to_allow_rest_api_root_call_lambda() {

    final List<Object> sourceArn = List.of(
        "arn:",
        Map.of("Ref", exact("AWS::Partition")),
        ":execute-api:",
        Map.of("Ref", exact("AWS::Region")),
        ":",
        Map.of("Ref", exact("AWS::AccountId")),
        ":",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/*/"
    );

    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaPermission(functionName, action, principal, sourceArn);
  }

  @Test
  void should_have_permission_to_allow_rest_api_root_test_call_lambda() {

    final List<Object> sourceArn = List.of(
        "arn:",
        Map.of("Ref", exact("AWS::Partition")),
        ":execute-api:",
        Map.of("Ref", exact("AWS::Region")),
        ":",
        Map.of("Ref", exact("AWS::AccountId")),
        ":",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/test-invoke-stage/*/*"
    );

    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaPermission(functionName, action, principal, sourceArn);
  }

  @Test
  void should_have_permission_to_allow_rest_api_proxy_to_call_lambda() {

    final List<Object> sourceArn = List.of(
        "arn:",
        Map.of("Ref", exact("AWS::Partition")),
        ":execute-api:",
        Map.of("Ref", exact("AWS::Region")),
        ":",
        Map.of("Ref", exact("AWS::AccountId")),
        ":",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/",
        Map.of("Ref",
            stringLikeRegexp("springnativeawslambdafunctionrestapiDeploymentStagetest(.*)")),
        "/*/*"
    );

    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaPermission(functionName, action, principal, sourceArn);
  }

  @Test
  void should_have_permission_to_allow_rest_api_proxy_test_to_call_lambda() {

    final List<Object> sourceArn = List.of(
        "arn:",
        Map.of("Ref", exact("AWS::Partition")),
        ":execute-api:",
        Map.of("Ref", exact("AWS::Region")),
        ":",
        Map.of("Ref", exact("AWS::AccountId")),
        ":",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/test-invoke-stage/*/*"
    );

    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaPermission(functionName, action, principal, sourceArn);
  }

  @Test
  void should_have_permission_to_allow_post_rest_api_method_to_call_lambda() {

    final List<Object> sourceArn = List.of(
        "arn:",
        Map.of("Ref", exact("AWS::Partition")),
        ":execute-api:",
        Map.of("Ref", exact("AWS::Region")),
        ":",
        Map.of("Ref", exact("AWS::AccountId")),
        ":",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/",
        Map.of("Ref",
            stringLikeRegexp("springnativeawslambdafunctionrestapiDeploymentStagetest(.*)")),
        "/POST/name"
    );

    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaPermission(functionName, action, principal, sourceArn);
  }

  @Test
  void should_have_permission_to_allow_post_rest_api_method_test_to_call_lambda() {

    final List<Object> sourceArn = List.of(
        "arn:",
        Map.of("Ref", exact("AWS::Partition")),
        ":execute-api:",
        Map.of("Ref", exact("AWS::Region")),
        ":",
        Map.of("Ref", exact("AWS::AccountId")),
        ":",
        Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")),
        "/test-invoke-stage/POST/name"
    );

    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "springnativeawslambdafunction(.*)";

    assertThat(template)
        .containsLambdaPermission(functionName, action, principal, sourceArn);
  }
}