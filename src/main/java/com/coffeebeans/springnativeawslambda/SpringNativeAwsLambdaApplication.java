package com.coffeebeans.springnativeawslambda;

import com.coffeebeans.springnativeawslambda.functions.ExampleFunction;
import com.coffeebeans.springnativeawslambda.model.Request;
import com.coffeebeans.springnativeawslambda.model.Response;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.nativex.hint.SerializationHint;

@SerializationHint(types = {Request.class, Response.class})
@SpringBootConfiguration
public class SpringNativeAwsLambdaApplication implements ApplicationContextInitializer<GenericApplicationContext> {

  public static void main(final String[] args) {
    FunctionalSpringApplication.run(SpringNativeAwsLambdaApplication.class, args);
  }

  @Override
  public void initialize(final GenericApplicationContext context) {
    context.registerBean("exampleFunction",
        FunctionRegistration.class,
        () -> new FunctionRegistration<>(new ExampleFunction()).type(ExampleFunction.class));
  }
}
