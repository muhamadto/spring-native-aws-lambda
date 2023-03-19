package com.coffeebeans.springnativeawslambda.function.infra;

import static com.coffeebeans.springnativeawslambda.function.infra.TagUtils.TAG_KEY_ENV;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;
import static software.amazon.awscdk.services.sqs.DeduplicationScope.MESSAGE_GROUP;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class SpringNativeAwsLambdaStack extends ApiBaseStack {

  static final String LAMBDA_FUNCTION_ID = "spring-native-aws-lambda-function";
  private static final String REST_API_ID = LAMBDA_FUNCTION_ID + "-rest-api";
  private static final String SNS_SUCCESS_TOPIC_ID = LAMBDA_FUNCTION_ID + "-success-topic";
  private static final String SNS_FAILURE_TOPIC_ID = LAMBDA_FUNCTION_ID + "-failure-topic";
  private static final String SQS_SUCCESS_QUEUE_ID = LAMBDA_FUNCTION_ID + "-success-queue";
  private static final String SQS_FAILURE_QUEUE_ID = LAMBDA_FUNCTION_ID + "-failure-queue";
  private static final String LAMBDA_HANDLER = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest";
  private static final String ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";

  public SpringNativeAwsLambdaStack(@NotNull final Construct scope,
      @NotBlank final String id,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String stage,
      @NotNull final StackProps props) {
    super(scope, id, props);

    final Queue successQueue = createFifoQueue(SQS_SUCCESS_QUEUE_ID, true, MESSAGE_GROUP);
    final SqsSubscription successQueueSubscription = createSqsSubscription(successQueue);
    final Topic successTopic = createFifoTopic(SNS_SUCCESS_TOPIC_ID, true, true);
    successTopic.addSubscription(successQueueSubscription);

    final Queue failureQueue = createQueue(SQS_FAILURE_QUEUE_ID);
    final SqsSubscription failureQueueSubscription = createSqsSubscription(failureQueue);
    final Topic failureTopic = createTopic(SNS_FAILURE_TOPIC_ID);
    failureTopic.addSubscription(failureQueueSubscription);

    final Map<String, String> environment = Map.of(ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE, stage, TAG_KEY_ENV, stage);

    final Role role = Role.Builder.create(this, LAMBDA_FUNCTION_ID + "-role")
        .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
        .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")))
        .build();

    final AssetCode assetCode = fromAsset(lambdaCodePath);

    final Function function = createFunction(LAMBDA_FUNCTION_ID, LAMBDA_HANDLER, assetCode, successTopic, failureTopic, role, environment);

    createLambdaRestApi(stage, REST_API_ID, "name", "POST", function, true);
  }
}