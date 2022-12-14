package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.resource.CdkResourceType.POLICY;
import static com.coffeebeans.cdk.resource.CdkResourceType.ROLE;
import static com.coffeebeans.cdk.resource.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.cdk.resource.JoinedManagedPolicyArn;
import com.coffeebeans.cdk.resource.PartitionedArn;
import com.coffeebeans.cdk.resource.Policy;
import com.coffeebeans.cdk.resource.PolicyDocument;
import com.coffeebeans.cdk.resource.PolicyPrincipal;
import com.coffeebeans.cdk.resource.PolicyProperties;
import com.coffeebeans.cdk.resource.PolicyStatement;
import com.coffeebeans.cdk.resource.ResourceReference;
import com.coffeebeans.cdk.resource.Role;
import com.coffeebeans.cdk.resource.RoleProperties;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Matcher;

class LambdaTest extends TemplateSupport {

  private static final String FUNCTION_RESOURCE_TYPE = "AWS::Lambda::Function";

  @Test
  void should_have_lambda_function() {
    final Map<String, Object> expected = Map.of(
        "Type", "AWS::Lambda::Function",
        "DependsOn",
        List.of(stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)"), stringLikeRegexp("springnativeawslambdafunctionrole(.*)")),
        "Properties", Map.of(
            "Code", Map.of("S3Bucket", "test-cdk-bucket", "S3Key", stringLikeRegexp("(.*).zip")),
            "Role", Map.of("Fn::GetAtt", List.of(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"), "Arn")),
            "Environment", Map.of("Variables", Map.of("ENV", "test", "SPRING_PROFILES_ACTIVE", "test")),
            "Tags", List.of(Map.of("Key", "COST_CENTRE", "Value", "coffeebeans-core"), Map.of("Key", "ENV", "Value", "test")),
            "Description", "Lambda example with spring native",
            "FunctionName", "spring-native-aws-lambda-function",
            "Handler", "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest",
            "MemorySize", 512,
            "Runtime", "provided.al2",
            "Timeout", 3
        ));

    final Map<String, Map<String, Object>> actual = template.findResources(FUNCTION_RESOURCE_TYPE, expected);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_role_with_AWSLambdaBasicExecutionRole_policy_for_lambda_to_assume() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .effect(ALLOW)
        .action("sts:AssumeRole")
        .principal(PolicyPrincipal.builder().service("lambda.amazonaws.com").build())
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

    final JoinedManagedPolicyArn managedPolicyArn = JoinedManagedPolicyArn.builder()
        .arn(EMPTY)
        .arn(partitionedArn.asList())
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