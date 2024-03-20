package com.coffeebeans.springnativeawslambda.infra.lambda;

import com.coffeebeans.springnativeawslambda.infra.TestLambdaUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Size;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.codeguruprofiler.ProfilingGroup;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.kms.Key;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.lambda.VersionOptions;
import software.amazon.awscdk.services.lambda.destinations.SnsDestination;
import software.amazon.awscdk.services.lambda.eventsources.ApiEventSource;
import software.amazon.awscdk.services.logs.RetentionDays;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static software.amazon.awscdk.services.lambda.Architecture.ARM_64;
import static software.amazon.awscdk.services.lambda.CodeSigningConfig.fromCodeSigningConfigArn;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2023;
import static software.amazon.awscdk.services.sns.Topic.fromTopicArn;

class CustomRuntime2023FunctionTest {

    public static final @NotNull Duration DEFAULT_TIMEOUT = Duration.seconds(10);
    @TempDir
    private static Path TEMP_DIR;
    private Stack stack;
    private String id;
    private SnsDestination onFailure;
    private SnsDestination onSuccess;
    private Path lambdaCodePath;
    private FunctionProps.Builder functionPropsBuilder;

    @BeforeEach
    void setUp() throws IOException {
        lambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(TEMP_DIR);

        stack = new Stack(new App(), "test-stack");
        id = "test-function";

        onFailure = new SnsDestination(
                fromTopicArn(stack, "failure-topic", "arn:aws:sns:us-east-1:***:failure-topic"));
        onSuccess = new SnsDestination(
                fromTopicArn(stack, "success-topic", "arn:aws:sns:us-east-1:***:success-topic"));

        functionPropsBuilder = FunctionProps.builder()
                .runtime(PROVIDED_AL2023)
                .maxEventAge(Duration.seconds(514))
                .onFailure(onFailure)
                .onSuccess(onSuccess)
                .retryAttempts(2)
                .allowPublicSubnet(false)
                .architecture(ARM_64)
                .codeSigningConfig(fromCodeSigningConfigArn(stack, "test-code-signing-config",
                        "arn:aws:lambda:us-east-1:***:code-signing-config:***"))
                .currentVersionOptions(VersionOptions.builder().build())
                .deadLetterQueue(null)
                .deadLetterQueueEnabled(false)
                .deadLetterTopic(null)
                .description("test function")
                .environment(Map.of("Account", "***"))
                .environmentEncryption(
                        Key.fromKeyArn(stack, "test-key", "arn:aws:kms:us-east-1:***:key/***"))
                .ephemeralStorageSize(Size.gibibytes(1))
                .events(List.of(new ApiEventSource("POST", "/test")))
                .filesystem(null)
                .functionName("test-function")
                .initialPolicy(List.of(PolicyStatement.Builder.create().build()))
                .insightsVersion(null)
                .layers(Collections.emptyList())
                .logRetention(RetentionDays.FIVE_DAYS)
                .logRetentionRole(
                        Role.fromRoleArn(stack, "test-log-role", "arn:aws:iam::***:role/test-log-role"))
                .memorySize(512)
                .profiling(false)
                .profilingGroup(ProfilingGroup.fromProfilingGroupName(stack, "test-profiling-group",
                        "test-profiling-group"))
                .reservedConcurrentExecutions(2)
                .role(Role.fromRoleArn(stack, "test-role", "arn:aws:iam::***:role/test-role"))
                .securityGroups(
                        List.of(SecurityGroup.fromSecurityGroupId(stack, "test-security-group", "sg-***")))
                .tracing(Tracing.ACTIVE)
                .vpc(Vpc.Builder.create(stack, "test-vpc").build())
                .vpcSubnets(SubnetSelection.builder().build())
                .code(Code.fromAsset(lambdaCodePath.toString()))
                .handler(
                        "com.coffeebeans.springnativeawslambda.infra.lambda.CustomRuntime2Function::handleRequest");
    }

    @Test
    void should_create_and_return_function() {
        final FunctionProps functionProps = functionPropsBuilder.build();
        final Function actual = new CustomRuntime2023Function(stack, id, functionProps).getFunction();

        assertThat(actual)
                .isNotNull();

        assertThat(actual.getRuntime())
                .isEqualTo(PROVIDED_AL2023);

        assertThat(actual.getTimeout()
                .toSeconds())
                .isEqualTo(DEFAULT_TIMEOUT.toSeconds());
    }

    @Test
    void should_not_override_provided_al2023() {
        final FunctionProps functionProps = functionPropsBuilder
                .runtime(PROVIDED_AL2)
                .build();
        final Function actual = new CustomRuntime2023Function(stack, id, functionProps).getFunction();

        assertThat(actual)
                .isNotNull();

        assertThat(actual.getRuntime())
                .isEqualTo(PROVIDED_AL2023);
    }

    @Test
    void should_create_and_return_function_when_with_non_default_timeout() {
        final Duration timeout = Duration.seconds(3);
        final FunctionProps functionProps = functionPropsBuilder
                .timeout(timeout)
                .build();
        final Function actual = new CustomRuntime2023Function(stack, id, functionProps).getFunction();

        assertThat(actual)
                .isNotNull();

        assertThat(actual.getTimeout()
                .toSeconds())
                .isEqualTo(3);
    }

    @Test
    void should_throw_exception_when_function_handler_is_empty_string() {
        final FunctionProps functionProps = functionPropsBuilder
                .handler(StringUtils.EMPTY)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message", "'handler' is required");
    }

    @Test
    void should_throw_exception_when_function_description_is_missing() {
        final FunctionProps functionProps = functionPropsBuilder
                .description(null)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message", "'description' is required");
    }

    @Test
    void should_throw_exception_when_function_description_is_empty_string() {
        final FunctionProps functionProps = functionPropsBuilder
                .description(StringUtils.EMPTY)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message", "'description' is required");
    }

    @Test
    void should_throw_exception_when_function_environment_is_empty_map() {
        final FunctionProps functionProps = functionPropsBuilder
                .environment(Collections.emptyMap())
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message", "'environment' is required");
    }

    @Test
    void should_throw_exception_when_function_environment_is_null() {
        final FunctionProps functionProps = functionPropsBuilder
                .environment(null)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message", "'environment' is required");
    }

    @Test
    void should_throw_exception_when_function_memory_size_is_less_than_128() {
        final FunctionProps functionProps = functionPropsBuilder
                .memorySize(120)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message",
                        "'memorySize' must be between 128 and 3008 (inclusive)");
    }

    @Test
    void should_throw_exception_when_function_memory_size_is_larger_than_3008() {
        final FunctionProps functionProps = functionPropsBuilder
                .memorySize(3500)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message",
                        "'memorySize' must be between 128 and 3008 (inclusive)");
    }

    @Test
    void should_throw_exception_when_function_memory_size_is_less_than_zero() {
        final FunctionProps functionProps = functionPropsBuilder
                .retryAttempts(-1)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message",
                        "'retryAttempts' must be between 0 and 2 (inclusive)");
    }

    @Test
    void should_throw_exception_when_function_memory_size_is_larger_than_two() {
        final FunctionProps functionProps = functionPropsBuilder
                .retryAttempts(3)
                .build();

        assertThatThrownBy(() -> new CustomRuntime2023Function(stack, id, functionProps))
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class)
                .hasFieldOrPropertyWithValue("message",
                        "'retryAttempts' must be between 0 and 2 (inclusive)");
    }
}