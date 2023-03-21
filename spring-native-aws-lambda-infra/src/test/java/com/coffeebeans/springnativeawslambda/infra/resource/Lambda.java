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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.LAMBDA_FUNCTION;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Builder;
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
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Lambda(@Singular @JsonProperty("DependsOn") List<Matcher> dependencies,
                     @JsonProperty("Properties") LambdaProperties properties,
                     @JsonProperty("Type") String typeValue) {

  @JsonProperty("Type")
  static String type = LAMBDA_FUNCTION.getValue();

  /**
   * This class is used to represent the lambda code.
   * <br>
   * Example of using {@code S3Bucket} and {@code S3Key} function
   * <pre>
   *   final LambdaCode lambdaCode = LambdaCode.builder()
   *          .s3Bucket(exact("test-cdk-bucket"))
   *          .s3Key(stringLikeRegexp("(.*).zip"))
   *          .build();
   *  </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaCode(@JsonProperty("S3Bucket") Matcher s3Bucket,
                           @JsonProperty("S3Key") Matcher s3Key) {

  }

  /**
   * This class is used to represent the lambda destination config. Example of using {@code OnFailure} and {@code OnSuccess} function
   * <pre>
   *   final LambdaDestinationConfig lambdaDestinationConfig = LambdaDestinationConfig.builder()
   *       .onFailure(LambdaDestinationReference.builder().destination(ResourceReference.builder().reference(stringLikeRegexp(failureDestinationPattern)).build()).build())
   *       .OnSuccess(LambdaDestinationReference.builder().destination(ResourceReference.builder().reference(stringLikeRegexp(successDestinationPattern)).build()).build())
   *       .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaDestinationConfig(@JsonProperty("OnFailure") LambdaDestinationReference onFailure,
                                        @JsonProperty("OnSuccess") LambdaDestinationReference onSuccess) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaProperties(@JsonProperty("Code") LambdaCode code,
                                 @JsonProperty("Role") IntrinsicFunctionBasedArn roleArn,
                                 @JsonProperty("Environment") LambdaEnvironment environment,
                                 @JsonProperty("Tags") @Singular List<Tag> tags,
                                 @JsonProperty("Description") Matcher description,
                                 @JsonProperty("FunctionName") Matcher functionName,
                                 @JsonProperty("Handler") Matcher handler,
                                 @JsonProperty("MemorySize") Integer memorySize,
                                 @JsonProperty("Runtime") Matcher runtime,
                                 @JsonProperty("Timeout") Integer timeout) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaDestinationReference(@JsonProperty("Destination") ResourceReference destination) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaEnvironment(@Singular @JsonProperty("Variables") Map<String, Matcher> variables) {

  }
}
