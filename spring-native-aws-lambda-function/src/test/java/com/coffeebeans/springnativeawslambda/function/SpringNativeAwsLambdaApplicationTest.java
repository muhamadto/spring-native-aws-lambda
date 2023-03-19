package com.coffeebeans.springnativeawslambda.function;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void should_return_200() throws JsonProcessingException {
    final APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
        .withBody("{\"name\":\"Coffeebeans\"}");

    final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody("{\"name\":\"Coffeebeans\",\"saved\":true}");

    assertThat(aws.exchange(objectMapper.writeValueAsString(request)).getPayload())
        .isEqualTo(objectMapper.writeValueAsString(response));

  }
}