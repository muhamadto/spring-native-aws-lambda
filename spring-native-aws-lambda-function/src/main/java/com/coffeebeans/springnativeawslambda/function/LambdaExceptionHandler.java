package com.coffeebeans.springnativeawslambda.function;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LambdaExceptionHandler {

  @ExceptionHandler(ClassCastException.class)
  public APIGatewayProxyResponseEvent handleException(
      final ClassCastException e,
      final APIGatewayProxyRequestEvent request) {
    log.error("Error processing request: {}", e.getMessage());
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(500)
        .withHeaders(Map.of("X-Requested-Id", request.getRequestContext().getRequestId()))
        .withBody("{\n"
            + "          \"message\": \"Internal Server Error\",\n"
            + "          \"errorCode\": \"SERVER_ERROR\n"
            + "        }");
  }

  @ExceptionHandler(JsonProcessingException.class)
  public APIGatewayProxyResponseEvent handleException(
      final JsonProcessingException e,
      final APIGatewayProxyRequestEvent request) {
    log.error("Error processing request: {}", e.getMessage());
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(500)
        .withHeaders(Map.of("X-Requested-Id", request.getRequestContext().getRequestId()))
        .withBody("{\n"
            + "          \"message\": \"Internal Server Error\",\n"
            + "          \"errorCode\": \"SERVER_ERROR\n"
            + "        }");
  }
}
