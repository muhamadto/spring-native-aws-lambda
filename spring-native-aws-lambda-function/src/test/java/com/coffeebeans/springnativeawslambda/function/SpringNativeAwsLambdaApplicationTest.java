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