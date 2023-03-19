package com.coffeebeans.springnativeawslambda.infra.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import software.amazon.awscdk.CfnDeletionPolicy;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Role {

  @JsonIgnore
  private final CdkResourceType type = CdkResourceType.ROLE;

  @JsonProperty("Properties")
  private RoleProperties properties;

  @JsonProperty("UpdateReplacePolicy")
  private CfnDeletionPolicy updateReplacePolicy;

  @JsonProperty("DeletionPolicy")
  private CfnDeletionPolicy deletionPolicy;

  @JsonProperty("Type")
  public String getType() {
    return type.getValue();
  }
}
