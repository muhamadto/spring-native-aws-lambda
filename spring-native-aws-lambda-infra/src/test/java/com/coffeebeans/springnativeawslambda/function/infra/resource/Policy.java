package com.coffeebeans.springnativeawslambda.function.infra.resource;

import static com.coffeebeans.springnativeawslambda.function.infra.resource.CdkResourceType.POLICY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Policy {

  @JsonIgnore
  private final CdkResourceType type = POLICY;

  @JsonProperty("Properties")
  private PolicyProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}

