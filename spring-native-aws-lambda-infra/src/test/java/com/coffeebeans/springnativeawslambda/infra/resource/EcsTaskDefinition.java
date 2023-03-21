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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.ECS_TASK_DEFINITION;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import software.amazon.awscdk.assertions.Matcher;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record EcsTaskDefinition(
    @JsonProperty("ContainerDefinitions") List<Container> containerDefinitions,
    @JsonProperty("Cpu") String cpu,
    @JsonProperty("ExecutionRoleArn") ExecutionRoleArn executionRoleArn,
    @JsonProperty("Family") String family,
    @JsonProperty("Memory") String memory,
    @JsonProperty("NetworkMode") String networkMode,
    @JsonProperty("RequiresCompatibilities") List<Matcher> requiresCompatibilities,
    @JsonProperty("TaskRoleArn") TaskRoleArn taskRoleArn
) {

  @JsonProperty("Type")
  static String type = ECS_TASK_DEFINITION.getValue();

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record Container(
      @JsonProperty("DockerLabels") DockerLabels dockerLabels,
      @JsonProperty("Environment") List<EnvironmentVariable> environment,
      @JsonProperty("Essential") Boolean essential,
      @JsonProperty("Image") ContainerImage image,
      @JsonProperty("LogConfiguration") LogConfiguration logConfiguration,
      @JsonProperty("Memory") Matcher memory,
      @JsonProperty("MemoryReservation") Matcher memoryReservation,
      @JsonProperty("Name") Matcher name,
      @JsonProperty("PortMappings") List<PortMapping> portMappings,
      @JsonProperty("Secrets") List<Secret> secrets
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DockerLabels(
      @JsonProperty("Env") Matcher env,
      @JsonProperty("Type") Matcher type,
      @JsonProperty("Version") Version version
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record EnvironmentVariable(
      @JsonProperty("Name") Matcher name,
      @JsonProperty("Value") Matcher value
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record ContainerImage(
      @JsonProperty("Fn::Join") List<Object> fnJoin
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LogConfiguration(
      @JsonProperty("LogDriver") Matcher logDriver
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PortMapping(
      @JsonProperty("ContainerPort") Integer containerPort,
      @JsonProperty("HostPort") Integer hostPort,
      @JsonProperty("Protocol") Matcher protocol
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record Secret(
      @JsonProperty("Name") Matcher name,
      @JsonProperty("ValueFrom") Matcher valueFrom
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record ExecutionRoleArn(
      @JsonProperty("Fn::GetAtt") List<Object> fnGetAtt
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record TaskRoleArn(
      @JsonProperty("Fn::GetAtt") List<Object> fnGetAtt
  ) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record Version(
      @JsonProperty("major") int major,
      @JsonProperty("minor") int minor,
      @JsonProperty("patch") int patch,
      @JsonProperty("preRelease") String preRelease,
      @JsonProperty("buildMetadata") String buildMetadata
  ) {

  }
}