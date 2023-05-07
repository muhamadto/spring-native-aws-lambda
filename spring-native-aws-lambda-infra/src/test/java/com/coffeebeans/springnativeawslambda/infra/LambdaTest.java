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

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.LambdaAssert.assertThat;
import static com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatement.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.assertion.IamAssert;
import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionArn;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda.LambdaCode;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda.LambdaDestinationReference;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda.LambdaEnvironment;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda.LambdaProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig.LambdaDestinationConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig.LambdaEventInvokeConfigProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermission;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermission.LambdaPermissionProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyDocument;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyPrincipal;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatement;
import com.coffeebeans.springnativeawslambda.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.infra.resource.Role;
import com.coffeebeans.springnativeawslambda.infra.resource.Role.RoleProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import java.util.List;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Matcher;

class LambdaTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_lambda_function() {
    final LambdaCode lambdaCode = LambdaCode.builder()
        .s3Bucket("test-cdk-bucket")
        .s3Key(stringLikeRegexp("(.*).zip"))
        .build();

    final IntrinsicFunctionArn roleArn = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .attributesArn("Arn")
        .build();

    final LambdaEnvironment lambdaEnvironment = LambdaEnvironment.builder()
        .variable("ENV", TEST)
        .variable("SPRING_PROFILES_ACTIVE", TEST)
        .build();

    final LambdaProperties lambdaProperties = LambdaProperties.builder()
        .functionName("spring-native-aws-lambda-function")
        .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
        .memorySize(512)
        .runtime("provided.al2")
        .timeout(3)
        .description("Lambda example with spring native")
        .code(lambdaCode)
        .roleArn(roleArn)
        .tag(Tag.builder().key("COST_CENTRE").value(TAG_VALUE_COST_CENTRE).build())
        .tag(Tag.builder().key("ENV").value(TEST).build())
        .environment(lambdaEnvironment)
        .build();

    final Lambda expected = Lambda.builder()
        .dependency(stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .properties(lambdaProperties)
        .build();

    assertThat(template)
        .hasFunction(expected);
  }

  @Test
  void should_have_role_with_AWSLambdaBasicExecutionRole_policy_for_lambda_to_assume() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .principal(PolicyPrincipal.builder().service("lambda.amazonaws.com").build())
        .effect(ALLOW)
        .action("sts:AssumeRole")
        .build();

    final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder()
        .statement(policyStatement)
        .build();

    final List<Object> arn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole");

    final IntrinsicFunctionArn managedPolicyArn = IntrinsicFunctionArn.builder()
        .joinArn(EMPTY)
        .joinArn(arn)
        .build();

    final RoleProperties roleProperties = RoleProperties.builder()
        .managedPolicyArn(managedPolicyArn)
        .assumeRolePolicyDocument(assumeRolePolicyDocument)
        .build();

    final Role expected = Role.builder()
        .properties(roleProperties)
        .build();

    IamAssert.assertThat(template)
        .hasRole(expected);
  }

  @Test
  void should_have_default_policy_to_allow_lambda_publish_to_sns() {

    final Matcher policyName = stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)");

    final ResourceReference role = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .build();

    final PolicyStatement successSNSPublishPolicyStatement = getAllowSnsPublishPolicyStatement("springnativeawslambdafunctionsuccesstopic(.*)");

    final PolicyStatement failureSNSPublishPolicyStatement = getAllowSnsPublishPolicyStatement("springnativeawslambdafunctionfailuretopic(.*)");

    final PolicyDocument policyDocument = PolicyDocument.builder()
        .statement(failureSNSPublishPolicyStatement)
        .statement(successSNSPublishPolicyStatement)
        .build();

    final PolicyProperties policyProperties = PolicyProperties.builder()
        .policyName(policyName)
        .role(role)
        .policyDocument(policyDocument)
        .build();

    final Policy policy = Policy.builder()
        .properties(policyProperties)
        .build();

    IamAssert.assertThat(template)
        .hasPolicy(policy);
  }

  @Test
  void should_have_event_invoke_config_for_success_and_failure() {

    final LambdaDestinationReference onFailure = createDestinationReference("springnativeawslambdafunctionfailuretopic(.*)");
    final LambdaDestinationReference onSuccess = createDestinationReference("springnativeawslambdafunctionsuccesstopic(.*)");

    final LambdaEventInvokeConfigProperties lambdaEventInvokeConfigProperties = LambdaEventInvokeConfigProperties.builder()
        .functionName(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunction(.*)")).build())
        .maximumRetryAttempts(2)
        .qualifier(exact("$LATEST"))
        .lambdaDestinationConfig(LambdaDestinationConfig.builder()
            .onFailure(onFailure)
            .onSuccess(onSuccess)
            .build())
        .build();

    final LambdaEventInvokeConfig expected = LambdaEventInvokeConfig.builder()
        .properties(lambdaEventInvokeConfigProperties)
        .build();

    assertThat(template)
        .hasLambdaEventInvokeConfig(expected);
  }

  @Test
  void should_have_permission_to_allow_rest_api_root_call_lambda() {

    final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final List<Object> joinArn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":execute-api:",
        ResourceReference.builder().reference(exact("AWS::Region")).build(),
        ":",
        ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
        ":",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/*/"
    );

    final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission expected = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    assertThat(template)
        .hasLambdaPermission(expected);
  }

  @Test
  void should_have_permission_to_allow_rest_api_root_test_call_lambda() {

    final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final List<Object> joinArn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":execute-api:",
        ResourceReference.builder().reference(exact("AWS::Region")).build(),
        ":",
        ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
        ":",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/test-invoke-stage/*/*"
    );

    final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission expected = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    assertThat(template)
        .hasLambdaPermission(expected);
  }

  @Test
  void should_have_permission_to_allow_rest_api_proxy_to_call_lambda() {

    final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final List<Object> joinArn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":execute-api:",
        ResourceReference.builder().reference(exact("AWS::Region")).build(),
        ":",
        ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
        ":",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiDeploymentStagetest(.*)")).build(),
        "/*/*"
    );

    final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission expected = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    assertThat(template)
        .hasLambdaPermission(expected);
  }

  @Test
  void should_have_permission_to_allow_rest_api_proxy_test_to_call_lambda() {

    final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final List<Object> joinArn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":execute-api:",
        ResourceReference.builder().reference(exact("AWS::Region")).build(),
        ":",
        ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
        ":",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/test-invoke-stage/*/*"
    );

    final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission expected = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    assertThat(template)
        .hasLambdaPermission(expected);
  }

  @Test
  void should_have_permission_to_allow_post_rest_api_method_to_call_lambda() {

    final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final List<Object> joinArn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":execute-api:",
        ResourceReference.builder().reference(exact("AWS::Region")).build(),
        ":",
        ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
        ":",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiDeploymentStagetest(.*)")).build(),
        "/POST/name"
    );

    final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission expected = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    assertThat(template)
        .hasLambdaPermission(expected);
  }

  @Test
  void should_have_permission_to_allow_post_rest_api_method_test_to_call_lambda() {

    final IntrinsicFunctionArn functionName = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final List<Object> joinArn = List.of(
        "arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":execute-api:",
        ResourceReference.builder().reference(exact("AWS::Region")).build(),
        ":",
        ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
        ":",
        ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build(),
        "/test-invoke-stage/POST/name"
    );

    final IntrinsicFunctionArn sourceArn = IntrinsicFunctionArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission expected = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    assertThat(template)
        .hasLambdaPermission(expected);
  }

  private static PolicyStatement getAllowSnsPublishPolicyStatement(final String pattern) {
    final ResourceReference resourceReference = ResourceReference.builder().reference(stringLikeRegexp(pattern)).build();

    return PolicyStatement.builder()
        .effect(ALLOW)
        .action("sns:Publish")
        .resource(resourceReference)
        .build();
  }

  private static LambdaDestinationReference createDestinationReference(String pattern) {
    return LambdaDestinationReference.builder()
        .destination(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
        .build();
  }
}