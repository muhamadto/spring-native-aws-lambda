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
 * This record is used to represent a Lambda function.
 * <pre>
 *   final LambdaCode lambdaCode = LambdaCode.builder()
 *         .s3Bucket("test-cdk-bucket")
 *         .s3Key(stringLikeRegexp("(.*).zip"))
 *         .build();
 *
 *     final IntrinsicFunctionBasedArn roleArn = IntrinsicFunctionBasedArn.builder()
 *         .attributesArn(stringLikeRegexp("someFunctionIdentifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final LambdaEnvironment lambdaEnvironment = LambdaEnvironment.builder()
 *         .variable("ENV", "TEST")
 *         .build();
 *
 *     final LambdaProperties lambdaProperties = LambdaProperties.builder()
 *         .functionName("lambda-function")
 *         .handler("class.fully.qualified.name::handlerMethod")
 *         .memorySize(512)
 *         .runtime("provided.al2")
 *         .timeout(3)
 *         .description("Lambda example")
 *         .code(lambdaCode)
 *         .roleArn(roleArn)
 *         .tag(Tag.builder().key("COST_CENTRE").value("core").build())
 *         .tag(Tag.builder().key("ENV").value("TEST").build())
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
   * This record represents lambda code.
   * <br>
   * Example of using {@code S3Bucket} and {@code S3Key} function
   * <pre>
   *   final LambdaCode lambdaCode = LambdaCode.builder()
   *          .s3Bucket("test-cdk-bucket")
   *          .s3Key(stringLikeRegexp("(.*).zip"))
   *          .build();
   *  </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaCode(@JsonProperty("S3Bucket") String s3Bucket,
                           @JsonProperty("S3Key") Matcher s3Key) {

  }

  /**
   * This record represents lambda properties.
   * <pre>
   *   final LambdaCode lambdaCode = LambdaCode.builder()
   *         .s3Bucket("test-cdk-bucket")
   *         .s3Key(stringLikeRegexp("(.*).zip"))
   *         .build();
   *
   *     final IntrinsicFunctionBasedArn roleArn = IntrinsicFunctionBasedArn.builder()
   *         .attributesArn(stringLikeRegexp("someFunctionIdentifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final LambdaEnvironment lambdaEnvironment = LambdaEnvironment.builder()
   *         .variable("ENV", "TEST")
   *         .build();
   *
   *     final LambdaProperties lambdaProperties = LambdaProperties.builder()
   *         .functionName("lambda-function")
   *         .handler("class.fully.qualified.name::handlerMethod")
   *         .memorySize(512)
   *         .runtime("provided.al2")
   *         .timeout(3)
   *         .description("Lambda example")
   *         .code(lambdaCode)
   *         .roleArn(roleArn)
   *         .tag(Tag.builder().key("COST_CENTRE").value("core").build())
   *         .tag(Tag.builder().key("ENV").value("TEST").build())
   *         .environment(lambdaEnvironment)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaProperties(@JsonProperty("Code") LambdaCode code,
                                 @JsonProperty("Role") IntrinsicFunctionArn roleArn,
                                 @JsonProperty("Environment") LambdaEnvironment environment,
                                 @JsonProperty("Tags") @Singular List<Tag> tags,
                                 @JsonProperty("Description") String description,
                                 @JsonProperty("FunctionName") String functionName,
                                 @JsonProperty("Handler") String handler,
                                 @JsonProperty("MemorySize") Integer memorySize,
                                 @JsonProperty("Runtime") String runtime,
                                 @JsonProperty("Timeout") Integer timeout) {

  }

  /**
   * This record represents lambda destination reference.
   * <pre>
   *   final LambdaDestinationReference lambdaDestinationReference = LambdaDestinationReference.builder()
   *    .destination(ResourceReference.builder().reference(stringLikeRegexp(failureDestinationPattern)).build())
   *    .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaDestinationReference(@JsonProperty("Destination") ResourceReference destination) {

  }

  /**
   * This record represents lambda environment.
   * <pre>
   *   final LambdaEnvironment lambdaEnvironment = LambdaEnvironment.builder()
   *    .variable("ENV", "TEST")
   *    .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LambdaEnvironment(@Singular @JsonProperty("Variables") Map<String, String> variables) {

  }
}
