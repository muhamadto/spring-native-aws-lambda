package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.springnativeawslambda.infra.SpringNativeAwsLambdaStack.LAMBDA_FUNCTION_ID;
import static com.coffeebeans.springnativeawslambda.infra.StackUtils.createStack;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_KEY_ENV;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.createTags;
import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import java.util.Objects;
import lombok.NoArgsConstructor;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;

@NoArgsConstructor(access = PRIVATE)
public final class Application {

  private static final String DEV_STACK_NAME = "spring-native-aws-lambda-function-dev-stack";
  private static final String PRD_STACK_NAME = "spring-native-aws-lambda-function-prd-stack";
  private static final String ENVIRONMENT_NAME_DEV = "dev";
  private static final String ENVIRONMENT_NAME_PRD = "prd";
  private static final String LAMBDA_CODE_PATH = LAMBDA_FUNCTION_ID + "/target/spring-native-aws-lambda-function-native-zip.zip";
  private static final String QUALIFIER = "cbcore";
  private static final String FILE_ASSETS_BUCKET_NAME = "cbcore-cdk-bucket";

  public static void main(final String... args) {
    final App app = new App();

    final String env = System.getenv(TAG_KEY_ENV);
    checkNotNull(env, "'env' environment variable is required");
    final Map<String, String> tags = createTags(env, TAG_VALUE_COST_CENTRE);

    switch (env) {
      case ENVIRONMENT_NAME_DEV -> createStack(app, DEV_STACK_NAME, LAMBDA_CODE_PATH, QUALIFIER, FILE_ASSETS_BUCKET_NAME, env);
      case ENVIRONMENT_NAME_PRD -> createStack(app, PRD_STACK_NAME, LAMBDA_CODE_PATH, QUALIFIER, FILE_ASSETS_BUCKET_NAME, env);
      default -> throw new IllegalArgumentException("Environment variable " + TAG_KEY_ENV + " is not set to a valid value. Set it to '[dev|prd]'");
    }

    tags.entrySet().stream()
        .filter(tag -> Objects.nonNull(tag.getValue()))
        .forEach(tag -> Tags.of(app).add(tag.getKey(), tag.getValue()));

    app.synth();
  }
}