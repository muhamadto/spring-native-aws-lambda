package com.coffeebeans.springnativeawslambda;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.function.adapter.test.aws.AWSCustomRuntime;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"spring.main.web-application-type=servlet"})
@ContextConfiguration(classes = {AWSCustomRuntime.class, SpringNativeAwsLambdaApplication.class})
@TestPropertySource(properties = {"_HANDLER=exampleFunction"})
class SpringNativeAwsLambdaApplicationTest {

  @Autowired
  private AWSCustomRuntime aws;

  @Test
  void testWithCustomRuntime() {
    assertThat(aws.exchange("{\"name\": \"CoffeeBeans\"}").getPayload()).isEqualTo("{\"name\":\"CoffeeBeans\",\"saved\":true}");
  }

}