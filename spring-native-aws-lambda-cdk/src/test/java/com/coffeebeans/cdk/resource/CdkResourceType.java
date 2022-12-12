package com.coffeebeans.cdk.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CdkResourceType {
  LAMBDA_FUNCTION("AWS::Lambda::Function"),
  POLICY("AWS::IAM::Policy");

  private String value;
}
