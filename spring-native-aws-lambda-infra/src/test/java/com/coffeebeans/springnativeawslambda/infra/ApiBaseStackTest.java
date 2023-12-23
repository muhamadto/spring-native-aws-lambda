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

package com.coffeebeans.springnativeawslambda.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.services.ec2.Vpc.Builder.create;
import static software.amazon.awscdk.services.iam.Role.fromRoleArn;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2023;
import static software.amazon.awscdk.services.sns.Topic.fromTopicArn;
import static software.amazon.awscdk.services.sqs.DeduplicationScope.MESSAGE_GROUP;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;

class ApiBaseStackTest {

  private static final String ENV = "test";

  private ApiBaseStack apiBaseStack;

  private final App app = new App();

  private Path lambdaCodePath;

  @TempDir
  private static Path TEMP_DIR;

  @BeforeEach
  void setUp() throws IOException {

    lambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(TEMP_DIR);

    this.apiBaseStack = StackUtils.createStack(app, "test-stack", lambdaCodePath.toString(), ENV,
        "test-cdk-bucket", ENV);
  }

  @Test
  void should_create_and_return_queue() {
    final String queueId = "test-queue";

    final Queue actual = this.apiBaseStack.createQueue(queueId);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .hasFieldOrProperty("deadLetterQueue")
        .extracting("fifo")
        .isEqualTo(false);
  }

  @Test
  void should_create_and_return_fifo_queue() {
    final String queueId = "test-queue";

    final Queue actual = this.apiBaseStack.createFifoQueue(queueId, true, MESSAGE_GROUP);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .hasFieldOrProperty("deadLetterQueue")
        .extracting("fifo")
        .isEqualTo(true);

  }

  @Test
  void should_create_and_return_dead_letter_queue() {
    final String deadLetterQueueId = "test-dead-letter-queue";

    final DeadLetterQueue actual = this.apiBaseStack.createDeadLetterQueue(deadLetterQueueId);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("maxReceiveCount")
        .hasFieldOrProperty("queue");

    assertThat(actual.getMaxReceiveCount())
        .isEqualTo(3);

    assertThat(actual.getQueue())
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .extracting("fifo")
        .isEqualTo(false);
  }

  @Test
  void should_create_and_return_fifo_dead_letter_queue() {
    final String deadLetterQueueId = "test-fifo-dead-letter-queue";

    final DeadLetterQueue actual = this.apiBaseStack.createFifoDeadLetterQueue(deadLetterQueueId,
        true, MESSAGE_GROUP);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("maxReceiveCount")
        .hasFieldOrProperty("queue");

    assertThat(actual.getMaxReceiveCount())
        .isEqualTo(3);

    assertThat(actual.getQueue())
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .extracting("fifo")
        .isEqualTo(true);
  }

  @Test
  void should_create_and_return_topic() {
    final String topicId = "test-topic";

    final Topic actual = this.apiBaseStack.createTopic(topicId);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("topicName")
        .hasFieldOrProperty("topicArn")
        .extracting("fifo")
        .isEqualTo(false);
  }

  @Test
  void should_create_and_return_fifo_topic() {
    final String topicId = "test-topic";

    final Topic actual = this.apiBaseStack.createFifoTopic(topicId, true, true);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("topicName")
        .hasFieldOrProperty("topicArn")
        .extracting("fifo")
        .isEqualTo(true);
  }

  @Test
  void should_create_and_return_lambda_function() {
    final Vpc vpc = create(this.apiBaseStack, "test-vpc").build();

    final Function actual = this.apiBaseStack.createFunction(vpc,
        "test-function",
        "com.coffeebeans.springnativeawslambda.infra.lambda.CustomRuntime2Function::handleRequest",
        Code.fromAsset(this.lambdaCodePath.toString()),
        fromTopicArn(this.apiBaseStack, "success-topic", "arn:aws:sns:us-east-1:***:success-topic"),
        fromRoleArn(this.apiBaseStack, "test-role", "arn:aws:iam::***:role/test-role"),
        Map.of("Account", "***"));

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("functionArn")
        .hasFieldOrProperty("role")
        .hasFieldOrProperty("functionName")
        .hasFieldOrProperty("functionArn")
        .hasFieldOrProperty("env")
        .hasFieldOrProperty("architecture")
        .hasFieldOrProperty("runtime")
        .hasFieldOrProperty("timeout");

    assertThat(actual.getRuntime())
        .isEqualTo(PROVIDED_AL2023);
  }

  @Test
  void should_create_and_return_lambda_rest_api() {
    final Vpc vpc = create(this.apiBaseStack, "test-vpc").build();

    final Function function = this.apiBaseStack.createFunction(vpc,
        "test-function",
        "com.coffeebeans.springnativeawslambda.infra.lambda.CustomRuntime2Function::handleRequest",
        Code.fromAsset(this.lambdaCodePath.toString()),
        fromTopicArn(this.apiBaseStack, "success-topic", "arn:aws:sns:us-east-1:***:success-topic"),
        fromRoleArn(this.apiBaseStack, "test-role", "arn:aws:iam::***:role/test-role"),
        Map.of("Account", "***"));

    final LambdaRestApi actual = this.apiBaseStack.createLambdaRestApi("test", "rest-api", "name",
        "POST", function, false);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("deploymentStage")
        .hasFieldOrProperty("env")
        .hasFieldOrProperty("restApiName")
        .hasFieldOrProperty("root")
        .hasFieldOrProperty("url")
        .hasFieldOrProperty("restApiRootResourceId")
        .hasFieldOrProperty("restApiId")
        .hasFieldOrProperty("methods");
  }
}