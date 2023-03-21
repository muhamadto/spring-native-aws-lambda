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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.QUEUE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Queue(@JsonProperty("Properties") QueueProperties properties,
                    @JsonProperty("UpdateReplacePolicy") Matcher updateReplacePolicy,
                    @JsonProperty("DeletionPolicy") Matcher deletionPolicy) {

  @JsonProperty("Type")
  static String type = QUEUE.getValue();

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record QueueProperties(@JsonProperty("ContentBasedDeduplication") Boolean contentBasedDeduplication,
                                @JsonProperty("FifoQueue") Boolean fifoQueue,
                                @JsonProperty("QueueName") Matcher queueName,
                                @JsonProperty("DeduplicationScope") Matcher deduplicationScope,
                                @JsonProperty("RedrivePolicy") QueueRedrivePolicy redrivePolicy,
                                @Singular @JsonProperty("Tags") List<Tag> tags) {

  }

  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record QueueRedrivePolicy(@JsonProperty("deadLetterTargetArn") IntrinsicFunctionBasedArn deadLetterTargetArn,
                                   @JsonProperty("maxReceiveCount") Integer maxReceiveCount) {

  }
}
