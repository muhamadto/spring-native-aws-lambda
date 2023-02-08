package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class RestApiMethodProperties {

  @JsonProperty("HttpMethod")
  protected Matcher httpMethod;

  @JsonProperty("RestApiId")
  protected ResourceReference restApiId;

  @JsonProperty("Integration")
  protected RestApiMethodIntegration integration;

  @JsonProperty("AuthorizationType")
  protected Matcher authorizationType;
}
