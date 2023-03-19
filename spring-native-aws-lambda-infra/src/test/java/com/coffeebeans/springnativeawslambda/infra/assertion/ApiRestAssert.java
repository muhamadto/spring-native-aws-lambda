package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_METHOD;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_RESOURCE;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.APIGATEWAY_RESTAPI_STAGE;

import com.coffeebeans.springnativeawslambda.infra.resource.RestApi;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiAccount;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiDeployment;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiMethod;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiResource;
import com.coffeebeans.springnativeawslambda.infra.resource.RestApiStage;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

public class ApiRestAssert extends AbstractAssert<ApiRestAssert, Template> {

  private ApiRestAssert(final Template template) {
    super(template, ApiRestAssert.class);
  }

  public static ApiRestAssert assertThat(final Template template) {
    return new ApiRestAssert(template);
  }

  public ApiRestAssert hasRestApi(final RestApi expected) {
    final Map<String, Map<String, Object>> restApis = actual.findResources(APIGATEWAY_RESTAPI.getValue(), expected);

    Assertions.assertThat(restApis)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public ApiRestAssert hasRestApiAccount(final RestApiAccount expected) {
    final Map<String, Map<String, Object>> policies = actual.findResources(APIGATEWAY_RESTAPI_ACCOUNT.getValue(), expected);

    Assertions.assertThat(policies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public ApiRestAssert hasRestApiDeployment(final RestApiDeployment expected) {
    final Map<String, Map<String, Object>> policies = actual.findResources(APIGATEWAY_RESTAPI_DEPLOYMENT.getValue(), expected);

    Assertions.assertThat(policies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public ApiRestAssert hasRestApiStage(final RestApiStage expected) {
    final Map<String, Map<String, Object>> policies = actual.findResources(APIGATEWAY_RESTAPI_STAGE.getValue(), expected);

    Assertions.assertThat(policies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public ApiRestAssert hasRestApiResource(final RestApiResource expected) {
    final Map<String, Map<String, Object>> policies = actual.findResources(APIGATEWAY_RESTAPI_RESOURCE.getValue(), expected);

    Assertions.assertThat(policies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }

  public ApiRestAssert hasRestApiMethod(final RestApiMethod expected) {
    final Map<String, Map<String, Object>> policies = actual.findResources(APIGATEWAY_RESTAPI_METHOD.getValue(), expected);

    Assertions.assertThat(policies)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);

    return this;
  }
}
