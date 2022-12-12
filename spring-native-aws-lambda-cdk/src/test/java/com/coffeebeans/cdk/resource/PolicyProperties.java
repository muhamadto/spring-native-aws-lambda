package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({
    "PolicyName",
    "PolicyDocument",
    "Roles"
})
public class PolicyProperties {

  @JsonProperty("PolicyName")
  private Matcher policyName;

  @JsonProperty("PolicyDocument")
  private PolicyDocument policyDocument;

  @Singular
  @JsonProperty("Roles")
  private List<RoleRef> roles;
}
