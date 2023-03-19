package com.coffeebeans.cdk.resource;

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
public class LambdaPermissionProperties {

  @JsonProperty("Action")
  private String action;
  @JsonProperty("FunctionName")
  private IntrinsicFunctionBasedArn functionName;
  @JsonProperty("Principal")
  private String principal;
  @JsonProperty("SourceArn")
  private IntrinsicFunctionBasedArn sourceArn;
}
