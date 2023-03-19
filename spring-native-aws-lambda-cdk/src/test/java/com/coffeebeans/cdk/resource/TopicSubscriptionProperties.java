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
public class TopicSubscriptionProperties {

  @JsonProperty("Protocol")
  private Matcher protocol;

  @JsonProperty("TopicArn")
  private ResourceReference topicArn;

  @JsonProperty("Endpoint")
  private IntrinsicFunctionBasedArn endpoint;
}
