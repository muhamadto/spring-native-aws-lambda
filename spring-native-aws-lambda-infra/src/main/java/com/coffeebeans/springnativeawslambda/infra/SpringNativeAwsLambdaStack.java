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

import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.IManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.sns.Topic;
import software.constructs.Construct;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_ENV;
import static software.amazon.awscdk.services.iam.ManagedPolicy.fromAwsManagedPolicyName;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;

public class SpringNativeAwsLambdaStack extends ApiBaseStack {

  static final String LAMBDA_FUNCTION_ID = "spring-native-aws-lambda-function";
  private static final String REST_API_ID = LAMBDA_FUNCTION_ID + "-rest-api";
  private static final String DEAD_LETTER_TOPIC_ID = LAMBDA_FUNCTION_ID + "-dead-letter-topic";
  private static final String LAMBDA_HANDLER = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest";
  private static final String ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";

  public SpringNativeAwsLambdaStack(
      @NotNull final Construct scope,
      @NotBlank final String id,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String stage,
      @NotNull final StackProps props) {

    super(scope, id, props);

    final Topic deadLetterTopic = createTopic(DEAD_LETTER_TOPIC_ID);

    final List<IManagedPolicy> managedPolicies =
        List.of(fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole"));

    final Role role = Role.Builder.create(this, LAMBDA_FUNCTION_ID + "-role")
        .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
        .managedPolicies(managedPolicies)
        .build();

    final AssetCode assetCode = fromAsset(lambdaCodePath);

    final Map<String, String> environment =
            Map.of(ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE, stage, KEY_ENV, stage);

    final Function function = createFunction(
        LAMBDA_FUNCTION_ID,
        LAMBDA_HANDLER,
        assetCode,
        deadLetterTopic,
        role,
        environment);

    createLambdaRestApi(stage, REST_API_ID, "name", "POST", function, true);
  }
}