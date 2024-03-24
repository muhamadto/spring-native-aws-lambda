package com.coffeebeans.springnativeawslambda.repository;

import com.coffeebeans.springnativeawslambda.entity.Secret;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

@Repository
public class SecretRepository {

  private final DynamoDbEnhancedClient enhancedClient;

  public SecretRepository(@NotNull final DynamoDbEnhancedClient enhancedClient) {
    this.enhancedClient = enhancedClient;
  }

//  public Secret findById(String id) {
//    final Key key = Key.builder().partitionValue(Integer.valueOf(id)).build();
//
//    final Secret secret = this.dynamoDbTemplate.load(key, Secret.class);
//    return Optional.ofNullable(secret)
//        .orElseGet(Secret::new);
//  }

  public void save(final Secret secret) {

    final TableSchema<Secret> tableSchema = StaticTableSchema.builder(Secret.class)
        .newItemSupplier(Secret::new)
        .addAttribute(String.class, a -> a.name("id")
            .getter(Secret::getPartitionKey)
            .setter(Secret::setPartitionKey)
            .tags(StaticAttributeTags.primaryPartitionKey())
        )
        .addAttribute(String.class, a -> a.name("env")
            .getter(Secret::getEnv)
            .setter(Secret::setEnv)
        )
        .addAttribute(String.class, a -> a.name("costCentre")
            .getter(Secret::getCostCentre)
            .setter(Secret::setCostCentre)
        )
        .addAttribute(String.class, a -> a.name("applicationName")
            .getter(Secret::getApplicationName)
            .setter(Secret::setApplicationName)
        )
        .addAttribute(EnhancedType.mapOf(String.class, String.class), a -> a.name("variables")
            .getter(Secret::getItems)
            .setter(Secret::setItems)
        )
        .build();

    final DynamoDbTable<Secret> mappedTable = enhancedClient.table("dev-coffeebeans-secret", tableSchema);

    mappedTable.putItem(secret);
  }
}
