package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import software.amazon.awscdk.assertions.Matcher;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TopicProperties {

  @JsonProperty("ContentBasedDeduplication")
  private Boolean contentBasedDeduplication;

  @JsonProperty("FifoTopic")
  private Boolean fifoTopic;

  @JsonProperty("TopicName")
  private Matcher topicName;

  @Singular
  @JsonProperty("Tags")
  private List<Tag> tags;
}
