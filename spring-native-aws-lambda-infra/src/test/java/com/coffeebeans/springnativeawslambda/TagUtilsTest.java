package com.coffeebeans.springnativeawslambda.function.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.coffeebeans.springnativeawslambda.infra.TagUtils;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TagUtilsTest {

  @Test
  void should_create_and_return_tag_map() {
    // given
    final String env = "test";
    final String costCentre = "coffeeBeans-core";

    // when
    final Map<String, String> tags = TagUtils.createTags(env, costCentre);

    assertThat(tags)
        .containsEntry(TagUtils.TAG_KEY_ENV, env)
        .containsEntry(TagUtils.TAG_KEY_COST_CENTRE, costCentre);
  }
}