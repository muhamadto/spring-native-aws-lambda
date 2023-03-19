package com.coffeebeans.springnativeawslambda.function.infra.resource;

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
public class QueuePolicy {

  @JsonIgnore
  private final CdkResourceType type = CdkResourceType.QUEUE_POLICY;

  @JsonProperty("Properties")
  private QueuePolicyProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
