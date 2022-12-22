package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI;

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
public class RestApi {

  @JsonIgnore
  private final CdkResourceType type = APIGATEWAY_RESTAPI;

  @JsonProperty("Properties")
  private RestApiProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
