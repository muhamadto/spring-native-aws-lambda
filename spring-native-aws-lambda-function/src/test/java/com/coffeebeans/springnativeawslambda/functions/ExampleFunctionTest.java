package com.coffeebeans.springnativeawslambda.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

class ExampleFunctionTest {

  private ExampleFunction exampleFunction;

  @Spy
  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    exampleFunction = new ExampleFunction(objectMapper);
  }

  @Test
  void should_return_APIGatewayProxyResponseEvent() {
    final String requestBody = "{\"name\":\"Coffeebeans\"}";
    final String responseBody = "{\"name\":\"Coffeebeans\",\"saved\":true}";

    final APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
        .withBody(requestBody);

    final APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody(responseBody);

    final APIGatewayProxyResponseEvent actual = exampleFunction.apply(request);

    assertThat(actual)
        .isEqualTo(apiGatewayProxyResponseEvent);
  }

  @Test
  void should_throw_JsonProcessingException() {

    final APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
        .withBody("Coffeebeans");

    assertThatThrownBy(() -> exampleFunction.apply(request))
        .isInstanceOf(JsonProcessingException.class);
  }
}