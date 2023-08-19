package com.coffeebeans.springnativeawslambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
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

  @ExceptionHandler(InvalidDefinitionException.class)
  public APIGatewayProxyResponseEvent handleException(
      final InvalidDefinitionException e,
      final APIGatewayProxyRequestEvent request) {
    log.error("Error processing request: {}", e.getMessage());
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(400)
        .withHeaders(Map.of("X-Requested-Id", request.getRequestContext().getRequestId()))
        .withBody("{\n"
            + "          \"message\": \"Request body is Invalid\",\n"
            + "          \"errorCode\": \"INVALID_INPUT\n"
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
