/*
 *   Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.coffeebeans.springnativeawslambda.infra.assertion;

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
  TOPIC("AWS::SNS::Topic"),
  TOPIC_SUBSCRIPTION("AWS::SNS::Subscription"),
  QUEUE("AWS::SQS::Queue"),
  QUEUE_POLICY("AWS::SQS::QueuePolicy"),
  BUCKET("AWS::S3::Bucket"),
  ECS_TASK_DEFINITION("AWS::ECS::TaskDefinition"),
  DYNAMODB_TABLE("AWS::DynamoDB::Table"),
  ;

  private String value;
}
