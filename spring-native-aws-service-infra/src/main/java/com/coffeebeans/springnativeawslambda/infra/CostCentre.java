/*
 *  Licensed to Muhammad Hamadto
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
 */

package com.coffeebeans.springnativeawslambda.infra;

import io.sadpipers.cdk.type.AlphanumericString;
import io.sandpipers.cdk.core.AbstractCostCentre;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CostCentre extends AbstractCostCentre {

  public static final CostCentre COFFEE_BEANS = CostCentre.builder()
      .value(AlphanumericString.of("cbcore"))
      .build();

  static {
    registerCostCentre(COFFEE_BEANS);
  }
}
