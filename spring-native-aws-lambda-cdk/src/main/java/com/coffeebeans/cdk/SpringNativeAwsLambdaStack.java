package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_KEY_ENV;
import static com.google.common.base.Preconditions.checkArgument;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;
import static software.amazon.awscdk.services.sqs.DeduplicationScope.MESSAGE_GROUP;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import org.apache.commons.collections4.MapUtils;
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

  private static final String LAMBDA_FUNCTION_ID = "spring-native-aws-lambda-function";
  private static final String REST_API_ID = "spring-native-aws-lambda-function-rest-api";
  private static final String SNS_SUCCESS_TOPIC_ID = "spring-native-aws-lambda-function-success-topic";
  private static final String SNS_FAILURE_TOPIC_ID = "spring-native-aws-lambda-function-failure-topic";
  private static final String SQS_SUCCESS_QUEUE_ID = "spring-native-aws-lambda-function-success-queue";
  private static final String SQS_FAILURE_QUEUE_ID = "spring-native-aws-lambda-function-failure-queue";
  private static final String LAMBDA_HANDLER = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest";
  private static final AssetCode ASSET_CODE = fromAsset(LAMBDA_FUNCTION_ID + "/target/spring-native-aws-lambda-function-native-zip.zip");
  private static final String ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";

  public SpringNativeAwsLambdaStack(@NotNull final Construct scope, @NotBlank final String id, @NotNull final StackProps props) {
    super(scope, id, props);

    final Map<String, String> tags = props.getTags();
    checkArgument(MapUtils.isNotEmpty(tags), "tags are required");

    final Queue successQueue = createFifoQueue(SQS_SUCCESS_QUEUE_ID, true, MESSAGE_GROUP, tags);
    final SqsSubscription successQueueSubscription = createSqsSubscription(successQueue);
    final Topic successTopic = createFifoTopic(SNS_SUCCESS_TOPIC_ID, successQueueSubscription, true, true, tags);

    final Queue failureQueue = createQueue(SQS_FAILURE_QUEUE_ID, tags);
    final SqsSubscription failureQueueSubscription = createSqsSubscription(failureQueue);
    final Topic failureTopic = createTopic(SNS_FAILURE_TOPIC_ID, failureQueueSubscription, tags);

    final String stage = tags.get(TAG_KEY_ENV);
    final Map<String, String> environment = Map.of(ENVIRONMENT_VARIABLE_SPRING_PROFILES_ACTIVE, stage, TAG_KEY_ENV, stage);

    final Role role = Role.Builder.create(this, LAMBDA_FUNCTION_ID + "-role")
        .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
        .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")))
        .build();

    final Function function =
        createFunction(LAMBDA_FUNCTION_ID, LAMBDA_HANDLER, ASSET_CODE, null, successTopic, failureTopic, role, tags, environment);

    createLambdaRestApi(stage, REST_API_ID, "name", "POST", function, true, tags);
  }
}