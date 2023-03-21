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
package com.coffeebeans.springnativeawslambda.infra.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

/**
 * Creates a Lambda function for AWS CDK stack testing. Here is an example of how to use it:
 * <pre>
 *   final LambdaCode lambdaCode = LambdaCode.builder()
 *         .s3Bucket(exact("test-cdk-bucket"))
 *         .s3Key(stringLikeRegexp("(.*).zip"))
 *         .build();
 *
 *     final IntrinsicFunctionBasedArn roleArn = IntrinsicFunctionBasedArn.builder()
 *         .attributesArn(stringLikeRegexp("someFunctionIdentifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final LambdaEnvironment lambdaEnvironment = LambdaEnvironment.builder()
 *         .variable("ENV", exact(TEST))
 *         .build();
 *
 *     final LambdaProperties lambdaProperties = LambdaProperties.builder()
 *         .functionName(exact("lambda-function"))
 *         .handler(exact("class.fully.qualified.name::handlerMethod"))
 *         .memorySize(512)
 *         .runtime(exact("provided.al2"))
 *         .timeout(3)
 *         .description(exact("Lambda example"))
 *         .code(lambdaCode)
 *         .roleArn(roleArn)
 *         .tag(Tag.builder().key("COST_CENTRE").value(exact("core")).build())
 *         .tag(Tag.builder().key("ENV").value(exact("TEST")).build())
 *         .environment(lambdaEnvironment)
 *         .build();
 *
 *     final Lambda lambda = Lambda.builder()
 *         .dependency(stringLikeRegexp("somePolicyIdentifier(.*)"))
 *         .dependency(stringLikeRegexp("someRoleIdentifier(.*)"))
 *         .properties(lambdaProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Lambda {

  @JsonIgnore
  private final CdkResourceType type = CdkResourceType.LAMBDA_FUNCTION;

  @Singular
  @JsonProperty("DependsOn")
  private List<Matcher> dependencies;

  @JsonProperty("Properties")
  private LambdaProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
