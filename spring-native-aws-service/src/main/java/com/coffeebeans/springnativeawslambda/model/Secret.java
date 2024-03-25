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

package com.coffeebeans.springnativeawslambda.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Secret {
  @NotBlank
  @JsonProperty(access = Access.READ_ONLY)
  private String id;

  @NotBlank
  private String env;

  @NotBlank
  private String costCentre;

  @NotBlank
  private String applicationName;

  @NotEmpty
  private Map<String, String> items;

  public String getId() {
    final String env = this.env.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
    final String costCentre = this.costCentre.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
    final String applicationName = this.applicationName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

    return "%s-%s-%s".formatted(env, costCentre, applicationName);
  }

  public static Secret of(final com.coffeebeans.springnativeawslambda.entity.Secret secretEntity) {
    final String env = secretEntity.getEnv();
    final String costCentre = secretEntity.getCostCentre();
    final String applicationName = secretEntity.getApplicationName();
    return Secret.builder()
        .env(env)
        .costCentre(costCentre)
        .applicationName(applicationName)
        .items(secretEntity.getItems())
        .id(secretEntity.getPartitionKey())
        .build();
  }
}
