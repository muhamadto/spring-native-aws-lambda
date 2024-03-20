package com.coffeebeans.springnativeawslambda.entity;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Secret implements Serializable {

  private String env;

  private String costCentre;

  private String applicationName;

  private String partitionKey;

  private Map<String, String> variables;

  @DynamoDbPartitionKey
  public String getPartitionKey() {
    return env + costCentre + applicationName;
  }

}
