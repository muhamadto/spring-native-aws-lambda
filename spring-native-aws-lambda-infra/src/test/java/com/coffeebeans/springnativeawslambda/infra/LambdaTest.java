package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_FUNCTION;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_PERMISSION;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.POLICY;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.ROLE;
import static com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaCode;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaDestinationConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaDestinationReference;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEnvironment;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfigProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermission;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermissionProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyDocument;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyPrincipal;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatement;
import com.coffeebeans.springnativeawslambda.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.infra.resource.Role;
import com.coffeebeans.springnativeawslambda.infra.resource.RoleProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Matcher;

class LambdaTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_lambda_function() {
    final LambdaCode lambdaCode = LambdaCode.builder()
        .s3Bucket(exact("test-cdk-bucket"))
        .s3Key(stringLikeRegexp("(.*).zip"))
        .build();

    final IntrinsicFunctionBasedArn roleArn = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .attributesArn("Arn")
        .build();

    final LambdaEnvironment lambdaEnvironment = LambdaEnvironment.builder()
        .variable("ENV", exact(TEST))
        .variable("SPRING_PROFILES_ACTIVE", exact(TEST))
        .build();

    final LambdaProperties lambdaProperties = LambdaProperties.builder()
        .functionName(exact("spring-native-aws-lambda-function"))
        .handler(exact("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"))
        .memorySize(512)
        .runtime(exact("provided.al2"))
        .timeout(3)
        .description(exact("Lambda example with spring native"))
        .code(lambdaCode)
        .roleArn(roleArn)
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .environment(lambdaEnvironment)
        .build();

    final Lambda lambda = Lambda.builder()
        .dependency(stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .properties(lambdaProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_FUNCTION.getValue(), lambda);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_role_with_AWSLambdaBasicExecutionRole_policy_for_lambda_to_assume() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .principal(PolicyPrincipal.builder().service(exact("lambda.amazonaws.com")).build())
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

    final IntrinsicFunctionBasedArn managedPolicyArn = IntrinsicFunctionBasedArn.builder()
        .joinArn(EMPTY)
        .joinArn(arn)
        .build();

    final RoleProperties roleProperties = RoleProperties.builder()
        .managedPolicyArn(managedPolicyArn)
        .assumeRolePolicyDocument(assumeRolePolicyDocument)
        .build();

    final Role role = Role.builder()
        .properties(roleProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(ROLE.getValue(), role);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_default_policy_to_allow_lambda_publish_to_sns() {

    final Matcher policyName = stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)");

    final ResourceReference role = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .build();

    final PolicyStatement successSNSPublishPolicyStatement = getAllowSnsPublishPolicyStatement("springnativeawslambdafunctionsuccesstopicfifo(.*)");

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

    final Map<String, Map<String, Object>> actual = template.findResources(POLICY.getValue(), policy);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_event_invoke_config_for_success_and_failure() {

    final LambdaDestinationReference onFailure = createDestinationReference("springnativeawslambdafunctionfailuretopic(.*)");
    final LambdaDestinationReference onSuccess = createDestinationReference("springnativeawslambdafunctionsuccesstopicfifo(.*)");

    final LambdaEventInvokeConfigProperties lambdaEventInvokeConfigProperties = LambdaEventInvokeConfigProperties.builder()
        .functionName(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunction(.*)")).build())
        .maximumRetryAttempts(2)
        .qualifier(exact("$LATEST"))
        .lambdaDestinationConfig(LambdaDestinationConfig.builder()
            .onFailure(onFailure)
            .onSuccess(onSuccess)
            .build())
        .build();

    final LambdaEventInvokeConfig lambdaEventInvokeConfig = LambdaEventInvokeConfig.builder()
        .properties(lambdaEventInvokeConfigProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), lambdaEventInvokeConfig);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_permission_to_allow_rest_api_root_call_lambda() {

    final IntrinsicFunctionBasedArn functionName = IntrinsicFunctionBasedArn.builder()
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

    final IntrinsicFunctionBasedArn sourceArn = IntrinsicFunctionBasedArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission lambdaPermission = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_PERMISSION.getValue(), lambdaPermission);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_permission_to_allow_rest_api_root_test_call_lambda() {

    final IntrinsicFunctionBasedArn functionName = IntrinsicFunctionBasedArn.builder()
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

    final IntrinsicFunctionBasedArn sourceArn = IntrinsicFunctionBasedArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission lambdaPermission = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_PERMISSION.getValue(), lambdaPermission);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_permission_to_allow_rest_api_proxy_to_call_lambda() {

    final IntrinsicFunctionBasedArn functionName = IntrinsicFunctionBasedArn.builder()
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

    final IntrinsicFunctionBasedArn sourceArn = IntrinsicFunctionBasedArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission lambdaPermission = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_PERMISSION.getValue(), lambdaPermission);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_permission_to_allow_rest_api_proxy_test_to_call_lambda() {

    final IntrinsicFunctionBasedArn functionName = IntrinsicFunctionBasedArn.builder()
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

    final IntrinsicFunctionBasedArn sourceArn = IntrinsicFunctionBasedArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission lambdaPermission = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_PERMISSION.getValue(), lambdaPermission);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_permission_to_allow_post_rest_api_method_to_call_lambda() {

    final IntrinsicFunctionBasedArn functionName = IntrinsicFunctionBasedArn.builder()
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

    final IntrinsicFunctionBasedArn sourceArn = IntrinsicFunctionBasedArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission lambdaPermission = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_PERMISSION.getValue(), lambdaPermission);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_permission_to_allow_post_rest_api_method_test_to_call_lambda() {

    final IntrinsicFunctionBasedArn functionName = IntrinsicFunctionBasedArn.builder()
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

    final IntrinsicFunctionBasedArn sourceArn = IntrinsicFunctionBasedArn.builder()
        .joinArns(List.of(EMPTY, joinArn))
        .build();

    final LambdaPermissionProperties lambdaPermissionProperties = LambdaPermissionProperties.builder()
        .action("lambda:InvokeFunction")
        .principal("apigateway.amazonaws.com")
        .functionName(functionName)
        .sourceArn(sourceArn)
        .build();

    final LambdaPermission lambdaPermission = LambdaPermission.builder()
        .properties(lambdaPermissionProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(LAMBDA_PERMISSION.getValue(), lambdaPermission);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
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