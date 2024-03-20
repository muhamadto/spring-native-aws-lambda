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
  public static SpringNativeAwsFunctionStack createStack(
      @NotNull final App app,
      @NotBlank final String stackName,
      @NotBlank final String lambdaCodePath,
      @NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName,
      @NotEmpty final String stage) {
    final StackProps stackProps = createStackProps(stackName, qualifier, fileAssetsBucketName);
    return new SpringNativeAwsFunctionStack(app, stackName, lambdaCodePath, stage, stackProps);
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
  private static DefaultStackSynthesizer createDefaultStackSynthesizer(
      @NotBlank final String qualifier,
      @NotBlank final String fileAssetsBucketName) {
    return DefaultStackSynthesizer.Builder.create()
        .qualifier(qualifier)
        .fileAssetsBucketName(fileAssetsBucketName)
        .build();
  }
}
