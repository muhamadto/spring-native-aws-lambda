package com.coffeebeans.cdk.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awscdk.assertions.Match;

/**
 * Represents a partitioned ARN for CDK testing To instantiate this class, use the {@link PartitionedArn#builder()} method.
 * <pre>
 *   PartitionedArn.builder()
 *         .partition("AWS::Partition")
 *         .service("iam")
 *         .resourceType("policy")
 *         .resourceId("service-role/AWSLambdaBasicExecutionRole")
 *         .build();
 *   </pre>
 * Note the leading colon in the arnSegment is options you can omit it.
 * <p>
 * To use this class in a CDK resource, use the {@link PartitionedArn#asList()} method.
 * <pre>
 *    partitionedArn.asList();
 *    </pre>
 * This will return
 * <pre>
 *      [
 *        "arn:",
 *        {"Ref":"AWS::Partition"},
 *        ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"]
 *      ]
 *      </pre>
 *
 * @see <a href="https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html">AWS
 * ARNs and Namespaces</a>
 */
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
// TODO fixme
public class PartitionedArn {

  private static final String ARN_SUFFIX = "arn:";
  @NotBlank
  @JsonIgnore
  private String resourceType;

  @NotBlank
  @JsonIgnore
  private String resourceId;

  @NotBlank
  @JsonIgnore
  private String service;

  @NotBlank
  @JsonIgnore
  private String partition;

  @JsonProperty("arn")
  public String getArn() {
    return StringUtils.join(this.asList());
  }

  /**
   * Returns the arn as a list of strings.
   *
   * @return the arn as a list of strings
   */
  @JsonIgnore
  public List<Object> asList() {
    if (StringUtils.isAnyBlank(resourceType, resourceId, service, partition)) {
      throw new IllegalArgumentException(
          "resourceType, resourceId, service, and partition must be set");
    }

    final ResourceReference partitionReference = ResourceReference.builder().reference(Match.exact(partition)).build();

    return Lists.newArrayList(ARN_SUFFIX, partitionReference, ":" + service + "::aws:" + resourceType + "/" + resourceId);
  }
}
