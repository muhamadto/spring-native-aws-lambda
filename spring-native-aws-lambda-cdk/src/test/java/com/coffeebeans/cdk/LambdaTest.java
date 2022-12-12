package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.resource.CdkResourceType.POLICY;
import static com.coffeebeans.cdk.resource.PolicyStatementEffect.ALLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.cdk.resource.Policy;
import com.coffeebeans.cdk.resource.PolicyDocument;
import com.coffeebeans.cdk.resource.PolicyProperties;
import com.coffeebeans.cdk.resource.PolicyStatement;
import com.coffeebeans.cdk.resource.ResourceRef;
import com.coffeebeans.cdk.resource.RoleRef;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Matcher;

class LambdaTest extends TemplateSupport {

  private static final String FUNCTION_RESOURCE_TYPE = "AWS::Lambda::Function";
  private static final String FUNCTION_ROLE_RESOURCE_TYPE = "AWS::IAM::Role";

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
    final Map<String, Object> expected = Map.of(
        "Type", "AWS::IAM::Role",
        "Properties",
        Map.of(
            "AssumeRolePolicyDocument",
            Map.of(
                "Statement",
                List.of(
                    Map.of("Action", "sts:AssumeRole",
                        "Effect", "Allow",
                        "Principal", Map.of("Service", "lambda.amazonaws.com")
                    )
                ),
                "Version", "2012-10-17"
            ),
            "ManagedPolicyArns",
            List.of(
                Map.of(
                    "Fn::Join",
                    List.of(
                        "",
                        List.of(
                            "arn:",
                            Map.of("Ref", "AWS::Partition"),
                            ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
                        )
                    )
                )
            )
        )
    );

    final Map<String, Map<String, Object>> actual = template.findResources(FUNCTION_ROLE_RESOURCE_TYPE, expected);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

  }

  @Test
  void should_have_default_policy_to_allow_lambda_publish_to_sns() {

    final Matcher policyName = stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)");

    final RoleRef role = RoleRef.builder()
        .ref(stringLikeRegexp("springnativeawslambdafunctionrole(.*)"))
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
    return PolicyStatement.builder()
        .effect(ALLOW)
        .action("sns:Publish")
        .resource(ResourceRef.builder().ref(stringLikeRegexp(pattern)).build())
        .build();
  }
}
