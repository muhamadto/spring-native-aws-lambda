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

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Policy(@Singular @JsonProperty("DependsOn") List<Matcher> dependencies,
@JsonProperty("Properties") PolicyProperties properties,
@JsonProperty("Type") String typeValue){

@JsonProperty("Type")
static String type=POLICY.getValue();

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PolicyProperties(

@JsonProperty("PolicyName") Matcher policyName,

@JsonProperty("PolicyDocument") PolicyDocument policyDocument,

@Singular @JsonProperty("Roles") List<ResourceReference> roles){

    }

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PolicyDocument(
@Singular @JsonProperty("Statement") List<PolicyStatement> statements){

@JsonProperty("Version")
static String version="2012-10-17";

    }

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PolicyStatement(
@JsonIgnore PolicyStatementEffect effect,
@Singular @JsonProperty("Action") @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) List<String> actions,
@Singular @JsonProperty("Resource") @JsonFormat(with = WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) List<Object> resources,
@JsonProperty("Principal") PolicyPrincipal principal,
@JsonProperty("Condition") PolicyStatementCondition condition
    ){

@JsonProperty("Effect")
public String getEffect(){
    return effect.getValue();
    }

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PolicyStatementEffect {
  ALLOW("Allow"),
  DENY("Deny");

  private String value;

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
  }

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PolicyPrincipal(
@JsonProperty("Service") Matcher service){

    }

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PolicyStatementCondition(
@JsonProperty("ArnEquals") Map<String, ResourceReference> arnEquals){

    }
    }

