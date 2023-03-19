package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static software.amazon.awscdk.CfnDeletionPolicy.RETAIN;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.assertion.ApiRestAssert;
import com.coffeebeans.springnativeawslambda.infra.assertion.IamAssert;
import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyDocument;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyPrincipal;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatement;
import com.coffeebeans.springnativeawslambda.infra.resource.PolicyStatementEffect;
import com.coffeebeans.springnativeawslambda.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApi;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiAccount;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiAccountProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiDeployment;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiDeploymentProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethod;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethodIntegration;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethodProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiNonRootMethodProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiResource;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiResourceProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiRootMethodProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiStage;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiStageProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Role;
import com.coffeebeans.springnativeawslambda.infra.resource.RoleProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApiRestTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_rest_api() {
    final RestApiProperties restApiProperties = RestApiProperties.builder().name(exact("spring-native-aws-lambda-function-rest-api"))
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build()).tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final RestApi expected = RestApi.builder()
        .properties(restApiProperties)
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApi(expected);
  }

  @Test
  void should_have_rest_api_account() {
    final IntrinsicFunctionBasedArn cloudWatchRoleArn = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final RestApiAccountProperties restApiAccountProperties = RestApiAccountProperties.builder()
        .cloudWatchRoleArn(cloudWatchRoleArn)
        .build();

    final RestApiAccount expected = RestApiAccount.builder()
        .properties(restApiAccountProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .updateReplacePolicy(stringLikeRegexp("Retain"))
        .deletionPolicy(stringLikeRegexp("Retain"))
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiAccount(expected);
  }

  @Test
  void should_have_rest_api_deployment() {

    final RestApiDeploymentProperties restApiDeploymentProperties = RestApiDeploymentProperties.builder()
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .description(exact("Automatically created by the RestApi construct")).build();

    final RestApiDeployment expected = RestApiDeployment.builder().properties(restApiDeploymentProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiproxyANY(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiproxy(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiANY(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapinamePOST(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiname(.*)"))
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiDeployment(expected);
  }

  @Test
  void should_have_rest_api_stage() {

    final ResourceReference restApiId = ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build();

    final ResourceReference deploymentId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapiDeployment(.*)")).build();

    final RestApiStageProperties restApiDeploymentProperties = RestApiStageProperties.builder().restApiId(restApiId).deploymentId(deploymentId)
        .stageName(exact("test")).tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build()).build();

    final RestApiStage expected = RestApiStage.builder().properties(restApiDeploymentProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiAccount(.*)"))
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiStage(expected);
  }

  @Test
  void should_have_proxy_resource() {

    final IntrinsicFunctionBasedArn parentId = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).attributesArn(exact("RootResourceId")).build();

    final ResourceReference restApiId = ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build();

    final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder().parentId(parentId).pathPart(exact("{proxy+}"))
        .restApiId(restApiId).build();

    final RestApiResource expected = RestApiResource.builder()
        .properties(restApiResourceProperties)
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiResource(expected);
  }

  @Test
  void should_have_account_resource() {

    final IntrinsicFunctionBasedArn parentId = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).attributesArn(exact("RootResourceId")).build();

    final ResourceReference restApiId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .build();

    final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder()
        .parentId(parentId).pathPart(exact("name"))
        .restApiId(restApiId)
        .build();

    final RestApiResource expected = RestApiResource.builder()
        .properties(restApiResourceProperties)
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiResource(expected);
  }

  @Test
  void should_have_post_method() {

    final IntrinsicFunctionBasedArn uri = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(
            List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(), ":apigateway:",
                ResourceReference.builder().reference(exact("AWS::Region")).build(), ":lambda:path/2015-03-31/functions/",
                IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
                "/invocations"))
        .build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type(exact("AWS_PROXY"))
        .integrationHttpMethod(exact("POST"))
        .uri(uri)
        .build();

    final RestApiMethodProperties restApiMethodProperties = RestApiNonRootMethodProperties.builder().httpMethod(exact("POST"))
        .resourceId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiname(.*)")).build())
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType(exact("NONE")).integration(restApiMethodIntegration)
        .build();

    final RestApiMethod expected = RestApiMethod.builder()
        .properties(restApiMethodProperties)
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiMethod(expected);
  }

  @Test
  void should_have_proxy_method() {

    final IntrinsicFunctionBasedArn uri = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(
        List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(), ":apigateway:",
            ResourceReference.builder().reference(exact("AWS::Region")).build(), ":lambda:path/2015-03-31/functions/",
            IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
            "/invocations")).build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type(exact("AWS_PROXY"))
        .integrationHttpMethod(exact("POST")).uri(uri).build();

    final RestApiMethodProperties restApiMethodProperties = RestApiNonRootMethodProperties.builder().httpMethod(exact("ANY"))
        .resourceId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiproxy(.*)")).build())
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType(exact("NONE")).integration(restApiMethodIntegration).build();

    final RestApiMethod expected = RestApiMethod.builder()
        .properties(restApiMethodProperties)
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiMethod(expected);
  }

  @Test
  void should_have_root_method() {

    final IntrinsicFunctionBasedArn uri = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(
            List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(), ":apigateway:",
                ResourceReference.builder().reference(exact("AWS::Region")).build(), ":lambda:path/2015-03-31/functions/",
                IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
                "/invocations"))
        .build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type(exact("AWS_PROXY"))
        .integrationHttpMethod(exact("POST"))
        .uri(uri)
        .build();

    final RestApiMethodProperties restApiMethodProperties = RestApiRootMethodProperties.builder().resourceId(
            IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
                .attributesArn(exact("RootResourceId")).build()).httpMethod(exact("ANY"))
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType(exact("NONE")).integration(restApiMethodIntegration)
        .build();

    final RestApiMethod expected = RestApiMethod.builder()
        .properties(restApiMethodProperties)
        .build();

    ApiRestAssert.assertThat(template)
        .hasRestApiMethod(expected);
  }

  @Test
  void should_have_role_with_AmazonAPIGatewayPushToCloudWatchLogs_policy_for_rest_api_to_push_logs_to_cloud_watch() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .principal(PolicyPrincipal.builder().service(exact("apigateway.amazonaws.com")).build()).effect(PolicyStatementEffect.ALLOW)
        .action("sts:AssumeRole").build();

    final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder().statement(policyStatement).build();

    final List<Object> arn = List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs");

    final IntrinsicFunctionBasedArn managedPolicyArn = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(arn).build();

    final RoleProperties roleProperties = RoleProperties.builder().managedPolicyArn(managedPolicyArn)
        .assumeRolePolicyDocument(assumeRolePolicyDocument).build();

    final Role expected = Role.builder().properties(roleProperties).deletionPolicy(RETAIN).updateReplacePolicy(RETAIN).build();

    IamAssert.assertThat(template)
        .hasRole(expected);
  }
}