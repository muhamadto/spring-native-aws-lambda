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

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Match;

import java.util.List;
import java.util.Map;

import static cloud.pianola.cdk.fluent.assertion.CDKStackAssert.assertThat;
import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_COST_CENTRE;

class RestApiTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_rest_api() {

    assertThat(template)
        .containsRestApi("spring-native-aws-lambda-function-rest-api")
            .hasTag("COST_CENTRE", KEY_COST_CENTRE)
        .hasTag("ENV", TEST);
  }

  @Test
  void should_have_rest_api_account() {
    final String cloudWatchRoleArn = "springnativeawslambdafunctionrestapiCloudWatchRole(.*)";
    final String dependency = "springnativeawslambdafunctionrestapi(.*)";

    assertThat(template)
        .containsRestApiAccountWithCloudWatchRoleArn(cloudWatchRoleArn)
        .hasDependency(dependency)
        .hasUpdateReplacePolicy("Retain")
        .hasDeletionPolicy("Retain");
  }

  @Test
  void should_have_rest_api_deployment() {

    assertThat(template)
        .containsRestApiDeployment("springnativeawslambdafunctionrestapi(.*)")
        .hasDependency("springnativeawslambdafunctionrestapiproxyANY(.*)")
        .hasDependency("springnativeawslambdafunctionrestapiproxy(.*)")
        .hasDependency("springnativeawslambdafunctionrestapiANY(.*)")
        .hasDependency("springnativeawslambdafunctionrestapinamePOST(.*)")
        .hasDependency("springnativeawslambdafunctionrestapiname(.*)")
        .hasDescription("Automatically created by the RestApi construct");
  }

  @Test
  void should_have_rest_api_stage() {

    assertThat(template)
        .containsRestApiStage("test")
        .hasRestApiId(("springnativeawslambdafunctionrestapi(.*)"))
        .hasDeploymentId(("springnativeawslambdafunctionrestapiDeployment(.*)"))
        .hasDependency("springnativeawslambdafunctionrestapiAccount(.*)")
            .hasTag("COST_CENTRE", KEY_COST_CENTRE)
        .hasTag("ENV", TEST);
  }

  @Test
  void should_have_proxy_resource() {
    final String restApiId = "springnativeawslambdafunctionrestapi(.*)";

    final Map<String, List<Object>> parentId = Map.of(
        "Fn::GetAtt", List.of(Match.stringLikeRegexp(restApiId), "RootResourceId")
    );

    assertThat(template)
        .containsRestApiResource("{proxy+}", restApiId, parentId)
        .hasRestApiId(restApiId)
        .hasParentId(restApiId);
  }

  @Test
  void should_have_account_resource() {
    final String restApiId = "springnativeawslambdafunctionrestapi(.*)";

    final Map<String, List<Object>> parentId = Map.of(
        "Fn::GetAtt", List.of(Match.stringLikeRegexp(restApiId), "RootResourceId")
    );

    assertThat(template)
        .containsRestApiResource("name", restApiId, parentId)
        .hasRestApiId(restApiId)
        .hasParentId(restApiId);
  }

  @Test
  void should_have_post_method() {

    final String integrationType = "AWS_PROXY";
    final String httpMethod = "POST";

    assertThat(template)
        .containsNonRootRestApiMethod(httpMethod, "springnativeawslambdafunctionrestapiname(.*)")
        .hasHttpMethod(httpMethod)
        .hasIntegration(httpMethod, integrationType)
        .hasAuthorizationType("NONE")
        .hasRestApiId(("springnativeawslambdafunctionrestapi(.*)"));
  }

  @Test
  void should_have_proxy_method() {

    final String method = "ANY";
    final String integrationType = "AWS_PROXY";

    assertThat(template)
        .containsNonRootRestApiMethod(method, "springnativeawslambdafunctionrestapiproxy(.*)")
        .hasHttpMethod(method)
        .hasIntegration("POST", integrationType)
        .hasAuthorizationType("NONE")
        .hasRestApiId(("springnativeawslambdafunctionrestapi(.*)"));
  }

  @Test
  void should_have_root_method() {

    final String method = "ANY";
    final String integrationType = "AWS_PROXY";

    assertThat(template)
        .containsRootRestApiMethod(method, "springnativeawslambdafunctionrestapi(.*)")
        .hasHttpMethod(method)
        .hasIntegration("POST", integrationType)
        .hasAuthorizationType("NONE")
        .hasRestApiId(("springnativeawslambdafunctionrestapi(.*)"));
  }

  @Test
  void should_have_role_with_AmazonAPIGatewayPushToCloudWatchLogs_policy_for_rest_api_to_push_logs_to_cloud_watch() {
    final String principal = "apigateway.amazonaws.com";
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";
    final String managedPolicyArn = ":iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs";

    assertThat(template)
        .containsRoleWithManagedPolicyArn(managedPolicyArn)
        .hasAssumeRolePolicyDocument(principal,
            null,
            effect,
            policyDocumentVersion,
            "sts:AssumeRole");
  }
}