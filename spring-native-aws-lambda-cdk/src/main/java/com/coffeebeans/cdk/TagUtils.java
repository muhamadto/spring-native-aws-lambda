package com.coffeebeans.cdk;

import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import software.amazon.awscdk.Tags;
import software.constructs.Construct;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

  public static final String TAG_KEY_ENV = "ENV";
  public static final String TAG_KEY_COST_CENTRE = "COST_CENTRE";
  public static final String TAG_VALUE_COST_CENTRE = "coffeebeans-core";

  public static void addTags(final Construct construct, final Map<String, String> tags) {
    tags.entrySet().stream()
        .filter(entry -> Objects.nonNull(entry.getValue()))
        .forEach(entry -> addTag(construct, entry.getKey(), entry.getValue()));
  }

  public static void addTags(final Construct construct, final String env, final String costCenter) {
    addTag(construct, TAG_KEY_ENV, env);
    addTag(construct, TAG_KEY_COST_CENTRE, costCenter);
  }

  public static void addTag(final Construct construct, final String key, final String value) {
    Tags.of(construct).add(key, value);
  }
}
