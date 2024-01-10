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

import lombok.NoArgsConstructor;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;

import java.util.Map;
import java.util.Objects;

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_ENV;
import static com.coffeebeans.springnativeawslambda.infra.StackUtils.createStack;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.createTags;
import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Application {

    private static final String DEV_STACK_NAME = "spring-native-aws-lambda-function-dev-stack";
    private static final String PRD_STACK_NAME = "spring-native-aws-lambda-function-prd-stack";
    private static final String ENVIRONMENT_NAME_DEV = "dev";
    private static final String ENVIRONMENT_NAME_PRD = "prd";
    private static final String LAMBDA_CODE_PATH =
            SpringNativeAwsLambdaStack.LAMBDA_FUNCTION_ID
                    + "/target/spring-native-aws-lambda-function-native-zip.zip";
    private static final String QUALIFIER = "cbcore";
    private static final String FILE_ASSETS_BUCKET_NAME = "cbcore-cdk-bucket";

    public static void main(final String... args) {
        final App app = new App();

        final String env = System.getenv(KEY_ENV);
        checkNotNull(env, "'env' environment variable is required");
        final Map<String, String> tags = createTags(env, KEY_COST_CENTRE);

        switch (env) {
            case ENVIRONMENT_NAME_DEV ->
                    createStack(app, DEV_STACK_NAME, LAMBDA_CODE_PATH, QUALIFIER, FILE_ASSETS_BUCKET_NAME, env);
            case ENVIRONMENT_NAME_PRD ->
                    createStack(app, PRD_STACK_NAME, LAMBDA_CODE_PATH, QUALIFIER, FILE_ASSETS_BUCKET_NAME, env);
            default -> throw new IllegalArgumentException("Environment variable " + KEY_ENV
                    + " is not set to a valid value. Set it to '[dev|prd]'");
        }

        tags.entrySet().stream()
                .filter(tag -> Objects.nonNull(tag.getValue()))
                .forEach(tag -> Tags.of(app).add(tag.getKey(), tag.getValue()));

        app.synth();
    }
}