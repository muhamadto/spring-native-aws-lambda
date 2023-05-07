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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_METHOD;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_RESOURCE;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_STAGE;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.BUCKET;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_FUNCTION;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_PERMISSION;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.POLICY;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.QUEUE;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.QUEUE_POLICY;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.ROLE;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.TOPIC;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.TOPIC_SUBSCRIPTION;

import com.coffeebeans.springnativeawslambda.infra.resource.Bucket;
import com.coffeebeans.springnativeawslambda.infra.resource.Lambda;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermission;
import com.coffeebeans.springnativeawslambda.infra.resource.Policy;
import com.coffeebeans.springnativeawslambda.infra.resource.Queue;
import com.coffeebeans.springnativeawslambda.infra.resource.QueuePolicy;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApi;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiAccount;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiDeployment;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethod;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiResource;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiStage;
import com.coffeebeans.springnativeawslambda.infra.resource.Role;
import com.coffeebeans.springnativeawslambda.infra.resource.Topic;
import com.coffeebeans.springnativeawslambda.infra.resource.TopicSubscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

public class CDKAssert extends AbstractAssert<CDKAssert, Template> {

  final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private CDKAssert(final Template actual) {
    super(actual, CDKAssert.class);
  }

  public static CDKAssert assertThat(final Template actual) {
    return new CDKAssert(actual);
  }

  public ApiRestAssert hasRestApiWithName(final String expected) {
    final Map<String, Map<String, Object>> resources = actual.findResources(APIGATEWAY_RESTAPI.getValue());

    Assertions.assertThat(resources)
        .isNotEmpty()
        .isNotEmpty()
        .hasSize(1);

    final RestApi restApi = resources.values().stream()
        .map(value -> OBJECT_MAPPER.convertValue(value, RestApi.class))
        .filter(value -> value.properties().name().equals(expected))
        .findFirst()
        .orElseThrow(() -> new AssertionError("RestApi with name " + expected + " not found"));

    return ApiRestAssert.assertThat(restApi);
  }

  public CDKAssert hasRestApiAccount(final RestApiAccount expected) {
    actual.hasResource(APIGATEWAY_RESTAPI_ACCOUNT.getValue(), expected);

    return this;
  }

  public CDKAssert hasRestApiDeployment(final RestApiDeployment expected) {
    actual.hasResource(APIGATEWAY_RESTAPI_DEPLOYMENT.getValue(), expected);

    return this;
  }

  public CDKAssert hasRestApiStage(final RestApiStage expected) {
    actual.hasResource(APIGATEWAY_RESTAPI_STAGE.getValue(), expected);

    return this;
  }

  public CDKAssert hasRestApiResource(final RestApiResource expected) {
    actual.hasResource(APIGATEWAY_RESTAPI_RESOURCE.getValue(), expected);

    return this;
  }

  public CDKAssert hasRestApiMethod(final RestApiMethod expected) {
    actual.hasResource(APIGATEWAY_RESTAPI_METHOD.getValue(), expected);

    return this;
  }

  public CDKAssert hasBucket(final Bucket expected) {
    actual.hasResource(BUCKET.getValue(), expected);

    return this;
  }

  public CDKAssert hasRole(final Role expected) {
    actual.hasResource(ROLE.getValue(), expected);

    return this;
  }

  public CDKAssert hasPolicy(final Policy expected) {
    actual.hasResource(POLICY.getValue(), expected);

    return this;
  }

  public CDKAssert hasFunction(final Lambda expected) {

    actual.hasResource(LAMBDA_FUNCTION.getValue(), expected);

    return this;
  }

  public CDKAssert hasLambdaEventInvokeConfig(final LambdaEventInvokeConfig expected) {

    actual.hasResource(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), expected);

    return this;
  }

  public CDKAssert hasLambdaPermission(final LambdaPermission expected) {

    actual.hasResource(LAMBDA_PERMISSION.getValue(), expected);

    return this;
  }

  public CDKAssert hasQueue(final Queue expected) {
    actual.hasResource(QUEUE.getValue(), expected);

    return this;
  }

  public CDKAssert hasQueuePolicy(final QueuePolicy expected) {
    actual.hasResource(QUEUE_POLICY.getValue(), expected);

    return this;
  }

  public CDKAssert hasTopic(final Topic expected) {

    actual.hasResource(TOPIC.getValue(), expected);

    return this;
  }

  public CDKAssert hasTopicSubscription(final TopicSubscription expected) {
    actual.hasResource(TOPIC_SUBSCRIPTION.getValue(), expected);

    return this;
  }

}
