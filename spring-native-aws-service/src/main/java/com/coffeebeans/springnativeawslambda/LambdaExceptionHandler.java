package com.coffeebeans.springnativeawslambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

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

  @ExceptionHandler(MethodNotAllowedException.class)
  public APIGatewayProxyResponseEvent handleException(
      final MethodNotAllowedException e,
      final APIGatewayProxyRequestEvent request) {
    log.error("Error processing request: {}", e.getMessage());

    final Set<HttpMethod> supportedMethods = e.getSupportedMethods();

    return new APIGatewayProxyResponseEvent()
        .withStatusCode(405)
        .withHeaders(Map.of("X-Requested-Id", request.getRequestContext().getRequestId()))
        .withBody(
            ("{\n"
                + "\"message\": \"Method not allowed. Supported methods [%s]\",\n"
                + "          \"errorCode\": \"METHOD_NOT_ALLOWED\n"
                + "        }").formatted(
                supportedMethods));
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
