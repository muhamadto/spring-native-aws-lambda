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

import com.coffeebeans.springnativeawslambda.infra.lambda.CustomRuntime2023Function;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.iam.IManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.Topic.Builder;
import software.constructs.Construct;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_ENV;
import static software.amazon.awscdk.services.iam.ManagedPolicy.fromAwsManagedPolicyName;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2023;

public class SpringNativeAwsFunctionStack extends Stack {
  private static final int LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS = 3;
  private static final int LAMBDA_FUNCTION_MEMORY_SIZE = 512;
  private static final int LAMBDA_FUNCTION_RETRY_ATTEMPTS = 2;
  private static final String LAMBDA_HANDLER = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest";
  private static final String ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";

  public SpringNativeAwsFunctionStack(
      @NotNull final Construct scope,
      @NotBlank final String id,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String stage,
      @NotNull final StackProps props) {

    super(scope, id);

    final Topic deadLetterTopic = Topic.Builder.create(this, "DeadLetterTopic")
        .build();

    final List<IManagedPolicy> managedPolicies =
        List.of(fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole"));

    final Role role = Role.Builder.create(this, "Role")
        .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
        .managedPolicies(managedPolicies)
        .build();

    final AssetCode assetCode = fromAsset(lambdaCodePath);

    final Map<String, String> environment =
            Map.of(ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE, stage, KEY_ENV, stage);

    final FunctionProps functionProps = FunctionProps.builder()
        .runtime(PROVIDED_AL2023)
        .description("Example of a Spring Native AWS Lambda Function using CDK")
        .code(assetCode)
        .handler(LAMBDA_HANDLER)
        .role(role)
        .environment(environment)
        .deadLetterTopic(deadLetterTopic)
        .timeout(Duration.seconds(LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS))
        .memorySize(LAMBDA_FUNCTION_MEMORY_SIZE)
        .retryAttempts(LAMBDA_FUNCTION_RETRY_ATTEMPTS)
        .build();

    final Function function = new CustomRuntime2023Function(this, "Lambda", functionProps)
        .getFunction();

    // point to the lambda
    final LambdaRestApi lambdaRestApi = LambdaRestApi.Builder.create(this, "ResrApi")
        .handler(function)
        .proxy(true)
        .deployOptions(StageOptions.builder().stageName(stage).build())
        .build();

    // get root resource to add methods
    final Resource resource = lambdaRestApi.getRoot().addResource("variables");
    resource.addMethod(StringUtils.toRootUpperCase("ANY"));
    resource.addMethod(StringUtils.toRootUpperCase("GET"));
  }
}