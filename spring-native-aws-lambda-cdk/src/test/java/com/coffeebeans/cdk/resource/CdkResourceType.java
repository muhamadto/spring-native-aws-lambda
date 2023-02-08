package com.coffeebeans.cdk.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CdkResourceType {
  APIGATEWAY_RESTAPI("AWS::ApiGateway::RestApi"),
  APIGATEWAY_RESTAPI_METHOD("AWS::ApiGateway::Method"),

  APIGATEWAY_RESTAPI_RESOURCE("AWS::ApiGateway::Resource"),

  LAMBDA_FUNCTION("AWS::Lambda::Function"),
  LAMBDA_EVENT_INVOKE_CONFIG("AWS::Lambda::EventInvokeConfig"),
  LAMBDA_PERMISSION("AWS::Lambda::Permission"),
  POLICY("AWS::IAM::Policy"),
  ROLE("AWS::IAM::Role");

  private String value;
}
