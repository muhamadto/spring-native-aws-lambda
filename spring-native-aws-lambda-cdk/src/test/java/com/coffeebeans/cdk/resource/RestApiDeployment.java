package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_DEPLOYMENT;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestApiDeployment {

  @JsonIgnore
  private final CdkResourceType type = APIGATEWAY_RESTAPI_DEPLOYMENT;

  @JsonProperty("Properties")
  private RestApiDeploymentProperties properties;

  @Singular
  @JsonProperty("DependsOn")
  private List<Matcher> dependencies;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
