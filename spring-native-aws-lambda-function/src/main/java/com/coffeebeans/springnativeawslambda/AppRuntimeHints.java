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

package com.coffeebeans.springnativeawslambda;

import com.coffeebeans.springnativeawslambda.model.Request;
import com.coffeebeans.springnativeawslambda.model.Response;
import java.util.ArrayList;
import java.util.List;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class AppRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {
    hints.serialization().registerType(Request.class);
    hints.serialization().registerType(Response.class);
    hints.resources().registerPattern("org/joda/time/tz/*");

    final List<TypeReference> typeReferences = List.of(
        TypeReference.of("org.apache.logging.log4j.message.DefaultFlowMessageFactory"),
        TypeReference.of("org.apache.logging.log4j.message.ParameterizedMessageFactory"),
        TypeReference.of("java.util.ServiceLoader")
    );

        hints.reflection().registerTypes(typeReferences,
            builder -> builder.withMembers(MemberCategory.values()));
  }
}