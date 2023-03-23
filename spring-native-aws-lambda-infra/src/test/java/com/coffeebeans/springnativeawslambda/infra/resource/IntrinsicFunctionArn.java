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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

/**
 * This record represents intrinsic function based arn.
 * <br>
 * Example of using {@code Fn::GetAtt} function
 * <pre>
 *   final IntrinsicFunctionArn arn = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *  </pre>
 * <p>
 * Example from Rest API assertion to illustrate the usage of {@code Fn::Join} function
 * <pre>
 *  final List&lt Object &gt joinArns = List.of(
 *         "arn:",
 *         ResourceReference.builder().reference(exact("AWS::Partition")).build(),
 *         ":execute-api:",
 *         ResourceReference.builder().reference(exact("AWS::Region")).build(),
 *         ":",
 *         ResourceReference.builder().reference(exact("AWS::AccountId")).build(),
 *         ":",
 *         ResourceReference.builder().reference(stringLikeRegexp("functionrestapi(.*)")).build(),
 *         "/",
 *         ResourceReference.builder().reference(stringLikeRegexp("functionrestapiDeploymentStagetest(.*)")).build(),
 *         "\/*\/*"
 *   );
 *   final IntrinsicFunctionArn arn = IntrinsicFunctionArn.builder()
 *         .joinArns(joinArn)
 *         .build();
 *  </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record IntrinsicFunctionArn(
    @Singular @JsonProperty("Fn::GetAtt") List<Object> attributesArns,
    @Singular @JsonProperty("Fn::Join") List<Object> joinArns) {

}
