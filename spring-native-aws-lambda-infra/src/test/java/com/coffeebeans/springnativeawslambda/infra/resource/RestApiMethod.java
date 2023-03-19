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
public class RestApiMethod {

  @JsonIgnore
  private final CdkResourceType type = CdkResourceType.APIGATEWAY_RESTAPI_METHOD;

  @JsonProperty("Properties")
  private RestApiMethodProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
