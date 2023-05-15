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

import com.coffeebeans.springnativeawslambda.infra.lambda.CustomRuntime2Function;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.sns.ITopic;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.DeduplicationScope;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class ApiBaseStack extends Stack {

  private static final int LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS = 3;
  private static final int LAMBDA_FUNCTION_MEMORY_SIZE = 512;
  private static final int LAMBDA_FUNCTION_RETRY_ATTEMPTS = 2;
  private static final String FIFO_SUFFIX = ".fifo";
  private static final String DEAD_LETTER_QUEUE_SUFFIX = "-dlq";

  public ApiBaseStack(
      @NotNull final Construct scope,
      @NotBlank final String id,
      @NotNull final StackProps props) {
    super(scope, id, props);
  }

  @NotNull
  protected Queue createQueue(@NotBlank final String queueId) {
    final DeadLetterQueue deadLetterQueue = createDeadLetterQueue(
        queueId + DEAD_LETTER_QUEUE_SUFFIX);

    return Queue.Builder.create(this, queueId)
        .queueName(queueId)
        .deadLetterQueue(deadLetterQueue)
        .build();
  }

  @NotNull
  protected Queue createFifoQueue(
      @NotBlank final String queueId,
      final boolean contentBasedDeduplication,
      @NotNull final DeduplicationScope messageGroup) {
    final String fifoQueueId = queueId + FIFO_SUFFIX;
    final String fifoDeadLetterQueueId = queueId + DEAD_LETTER_QUEUE_SUFFIX;

    final DeadLetterQueue deadLetterQueue =
        createFifoDeadLetterQueue(fifoDeadLetterQueueId, contentBasedDeduplication, messageGroup);

    return Queue.Builder.create(this, fifoQueueId)
        .queueName(fifoQueueId)
        .fifo(true)
        .deadLetterQueue(deadLetterQueue)
        .contentBasedDeduplication(contentBasedDeduplication)
        .deduplicationScope(messageGroup)
        .build();
  }

  @NotNull
  protected DeadLetterQueue createDeadLetterQueue(@NotBlank final String deadLetterQueueId) {
    final Queue queue = Queue.Builder.create(this, deadLetterQueueId)
        .queueName(deadLetterQueueId)
        .build();

    return DeadLetterQueue.builder()
        .queue(queue)
        .maxReceiveCount(3)
        .build();
  }

  @NotNull
  protected DeadLetterQueue createFifoDeadLetterQueue(
      @NotBlank final String deadLetterQueueId,
      final boolean contentBasedDeduplication,
      @NotNull final DeduplicationScope messageGroup) {
    final String fifoDeadLetterQueueId = deadLetterQueueId + FIFO_SUFFIX;
    final Queue queue = Queue.Builder.create(this, fifoDeadLetterQueueId)
        .queueName(fifoDeadLetterQueueId)
        .fifo(true)
        .contentBasedDeduplication(contentBasedDeduplication)
        .deduplicationScope(messageGroup)
        .build();

    return DeadLetterQueue.builder()
        .queue(queue)
        .maxReceiveCount(3)
        .build();
  }

  @NotNull
  protected Topic createTopic(
      @NotBlank final String topicId) {

    return Topic.Builder.create(this, topicId)
        .topicName(topicId)
        .build();
  }

  @NotNull
  protected Topic createFifoTopic(
      @NotBlank final String topicId,
      final boolean fifo,
      final boolean contentBasedDeduplication) {
    String fifoTopicId = topicId + FIFO_SUFFIX;

    return Topic.Builder.create(this, fifoTopicId)
        .topicName(fifoTopicId)
        .fifo(fifo)
        .contentBasedDeduplication(contentBasedDeduplication)
        .build();
  }

  @NotNull
  protected static SqsSubscription createSqsSubscription(@NotNull final Queue queue) {
    return SqsSubscription.Builder.create(queue).build();
  }

  @NotNull
  protected Function createFunction(
      @NotBlank final String lambdaId,
      @NotBlank final String handler,
      @NotNull final Code code,
      @NotNull final Topic deadLetterTopic,
      @NotNull Role role,
      @NotEmpty final Map<String, String> environment) {
    return this.createFunction(null,
        lambdaId,
        handler,
        code,
        deadLetterTopic,
        role,
        environment);
  }

  @NotNull
  protected Function createFunction(
      final IVpc vpc,
      @NotBlank final String lambdaId,
      @NotBlank final String handler,
      @NotNull final Code code,
      @NotNull final ITopic deadLetterTopic,
      @NotNull IRole role,
      @NotEmpty final Map<String, String> environment) {
    return CustomRuntime2Function.Builder.create(this, lambdaId)
        .functionName(lambdaId)
        .description("Lambda example with spring native")
        .code(code)
        .handler(handler)
        .role(role)
        .vpc(vpc)
        .environment(environment)
        .deadLetterTopic(deadLetterTopic)
        .timeout(Duration.seconds(LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS))
        .memorySize(LAMBDA_FUNCTION_MEMORY_SIZE)
        .retryAttempts(LAMBDA_FUNCTION_RETRY_ATTEMPTS)
        .build();
  }

  @NotNull
  protected LambdaRestApi createLambdaRestApi(
      @NotBlank final String stageName,
      @NotBlank final String restApiId,
      @NotNull final String resourceName,
      @NotNull final String httpMethod,
      @NotNull final Function function,
      final boolean proxy) {

    // point to the lambda
    final LambdaRestApi lambdaRestApi = LambdaRestApi.Builder.create(this, restApiId)
        .restApiName(restApiId)
        .handler(function)
        .proxy(proxy)
        .deployOptions(StageOptions.builder().stageName(stageName).build())
        .build();

    // get root resource to add methods
    final Resource resource = lambdaRestApi.getRoot().addResource(resourceName);
    resource.addMethod(StringUtils.toRootUpperCase(httpMethod));

    return lambdaRestApi;
  }
}