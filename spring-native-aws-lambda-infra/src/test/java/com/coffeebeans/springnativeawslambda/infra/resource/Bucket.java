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

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Bucket {
    @JsonProperty("Type")
    private final String type = "AWS::S3::Bucket";

    @JsonProperty("Properties")
    private final Properties properties;

    @JsonProperty("UpdateReplacePolicy")
    private final Matcher updateReplacePolicy;

    @JsonProperty("DeletionPolicy")
    private final Matcher deletionPolicy;

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Properties {
        @JsonProperty("BucketName")
        private final String bucketName;

        @JsonProperty("LifecycleConfiguration")
        private final LifecycleConfiguration lifecycleConfiguration;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class LifecycleConfiguration {
        @JsonProperty("Rules")
        private final List<Rule> rules;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Rule {
        @JsonProperty("ExpirationInDays")
        private final int expirationInDays;

        @JsonProperty("Status")
        private final String status;

        @JsonProperty("Prefix")
        private final String prefix;
    }
}