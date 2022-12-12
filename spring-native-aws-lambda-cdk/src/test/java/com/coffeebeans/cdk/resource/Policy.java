package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.POLICY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({
    "Type",
    "Properties"
})
public class Policy {

  @JsonIgnore
  private final CdkResourceType type = POLICY;

  @JsonProperty("Properties")
  private PolicyProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}

