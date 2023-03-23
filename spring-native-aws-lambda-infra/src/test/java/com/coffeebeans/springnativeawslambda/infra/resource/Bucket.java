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

/**
 * Creates S3 bucket for AWS CDK stack testing. Here is an example of how to use it:
 * <pre>
 *       final Bucket Bucket = Bucket.builder()
 *         .properties(Bucket.BucketProperties.builder()
 *             .bucketName("my-bucket")
 *             .lifecycleConfiguration(Bucket.LifecycleConfiguration.builder()
 *                 .rules(List.of(Bucket.Rule.builder()
 *                     .expirationInDays(30)
 *                     .status("Enabled")
 *                     .prefix("my-prefix")
 *                     .build()))
 *                 .build())
 *             .build())
 *         .build();
 *    </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Bucket(@JsonProperty("Properties") BucketProperties bucketProperties,
@JsonProperty("UpdateReplacePolicy") Matcher updateReplacePolicy,
@JsonProperty("DeletionPolicy") Matcher deletionPolicy){

@JsonProperty("Type")
static String type=BUCKET.getValue();

/**
 * This record is used to represent the S3 bucket properties.
 * <pre>
 *   BucketProperties bucketProperties = BucketProperties.builder()
 *             .bucketName("my-bucket")
 *             .lifecycleConfiguration(Bucket.LifecycleConfiguration.builder()
 *                 .rules(List.of(Bucket.Rule.builder()
 *                     .expirationInDays(30)
 *                     .status("Enabled")
 *                     .prefix("my-prefix")
 *                     .build()))
 *                 .build())
 *             .build();
 *
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record BucketProperties(@JsonProperty("BucketName") String bucketName,
@JsonProperty("LifecycleConfiguration") LifecycleConfiguration lifecycleConfiguration){

    }

/**
 * This record is used to represent the S3 bucket lifecycle configuration.
 * <pre>
 *   final LifecycleConfiguration lifecycleConfiguration = LifecycleConfiguration.builder()
 *         .rules(List.of(Rule.builder()
 *             .expirationInDays(30)
 *             .status("Enabled")
 *             .prefix("my-prefix")
 *             .build()))
 *         .build();
 *
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LifecycleConfiguration(@JsonProperty("Rules") List<Rule> rules){

    }

/**
 * This record is used to represent the S3 bucket lifecycle configuration rule.
 * <pre>
 *   final Rule rule = Rule.builder()
 *         .expirationInDays(30)
 *         .status("Enabled")
 *         .prefix("my-prefix")
 *         .build();
 *
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Rule(@JsonProperty("ExpirationInDays") int expirationInDays,
@JsonProperty("Status") String status,
@JsonProperty("Prefix") String prefix){

    }
    }