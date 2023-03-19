package com.coffeebeans.springnativeawslambda.infra.resource;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.SQS;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Queue {

  @JsonIgnore
  private final CdkResourceType type = SQS;

  @JsonProperty("Properties")
  private QueueProperties properties;

  @JsonProperty("UpdateReplacePolicy")
  private Matcher updateReplacePolicy;

  @JsonProperty("DeletionPolicy")
  private Matcher deletionPolicy;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
