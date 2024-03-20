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

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_APPLICATION_VALUE;
import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_ENV;
import static com.coffeebeans.springnativeawslambda.infra.Environment.COFFEE_BEANS_DEV_111111111111_AP_SOUTHEAST_2;
import static com.coffeebeans.springnativeawslambda.infra.Environment.COFFEE_BEANS_PRD_111111111111_AP_SOUTHEAST_2;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.createTags;
import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;

import com.coffeebeans.cdk.core.AbstractApp;
import com.coffeebeans.cdk.core.type.SafeString;
import java.util.Map;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Tags;

public final class Application extends AbstractApp {
  private static final String ENVIRONMENT_NAME_DEV = "dev";
  private static final String ENVIRONMENT_NAME_PRD = "prd";
  private static final String LAMBDA_CODE_PATH = "spring-native-aws-service/target/spring-native-aws-function-native-zip.zip";

  public static void main(final String... args) {
    final Application app = new Application();

    final String env = System.getenv(KEY_ENV);
    checkNotNull(env, "'env' environment variable is required");

    switch (env) {
      case ENVIRONMENT_NAME_DEV -> new SpringNativeAwsFunctionStack(app, COFFEE_BEANS_DEV_111111111111_AP_SOUTHEAST_2, LAMBDA_CODE_PATH, env);
      case ENVIRONMENT_NAME_PRD -> new SpringNativeAwsFunctionStack(app, COFFEE_BEANS_PRD_111111111111_AP_SOUTHEAST_2, LAMBDA_CODE_PATH, env);
      default -> throw new IllegalArgumentException("Environment name '%s' is not set to a valid value. Set it to '[dev|prd]'".formatted(KEY_ENV));
    }

    final Map<String, String> tags = createTags(env, CostCentre.COFFEE_BEANS, app.getApplicationName().getValue());
    tags.entrySet().stream()
        .filter(tag -> Objects.nonNull(tag.getValue()))
        .forEach(tag -> Tags.of(app).add(tag.getKey(), tag.getValue()));

    app.synth();
  }

  @Override
  public @NotNull SafeString getApplicationName() {
    return SafeString.of(KEY_APPLICATION_VALUE);
  }
}