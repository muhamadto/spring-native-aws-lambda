package com.coffeebeans.springnativeawslambda.infra.resource;

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
public class RestApiResourceProperties {

  @JsonProperty("ParentId")
  protected IntrinsicFunctionBasedArn parentId;

  @JsonProperty("PathPart")
  protected Matcher pathPart;

  @JsonProperty("RestApiId")
  protected ResourceReference restApiId;
}
