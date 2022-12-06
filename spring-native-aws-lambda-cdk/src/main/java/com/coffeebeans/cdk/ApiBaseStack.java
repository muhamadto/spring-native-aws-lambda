package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.addTags;

import com.coffeebeans.cdk.lambda.CustomRuntime2Function;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.destinations.SnsDestination;
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
  protected Queue createQueue(
      @NotBlank final String queueId,
      @NotEmpty final Map<String, String> tags) {
    final DeadLetterQueue deadLetterQueue = createDeadLetterQueue(queueId + DEAD_LETTER_QUEUE_SUFFIX);

    final Queue queue = Queue.Builder.create(this, queueId)
        .queueName(queueId)
        .deadLetterQueue(deadLetterQueue)
        .build();
    addTags(queue, tags);

    return queue;
  }

  @NotNull
  protected Queue createFifoQueue(
      @NotBlank final String queueId,
      final boolean contentBasedDeduplication,
      @NotNull final DeduplicationScope messageGroup,
      @NotEmpty final Map<String, String> tags) {
    final String fifoQueueId = queueId + FIFO_SUFFIX;
    final String fifoDeadLetterQueueId = queueId + DEAD_LETTER_QUEUE_SUFFIX;

    final DeadLetterQueue deadLetterQueue =
        createFifoDeadLetterQueue(fifoDeadLetterQueueId, contentBasedDeduplication, messageGroup);

    final Queue queue = Queue.Builder.create(this, fifoQueueId)
        .queueName(fifoQueueId)
        .fifo(true)
        .deadLetterQueue(deadLetterQueue)
        .contentBasedDeduplication(contentBasedDeduplication)
        .deduplicationScope(messageGroup)
        .build();
    addTags(queue, tags);

    return queue;
  }

  @NotNull
  private DeadLetterQueue createDeadLetterQueue(@NotBlank final String deadLetterQueueId) {
    final Queue queue = Queue.Builder.create(this, deadLetterQueueId)
        .queueName(deadLetterQueueId)
        .build();

    return DeadLetterQueue.builder()
        .queue(queue)
        .maxReceiveCount(3)
        .build();
  }

  @NotNull
  private DeadLetterQueue createFifoDeadLetterQueue(
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
      @NotBlank final String topicId,
      @NotNull final SqsSubscription subscription,
      @NotEmpty final Map<String, String> tags) {

    final Topic topic = Topic.Builder.create(this, topicId)
        .topicName(topicId)
        .build();

    topic.addSubscription(subscription);
    addTags(topic, tags);

    return topic;
  }

  @NotNull
  protected Topic createFifoTopic(
      @NotBlank final String topicId,
      @NotNull final SqsSubscription subscription,
      final boolean fifo,
      final boolean contentBasedDeduplication,
      @NotEmpty final Map<String, String> tags) {
    String fifoTopicId = topicId + FIFO_SUFFIX;
    final Topic topic = Topic.Builder.create(this, fifoTopicId)
        .topicName(fifoTopicId)
        .fifo(fifo)
        .contentBasedDeduplication(contentBasedDeduplication)
        .build();

    topic.addSubscription(subscription);
    addTags(topic, tags);

    return topic;
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
      final IVpc vpc,
      @NotNull final Topic successTopic,
      @NotNull final Topic failureTopic,
      @NotNull Role role,
      @NotEmpty final Map<String, String> tags,
      @NotEmpty final Map<String, String> environment) {
    final CustomRuntime2Function function = CustomRuntime2Function.Builder.create(this, lambdaId)
        .functionName(lambdaId)
        .description("Lambda example with spring native")
        .code(code)
        .handler(handler)
        .role(role)
        .vpc(vpc)
        .environment(environment)
        .onSuccess(new SnsDestination(successTopic))
        .onFailure(new SnsDestination(failureTopic))
        .timeout(Duration.seconds(LAMBDA_FUNCTION_TIMEOUT_IN_SECONDS))
        .memorySize(LAMBDA_FUNCTION_MEMORY_SIZE)
        .retryAttempts(LAMBDA_FUNCTION_RETRY_ATTEMPTS)
        .build();
    addTags(function, tags);

    return function;
  }

  @NotNull
  protected LambdaRestApi createLambdaRestApi(
      @NotBlank final String stageName,
      @NotBlank final String restApiId,
      @NotNull final Function function,
      final boolean proxy,
      @NotEmpty final Map<String, String> tags) {

    // point to the lambda
    final LambdaRestApi lambdaRestApi = LambdaRestApi.Builder.create(this, restApiId)
        .restApiName(restApiId)
        .handler(function)
        .proxy(proxy)
        .deployOptions(StageOptions.builder().stageName(stageName).build())
        .build();

    addTags(lambdaRestApi, tags);

    return lambdaRestApi;
  }
}