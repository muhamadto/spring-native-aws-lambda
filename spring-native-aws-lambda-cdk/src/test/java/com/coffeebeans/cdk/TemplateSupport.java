package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.Application.createStack;

import org.junit.jupiter.api.BeforeAll;
import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;

public abstract class TemplateSupport {

  static final String ENV = "test";
  static Template template;
  private static final String STACK_NAME = "spring-native-aws-lambda-function-test-stack";


  @BeforeAll
  static void initAll() {
    final SpringNativeAwsLambdaStack springNativeAwsLambdaStack = createStack(new App(), ENV, STACK_NAME);
    template = Template.fromStack(springNativeAwsLambdaStack);
  }
}
