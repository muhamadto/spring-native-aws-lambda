package com.coffeebeans.cdk.resource;

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
public class RestApiMethodProperties {

  @JsonProperty("HttpMethod")
  private Matcher httpMethod;

  @JsonProperty("ResourceId")
  private ResourceReference resourceId;

  @JsonProperty("RestApiId")
  private ResourceReference restApiId;

  @JsonProperty("Integration")
  private RestApiMethodIntegration integration;

  @JsonProperty("AuthorizationType")
  private Matcher authorizationType;
}
