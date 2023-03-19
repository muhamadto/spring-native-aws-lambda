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
public class RestApiStageProperties {

  @JsonProperty("RestApiId")
  protected ResourceReference restApiId;

  @JsonProperty("DeploymentId")
  protected ResourceReference deploymentId;

  @JsonProperty("StageName")
  private Matcher stageName;

  @Singular
  @JsonProperty("Tags")
  private List<Tag> tags;
}
