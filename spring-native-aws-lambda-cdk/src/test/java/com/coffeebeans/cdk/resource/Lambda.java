package com.coffeebeans.cdk.resource;

import static com.coffeebeans.cdk.resource.CdkResourceType.LAMBDA_FUNCTION;

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
public class Lambda {

  @JsonIgnore
  private final CdkResourceType type = LAMBDA_FUNCTION;

  @Singular
  @JsonProperty("DependsOn")
  private List<Matcher> dependencies;

  @JsonProperty("Properties")
  private LambdaProperties properties;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
