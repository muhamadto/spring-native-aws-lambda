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

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.coffeebeans.springnativeawslambda.model.Request;
import com.coffeebeans.springnativeawslambda.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import java.util.function.Function;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Slf4j
@Validated
public class ExampleFunction implements
    Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

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