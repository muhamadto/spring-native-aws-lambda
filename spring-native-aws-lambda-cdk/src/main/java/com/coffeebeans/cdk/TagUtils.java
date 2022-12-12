package com.coffeebeans.cdk;

import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Tags;
import software.constructs.Construct;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagUtils {

  public static final String TAG_KEY_ENV = "ENV";
  public static final String TAG_KEY_COST_CENTRE = "COST_CENTRE";
  public static final String TAG_VALUE_COST_CENTRE = "coffeebeans-core";

  @NotNull
  public static Map<String, String> createTags(@NotBlank final String env, @NotBlank final String costCenter) {
    return Map.of(TAG_KEY_COST_CENTRE, TAG_VALUE_COST_CENTRE, TAG_KEY_ENV, env);
  }

  public static void addTags(@NotNull final Construct construct, @NotEmpty final Map<String, String> tags) {
    tags.entrySet().stream()
        .filter(entry -> Objects.nonNull(entry.getValue()))
        .forEach(entry -> addTag(construct, entry.getKey(), entry.getValue()));
  }

  public static void addTag(@NotNull final Construct construct, @NotBlank final String key, @NotBlank final String value) {
    Tags.of(construct).add(key, value);
  }
}
