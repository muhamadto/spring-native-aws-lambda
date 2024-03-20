package com.coffeebeans.springnativeawslambda.infra;

import static com.coffeebeans.cdk.core.util.Constants.AWS_REGION_AP_SOUTHEAST_2;

import com.coffeebeans.cdk.core.AbstractEnvironment;
import com.coffeebeans.cdk.core.type.AWSAccount;
import com.coffeebeans.cdk.core.type.SafeString;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Environment extends AbstractEnvironment {

  public static final Environment COFFEE_BEANS_DEV_111111111111_AP_SOUTHEAST_2;
  public static final Environment COFFEE_BEANS_PRD_111111111111_AP_SOUTHEAST_2;
  public static final Environment COFFEE_BEANS_TEST_111111111111_AP_SOUTHEAST_2;

  static {
    final AWSAccount awsAccount = AWSAccount.of("111111111111");
    final SafeString awsRegion = SafeString.of(AWS_REGION_AP_SOUTHEAST_2);

    final software.amazon.awscdk.Environment awsEnvironment = software.amazon.awscdk.Environment.builder()
        .account(awsAccount.getValue())
        .account(awsRegion.getValue())
        .build();

    COFFEE_BEANS_DEV_111111111111_AP_SOUTHEAST_2 = Environment.builder()
        .awsEnvironment(awsEnvironment)
        .costCentre(CostCentre.COFFEE_BEANS)
        .environmentName(SafeString.of("DEV"))
        .environmentKey(SafeString.of("COFFEE_BEANS_TEST_111111111111_AP_SOUTHEAST_2"))
        .build();

    COFFEE_BEANS_PRD_111111111111_AP_SOUTHEAST_2 = Environment.builder()
        .awsEnvironment(awsEnvironment)
        .costCentre(CostCentre.COFFEE_BEANS)
        .environmentName(SafeString.of("PRD"))
        .environmentKey(SafeString.of("COFFEE_BEANS_PRD_111111111111_AP_SOUTHEAST_2"))
        .build();

    COFFEE_BEANS_TEST_111111111111_AP_SOUTHEAST_2 = Environment.builder()
        .awsEnvironment(awsEnvironment)
        .costCentre(CostCentre.COFFEE_BEANS)
        .environmentName(SafeString.of("TEST"))
        .environmentKey(SafeString.of("COFFEE_BEANS_TEST_111111111111_AP_SOUTHEAST_2"))
        .build();

    registerEnvironment(COFFEE_BEANS_DEV_111111111111_AP_SOUTHEAST_2);
    registerEnvironment(COFFEE_BEANS_PRD_111111111111_AP_SOUTHEAST_2);
    registerEnvironment(COFFEE_BEANS_TEST_111111111111_AP_SOUTHEAST_2);
  }
}
