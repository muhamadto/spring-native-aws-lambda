package com.coffeebeans.cdk;

import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LambdaTest extends TemplateSupport {

  private static final String FUNCTION_RESOURCE_TYPE = "AWS::Lambda::Function";
  private static final String FUNCTION_ROLE_RESOURCE_TYPE = "AWS::IAM::Role";
  private static final String FUNCTION_POLICY_RESOURCE_TYPE = "AWS::IAM::Policy";
  @Test
  void should_have_lambda_function() {
    File tempDir = new File("/tmp/spring-native-aws-lambda-function-native-zip.zip");
    final Map<String, Object> expected = Map.of(
        "Type", "AWS::Lambda::Function",
        "DependsOn",
        List.of(stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)"), stringLikeRegexp("springnativeawslambdafunctionrole(.*)")),
        "Properties", Map.of(
            "Code", Map.of("S3Bucket", "cbcore-cdk-bucket", "S3Key", stringLikeRegexp("(.*).zip")),
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
    final Map<String, Object> expected = Map.of(
        "Type", "AWS::IAM::Policy",
        "Properties",
        Map.of(
            "PolicyDocument",
            Map.of(
                "Statement",
                List.of(
                    Map.of(
                        "Action", "sns:Publish",
                        "Effect", "Allow",
                        "Resource", Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionfailuretopic(.*)"))
                    ),
                    Map.of(
                        "Action", "sns:Publish",
                        "Effect", "Allow",
                        "Resource", Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionsuccesstopicfifo(.*)"))
                    )
                ),
                "Version", "2012-10-17"
            ),
            "PolicyName", stringLikeRegexp("springnativeawslambdafunctionroleDefaultPolicy(.*)"),
            "Roles", List.of(Map.of("Ref", stringLikeRegexp("springnativeawslambdafunctionrole(.*)")))
        )
    );

    final Map<String, Map<String, Object>> actual = template.findResources(FUNCTION_POLICY_RESOURCE_TYPE, expected);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }
}
