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

import com.coffeebeans.springnativeawslambda.infra.resource.RestApi;
import com.coffeebeans.springnativeawslambda.infra.resource.Tag;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ApiRestAssert extends AbstractAssert<ApiRestAssert, RestApi> {

  private ApiRestAssert(final RestApi actual) {
    super(actual, ApiRestAssert.class);
  }

  public static ApiRestAssert assertThat(final RestApi actual) {
    return new ApiRestAssert(actual);
  }

  public ApiRestAssert hasTag(final Tag expected) {
    Assertions.assertThat(actual.properties().tags())
        .contains(expected);

    return this;
  }

  public ApiRestAssert hasTag(final String key, final String value) {
    Assertions.assertThat(actual.properties().tags())
        .contains(Tag.builder().key(key).value(value).build());

    return this;
  }

//  public ApiRestAssert hasRestApi(final RestApi expected) {
//    actual.hasResource(APIGATEWAY_RESTAPI.getValue(), expected);
//
//    return this;
//  }
//
//  public ApiRestAssert hasRestApiAccount(final RestApiAccount expected) {
//    actual.hasResource(APIGATEWAY_RESTAPI_ACCOUNT.getValue(), expected);
//
//    return this;
//  }
//
//  public ApiRestAssert hasRestApiDeployment(final RestApiDeployment expected) {
//    actual.hasResource(APIGATEWAY_RESTAPI_DEPLOYMENT.getValue(), expected);
//
//    return this;
//  }
//
//  public ApiRestAssert hasRestApiStage(final RestApiStage expected) {
//    actual.hasResource(APIGATEWAY_RESTAPI_STAGE.getValue(), expected);
//
//    return this;
//  }
//
//  public ApiRestAssert hasRestApiResource(final RestApiResource expected) {
//    actual.hasResource(APIGATEWAY_RESTAPI_RESOURCE.getValue(), expected);
//
//    return this;
//  }
//
//  public ApiRestAssert hasRestApiMethod(final RestApiMethod expected) {
//    actual.hasResource(APIGATEWAY_RESTAPI_METHOD.getValue(), expected);
//
//    return this;
//  }
}
