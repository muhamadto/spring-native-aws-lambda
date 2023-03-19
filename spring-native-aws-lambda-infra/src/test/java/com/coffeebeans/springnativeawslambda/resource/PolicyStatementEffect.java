package com.coffeebeans.springnativeawslambda.function.infra.resource;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PolicyStatementEffect {
  ALLOW("Allow"),
  DENY("Deny");

  private String value;

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
