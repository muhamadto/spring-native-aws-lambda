package com.coffeebeans.springnativeawslambda.entity;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Secret implements Serializable {

  private String env;

  private String costCentre;

  private String applicationName;

  private String partitionKey;

  private Map<String, String> items;

  public static Secret of(final com.coffeebeans.springnativeawslambda.model.Secret secretModel) {
    final String env = secretModel.getEnv();
    final String costCentre = secretModel.getCostCentre();
    final String applicationName = secretModel.getApplicationName();
    return Secret.builder()
        .env(env)
        .costCentre(costCentre)
        .applicationName(applicationName)
        .items(secretModel.getItems())
        .partitionKey(secretModel.getId())
        .build();
  }
}
