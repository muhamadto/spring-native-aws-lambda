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

package com.coffeebeans.springnativeawslambda.infra.assertion;

import static com.coffeebeans.springnativeawslambda.infra.resource.CdkResourceType.BUCKET;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

import com.coffeebeans.springnativeawslambda.infra.resource.Bucket;
import com.coffeebeans.springnativeawslambda.infra.resource.Bucket.BucketProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.MapUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;


public class BucketAssert extends AbstractAssert<BucketAssert, Template> {

  public BucketAssert(final Template actual) {
    super(actual, BucketAssert.class);
  }

  public static BucketAssert assertThat(final Template actual) {
    return new BucketAssert(actual);
  }

  public BucketAssert hasBucket(final Bucket expected) {
    actual.hasResource(BUCKET.getValue(), expected);

    return this;
  }

  public BucketAssert hasBucketWithName(final String bucketName) {
    isNotNull();
    final Map<String, Map<String, Object>> buckets = actual.findResources(BUCKET.getValue());

    if (MapUtils.isEmpty(buckets)) {
      failWithMessage("No bucket found");
    }

    final Optional<Bucket> bucket = buckets.keySet().stream()
        .map((value) -> new ObjectMapper().convertValue(value, Bucket.class))
        .filter((b) -> equalsIgnoreCase(bucketName, b.bucketProperties().bucketName()))
        .findFirst();

    if (bucket.isPresent()) {
      failWithMessage("No bucket found");
    }

    final BucketProperties bucketProperties = bucket.get().bucketProperties();

    Assertions
        .assertThat(bucketProperties.bucketName())
        .isEqualTo(bucketName);
    return this;
  }

//  public BucketAssert hasLifecycleConfiguration(final LifecycleConfiguration lifecycleConfig) {
//    isNotNull();
//    final Bucket actualBucket = (Bucket) actual.getResource(BUCKET.getValue());
//    final Properties properties = actualBucket.properties();
//    assertThat(properties.lifecycleConfiguration()).isEqualTo(lifecycleConfig);
//    return this;
//  }
//
//  public BucketAssert hasRuleWithPrefix(final String prefix) {
//    isNotNull();
//    final Bucket actualBucket = (Bucket) actual.getResource(BUCKET.getValue());
//    final LifecycleConfiguration lifecycleConfig = actualBucket.properties().lifecycleConfiguration();
//    assertThat(lifecycleConfig.rules())
//        .filteredOn(rule -> rule.prefix().equals(prefix))
//        .hasSize(1);
//    return this;
//  }
//
//  public BucketAssert hasNoRuleWithPrefix(final String prefix) {
//    isNotNull();
//    final Bucket actualBucket = (Bucket) actual.findResources(BUCKET.getValue());
//    final LifecycleConfiguration lifecycleConfig = actualBucket.properties().lifecycleConfiguration();
//    assertThat(lifecycleConfig.rules())
//        .filteredOn(rule -> rule.prefix().equals(prefix))
//        .isEmpty();
//    return this;
//  }
}
