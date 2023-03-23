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

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.TOPIC;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

/**
 * This record represents SNS Topic.
 *
 * <pre>
 *       final TopicProperties topicProperties = TopicProperties.builder()
 *         .contentBasedDeduplication(true)
 *         .fifoTopic(true)
 *         .tag(Tag.builder().key("COST_CENTRE").value("test").build())
 *         .tag(Tag.builder().key("ENV").value("test").build())
 *         .topicName("topic-name.fifo")
 *         .build();
 *
 *     final Topic topic = Topic.builder()
 *         .properties(topicProperties)
 *         .build();
 * </pre>
 *
 * @author Muhammad Hamadto
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Topic(@JsonProperty("Properties") TopicProperties properties) {

  @JsonProperty("Type")
  static String type = TOPIC.getValue();

  /**
   * This record represents SNS Topic properties.
   *
   * <pre>
   *       final TopicProperties topicProperties = TopicProperties.builder()
   *         .contentBasedDeduplication(true)
   *         .fifoTopic(true)
   *         .tag(Tag.builder().key("COST_CENTRE").value("test").build())
   *         .tag(Tag.builder().key("ENV").value("test").build())
   *         .topicName("topic-name.fifo")
   *         .build();
   * </pre>
   *
   * @author Muhammad Hamadto
   */
  @Builder
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public record TopicProperties(@JsonProperty("ContentBasedDeduplication") Boolean contentBasedDeduplication,
                                @JsonProperty("FifoTopic") Boolean fifoTopic,
                                @JsonProperty("TopicName") String topicName,
                                @Singular @JsonProperty("Tags") List<Tag> tags) {

  }
}

