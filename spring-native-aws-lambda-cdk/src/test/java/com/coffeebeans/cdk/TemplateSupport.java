package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.Application.createStack;

import java.io.IOException;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;

public abstract class TemplateSupport {

  static final String ENV = "test";
  static Template template;
  private static final String STACK_NAME = "spring-native-aws-lambda-function-test-stack";
  @TempDir
  private static Path TEMP_DIR;

  @SneakyThrows(IOException.class)
  @BeforeAll
  static void initAll() {
    final Path lambdaCodePath = TEMP_DIR.resolve("lambda-package.zip");

    final boolean isCreated = lambdaCodePath.toFile().createNewFile();

    if (!isCreated) {
      throw new IOException("Failed to create lambda package");
    }

    final SpringNativeAwsLambdaStack springNativeAwsLambdaStack = createStack(new App(), ENV, STACK_NAME, lambdaCodePath.toString());

    template = Template.fromStack(springNativeAwsLambdaStack);
  }

  @AfterAll
  static void cleanup() {
    template = null;
  }
}
