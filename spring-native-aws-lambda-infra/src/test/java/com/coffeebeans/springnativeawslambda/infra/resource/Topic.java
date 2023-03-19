package com.coffeebeans.springnativeawslambda.infra.resource;

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
public class Topic {

  @JsonIgnore
  private final CdkResourceType type = CdkResourceType.SNS;

  @JsonProperty("Properties")
  private TopicProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
