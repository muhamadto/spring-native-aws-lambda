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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import software.amazon.awscdk.assertions.Matcher;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EcsTaskDefinition {

    @JsonProperty("ContainerDefinitions")
    private List<Container> containerDefinitions;
    
    @JsonProperty("Cpu")
    private String cpu;
    
    @JsonProperty("ExecutionRoleArn")
    private ExecutionRoleArn executionRoleArn;
    
    @JsonProperty("Family")
    private String family;
    
    @JsonProperty("Memory")
    private String memory;
    
    @JsonProperty("NetworkMode")
    private String networkMode;
    
    @JsonProperty("RequiresCompatibilities")
    private List<Matcher> requiresCompatibilities;
    
    @JsonProperty("TaskRoleArn")
    private TaskRoleArn taskRoleArn;

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Container {

        @JsonProperty("DockerLabels")
        private DockerLabels dockerLabels;

        @JsonProperty("Environment")
        private List<EnvironmentVariable> environment;

        @JsonProperty("Essential")
        private Boolean essential;

        @JsonProperty("Image")
        private ContainerImage image;

        @JsonProperty("LogConfiguration")
        private LogConfiguration logConfiguration;

        @JsonProperty("Memory")
        private Matcher memory;

        @JsonProperty("MemoryReservation")
        private Matcher memoryReservation;

        @JsonProperty("Name")
        private Matcher name;

        @JsonProperty("PortMappings")
        private List<PortMapping> portMappings;

        @JsonProperty("Secrets")
        private List<Secret> secrets;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class DockerLabels {

        @JsonProperty("Env")
        private Matcher env;

        @JsonProperty("Type")
        private Matcher type;

        @JsonProperty("Version")
        private Version version;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class EnvironmentVariable {

        @JsonProperty("Name")
        private Matcher name;

        @JsonProperty("Value")
        private Matcher value;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ContainerImage {

        @JsonProperty("Fn::Join")
        private List<Object> fnJoin;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class LogConfiguration {

        @JsonProperty("LogDriver")
        private Matcher logDriver;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class PortMapping {

        @JsonProperty("ContainerPort")
        private Integer containerPort;

        @JsonProperty("HostPort")
        private Integer hostPort;

        @JsonProperty("Protocol")
        private Matcher protocol;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Secret {

        @JsonProperty("Name")
        private Matcher name;

        @JsonProperty("ValueFrom")
        private Matcher valueFrom;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ExecutionRoleArn {

        @JsonProperty("Fn::GetAtt")
        private List<Object> fnGetAtt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class TaskRoleArn {

        @JsonProperty("Fn::GetAtt")
        private List<Object> fnGetAtt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Version {

        @JsonProperty("major")
        private int major;
    
        @JsonProperty("minor")
        private int minor;
    
        @JsonProperty("patch")
        private int patch;

        @JsonProperty("preRelease")
        private String preRelease;

        @JsonProperty("buildMetadata")
        private String buildMetadata;

    }
}