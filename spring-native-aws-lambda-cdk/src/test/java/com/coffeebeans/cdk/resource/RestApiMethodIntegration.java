package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestApiMethodIntegration {

  @JsonProperty("IntegrationHttpMethod")
  private Matcher integrationHttpMethod;

  @JsonProperty("Type")
  private Matcher type;

  @JsonProperty("Uri")
  private IntrinsicFunctionBasedArn uri;
}
