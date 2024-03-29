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

import java.util.List;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.coffeebeans.springnativeawslambda.model.Request;
import com.coffeebeans.springnativeawslambda.model.Response;
import org.joda.time.DateTime;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.lang.Nullable;

public class ReflectionRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
    final List<TypeReference> typeReferences = List.of(
        TypeReference.of(DateTime.class),
        TypeReference.of(Response.class),
        TypeReference.of(Request.class),
        TypeReference.of(APIGatewayProxyResponseEvent.class),
        TypeReference.of(APIGatewayProxyRequestEvent.class),
        TypeReference.of(APIGatewayProxyRequestEvent.ProxyRequestContext.class),
        TypeReference.of(APIGatewayProxyRequestEvent.RequestIdentity.class));

    hints.reflection().registerTypes(typeReferences, builder -> builder.withMembers(MemberCategory.values()));
  }
}
