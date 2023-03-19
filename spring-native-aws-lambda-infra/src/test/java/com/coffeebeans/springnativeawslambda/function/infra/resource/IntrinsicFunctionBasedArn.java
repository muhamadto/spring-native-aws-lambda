package com.coffeebeans.springnativeawslambda.function.infra.resource;

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
public class IntrinsicFunctionBasedArn {

  @Singular
  @JsonProperty("Fn::GetAtt")
  public List<Object> attributesArns;

  @Singular
  @JsonProperty("Fn::Join")
  public List<Object> joinArns;
}
