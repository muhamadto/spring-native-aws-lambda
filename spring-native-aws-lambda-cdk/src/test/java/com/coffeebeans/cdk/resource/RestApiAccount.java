package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI_ACCOUNT;

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
public class RestApiAccount {

  @JsonIgnore
  private final CdkResourceType type = APIGATEWAY_RESTAPI_ACCOUNT;

  @JsonProperty("Properties")
  private RestApiAccountProperties properties;
  @Singular
  @JsonProperty("DependsOn")
  private List<Matcher> dependencies;

  @JsonProperty("UpdateReplacePolicy")
  private Matcher updateReplacePolicy;

  @JsonProperty("DeletionPolicy")
  private Matcher deletionPolicy;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
