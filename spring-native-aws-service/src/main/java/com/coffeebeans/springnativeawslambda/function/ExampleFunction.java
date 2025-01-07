/*
 *   Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.coffeebeans.springnativeawslambda.function;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.coffeebeans.springnativeawslambda.model.Secret;
import com.coffeebeans.springnativeawslambda.repository.SecretRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MethodInvocationException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.MethodNotAllowedException;

@Component
@Slf4j
@Validated
public class ExampleFunction implements
    Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private final ObjectMapper objectMapper;

  private SecretRepository secretRepository;

  public ExampleFunction(
      @NotNull final SecretRepository secretRepository,
      @NotNull final ObjectMapper objectMapper
  ) {
    this.objectMapper = objectMapper;
    this.secretRepository = secretRepository;

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
    log.info("Received a request...'");

    final String httpMethod = proxyRequestEvent.getHttpMethod() == null ? "" : proxyRequestEvent.getHttpMethod().toUpperCase();
    return switch (httpMethod) {
      case "POST" -> doPost(proxyRequestEvent);
      case "GET" -> doGet(proxyRequestEvent);
      case null, default -> throw new MethodNotAllowedException(httpMethod, List.of(POST, GET));
    };
  }

  private APIGatewayProxyResponseEvent doPost(final APIGatewayProxyRequestEvent proxyRequestEvent) throws JsonProcessingException {
    log.info("POST: Converting request into a response");

    final Secret secret = objectMapper.readValue(proxyRequestEvent.getBody(), Secret.class);

    this.secretRepository.save(com.coffeebeans.springnativeawslambda.entity.Secret.of(secret));

    log.info("POST: Converted request into a response");

    return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody(objectMapper.writeValueAsString(secret));
  }

  private APIGatewayProxyResponseEvent doGet(final APIGatewayProxyRequestEvent proxyRequestEvent) throws JsonProcessingException {
    log.info("GET: Retrieving response");

    final Map<String, String> pathParameters = proxyRequestEvent.getPathParameters();
    final String id = pathParameters.get("proxy");

    final com.coffeebeans.springnativeawslambda.entity.Secret secretEntity = this.secretRepository.findById(id);

    final Secret secret = Secret.of(secretEntity);

    if (secret == null) {
      return new APIGatewayProxyResponseEvent()
          .withStatusCode(404)
          .withBody("Entity not found");
    }

    log.info("GET: response retrieved successfully");
    return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody(objectMapper.writeValueAsString(secret));
  }
}