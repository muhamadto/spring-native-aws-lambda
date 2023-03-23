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
import static com.coffeebeans.springnativeawslambda.infra.assertion.CDKAssert.assertThat;
import static com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatement.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static software.amazon.awscdk.assertions.Match.exact;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import com.coffeebeans.springnativeawslambda.infra.assertion.IamAssert;
import com.coffeebeans.springnativeawslambda.infra.resource.IntrinsicFunctionArn;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyDocument;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyPrincipal;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyStatement;
import com.coffeebeans.springnativeawslambda.infra.resource.ResourceReference;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiAccount;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiAccount.RestApiAccountProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiDeployment;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiDeployment.RestApiDeploymentProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethod;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethod.RestApiMethodIntegration;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethod.RestApiMethodProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiResource;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiResource.RestApiResourceProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiStage;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiStage.RestApiStageProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Role;
import com.coffeebeans.springnativeawslambda.infra.resource.Role.RoleProperties;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApiRestTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_rest_api() {

    assertThat(template)
        .hasRestApiWithName("spring-native-aws-lambda-function-rest-api")
        .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
        .hasTag("ENV", TEST);
  }

  @Test
  void should_have_rest_api_account() {
    final IntrinsicFunctionArn cloudWatchRoleArn = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)"))
        .attributesArn("Arn")
        .build();

    final RestApiAccountProperties restApiAccountProperties = RestApiAccountProperties.builder()
        .cloudWatchRoleArn(cloudWatchRoleArn)
        .build();

    final RestApiAccount expected = RestApiAccount.builder()
        .properties(restApiAccountProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .updateReplacePolicy("Retain")
        .deletionPolicy("Retain")
        .build();

    assertThat(template)
        .hasRestApiAccount(expected);
  }

  @Test
  void should_have_rest_api_deployment() {

    final RestApiDeploymentProperties restApiDeploymentProperties = RestApiDeploymentProperties.builder()
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .description("Automatically created by the RestApi construct").build();

    final RestApiDeployment expected = RestApiDeployment.builder().properties(restApiDeploymentProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiproxyANY(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiproxy(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiANY(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapinamePOST(.*)"))
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiname(.*)"))
        .build();

    assertThat(template)
        .hasRestApiDeployment(expected);
  }

  @Test
  void should_have_rest_api_stage() {

    final ResourceReference restApiId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .build();

    final ResourceReference deploymentId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapiDeployment(.*)")).build();

    final RestApiStageProperties restApiDeploymentProperties = RestApiStageProperties.builder()
        .restApiId(restApiId)
        .deploymentId(deploymentId)
        .stageName("test")
        .tag(Tag.builder().key("COST_CENTRE").value(TAG_VALUE_COST_CENTRE).build())
        .tag(Tag.builder().key("ENV").value(TEST).build())
        .build();

    final RestApiStage expected = RestApiStage.builder()
        .properties(restApiDeploymentProperties)
        .dependency(stringLikeRegexp("springnativeawslambdafunctionrestapiAccount(.*)"))
        .build();

    assertThat(template)
        .hasRestApiStage(expected);
  }

  @Test
  void should_have_proxy_resource() {

    final IntrinsicFunctionArn parentId = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .attributesArn("RootResourceId")
        .build();

    final ResourceReference restApiId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .build();

    final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder()
        .parentId(parentId)
        .pathPart("{proxy+}")
        .restApiId(restApiId)
        .build();

    final RestApiResource expected = RestApiResource.builder()
        .properties(restApiResourceProperties)
        .build();

    assertThat(template)
        .hasRestApiResource(expected);
  }

  @Test
  void should_have_account_resource() {

    final IntrinsicFunctionArn parentId = IntrinsicFunctionArn.builder()
        .attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).attributesArn("RootResourceId").build();

    final ResourceReference restApiId = ResourceReference.builder()
        .reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)"))
        .build();

    final RestApiResourceProperties restApiResourceProperties = RestApiResourceProperties.builder()
        .parentId(parentId)
        .pathPart("name")
        .restApiId(restApiId)
        .build();

    final RestApiResource expected = RestApiResource.builder()
        .properties(restApiResourceProperties)
        .build();

    assertThat(template)
        .hasRestApiResource(expected);
  }

  @Test
  void should_have_post_method() {

    final IntrinsicFunctionArn uri = IntrinsicFunctionArn.builder()
        .joinArn(EMPTY)
        .joinArn(List.of(
            "arn:",
            ResourceReference.builder().reference(exact("AWS::Partition")).build(),
            ":apigateway:",
            ResourceReference.builder().reference(exact("AWS::Region")).build(),
            ":lambda:path/2015-03-31/functions/",
            IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
            "/invocations"))
        .build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder()
        .type("AWS_PROXY")
        .integrationHttpMethod("POST")
        .uri(uri)
        .build();

    final RestApiMethodProperties restApiMethodProperties = RestApiMethodProperties.builder()
        .httpMethod("POST")
        .resourceId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiname(.*)")).build())
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType("NONE")
        .integration(restApiMethodIntegration)
        .build();

    final RestApiMethod expected = RestApiMethod.builder()
        .properties(restApiMethodProperties)
        .build();

    assertThat(template)
        .hasRestApiMethod(expected);
  }

  @Test
  void should_have_proxy_method() {

    final IntrinsicFunctionArn uri = IntrinsicFunctionArn.builder()
        .joinArn(EMPTY)
        .joinArn(List.of(
            "arn:",
            ResourceReference.builder().reference(exact("AWS::Partition")).build(),
            ":apigateway:",
            ResourceReference.builder().reference(exact("AWS::Region")).build(),
            ":lambda:path/2015-03-31/functions/",
            IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
            "/invocations"))
        .build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder()
        .type("AWS_PROXY")
        .integrationHttpMethod("POST")
        .uri(uri)
        .build();

    final RestApiMethodProperties restApiMethodProperties = RestApiMethodProperties.builder()
        .httpMethod("ANY")
        .resourceId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapiproxy(.*)")).build())
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType("NONE")
        .integration(restApiMethodIntegration)
        .build();

    final RestApiMethod expected = RestApiMethod.builder()
        .properties(restApiMethodProperties)
        .build();

    assertThat(template)
        .hasRestApiMethod(expected);
  }

  @Test
  void should_have_root_method() {

    final IntrinsicFunctionArn uri = IntrinsicFunctionArn.builder().joinArn(EMPTY).joinArn(
            List.of("arn:",
                ResourceReference.builder().reference(exact("AWS::Partition")).build(),
                ":apigateway:",
                ResourceReference.builder().reference(exact("AWS::Region")).build(),
                ":lambda:path/2015-03-31/functions/",
                IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunction(.*)")).attributesArn("Arn").build(),
                "/invocations"))
        .build();

    final RestApiMethodIntegration restApiMethodIntegration = RestApiMethodIntegration.builder()
        .type("AWS_PROXY")
        .integrationHttpMethod("POST")
        .uri(uri)
        .build();

    final RestApiMethodProperties restApiMethodProperties = RestApiMethodProperties.builder()
        .resourceId(
            IntrinsicFunctionArn.builder().attributesArn(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).attributesArn("RootResourceId")
                .build())
        .httpMethod("ANY")
        .restApiId(ResourceReference.builder().reference(stringLikeRegexp("springnativeawslambdafunctionrestapi(.*)")).build())
        .authorizationType("NONE")
        .integration(restApiMethodIntegration)
        .build();

    final RestApiMethod expected = RestApiMethod.builder()
        .properties(restApiMethodProperties)
        .build();

    assertThat(template)
        .hasRestApiMethod(expected);
  }

  @Test
  void should_have_role_with_AmazonAPIGatewayPushToCloudWatchLogs_policy_for_rest_api_to_push_logs_to_cloud_watch() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .principal(PolicyPrincipal.builder().service("apigateway.amazonaws.com").build())
        .effect(ALLOW)
        .action("sts:AssumeRole").build();

    final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder().statement(policyStatement).build();

    final List<Object> arn = List.of("arn:",
        ResourceReference.builder().reference(exact("AWS::Partition")).build(),
        ":iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs");

    final IntrinsicFunctionArn managedPolicyArn = IntrinsicFunctionArn.builder()
        .joinArn(EMPTY)
        .joinArn(arn)
        .build();

    final RoleProperties roleProperties = RoleProperties.builder()
        .managedPolicyArn(managedPolicyArn)
        .assumeRolePolicyDocument(assumeRolePolicyDocument).build();

    final Role expected = Role.builder()
        .properties(roleProperties)
        .deletionPolicy("Retain")
        .updateReplacePolicy("Retain")
        .build();

    IamAssert.assertThat(template)
        .hasRole(expected);
  }
}