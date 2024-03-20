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

package com.coffeebeans.springnativeawslambda.infra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Map;

import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_APPLICATION_NAME;
import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_COST_CENTRE;
import static com.coffeebeans.springnativeawslambda.infra.Constants.KEY_ENV;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

  public static Map<String, String> createTags(@NotBlank final String env,
      @NotBlank final CostCentre costCentre,
      @NotBlank final String applicationName) {
      return Map.of(
          KEY_ENV, env,
          KEY_COST_CENTRE, costCentre.getValueAsString(),
          KEY_APPLICATION_NAME, applicationName
      );
  }
}