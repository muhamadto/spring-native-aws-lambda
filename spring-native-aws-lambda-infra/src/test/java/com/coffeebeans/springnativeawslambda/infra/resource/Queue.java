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

/**
 * This record represents SQS Queue.
 *
 * <pre>
 *       final IntrinsicFunctionArn deadLetterTargetArn = IntrinsicFunctionArn.builder()
 *         .attributesArn(stringLikeRegexp("identifier(.*)"))
 *         .attributesArn("Arn")
 *         .build();
 *
 *     final QueueRedrivePolicy queueRedrivePolicy = QueueRedrivePolicy.builder()
 *         .deadLetterTargetArn(deadLetterTargetArn)
 *         .maxReceiveCount(3)
 *         .build();
 *
 *     final QueueProperties queueProperties = QueueProperties.builder()
 *         .contentBasedDeduplication(true)
 *         .fifoQueue(true)
 *         .deduplicationScope("messageGroup")
 *         .queueName("queue-name.fifo")
 *         .redrivePolicy(queueRedrivePolicy)
 *         .tag(Tag.builder().key("COST_CENTRE").value(TAG_VALUE_COST_CENTRE).build())
 *         .tag(Tag.builder().key("ENV").value(TEST).build())
 *         .build();
 *
 *     final Queue queue = Queue.builder()
 *         .properties(queueProperties)
 *         .updateReplacePolicy("Delete")
 *         .deletionPolicy("Delete")
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Queue(@JsonProperty("Properties") QueueProperties properties,
                    @JsonProperty("UpdateReplacePolicy") String updateReplacePolicy,
                    @JsonProperty("DeletionPolicy") String deletionPolicy) {

  @JsonProperty("Type")
  static String type = QUEUE.getValue();

  /**
   * This record represents SQS Queue properties.
   *
   * <pre>
   *       final IntrinsicFunctionArn deadLetterTargetArn = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final QueueRedrivePolicy queueRedrivePolicy = QueueRedrivePolicy.builder()
   *         .deadLetterTargetArn(deadLetterTargetArn)
   *         .maxReceiveCount(3)
   *         .build();
   *
   *     final QueueProperties queueProperties = QueueProperties.builder()
   *         .contentBasedDeduplication(true)
   *         .fifoQueue(true)
   *         .deduplicationScope("messageGroup")
   *         .queueName("queue-name.fifo")
   *         .redrivePolicy(queueRedrivePolicy)
   *         .tag(Tag.builder().key("COST_CENTRE").value(TAG_VALUE_COST_CENTRE).build())
   *         .tag(Tag.builder().key("ENV").value(TEST).build())
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record QueueProperties(@JsonProperty("ContentBasedDeduplication") Boolean contentBasedDeduplication,
                                @JsonProperty("FifoQueue") Boolean fifoQueue,
                                @JsonProperty("QueueName") String queueName,
                                @JsonProperty("DeduplicationScope") String deduplicationScope,
                                @JsonProperty("RedrivePolicy") QueueRedrivePolicy redrivePolicy,
                                @Singular @JsonProperty("Tags") List<Tag> tags) {

  }

  /**
   * This record represents SQS Queue redrive policy.
   *
   * <pre>
   *       final IntrinsicFunctionArn deadLetterTargetArn = IntrinsicFunctionArn.builder()
   *         .attributesArn(stringLikeRegexp("identifier(.*)"))
   *         .attributesArn("Arn")
   *         .build();
   *
   *     final QueueRedrivePolicy queueRedrivePolicy = QueueRedrivePolicy.builder()
   *         .deadLetterTargetArn(deadLetterTargetArn)
   *         .maxReceiveCount(3)
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record QueueRedrivePolicy(@JsonProperty("deadLetterTargetArn") IntrinsicFunctionArn deadLetterTargetArn,
                                   @JsonProperty("maxReceiveCount") Integer maxReceiveCount) {

  }
}
