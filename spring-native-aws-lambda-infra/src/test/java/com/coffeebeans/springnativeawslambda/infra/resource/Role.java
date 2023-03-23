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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.ROLE;

import com.coffeebeans.springnativeawslambda.infra.resource.Policy.PolicyDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

/**
 * This record is used to represent a IAM Role.
 *
 * <pre>
 *       final PolicyStatement policyStatement = PolicyStatement.builder()
 *         .principal(PolicyPrincipal.builder().service("lambda.amazonaws.com").build())
 *         .effect(ALLOW)
 *         .action("sts:AssumeRole")
 *         .build();
 *
 *     final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder()
 *         .statement(policyStatement)
 *         .build();
 *
 *     final List<Object> arn = List.of(
 *         "arn:",
 *         ResourceReference.builder().reference(exact("AWS::Partition")).build(),
 *         ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole");
 *
 *     final IntrinsicFunctionArn managedPolicyArn = IntrinsicFunctionArn.builder()
 *         .joinArn(EMPTY)
 *         .joinArn(arn)
 *         .build();
 *
 *     final RoleProperties roleProperties = RoleProperties.builder()
 *         .managedPolicyArn(managedPolicyArn)
 *         .assumeRolePolicyDocument(assumeRolePolicyDocument)
 *         .build();
 *
 *     final Role role = Role.builder()
 *         .properties(roleProperties)
 *         .deletionPolicy("Retain")
 *         .updateReplacePolicy("Retain")
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Role(@JsonProperty("Properties") RoleProperties properties,
                   @JsonProperty("UpdateReplacePolicy") String updateReplacePolicy,
                   @JsonProperty("DeletionPolicy") String deletionPolicy) {

  @JsonProperty("Type")
  static String type = ROLE.getValue();

  /**
   * This record is used to represent a IAM Role properties.
   *
   * <pre>
   *       final PolicyStatement policyStatement = PolicyStatement.builder()
   *         .principal(PolicyPrincipal.builder().service("lambda.amazonaws.com").build())
   *         .effect(ALLOW)
   *         .action("sts:AssumeRole")
   *         .build();
   *
   *     final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder()
   *         .statement(policyStatement)
   *         .build();
   *
   *     final List<Object> arn = List.of(
   *         "arn:",
   *         ResourceReference.builder().reference(exact("AWS::Partition")).build(),
   *         ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole");
   *
   *     final IntrinsicFunctionArn managedPolicyArn = IntrinsicFunctionArn.builder()
   *         .joinArn(EMPTY)
   *         .joinArn(arn)
   *         .build();
   *
   *     final RoleProperties roleProperties = RoleProperties.builder()
   *         .managedPolicyArn(managedPolicyArn)
   *         .assumeRolePolicyDocument(assumeRolePolicyDocument)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record RoleProperties(

      @JsonProperty("PolicyName") Matcher policyName,
      @JsonProperty("AssumeRolePolicyDocument") PolicyDocument assumeRolePolicyDocument,
      @Singular @JsonProperty("ManagedPolicyArns") List<IntrinsicFunctionArn> managedPolicyArns) {

  }
}
