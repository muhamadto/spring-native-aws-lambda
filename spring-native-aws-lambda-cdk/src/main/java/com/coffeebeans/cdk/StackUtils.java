package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.cdk.TagUtils.createTags;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.App;
import software.amazon.awscdk.DefaultStackSynthesizer;
import software.amazon.awscdk.StackProps;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StackUtils {

  @NotNull
  public static SpringNativeAwsLambdaStack createStack(
      @NotNull final App app,
      @NotBlank final String env,
      @NotBlank final String stackName,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName) {
    final Map<String, String> devTags = createTags(env, TAG_VALUE_COST_CENTRE);
    final StackProps stackProps = createStackProps(stackName, devTags, qualifier, fileAssetsBucketName);
    return new SpringNativeAwsLambdaStack(app, stackName, stackProps, lambdaCodePath);
  }

  @NotNull
  public static StackProps createStackProps(@NotBlank final String stackName,
      @NotEmpty final Map<String, String> prodTags,
      @NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName) {
    return StackProps.builder()
        .tags(prodTags)
        .synthesizer(createDefaultStackSynthesizer(qualifier, fileAssetsBucketName))
        .stackName(stackName)
        .build();
  }

  @NotNull
  public static DefaultStackSynthesizer createDefaultStackSynthesizer(@NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName) {
    return DefaultStackSynthesizer.Builder.create()
        .qualifier(qualifier)
        .fileAssetsBucketName(fileAssetsBucketName)
        .build();
  }
}
