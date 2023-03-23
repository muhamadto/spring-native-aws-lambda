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

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiMethod(@JsonProperty("Properties") RestApiMethodProperties properties){

@JsonProperty("Type")
static String type=APIGATEWAY_RESTAPI_METHOD.getValue();

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiMethodProperties(@JsonProperty("HttpMethod") Matcher httpMethod,
@JsonProperty("RestApiId") ResourceReference restApiId,
@JsonProperty("Integration") RestApiMethodIntegration integration,
@JsonProperty("AuthorizationType") Matcher authorizationType,
@JsonIgnore IntrinsicFunctionBasedArn rootResourceId,
@JsonIgnore ResourceReference nonRootResourceId){

@JsonProperty("ResourceId")
public Object getResourceId(){
final boolean allNull=ObjectUtils.allNull(rootResourceId,nonRootResourceId);

    if(allNull){
    throw new IllegalStateException("Either rootResourceId or nonRootResourceId must be set");

    }
    return rootResourceId!=null?rootResourceId:nonRootResourceId;
    }
    }

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record RestApiMethodIntegration(@JsonProperty("IntegrationHttpMethod") Matcher integrationHttpMethod,
@JsonProperty("Type") Matcher type,
@JsonProperty("Uri") IntrinsicFunctionBasedArn uri){

    }
    }
