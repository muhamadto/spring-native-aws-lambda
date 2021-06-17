package com.coffeebeans.springnativeawslambda;

import static org.assertj.core.api.Assertions.assertThat;

import com.coffeebeans.springnativeawslambda.model.Request;
import com.coffeebeans.springnativeawslambda.model.Response;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;

@FunctionalSpringBootTest
class SpringNativeAwsLambdaApplicationTests {

  @Autowired
  private FunctionCatalog catalog;

  @Test
  void should_return_greeting() {
    final Response expected = Response.builder().name("CoffeeBeans").saved(true).build();

    final Function<Request, Response> function = catalog.lookup(Function.class, "exampleFunction");

    final Response actual = function.apply(Request.builder().name("CoffeeBeans").build());

    assertThat(actual)
        .isEqualTo(expected);
  }
}
