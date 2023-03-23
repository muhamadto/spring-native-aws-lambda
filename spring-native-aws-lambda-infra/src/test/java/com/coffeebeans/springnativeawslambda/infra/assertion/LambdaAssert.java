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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_FUNCTION;
import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_PERMISSION;

import com.coffeebeans.springnativeawslambda.infra.resource.Lambda;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaEventInvokeConfig;
import com.coffeebeans.springnativeawslambda.infra.resource.LambdaPermission;
import org.assertj.core.api.AbstractAssert;
import software.amazon.awscdk.assertions.Template;

public class LambdaAssert extends AbstractAssert<LambdaAssert, Template> {

  private LambdaAssert(final Template actual) {
    super(actual, LambdaAssert.class);
  }

  public static LambdaAssert assertThat(final Template actual) {
    return new LambdaAssert(actual);
  }

  public LambdaAssert hasFunction(final Lambda expected) {

    actual.hasResource(LAMBDA_FUNCTION.getValue(), expected);

    return this;
  }

  public LambdaAssert hasLambdaEventInvokeConfig(final LambdaEventInvokeConfig expected) {

    actual.hasResource(LAMBDA_EVENT_INVOKE_CONFIG.getValue(), expected);

    return this;
  }

  public LambdaAssert hasLambdaPermission(final LambdaPermission expected) {

    actual.hasResource(LAMBDA_PERMISSION.getValue(), expected);

    return this;
  }
}
