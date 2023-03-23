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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.POLICY;
import static com.fasterxml.jackson.annotation.JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

/**
 * This record is used to represent a IAM Policy.
 *
 * <pre>
 *      final Matcher policyName = stringLikeRegexp("identifier(.*)");
 *
 *     final ResourceReference role = ResourceReference.builder()
 *         .reference(stringLikeRegexp("identifier(.*)"))
 *         .build();
 *
 *      final PolicyStatement statement1 = PolicyStatement.builder()
 *         .effect(ALLOW)
 *         .action("sns:Publish")
 *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
 *         .build();
 *
 *      final PolicyStatement statement2 = PolicyStatement.builder()
 *         .effect(ALLOW)
 *         .action("sns:Publish")
 *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
 *         .build();
 *
 *     final PolicyDocument policyDocument = PolicyDocument.builder()
 *         .statement(statement1)
 *         .statement(statement2)
 *         .build();
 *
 *     final PolicyProperties policyProperties = PolicyProperties.builder()
 *         .policyName(policyName)
 *         .role(role)
 *         .policyDocument(policyDocument)
 *         .build();
 *
 *     final Policy policy = Policy.builder()
 *         .properties(policyProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Policy(@Singular @JsonProperty("DependsOn") List<Matcher> dependencies,
                     @JsonProperty("Properties") PolicyProperties properties,
                     @JsonProperty("Type") String typeValue) {

  @JsonProperty("Type")
  static String type = POLICY.getValue();

  /**
   * This record is used to represent a IAM Policy properties.
   *
   * <pre>
   *      final Matcher policyName = stringLikeRegexp("identifier(.*)");
   *
   *     final ResourceReference role = ResourceReference.builder()
   *         .reference(stringLikeRegexp("identifier(.*)"))
   *         .build();
   *
   *      final PolicyStatement statement1 = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .action("sns:Publish")
   *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   *
   *      final PolicyStatement statement2 = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .action("sns:Publish")
   *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   *
   *     final PolicyDocument policyDocument = PolicyDocument.builder()
   *         .statement(statement1)
   *         .statement(statement2)
   *         .build();
   *
   *     final PolicyProperties policyProperties = PolicyProperties.builder()
   *         .policyName(policyName)
   *         .role(role)
   *         .policyDocument(policyDocument)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PolicyProperties(

      @JsonProperty("PolicyName") Matcher policyName,

      @JsonProperty("PolicyDocument") PolicyDocument policyDocument,

      @Singular @JsonProperty("Roles") List<ResourceReference> roles) {

  }

  /**
   * This record is used to represent a IAM Policy properties.
   *
   * <pre>
   *      final PolicyStatement statement1 = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .action("sns:Publish")
   *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   *
   *      final PolicyStatement statement2 = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .action("sns:Publish")
   *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   *
   *     final PolicyDocument policyDocument = PolicyDocument.builder()
   *         .statement(statement1)
   *         .statement(statement2)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PolicyDocument(
      @Singular @JsonProperty("Statement") List<PolicyStatement> statements) {

    @JsonProperty("Version")
    static String version = "2012-10-17";

  }

  /**
   * This record is used to represent a IAM Policy statement.
   *
   * <pre>
   *      final PolicyStatement statement = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .action("sns:Publish")
   *         .resource(ResourceReference.builder().reference(stringLikeRegexp(pattern)).build())
   *         .build();
   * </pre>
   * <p>
   * Example using condition
   *
   * <pre>
   *     final IntrinsicFunctionArn resource = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final PolicyStatementCondition policyStatementCondition = PolicyStatementCondition.builder()
   *         .arnEquals(Map.of("aws:SourceArn", ResourceReference.builder()
   *             .reference(stringLikeRegexp("identifier(.*)"))
   *             .build()))
   *         .build();
   *
   *     final PolicyStatement policyStatement = PolicyStatement.builder()
   *         .effect(ALLOW)
   *         .principal(PolicyPrincipal.builder()
   *         .service("sns.amazonaws.com").build())
   *         .resource(resource)
   *         .action("sqs:SendMessage")
   *         .condition(policyStatementCondition)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PolicyStatement(
      @JsonIgnore PolicyStatementEffect effect,
      @Singular @JsonProperty("Action") @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) List<String> actions,
      @Singular @JsonProperty("Resource") @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) List<Object> resources,
      @JsonProperty("Principal") PolicyPrincipal principal,
      @JsonProperty("Condition") PolicyStatementCondition condition
  ) {

    @JsonProperty("Effect")
    public String getEffect() {
      return effect.getValue();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum PolicyStatementEffect {
      ALLOW("Allow"),
      DENY("Deny");

      private String value;

      @JsonValue
      @Override
      public String toString() {
        return value;
      }
    }
  }

  /**
   * This record is used to represent a IAM Policy principal.
   *
   * <pre>
   *       final PolicyPrincipal build = PolicyPrincipal.builder()
   *         .service("lambda.amazonaws.com")
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PolicyPrincipal(
      @JsonProperty("Service") String service) {

  }

  /**
   * This record is used to represent a IAM Policy statement condition.
   *
   * <pre>
   *       final PolicyStatementCondition policyStatementCondition = PolicyStatementCondition.builder()
   *         .arnEquals(Map.of("aws:SourceArn", ResourceReference.builder().reference(stringLikeRegexp("identifier(.*)")).build()))
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PolicyStatementCondition(
      @JsonProperty("ArnEquals") Map<String, ResourceReference> arnEquals) {

  }
}

