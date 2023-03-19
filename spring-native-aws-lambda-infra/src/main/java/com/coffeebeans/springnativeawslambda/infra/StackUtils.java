package com.coffeebeans.springnativeawslambda.infra;

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
      @NotBlank final String stackName,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName,
      @NotEmpty final String stage) {
    final StackProps stackProps = createStackProps(stackName, qualifier, fileAssetsBucketName);
    return new SpringNativeAwsLambdaStack(app, stackName, lambdaCodePath, stage, stackProps);
  }

  @NotNull
  private static StackProps createStackProps(@NotBlank final String stackName,
      @NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName) {
    return StackProps.builder()
        .synthesizer(createDefaultStackSynthesizer(qualifier, fileAssetsBucketName))
        .stackName(stackName)
        .build();
  }

  @NotNull
  private static DefaultStackSynthesizer createDefaultStackSynthesizer(@NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName) {
    return DefaultStackSynthesizer.Builder.create()
        .qualifier(qualifier)
        .fileAssetsBucketName(fileAssetsBucketName)
        .build();
  }
}
