package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_METHOD;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_RESOURCE;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_STAGE;
import static com.coffeebeans.cdk.resource.CdkResourceType.ROLE;
import static com.coffeebeans.cdk.resource.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.CfnDeletionPolicy.RETAIN;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.cdk.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.cdk.resource.PolicyDocument;
import com.coffeebeans.cdk.resource.PolicyPrincipal;
import com.coffeebeans.cdk.resource.PolicyStatement;
import com.coffeebeans.cdk.resource.ResourceReference;
import com.coffeebeans.cdk.resource.RestApi;
import com.coffeebeans.cdk.resource.RestApiAccount;
import com.coffeebeans.cdk.resource.RestApiAccountProperties;
import com.coffeebeans.cdk.resource.RestApiDeployment;
import com.coffeebeans.cdk.resource.RestApiDeploymentProperties;
import com.coffeebeans.cdk.resource.RestApiMethod;
import com.coffeebeans.cdk.resource.RestApiMethodIntegration;
import com.coffeebeans.cdk.resource.RestApiMethodProperties;
import com.coffeebeans.cdk.resource.RestApiNonRootMethodProperties;
import com.coffeebeans.cdk.resource.RestApiProperties;
import com.coffeebeans.cdk.resource.RestApiResource;
import com.coffeebeans.cdk.resource.RestApiResourceProperties;
import com.coffeebeans.cdk.resource.RestApiRootMethodProperties;
import com.coffeebeans.cdk.resource.RestApiStage;
import com.coffeebeans.cdk.resource.RestApiStageProperties;
import com.coffeebeans.cdk.resource.Role;
import com.coffeebeans.cdk.resource.RoleProperties;
import com.coffeebeans.cdk.resource.Tag;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ApiRestTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_rest_api() {
    final RestApiProperties restApiProperties = RestApiProperties.builder().name(exact("spring-native-aws-lambda-function-rest-api"))
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build()).tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final RestApi restApi = RestApi.builder().properties(restApiProperties).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI.getValue(), restApi);

    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_rest_api_account() {
    final IntrinsicFunctionBasedArn cloudWatchRoleArn = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build();

    final RestApiAccountProperties restApiAccountProperties = RestApiAccountProperties.builder().cloudWatchRoleArn(cloudWatchRoleArn).build();

    final RestApiAccount restApiAccount = RestApiAccount.builder().properties(restApiAccountProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).updateReplacePolicy(stringLikeRegexp("Retain"))
        .deletionPolicy(stringLikeRegexp("Retain")).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_ACCOUNT.getValue(), restApiAccount);

    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_rest_api_deployment() {

    final RestApiDeploymentProperties restApiDeploymentProperties = RestApiDeploymentProperties.builder()
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .description(exact("Automatically created by the RestApi construct")).build();

    final RestApiDeployment restApiDeployment = RestApiDeployment.builder().properties(restApiDeploymentProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiproxyANY(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiproxy(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiANY(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapinamePOST(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiname(.*)")).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_DEPLOYMENT.getValue(), restApiDeployment);

    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_rest_api_stage() {

    final ResourceReference restApiId = ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build();

    final ResourceReference deploymentId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapiDeployment(.*)")).build();

    final RestApiStageProperties restApiDeploymentProperties = RestApiStageProperties.builder().restApiId(restApiId).deploymentId(deploymentId)
        .stageName(exact("test")).tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build()).build();

    final RestApiStage restApiDeployment = RestApiStage.builder().properties(restApiDeploymentProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiAccount(.*)")).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_STAGE.getValue(), restApiDeployment);

    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_proxy_resource() {

    final IntrinsicFunctionBasedArn parentId = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).attributesArn(exact("RootResourceId")).build();

    final ResourceReference restApiId = ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build();

    final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder().parentId(parentId).pathPart(exact("{proxy+}"))
        .restApiId(restApiId).build();

    final RestApiResource restApiResource = RestApiResource.builder().properties(restApiResourceProperties).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_RESOURCE.getValue(), restApiResource);

    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_account_resource() {

    final IntrinsicFunctionBasedArn parentId = IntrinsicFunctionBasedArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).attributesArn(exact("RootResourceId")).build();

    final ResourceReference restApiId = ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build();

    final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder().parentId(parentId).pathPart(exact("name"))
        .restApiId(restApiId).build();

    final RestApiResource restApiResource = RestApiResource.builder().properties(restApiResourceProperties).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_RESOURCE.getValue(), restApiResource);
    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_post_method() {

    final IntrinsicFunctionBasedArn uri = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(
        List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(), ":apigateway:",
            ResourceReference.builder().reference(exact("AWS::Region")).build(), ":lambda:path/2015-03-31/functions/",
            IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
            "/invocations")).build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type(exact("AWS_PROXY"))
        .integrationHttpMethod(exact("POST")).uri(uri).build();

    final RestApiMethodProperties restApiMethodProperties = RestApiNonRootMethodProperties.builder().httpMethod(exact("POST"))
        .resourceId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiname(.*)")).build())
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType(exact("NONE")).integration(restApiMethodIntegration).build();

    final RestApiMethod restApiMethod = RestApiMethod.builder().properties(restApiMethodProperties).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), restApiMethod);
    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
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

    final RestApiMethod restApiMethod = RestApiMethod.builder().properties(restApiMethodProperties).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), restApiMethod);
    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_root_method() {

    final IntrinsicFunctionBasedArn uri = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(
        List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(), ":apigateway:",
            ResourceReference.builder().reference(exact("AWS::Region")).build(), ":lambda:path/2015-03-31/functions/",
            IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
            "/invocations")).build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder().type(exact("AWS_PROXY"))
        .integrationHttpMethod(exact("POST")).uri(uri).build();

    final RestApiMethodProperties restApiMethodProperties = RestApiRootMethodProperties.builder().resourceId(
            IntrinsicFunctionBasedArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
                .attributesArn(exact("RootResourceId")).build()).httpMethod(exact("ANY"))
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType(exact("NONE")).integration(restApiMethodIntegration).build();

    final RestApiMethod restApiMethod = RestApiMethod.builder().properties(restApiMethodProperties).build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), restApiMethod);
    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void should_have_role_with_AmazonAPIGatewayPushToCloudWatchLogs_policy_for_rest_api_to_push_logs_to_cloud_watch() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .principal(PolicyPrincipal.builder().service(exact("apigateway.amazonaws.com")).build()).effect(ALLOW).action("sts:AssumeRole").build();

    final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder().statement(policyStatement).build();

    final List<Object> arn = List.of("arn:", ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs");

    final IntrinsicFunctionBasedArn managedPolicyArn = IntrinsicFunctionBasedArn.builder().joinArn(EMPTY).joinArn(arn).build();

    final RoleProperties roleProperties = RoleProperties.builder().managedPolicyArn(managedPolicyArn)
        .assumeRolePolicyDocument(assumeRolePolicyDocument).build();

    final Role role = Role.builder().properties(roleProperties).deletionPolicy(RETAIN).updateReplacePolicy(RETAIN).build();

    final Map<String, Map<String, Object>> actual = template.findResources(ROLE.getValue(), role);

    assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
  }
}