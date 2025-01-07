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

import io.sadpipers.cdk.type.KebabCaseString;
import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.AbstractEnvironment;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.dynamodb.TableV2;
import io.sandpipers.cdk.core.construct.dynamodb.TableV2.TableProps;
import io.sandpipers.cdk.core.construct.lambda.CustomRuntime2023Function;
import io.sandpipers.cdk.core.construct.lambda.CustomRuntime2023Function.CustomRuntime2023FunctionProps;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.iam.IManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_ENV;
import static software.amazon.awscdk.services.iam.ManagedPolicy.fromAwsManagedPolicyName;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;

public class SpringNativeAwsFunctionStack extends BaseStack {

  private static final int LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS = 3;
  private static final int LAMBDA_FUNCTION_MEMORY_SIZE = 512;
  private static final int LAMBDA_FUNCTION_RETRY_ATTEMPTS = 2;
  private static final String LAMBDA_HANDLER = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest";
  private static final String ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";

  public SpringNativeAwsFunctionStack(@NotNull final Application app,
      @NotNull final AbstractEnvironment environment,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String stage) {
    super(app, environment);

    final List<IManagedPolicy> managedPolicies =
        List.of(fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole"));

    final Role role = Role.Builder.create(this, "Role")
        .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
        .managedPolicies(managedPolicies)
        .build();

    final AssetCode assetCode = fromAsset(lambdaCodePath);

    final Map<String, String> lambdaEnvironment = Map.of(ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE, stage, KEY_ENV, stage);

    final CustomRuntime2023FunctionProps functionProps = CustomRuntime2023FunctionProps.builder()
        .description("Example of a Spring Native AWS Lambda Function using CDK")
        .code(assetCode)
        .handler(LAMBDA_HANDLER)
        .role(role)
        .environment(lambdaEnvironment)
        .deadLetterQueueEnabled(true)
        .timeout(Duration.seconds(LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS))
        .memorySize(LAMBDA_FUNCTION_MEMORY_SIZE)
        .retryAttempts(LAMBDA_FUNCTION_RETRY_ATTEMPTS)
        .build();

    final Function function = new CustomRuntime2023Function<>(this, SafeString.of("Lambda"), functionProps)
        .getFunction();

    final TableProps tableProps = TableProps.builder()
        .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
        .timeToLiveAttribute("creationTime")
        .tableName(KebabCaseString.of("secrets"))
        .removalPolicy(RemovalPolicy.RETAIN_ON_UPDATE_OR_DELETE)
        .build();

    final software.amazon.awscdk.services.dynamodb.TableV2 tableV2 = new TableV2(this, SafeString.of("Table"), tableProps).getTable();

    tableV2.grantWriteData(function);
    tableV2.grantReadData(function);

    // point to the lambda
    final LambdaRestApi lambdaRestApi = LambdaRestApi.Builder.create(this, "RestApi")
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