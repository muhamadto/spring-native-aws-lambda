package com.coffeebeans.springnativeawslambda.infra.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleProperties {

  @JsonProperty("PolicyName")
  private Matcher policyName;

  @JsonProperty("AssumeRolePolicyDocument")
  private PolicyDocument assumeRolePolicyDocument;

  @Singular
  @JsonProperty("ManagedPolicyArns")
  private List<IntrinsicFunctionBasedArn> managedPolicyArns;
}
