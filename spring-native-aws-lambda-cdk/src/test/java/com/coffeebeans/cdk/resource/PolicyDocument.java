package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({
    "Version",
    "Statement"
})
public class PolicyDocument {

  @JsonProperty("Version")
  private final String version = "2012-10-17";

  @Singular
  @JsonProperty("Statement")
  private List<PolicyStatement> statements;
}
