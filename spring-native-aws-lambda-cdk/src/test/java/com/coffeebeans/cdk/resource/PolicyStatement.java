package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PolicyStatement {

  @JsonIgnore
  private PolicyStatementEffect effect;

  @Singular
  @JsonProperty("Action")
  @JsonFormat(with = Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
  private List<String> actions;

  @Singular
  @JsonProperty("Resource")
  @JsonFormat(with = Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
  private List<ResourceReference> resources;

  @JsonProperty("Principal")
  private PolicyPrincipal principal;

  @JsonProperty("Effect")
  public String getEffect() {
    return effect.getValue();
  }
}
