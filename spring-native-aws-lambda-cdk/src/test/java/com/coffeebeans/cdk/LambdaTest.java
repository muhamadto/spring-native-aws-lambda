package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.cdk.resource.CdkResourceType.POLICY;
import static com.coffeebeans.cdk.resource.CdkResourceType.ROLE;
import static com.coffeebeans.cdk.resource.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.cdk.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.cdk.resource.Lambda;
import com.coffeebeans.cdk.resource.LambdaCode;
import com.coffeebeans.cdk.resource.LambdaEnvironment;
import com.coffeebeans.cdk.resource.LambdaProperties;
import com.coffeebeans.cdk.resource.PartitionedArn;
import com.coffeebeans.cdk.resource.Policy;
import com.coffeebeans.cdk.resource.PolicyDocument;
import com.coffeebeans.cdk.resource.PolicyPrincipal;
import com.coffeebeans.cdk.resource.PolicyProperties;
import com.coffeebeans.cdk.resource.PolicyStatement;
import com.coffeebeans.cdk.resource.ResourceReference;
import com.coffeebeans.cdk.resource.Role;
import com.coffeebeans.cdk.resource.RoleProperties;
import com.coffeebeans.cdk.resource.Tag;
import java.util.Map;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Matcher;

class LambdaTest extends TemplateSupport {

  private static final String FUNCTION_RESOURCE_TYPE = "AWS::Lambda::Function";
  public static final String TEST = "test";

  @Test
  void should_have_lambda_function() {
    final LambdaCode lambdaCode = LambdaCode.builder()
        .s3Bucket(exact("test-cdk-bucket"))
        .s3Key(stringLikeRegexp("(.*).zip"))
        .build();

    final IntrinsicFunctionBasedArn roleArn = IntrinsicFunctionBasedArn.builder()
        .getAttributesArn(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
        .getAttributesArn("Arn")
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

    final Map<String, Map<String, Object>> actual = template.findResources(FUNCTION_RESOURCE_TYPE, lambda);

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

    final PartitionedArn partitionedArn = PartitionedArn.builder()
        .partition("AWS::Partition")
        .service("iam")
        .resourceType("policy")
        .resourceId("service-role/AWSLambdaBasicExecutionRole")
        .build();

    final IntrinsicFunctionBasedArn managedPolicyArn = IntrinsicFunctionBasedArn.builder()
        .joinArn(EMPTY)
        .joinArn(partitionedArn.asList())
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

  private static PolicyStatement getAllowSnsPublishPolicyStatement(final String pattern) {
    final ResourceReference resourceReference = ResourceReference.builder().reference(stringLikeRegexp(pattern)).build();

    return PolicyStatement.builder()
        .effect(ALLOW)
        .action("sns:Publish")
        .resource(resourceReference)
        .build();
  }
}