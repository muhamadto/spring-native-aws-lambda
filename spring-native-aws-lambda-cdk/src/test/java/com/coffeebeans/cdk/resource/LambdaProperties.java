package com.coffeebeans.cdk.resource;

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
public class LambdaProperties {

  @JsonProperty("Code")
  private LambdaCode code;

  @JsonProperty("Role")
  private IntrinsicFunctionBasedArn roleArn;

  @JsonProperty("Environment")
  private LambdaEnvironment environment;

  @Singular
  @JsonProperty("Tags")
  private List<Tag> tags;

  @JsonProperty("Description")
  private Matcher description;

  @JsonProperty("FunctionName")
  private Matcher functionName;

  @JsonProperty("Handler")
  private Matcher handler;

  @JsonProperty("MemorySize")
  private Integer memorySize;

  @JsonProperty("Runtime")
  private Matcher runtime;

  @JsonProperty("Timeout")
  private Integer timeout;
}
