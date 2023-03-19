package com.coffeebeans.springnativeawslambda.infra.resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CdkResourceType {
  APIGATEWAY_RESTAPI("AWS::ApiGateway::RestApi"),
  APIGATEWAY_RESTAPI_METHOD("AWS::ApiGateway::Method"),
  APIGATEWAY_RESTAPI_RESOURCE("AWS::ApiGateway::Resource"),
  APIGATEWAY_RESTAPI_ACCOUNT("AWS::ApiGateway::Account"),
  APIGATEWAY_RESTAPI_DEPLOYMENT("AWS::ApiGateway::Deployment"),
  APIGATEWAY_RESTAPI_STAGE("AWS::ApiGateway::Stage"),
  LAMBDA_FUNCTION("AWS::Lambda::Function"),
  LAMBDA_EVENT_INVOKE_CONFIG("AWS::Lambda::EventInvokeConfig"),
  LAMBDA_PERMISSION("AWS::Lambda::Permission"),
  POLICY("AWS::IAM::Policy"),
  ROLE("AWS::IAM::Role"),
  SNS("AWS::SNS::Topic"),
  SNS_SUBSCRIPTION("AWS::SNS::Subscription"),
  SQS("AWS::SQS::Queue"),
  QUEUE_POLICY("AWS::SQS::QueuePolicy");

  private String value;
}
