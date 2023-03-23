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
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

/**
 * This record represents Task Definition
 * <pre>
 *     final IntrinsicFunctionArn taskRoleArn = IntrinsicFunctionArn.builder()
 *       .attributesArn(stringLikeRegexp("EcsTaskFargateTaskDefTaskRole"))
 *       .attributesArn("Arn")
 *       .build();
 *
 *   final IntrinsicFunctionArn taskDefExecutionRole = IntrinsicFunctionArn.builder()
 *       .attributesArn(stringLikeRegexp("EcsTaskFargateTaskDefExecutionRole"))
 *       .attributesArn("Arn")
 *       .build();
 *
 *   final IntrinsicFunctionArn containerImage = IntrinsicFunctionArn.builder()
 *       .joinArn("")
 *       .joinArn("**********.dkr.ecr.region.")
 *       .joinArn(ResourceReference.builder().reference(exact("AWS::suffix")).build())
 *       .joinArn("imageuri:tag")
 *       .build();
 *
 *   final DockerLabels dockerLabels = DockerLabels.builder()
 *       .env("test")
 *       .type("app-name")
 *       .version(Version.builder().major(12).minor(1).patch(0).build())
 *       .build();
 * *
 *     final PortMapping portMapping = PortMapping.builder()
 *         .containerPort(8080)
 *         .build();
 *
 *     final LogConfiguration logConfiguration = LogConfiguration.builder()
 *         .logDriver("awsfirelens")
 *         .build();
 *
 *     final Secret secret = Secret.builder()
 *         .name(exact("name"))
 *         .valueFrom(stringLikeRegexp("arn:aws:secretsmanager:region:******:secret:/team/topSecret"))
 *         .build();
 *
 *     final EnvironmentVariable environmentVariable = EnvironmentVariable.builder()
 *         .name("env")
 *         .value("test")
 *         .build();
 *
 *     final ContainerDefinition containerDefinitions = ContainerDefinition.builder()
 *         .dockerLabels(dockerLabels)
 *         .environmentVariable(environmentVariable)
 *         .essential(true)
 *         .containerImage(containerImage)
 *         .logConfiguration(logConfiguration)
 *         .memory(512)
 *         .memoryReservation(1024)
 *         .portMapping(portMapping)
 *         .secret(secret)
 *         .name("task-def-name")
 *         .build();
 *
 *   final EcsTaskDefinition ecsTaskDefinition = EcsTaskDefinition.builder()
 *       .containerDefinitions(containerDefinitions)
 *       .cpu(10)
 *       .memory(512)
 *       .networkMode("awsvpc")
 *       .executionRoleArn(taskDefExecutionRole)
 *       .family("team-env-app")
 *       .requiresCompatibility(exact("FARGATE"))
 *       .taskRoleArn(taskRoleArn)
 *       .build();
 *  </pre>
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record EcsTaskDefinition(@JsonProperty("ContainerDefinitions") List<ContainerDefinition> containerDefinitions,
                                @JsonProperty("Cpu") Integer cpu,
                                @JsonProperty("ExecutionRoleArn") IntrinsicFunctionArn executionRoleArn,
                                @JsonProperty("Family") String family,
                                @JsonProperty("Memory") Integer memory,
                                @JsonProperty("NetworkMode") String networkMode,
                                @Singular @JsonProperty("RequiresCompatibilities") List<Matcher> requiresCompatibilities,
                                @JsonProperty("TaskRoleArn") IntrinsicFunctionArn taskRoleArn) {

  @JsonProperty("Type")
  static String type = ECS_TASK_DEFINITION.getValue();

  /**
   * This record represents Container Definition
   * <pre>
   *   final IntrinsicFunctionArn containerImage = IntrinsicFunctionArn.builder()
   *       .joinArn("")
   *       .joinArn("**********.dkr.ecr.region.")
   *       .joinArn(ResourceReference.builder().reference(exact("AWS::suffix")).build())
   *       .joinArn("imageuri:tag")
   *       .build();
   *
   *   final DockerLabels dockerLabels = DockerLabels.builder()
   *       .env("test")
   *       .type("app-name")
   *       .version(Version.builder().major(12).minor(1).patch(0).build())
   *       .build();
   *
   *     final PortMapping portMapping = PortMapping.builder()
   *         .containerPort(8080)
   *         .build();
   *
   *     final LogConfiguration logConfiguration = LogConfiguration.builder()
   *         .logDriver("awsfirelens")
   *         .build();
   *
   *     final Secret secret = Secret.builder()
   *         .name(exact("name"))
   *         .valueFrom(stringLikeRegexp("arn:aws:secretsmanager:region:******:secret:/team/topSecret"))
   *         .build();
   *
   *     final EnvironmentVariable environmentVariable = EnvironmentVariable.builder()
   *         .name("env")
   *         .value("test")
   *         .build();
   *
   *     containerDefinitions = ContainerDefinition.builder()
   *         .dockerLabels(dockerLabels)
   *         .environmentVariable(environmentVariable)
   *         .essential(true)
   *         .containerImage(containerImage)
   *         .logConfiguration(logConfiguration)
   *         .memory(512)
   *         .memoryReservation(1024)
   *         .portMapping(portMapping)
   *         .secret(secret)
   *         .name("task-def-name")
   *         .build();
   *  </pre>
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record ContainerDefinition(@JsonProperty("DockerLabels") DockerLabels dockerLabels,
                                    @Singular @JsonProperty("Environment") List<EnvironmentVariable> environmentVariables,
                                    @JsonProperty("Essential") Boolean essential,
                                    @JsonProperty("Image") IntrinsicFunctionArn containerImage,
                                    @JsonProperty("LogConfiguration") LogConfiguration logConfiguration,
                                    @JsonProperty("Memory") Integer memory,
                                    @JsonProperty("MemoryReservation") Integer memoryReservation,
                                    @JsonProperty("Name") String name,
                                    @Singular @JsonProperty("PortMappings") List<PortMapping> portMappings,
                                    @Singular @JsonProperty("Secrets") List<Secret> secrets) {

  }

  /**
   * This record represents Container docker labels
   * <pre>
   *   final DockerLabels dockerLabels = DockerLabels.builder()
   *       .env("test")
   *       .type("app-name")
   *       .version(Version.builder().major(12).minor(1).patch(0).build())
   *       .build();
   * </pre>
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record DockerLabels(@JsonProperty("Env") String env,
                             @JsonProperty("Type") String type,
                             @JsonProperty("Version") Version version) {

  }

  /**
   * This record represents Container log configuration
   * <pre>
   *     final LogConfiguration logConfiguration = LogConfiguration.builder()
   *         .logDriver("awsfirelens")
   *         .build();
   * </pre>
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record LogConfiguration(@JsonProperty("LogDriver") String logDriver) {

  }

  /**
   * This record represents Container port mapping
   * <pre>
   *     final PortMapping portMapping = PortMapping.builder()
   *         .containerPort(8080)
   *         .build();
   * </pre>
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record PortMapping(@JsonProperty("ContainerPort") Integer containerPort,
                            @JsonProperty("HostPort") Integer hostPort,
                            @JsonProperty("Protocol") String protocol) {

  }

  /**
   * This record represents Container docker labels version
   * <pre>
   *   final Version dockerLabelsVersion = Version.builder()
   *       .major(12)
   *       .minor(1)
   *       .patch(0)
   *       .build();
   * </pre>
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record Version(@JsonProperty("major") Integer major,
                        @JsonProperty("minor") Integer minor,
                        @JsonProperty("patch") Integer patch,
                        @JsonProperty("preRelease") String preRelease,
                        @JsonProperty("buildMetadata") String buildMetadata) {

  }
}