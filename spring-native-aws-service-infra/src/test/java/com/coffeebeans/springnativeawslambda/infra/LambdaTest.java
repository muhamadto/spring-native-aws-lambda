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


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;

class LambdaTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_lambda_function() {

    assertThat(template)
        .containsFunction("^Lambda[A-Z0-9]{8}$")
        .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .hasCode("cdk-cbcore-assets-\\$\\{AWS\\:\\:AccountId\\}-\\$\\{AWS\\:\\:Region\\}", "(.*).zip")
        .hasRole("^Role[A-Z0-9]{8}$")
        .hasDependency("^RoleDefaultPolicy[A-Z0-9]{8}$")
        .hasDependency("^Role[A-Z0-9]{8}$")
        .hasTag("COST_CENTRE", "cbcore")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "spring-native-aws-function")
        .hasEnvironmentVariable("ENVIRONMENT", TEST)
        .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", TEST)
        .hasDescription("Example of a Spring Native AWS Lambda Function using CDK")
        .hasMemorySize(512)
        .hasRuntime("provided.al2023")
        .hasTimeout(3)
        .hasDeadLetterTarget("^LambdaDeadLetterQueue[A-Z0-9]{8}$");
  }

  @Test
  void should_have_role_with_AWSLambdaBasicExecutionRole_policy_to_assume_by_lambda() {
    final Map<String, String> principal = Map.of("Service", "lambda.amazonaws.com");
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";
    final String managedPolicyArn = ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole";

    assertThat(template)
        .containsRole("^Role[A-Z0-9]{8}$")
        .hasManagedPolicyArn(managedPolicyArn)
        .hasAssumeRolePolicyDocument(principal, null, effect, policyDocumentVersion, "sts:AssumeRole");
  }

  @Test
  @Disabled
  void should_have_default_policy_to_allow_lambda_send_messages_to_sqs() {

    assertThat(template)
        .containsPolicy("^RoleDefaultPolicy[A-Z0-9]{8}$")
        .isAssociatedWithRole("^Role[A-Z0-9]{8}$")
        .hasPolicyDocumentStatement(null,
            "^LambdaDeadLetterQueue[A-Z0-9]{8}$",
            "sqs:SendMessage",
            "Allow",
            "2012-10-17");
  }

  @Test
  void should_have_event_invoke_config_for_success_and_failure() {

    assertThat(template)
        .containsLambdaEventInvokeConfig("^LambdaEventInvokeConfig[A-Z0-9]{8}$")
        .hasQualifier("$LATEST")
        .hasFunctionName("^Lambda[A-Z0-9]{8}$")
        .hasMaximumEventAgeInSeconds(60)
        .hasMaximumRetryAttempts(2);
  }

  @ParameterizedTest
  @CsvSource(
      {
              "^RestApiproxyANYApiPermissionSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}ANYproxy[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/RestApiDeploymentStagetest[A-Z0-9]{8}/\\*/\\*",
              "^RestApiproxyANYApiPermissionTestSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}ANYproxy[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/test-invoke-stage/\\*/\\*",
              "^RestApiANYApiPermissionSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}ANY[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/RestApiDeploymentStagetest[A-Z0-9]{8}/\\*/",
              "^RestApiANYApiPermissionTestSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}ANY[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/test-invoke-stage/\\*/",
              "^RestApivariablesANYApiPermissionSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}ANYvariables[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/RestApiDeploymentStagetest[A-Z0-9]{8}/\\*/variables",
              "^RestApivariablesANYApiPermissionTestSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}ANYvariables[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/test-invoke-stage/\\*/variables",
              "^RestApivariablesGETApiPermissionSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}GETvariables[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/RestApiDeploymentStagetest[A-Z0-9]{8}/GET/variables",
              "^RestApivariablesGETApiPermissionTestSpringNativeAwsFunctionStackRestApi[A-Z0-9]{8}GETvariables[A-Z0-9]{8}$, arn:aws:execute-api::AWS::AccountId:RestApi[A-Z0-9]{8}/test-invoke-stage/GET/variables"
      }
  )
  void should_have_permission_to_allow_rest_api_to_call_lambda(final String lambdaPermissionResourceId,
      final String sourceArnPattern) {

    assertThat(template)
        .containsLambdaPermission(lambdaPermissionResourceId)
        .hasLambdaPermission("^Lambda[A-Z0-9]{8}$",
            "lambda:InvokeFunction",
            "apigateway.amazonaws.com",
            sourceArnPattern);
  }
}