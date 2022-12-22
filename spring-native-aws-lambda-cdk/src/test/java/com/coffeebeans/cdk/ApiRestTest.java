package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.cdk.resource.CdkResourceType.APIGATEWAY_RESTAPI;
import static com.coffeebeans.cdk.resource.CdkResourceType.ROLE;
import static com.coffeebeans.cdk.resource.PolicyStatementEffect.ALLOW;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.CfnDeletionPolicy.RETAIN;
import static software.amazon.awscdk.assertions.Match.exact;

import com.coffeebeans.cdk.resource.IntrinsicFunctionBasedArn;
import com.coffeebeans.cdk.resource.PartitionedArn;
import com.coffeebeans.cdk.resource.PolicyDocument;
import com.coffeebeans.cdk.resource.PolicyPrincipal;
import com.coffeebeans.cdk.resource.PolicyStatement;
import com.coffeebeans.cdk.resource.RestApi;
import com.coffeebeans.cdk.resource.RestApiProperties;
import com.coffeebeans.cdk.resource.Role;
import com.coffeebeans.cdk.resource.RoleProperties;
import com.coffeebeans.cdk.resource.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ApiRestTest extends TemplateSupport {

  public static final String TEST = "test";

  @Test
  void should_have_rest_api() throws JsonProcessingException {
    final RestApiProperties restApiProperties = RestApiProperties.builder()
        .name(exact("spring-native-aws-lambda-function-rest-api"))
        .tag(Tag.builder().key("COST_CENTRE").value(exact(TAG_VALUE_COST_CENTRE)).build())
        .tag(Tag.builder().key("ENV").value(exact(TEST)).build())
        .build();

    final RestApi restApi = RestApi.builder()
        .properties(restApiProperties)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(APIGATEWAY_RESTAPI.getValue(), restApi);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void should_have_role_with_AmazonAPIGatewayPushToCloudWatchLogs_policy_for_rest_api_to_push_logs_to_cloud_watch() {
    final PolicyStatement policyStatement = PolicyStatement.builder()
        .principal(PolicyPrincipal.builder().service(exact("apigateway.amazonaws.com")).build())
        .effect(ALLOW)
        .action("sts:AssumeRole")
        .build();

    final PolicyDocument assumeRolePolicyDocument = PolicyDocument.builder()
        .statement(policyStatement)
        .build();

    final PartitionedArn partitionedArn = PartitionedArn.builder()
        .partition("AWS::Partition")
        .service("iam")
        .resourceType("policy")
        .resourceId("service-role/AmazonAPIGatewayPushToCloudWatchLogs")
        .build();

    final IntrinsicFunctionBasedArn managedPolicyArn = IntrinsicFunctionBasedArn.builder()
        .joinArn(EMPTY)
        .joinArn(partitionedArn.asList())
        .build();

    final RoleProperties roleProperties = RoleProperties.builder()
        .managedPolicyArn(managedPolicyArn)
        .assumeRolePolicyDocument(assumeRolePolicyDocument)
        .build();

    final Role role = Role.builder()
        .properties(roleProperties)
        .deletionPolicy(RETAIN)
        .updateReplacePolicy(RETAIN)
        .build();

    final Map<String, Map<String, Object>> actual = template.findResources(ROLE.getValue(), role);

    assertThat(actual)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }
}