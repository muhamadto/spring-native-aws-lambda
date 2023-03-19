package com.coffeebeans.springnativeawslambda.function.infra.resource;

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
public class QueueProperties {

  @JsonProperty("ContentBasedDeduplication")
  private Boolean contentBasedDeduplication;

  @JsonProperty("FifoQueue")
  private Boolean fifoQueue;

  @JsonProperty("QueueName")
  private Matcher queueName;

  @JsonProperty("DeduplicationScope")
  private Matcher deduplicationScope;

  @JsonProperty("RedrivePolicy")
  private QueueRedrivePolicy redrivePolicy;

  @Singular
  @JsonProperty("Tags")
  private List<Tag> tags;
}
