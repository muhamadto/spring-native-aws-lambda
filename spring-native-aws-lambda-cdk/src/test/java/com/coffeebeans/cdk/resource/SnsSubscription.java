package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.SNS_SUBSCRIPTION;

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
public class SnsSubscription {

  @JsonIgnore
  private final CdkResourceType type = SNS_SUBSCRIPTION;

  @JsonProperty("Properties")
  private SnsSnsSubscriptionProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
