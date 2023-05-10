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

package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.APIGATEWAY_RESTAPI;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.APIGATEWAY_RESTAPI_METHOD;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.APIGATEWAY_RESTAPI_RESOURCE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.APIGATEWAY_RESTAPI_STAGE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.LAMBDA_FUNCTION;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.LAMBDA_PERMISSION;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.POLICY;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.QUEUE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.QUEUE_POLICY;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.ROLE;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.TOPIC;
import static com.coffeebeans.springnativeawslambda.infra.assertion.CdkResourceType.TOPIC_SUBSCRIPTION;
import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;

public class CDKStackAssert extends AbstractAssert<CDKStackAssert, Template> {

  private CDKStackAssert(final Template actual) {
    super(actual, CDKStackAssert.class);
  }

  public static CDKStackAssert assertThat(final Template actual) {
    return new CDKStackAssert(actual);
  }

  public RestApiAssert containsRestApi(final String expected) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "Name", stringLikeRegexp(expected))
    );

    final Map<String, Map<String, Object>> resources = actual.findResources(
        APIGATEWAY_RESTAPI.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiAssert.assertThat(resources.values().stream().findFirst().get())
        .hasRestApi(expected);
  }

  public RestApiAccountAssert containsRestApiAccountWithCloudWatchRoleArn(
      final String cloudWatchRoleArn) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "CloudWatchRoleArn", Map.of("Fn::GetAtt", List.of(cloudWatchRoleArn, "Arn"))
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_ACCOUNT.getValue());

    Assertions.assertThat(resources)
        .isNotEmpty();

    return RestApiAccountAssert.assertThat(resources.values().stream().findFirst().get())
        .hasCloudWatchRole(cloudWatchRoleArn);
  }

  public RestApiDeploymentAssert containsRestApiDeploymentWithRestApiId(final String expected) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "RestApiId", Map.of("Ref", stringLikeRegexp(expected))
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_DEPLOYMENT.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiDeploymentAssert.assertThat(resources.values().stream().findFirst().get())
        .hasRestApiId(expected);
  }

  public RestApiMethodAssert containsNonRootRestApiMethod(final String method,
      final String resourceId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "HttpMethod", method,
            "ResourceId", Map.of("Ref", stringLikeRegexp(resourceId))
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiMethodAssert.assertThat(resources.values().stream().findFirst().get())
        .hasNonRootResourceId(resourceId);
  }

  public RestApiMethodAssert containsRootRestApiMethod(final String method,
      final String resourceId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "HttpMethod", method,
            "ResourceId", Map.of(
                "Fn::GetAtt", List.of(stringLikeRegexp(resourceId), "RootResourceId")
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiMethodAssert.assertThat(resources.values().stream().findFirst().get())
        .hasRootResourceId(resourceId);
  }

  public RestApiStageAssert containsRestApiStage(final String stageName) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "StageName", stringLikeRegexp(stageName)
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_STAGE.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiStageAssert.assertThat(resources.values().stream().findFirst().get())
        .hasStageName(stageName);
  }

  public RestApiResourceAssert containsRestApiResource(final String pathPart,
      final String restApiId,
      final Map<String, List<Object>> parentId) {
    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "PathPart", pathPart,
            "RestApiId", Map.of("Ref", stringLikeRegexp(restApiId)),
            "ParentId", parentId
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(APIGATEWAY_RESTAPI_RESOURCE.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return RestApiResourceAssert.assertThat(resources.values().stream().findFirst().get())
        .hasPath(pathPart);
  }

  public IamPolicyAssert containsPolicy(final String policyName) {
    final Map<String, Map<String, Object>> properties =
        Map.of("Properties", Map.of("PolicyName", Match.stringLikeRegexp(policyName)));

    final Map<String, Map<String, Object>> resources =
        actual.findResources(POLICY.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return IamPolicyAssert.assertThat(resources.values().stream().findFirst().get())
        .hasName(policyName);
  }


  public IamRoleAssert containsRoleWithManagedPolicyArn(final String managedPolicyArn) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "ManagedPolicyArns", List.of(
                Map.of("Fn::Join", List.of(
                        "",
                        List.of("arn:",
                            Map.of("Ref", "AWS::Partition"),
                            managedPolicyArn)
                    )
                )
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(ROLE.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return IamRoleAssert.assertThat(resources.values().stream().findFirst().get())
        .hasManagedPolicyArn(managedPolicyArn);
  }

  public LambdaAssert containsFunction(final String expected) {
    final Map<String, Map<String, String>> properties = Map.of("Properties",
        Map.of("FunctionName", expected));

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_FUNCTION.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaAssert.assertThat(resources.values().stream().findFirst().get())
        .hasFunction(expected);
  }

  public LambdaEventInvokeConfigAssert containsLambdaEventInvokeConfig(final String functionName,
      final String successEventDestination,
      final String failureEventDestination) {

    final Map<String, Map<String, Object>> properties = Map.of("Properties",
        Map.of("FunctionName", Map.of("Ref", stringLikeRegexp(functionName)),
            "DestinationConfig", Map.of(
                "OnFailure",
                Map.of("Destination", Map.of("Ref", stringLikeRegexp(failureEventDestination))),
                "OnSuccess",
                Map.of("Destination", Map.of("Ref", stringLikeRegexp(successEventDestination)))
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaEventInvokeConfigAssert.assertThat(resources.values().stream().findFirst().get())
        .hasLambdaEventInvokeConfig(functionName, successEventDestination, failureEventDestination);
  }

  public LambdaPermissionAssert containsLambdaPermission(
      final String functionName,
      final String action,
      final String principal,
      final List<Object> sourceArn) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of(
            "Action", action,
            "Principal", principal,
            "SourceArn", Map.of("Fn::Join", List.of("", sourceArn)),
            "FunctionName", Map.of("Fn::GetAtt", List.of(stringLikeRegexp(functionName), "Arn"))));

    final Map<String, Map<String, Object>> resources =
        actual.findResources(LAMBDA_PERMISSION.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return LambdaPermissionAssert.assertThat(resources.values().stream().findFirst().get())
        .hasLambdaPermission(functionName, action, principal);
  }

  public QueuePolicyAssert containsQueuePolicy(final String queueReference,
      final String policyStatementAction,
      final String policyStatementResource,
      final String policyStatementPrincipalService) {

    final Map<String, Map<String, Object>> properties = Map.of(
        "Properties", Map.of("Queues", List.of(Map.of("Ref", stringLikeRegexp(queueReference))),
            "PolicyDocument", Map.of(
                "Version", "2012-10-17",
                "Statement", List.of(
                    Map.of(
                        "Action", policyStatementAction,
                        "Resource", Map.of("Fn::GetAtt",
                            List.of(stringLikeRegexp(policyStatementResource), "Arn")),
                        "Principal", Map.of(
                            "Service", policyStatementPrincipalService
                        )
                    )
                )
            )
        )
    );

    final Map<String, Map<String, Object>> resources =
        actual.findResources(QUEUE_POLICY.getValue(), properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return QueuePolicyAssert.assertThat(resources.values().stream().findFirst().get())
        .hasQueuePolicy(queueReference,
            policyStatementAction,
            policyStatementResource,
            policyStatementPrincipalService);
  }

  public QueueAssert containsQueue(final String name) {

    final Map<String, Map<String, Object>> resources =
        actual.findResources(QUEUE.getValue(), Map.of("Properties", Map.of("QueueName", name)));

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return QueueAssert.assertThat(resources.values().stream().findFirst().get())
        .hasQueue(name);
  }

  public TopicAssert containsTopic(final String name) {

    final Map<String, Map<String, Object>> resources =
        actual.findResources(TOPIC.getValue(), Map.of("Properties", Map.of("TopicName", name)));

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    return TopicAssert.assertThat(resources.values().stream().findFirst().get())
        .hasTopic(name);
  }

  public void containsTopicSubscription(final String topicArn,
      final String protocol,
      final String endpoint) {
    final Map<String, Map<String, Object>> properties =
        Map.of("Properties", Map.of(
            "Endpoint", Map.of("Fn::GetAtt", List.of(stringLikeRegexp(endpoint), "Arn")),
            "Protocol", protocol,
            "TopicArn", Map.of("Ref", stringLikeRegexp(topicArn)))
        );

    final Map<String, Map<String, Object>> resources = actual.findResources(
        TOPIC_SUBSCRIPTION.getValue(),
        properties);

    Assertions.assertThat(resources)
        .isNotEmpty()
        .hasSize(1);

    TopicSubscriptionAssert.assertThat(resources.values().stream().findFirst().get())
        .hasTopicSubscription(topicArn, protocol, endpoint);
  }
}
