package com.coffeebeans.springnativeawslambda.function.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.coffeebeans.springnativeawslambda.function.model.Request;
import com.coffeebeans.springnativeawslambda.function.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Function;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Slf4j
@Validated
public class ExampleFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private final ObjectMapper objectMapper;

  public ExampleFunction(@NotNull final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Lambda function handler that takes a request and returns a response.
   *
   * @param proxyRequestEvent the function argument
   * @return {@link APIGatewayProxyResponseEvent}
   * @throws JsonProcessingException
   */
  @Override
  @SneakyThrows(value = JsonProcessingException.class)
  public APIGatewayProxyResponseEvent apply(final APIGatewayProxyRequestEvent proxyRequestEvent) {
    log.info("Converting request into a response...'");

    final Request request = objectMapper.readValue(proxyRequestEvent.getBody(), Request.class);

    final Response response = Response.builder()
        .name(request.getName())
        .saved(true)
        .build();

    log.info("Converted request into a response.");

    return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody(objectMapper.writeValueAsString(response));
  }
}