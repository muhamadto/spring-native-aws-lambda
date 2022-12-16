package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.StackUtils.createStack;
import static com.coffeebeans.cdk.TestLambdaUtils.getTestLambdaCodePath;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;

public abstract class TemplateSupport {

  public static final String ENV = "test";
  public static final String TEST_CDK_BUCKET = "test-cdk-bucket";
  public static final String QUALIFIER = "test";

  static Template template;
  private static final String STACK_NAME = "spring-native-aws-lambda-function-test-stack";
  @TempDir
  private static Path TEMP_DIR;

  @BeforeAll
  static void initAll() throws IOException {
    final Path lambdaCodePath = getTestLambdaCodePath(TEMP_DIR);

    final SpringNativeAwsLambdaStack springNativeAwsLambdaStack =
        createStack(new App(), ENV, STACK_NAME, lambdaCodePath.toString(), QUALIFIER, TEST_CDK_BUCKET);
    template = Template.fromStack(springNativeAwsLambdaStack);
  }

  @AfterAll
  static void cleanup() {
    template = null;
  }
}
