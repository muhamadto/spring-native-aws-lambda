package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_KEY_COST_CENTRE;
import static com.coffeebeans.cdk.TagUtils.TAG_KEY_ENV;
import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.google.common.base.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.App;
import software.amazon.awscdk.DefaultStackSynthesizer;
import software.amazon.awscdk.StackProps;

@NoArgsConstructor(access = PRIVATE)
public final class Application {

  private static final String DEV_STACK_NAME = "spring-native-aws-lambda-function-dev-stack";
  private static final String PRD_STACK_NAME = "spring-native-aws-lambda-function-prd-stack";
  private static final String ENVIRONMENT_NAME_DEV = "dev";
  private static final String ENVIRONMENT_NAME_PRD = "prd";
  private static final DefaultStackSynthesizer DEFAULT_STACK_SYNTHESIZER = DefaultStackSynthesizer.Builder.create()
      .qualifier("cbcore")
      .fileAssetsBucketName("cbcore-cdk-bucket")
      .build();

  public static void main(final String... args) {
    final App app = new App();

    final String env = System.getenv(TAG_KEY_ENV);
    checkNotNull(env, "'env' environment variable is required");
    switch (env) {
      case ENVIRONMENT_NAME_DEV -> createStack(app, ENVIRONMENT_NAME_DEV, DEV_STACK_NAME);
      case ENVIRONMENT_NAME_PRD -> createStack(app, ENVIRONMENT_NAME_PRD, PRD_STACK_NAME);
      default -> throw new IllegalArgumentException("Environment variable " + TAG_KEY_ENV + " is not set to a valid value. Set it to '[dev|prd]'");
    }

    app.synth();
  }

  @NotNull
  private static Map<String, String> createTags(final String env) {
    return Map.of(TAG_KEY_COST_CENTRE, TAG_VALUE_COST_CENTRE, TAG_KEY_ENV, env);
  }

  @NotNull
  private static StackProps getStackProps(final String stackName, final Map<String, String> prodTags) {
    return StackProps.builder()
        .tags(prodTags)
        .synthesizer(DEFAULT_STACK_SYNTHESIZER)
        .stackName(stackName)
        .build();
  }

  static SpringNativeAwsLambdaStack createStack(final App app, final String env, final String stackName) {
    final Map<String, String> devTags = createTags(env);
    final StackProps stackProps = getStackProps(stackName, devTags);
    return new SpringNativeAwsLambdaStack(app, stackName, stackProps);
  }
}