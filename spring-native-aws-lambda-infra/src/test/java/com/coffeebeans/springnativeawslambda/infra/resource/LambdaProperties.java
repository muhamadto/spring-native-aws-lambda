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

package com.coffeebeans.springnativeawslambda.infra.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LambdaProperties {

  @JsonProperty("Code")
  private LambdaCode code;

  @JsonProperty("Role")
  private IntrinsicFunctionBasedArn roleArn;

  @JsonProperty("Environment")
  private LambdaEnvironment environment;

  @Singular
  @JsonProperty("Tags")
  private List<Tag> tags;

  @JsonProperty("Description")
  private Matcher description;

  @JsonProperty("FunctionName")
  private Matcher functionName;

  @JsonProperty("Handler")
  private Matcher handler;

  @JsonProperty("MemorySize")
  private Integer memorySize;

  @JsonProperty("Runtime")
  private Matcher runtime;

  @JsonProperty("Timeout")
  private Integer timeout;
}
