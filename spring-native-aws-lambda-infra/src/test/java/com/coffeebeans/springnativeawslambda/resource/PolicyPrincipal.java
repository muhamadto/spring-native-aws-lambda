package com.coffeebeans.springnativeawslambda.function.infra.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PolicyPrincipal {

  @JsonProperty("Service")
  private Matcher service;
}
