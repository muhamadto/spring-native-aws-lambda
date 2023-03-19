package com.coffeebeans.springnativeawslambda.function.infra;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

  public static final String TAG_KEY_ENV = "ENV";
  public static final String TAG_KEY_COST_CENTRE = "COST_CENTRE";
  public static final String TAG_VALUE_COST_CENTRE = "coffeeBeans-core";

  public static Map<String, String> createTags(@NotBlank final String env, @NotBlank final String costCentre) {
    return Map.of(TAG_KEY_ENV, env, TAG_KEY_COST_CENTRE, costCentre);
  }
}
