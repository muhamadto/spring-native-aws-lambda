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
public class QueueRedrivePolicy {

  @JsonProperty("maxReceiveCount")
  private Integer maxReceiveCount;

  @JsonProperty("deadLetterTargetArn")
  private IntrinsicFunctionBasedArn deadLetterTargetArn;

}
