package com.coffeebeans.springnativeawslambda.function.infra.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CloudFormationIntrinsicFunction {
  JOIN("Fn::Join"),
  SUBSTITUTES("Fn::Sub");

  private String value;

}
