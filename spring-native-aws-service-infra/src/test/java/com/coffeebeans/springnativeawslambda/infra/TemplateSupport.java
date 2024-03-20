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

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.TagUtils.createTags;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Tags;
import software.amazon.awscdk.assertions.Template;

public abstract class TemplateSupport {

  public static final String ENV = "test";
  public static final String TEST_CDK_BUCKET = "test-cdk-bucket";
  public static final String QUALIFIER = "test";
  static Template template;
  private static final String STACK_NAME = "spring-native-aws-function-test-stack";
  @TempDir
  private static Path TEMP_DIR;

  @BeforeAll
  static void initAll() throws IOException {
    final Path lambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(TEMP_DIR);

    final Map<String, String> tags = createTags(ENV, KEY_COST_CENTRE);
    final App app = new App();
    final SpringNativeAwsFunctionStack stack = StackUtils.createStack(app, STACK_NAME,
        lambdaCodePath.toString(), QUALIFIER, TEST_CDK_BUCKET, ENV);

    tags.entrySet().stream()
        .filter(tag -> Objects.nonNull(tag.getValue()))
        .forEach(tag -> Tags.of(app).add(tag.getKey(), tag.getValue()));

    template = Template.fromStack(stack);
  }

  @AfterAll
  static void cleanup() {
    template = null;
  }
}
