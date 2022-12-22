package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG;

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
public class LambdaEventInvokeConfig {

  @JsonIgnore
  private final CdkResourceType type = LAMBDA_EVENT_INVOKE_CONFIG;

  @JsonProperty("Properties")
  private LambdaEventInvokeConfigProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
