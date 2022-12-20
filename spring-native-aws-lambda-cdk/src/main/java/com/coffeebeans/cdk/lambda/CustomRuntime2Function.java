package com.coffeebeans.cdk.lambda;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Size;
import software.amazon.awscdk.services.codeguruprofiler.IProfilingGroup;
import software.amazon.awscdk.services.ec2.ISecurityGroup;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.kms.IKey;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.FileSystem;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.ICodeSigningConfig;
import software.amazon.awscdk.services.lambda.IDestination;
import software.amazon.awscdk.services.lambda.IEventSource;
import software.amazon.awscdk.services.lambda.ILayerVersion;
import software.amazon.awscdk.services.lambda.LambdaInsightsVersion;
import software.amazon.awscdk.services.lambda.LogRetentionRetryOptions;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.lambda.VersionOptions;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.sns.ITopic;
import software.amazon.awscdk.services.sqs.IQueue;
import software.constructs.Construct;

/**
 * A lambda function with runtime provided.al2 (custom runtime 2). Creating function with any other runtime, will result in unexpected behaviour.
 */

public class CustomRuntime2Function extends Function {

  private static final int FUNCTION_DEFAULT_TIMEOUT_IN_SECONDS = 10;
  private static final int FUNCTION_DEFAULT_MEMORY_SIZE = 512;
  private static final int FUNCTION_DEFAULT_RETRY_ATTEMPTS = 2;

  /**
   * @param scope This parameter is required.
   * @param id    This parameter is required.
   * @param props This parameter is required.
   */
  private CustomRuntime2Function(@NotNull final Construct scope, @NotNull final String id, @NotNull final FunctionProps props) {
    super(scope, id, props);
  }

  /**
   * The runtime configured for this lambda.
   */
  @Override
  public @NotNull Runtime getRuntime() {
    return PROVIDED_AL2;
  }

  public static final class Builder implements software.amazon.jsii.Builder<CustomRuntime2Function> {

    /**
     * @param scope This parameter is required.
     * @param id    This parameter is required.
     * @return a new instance of {@link CustomRuntime2Function.Builder}.
     */
    public static CustomRuntime2Function.Builder create(final Construct scope, final String id) {
      return new CustomRuntime2Function.Builder(scope, id);
    }

    private final Construct scope;
    private final String id;
    private final FunctionProps.Builder props;

    private Builder(final Construct scope, final String id) {
      this.scope = scope;
      this.id = id;
      this.props = new FunctionProps.Builder();

      this.props.runtime(PROVIDED_AL2);
    }

    /**
     * The maximum age of a request that Lambda sends to a function for processing.
     * <p>
     * Minimum: 60 seconds Maximum: 6 hours
     * <p>
     * Default: Duration.hours(6)
     * <p>
     *
     * @param maxEventAge The maximum age of a request that Lambda sends to a function for processing. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder maxEventAge(final Duration maxEventAge) {
      this.props.maxEventAge(maxEventAge);
      return this;
    }

    /**
     * The destination for failed invocations.
     * <p>
     * Default: - no destination
     * <p>
     *
     * @param onFailure The destination for failed invocations. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder onFailure(final IDestination onFailure) {
      this.props.onFailure(onFailure);
      return this;
    }

    /**
     * The destination for successful invocations.
     * <p>
     * Default: - no destination
     * <p>
     *
     * @param onSuccess The destination for successful invocations. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder onSuccess(final IDestination onSuccess) {
      this.props.onSuccess(onSuccess);
      return this;
    }

    /**
     * The maximum number of times to retry when the function returns an error.
     * <p>
     * Minimum: 0 Maximum: 2
     * <p>
     * Default: 2
     * <p>
     *
     * @param retryAttempts The maximum number of times to retry when the function returns an error. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder retryAttempts(final Number retryAttempts) {
      this.props.retryAttempts(retryAttempts == null ? FUNCTION_DEFAULT_RETRY_ATTEMPTS : retryAttempts);
      return this;
    }

    /**
     * Whether to allow the Lambda to send all network traffic.
     * <p>
     * If set to false, you must individually add traffic rules to allow the Lambda to connect to network targets.
     * <p>
     * Default: true
     * <p>
     *
     * @param allowAllOutbound Whether to allow the Lambda to send all network traffic. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder allowAllOutbound(final Boolean allowAllOutbound) {
      this.props.allowAllOutbound(allowAllOutbound);
      return this;
    }

    /**
     * Lambda Functions in a public subnet can NOT access the internet.
     * <p>
     * Use this property to acknowledge this limitation and still place the function in a public subnet.
     * <p>
     * Default: false
     * <p>
     *
     * @param allowPublicSubnet Lambda Functions in a public subnet can NOT access the internet. This parameter is required.
     * @return {@code this}
     * @see <a
     * href="https://stackoverflow.com/questions/52992085/why-cant-an-aws-lambda-function-inside-a-public-subnet-in-a-vpc-connect-to-the/52994841#52994841">https://stackoverflow.com/questions/52992085/why-cant-an-aws-lambda-function-inside-a-public-subnet-in-a-vpc-connect-to-the/52994841#52994841</a>
     */
    public CustomRuntime2Function.Builder allowPublicSubnet(final Boolean allowPublicSubnet) {
      this.props.allowPublicSubnet(allowPublicSubnet);
      return this;
    }

    /**
     * The system architectures compatible with this lambda function.
     * <p>
     * Default: Architecture.X86_64
     * <p>
     *
     * @param architecture The system architectures compatible with this lambda function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder architecture(final Architecture architecture) {
      this.props.architecture(architecture);
      return this;
    }

    /**
     * Code signing config associated with this function.
     * <p>
     * Default: - Not Sign the Code
     * <p>
     *
     * @param codeSigningConfig Code signing config associated with this function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder codeSigningConfig(final ICodeSigningConfig codeSigningConfig) {
      this.props.codeSigningConfig(codeSigningConfig);
      return this;
    }

    /**
     * Options for the `lambda.Version` resource automatically created by the `fn.currentVersion` method.
     * <p>
     * Default: - default options as described in `VersionOptions`
     * <p>
     *
     * @param currentVersionOptions Options for the `lambda.Version` resource automatically created by the `fn.currentVersion` method. This parameter
     *                              is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder currentVersionOptions(final VersionOptions currentVersionOptions) {
      this.props.currentVersionOptions(currentVersionOptions);
      return this;
    }

    /**
     * The SQS queue to use if DLQ is enabled.
     * <p>
     * If SNS topic is desired, specify <code>deadLetterTopic</code> property instead.
     * <p>
     * Default: - SQS queue with 14 day retention period if `deadLetterQueueEnabled` is `true`
     * <p>
     *
     * @param deadLetterQueue The SQS queue to use if DLQ is enabled. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder deadLetterQueue(final IQueue deadLetterQueue) {
      this.props.deadLetterQueue(deadLetterQueue);
      return this;
    }

    /**
     * Enabled DLQ.
     * <p>
     * If <code>deadLetterQueue</code> is undefined, an SQS queue with default options will be defined for your Function.
     * <p>
     * Default: - false unless `deadLetterQueue` is set, which implies DLQ is enabled.
     * <p>
     *
     * @param deadLetterQueueEnabled Enabled DLQ. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder deadLetterQueueEnabled(final Boolean deadLetterQueueEnabled) {
      this.props.deadLetterQueueEnabled(deadLetterQueueEnabled);
      return this;
    }

    /**
     * The SNS topic to use as a DLQ.
     * <p>
     * Note that if <code>deadLetterQueueEnabled</code> is set to <code>true</code>, an SQS queue will be created rather than an SNS topic. Using an
     * SNS topic as a DLQ requires this property to be set explicitly.
     * <p>
     * Default: - no SNS topic
     * <p>
     *
     * @param deadLetterTopic The SNS topic to use as a DLQ. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder deadLetterTopic(final ITopic deadLetterTopic) {
      this.props.deadLetterTopic(deadLetterTopic);
      return this;
    }

    /**
     * A description of the function.
     * <p>
     * Default: - No description.
     * <p>
     *
     * @return {@code this}
     */
    public Builder description() {
      return description(null);
    }

    /**
     * A description of the function.
     * <p>
     * Default: - No description.
     * <p>
     *
     * @param description A description of the function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder description(final String description) {
      this.props.description(description);
      return this;
    }

    /**
     * Key-value pairs that Lambda caches and makes available for your Lambda functions.
     * <p>
     * Use environment variables to apply configuration changes, such as test and production environment configurations, without changing your Lambda
     * function source code.
     * <p>
     * Default: - No environment variables.
     * <p>
     *
     * @param environment Key-value pairs that Lambda caches and makes available for your Lambda functions. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder environment(final Map<String, String> environment) {
      this.props.environment(environment);
      return this;
    }

    /**
     * The AWS KMS key that's used to encrypt your function's environment variables.
     * <p>
     * Default: - AWS Lambda creates and uses an AWS managed customer master key (CMK).
     * <p>
     *
     * @param environmentEncryption The AWS KMS key that's used to encrypt your function's environment variables. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder environmentEncryption(final IKey environmentEncryption) {
      this.props.environmentEncryption(environmentEncryption);
      return this;
    }

    /**
     * The size of the function’s /tmp directory in MiB.
     * <p>
     * Default: 512 MiB
     * <p>
     *
     * @param ephemeralStorageSize The size of the function’s /tmp directory in MiB. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder ephemeralStorageSize(final Size ephemeralStorageSize) {
      this.props.ephemeralStorageSize(ephemeralStorageSize);
      return this;
    }

    /**
     * Event sources for this function.
     * <p>
     * You can also add event sources using <code>addEventSource</code>.
     * <p>
     * Default: - No event sources.
     * <p>
     *
     * @param events Event sources for this function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder events(final List<? extends IEventSource> events) {
      this.props.events(events);
      return this;
    }

    /**
     * The filesystem configuration for the lambda function.
     * <p>
     * Default: - will not mount any filesystem
     * <p>
     *
     * @param filesystem The filesystem configuration for the lambda function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder filesystem(final FileSystem filesystem) {
      this.props.filesystem(filesystem);
      return this;
    }

    /**
     * A name for the function.
     * <p>
     * Default: - AWS CloudFormation generates a unique physical ID and uses that ID for the function's name. For more information, see Name Type.
     * <p>
     *
     * @param functionName A name for the function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder functionName(final String functionName) {
      this.props.functionName(functionName);
      return this;
    }

    /**
     * Initial policy statements to add to the created Lambda Role.
     * <p>
     * You can call <code>addToRolePolicy</code> to the created lambda to add statements post creation.
     * <p>
     * Default: - No policy statements are added to the created Lambda role.
     * <p>
     *
     * @param initialPolicy Initial policy statements to add to the created Lambda Role. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder initialPolicy(final List<? extends PolicyStatement> initialPolicy) {
      this.props.initialPolicy(initialPolicy);
      return this;
    }

    /**
     * Specify the version of CloudWatch Lambda insights to use for monitoring.
     * <p>
     * Default: - No Lambda Insights
     * <p>
     *
     * @param insightsVersion Specify the version of CloudWatch Lambda insights to use for monitoring. This parameter is required.
     * @return {@code this}
     * @see <a
     * href="https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/Lambda-Insights-Getting-Started-docker.html">https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/Lambda-Insights-Getting-Started-docker.html</a>
     */
    public CustomRuntime2Function.Builder insightsVersion(final LambdaInsightsVersion insightsVersion) {
      this.props.insightsVersion(insightsVersion);
      return this;
    }

    /**
     * A list of layers to add to the function's execution environment.
     * <p>
     * You can configure your Lambda function to pull in additional code during initialization in the form of layers. Layers are packages of libraries
     * or other dependencies that can be used by multiple functions.
     * <p>
     * Default: - No layers.
     * <p>
     *
     * @param layers A list of layers to add to the function's execution environment. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder layers(final List<? extends ILayerVersion> layers) {
      this.props.layers(layers);
      return this;
    }

    /**
     * The number of days log events are kept in CloudWatch Logs.
     * <p>
     * When updating this property, unsetting it doesn't remove the log retention policy. To remove the retention policy, set the value to
     * <code>INFINITE</code>.
     * <p>
     * Default: logs.RetentionDays.INFINITE
     * <p>
     *
     * @param logRetention The number of days log events are kept in CloudWatch Logs. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder logRetention(final RetentionDays logRetention) {
      this.props.logRetention(logRetention);
      return this;
    }

    /**
     * When log retention is specified, a custom resource attempts to create the CloudWatch log group.
     * <p>
     * These options control the retry policy when interacting with CloudWatch APIs.
     * <p>
     * Default: - Default AWS SDK retry options.
     * <p>
     *
     * @return {@code this}
     */
    public Builder logRetentionRetryOptions() {
      return logRetentionRetryOptions(null);
    }

    /**
     * When log retention is specified, a custom resource attempts to create the CloudWatch log group.
     * <p>
     * These options control the retry policy when interacting with CloudWatch APIs.
     * <p>
     * Default: - Default AWS SDK retry options.
     * <p>
     *
     * @param logRetentionRetryOptions When log retention is specified, a custom resource attempts to create the CloudWatch log group. This parameter
     *                                 is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder logRetentionRetryOptions(final LogRetentionRetryOptions logRetentionRetryOptions) {
      this.props.logRetentionRetryOptions(logRetentionRetryOptions);
      return this;
    }

    /**
     * The IAM role for the Lambda function associated with the custom resource that sets the retention policy.
     * <p>
     * Default: - A new role is created.
     * <p>
     *
     * @param logRetentionRole The IAM role for the Lambda function associated with the custom resource that sets the retention policy. This parameter
     *                         is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder logRetentionRole(final IRole logRetentionRole) {
      this.props.logRetentionRole(logRetentionRole);
      return this;
    }

    /**
     * The amount of memory, in MB, that is allocated to your Lambda function.
     * <p>
     * Lambda uses this value to proportionally allocate the amount of CPU power. For more information, see Resource Model in the AWS Lambda Developer
     * Guide.
     * <p>
     * Default: 128
     * <p>
     *
     * @param memorySize The amount of memory, in MB, that is allocated to your Lambda function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder memorySize(final Number memorySize) {
      this.props.memorySize(memorySize == null ? FUNCTION_DEFAULT_MEMORY_SIZE : memorySize);
      return this;
    }

    /**
     * Enable profiling.
     * <p>
     * Default: - No profiling.
     * <p>
     *
     * @param profiling Enable profiling. This parameter is required.
     * @return {@code this}
     * @see <a
     * href="https://docs.aws.amazon.com/codeguru/latest/profiler-ug/setting-up-lambda.html">https://docs.aws.amazon.com/codeguru/latest/profiler-ug/setting-up-lambda.html</a>
     */
    public CustomRuntime2Function.Builder profiling(final Boolean profiling) {
      this.props.profiling(profiling);
      return this;
    }

    /**
     * Profiling Group.
     * <p>
     * Default: - A new profiling group will be created if `profiling` is set.
     * <p>
     *
     * @param profilingGroup Profiling Group. This parameter is required.
     * @return {@code this}
     * @see <a
     * href="https://docs.aws.amazon.com/codeguru/latest/profiler-ug/setting-up-lambda.html">https://docs.aws.amazon.com/codeguru/latest/profiler-ug/setting-up-lambda.html</a>
     */
    public CustomRuntime2Function.Builder profilingGroup(final IProfilingGroup profilingGroup) {
      this.props.profilingGroup(profilingGroup);
      return this;
    }

    /**
     * The maximum of concurrent executions you want to reserve for the function.
     * <p>
     * Default: - No specific limit - account limit.
     * <p>
     *
     * @param reservedConcurrentExecutions The maximum of concurrent executions you want to reserve for the function. This parameter is required.
     * @return {@code this}
     * @see <a
     * href="https://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html">https://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html</a>
     */
    public CustomRuntime2Function.Builder reservedConcurrentExecutions(final Number reservedConcurrentExecutions) {
      this.props.reservedConcurrentExecutions(reservedConcurrentExecutions);
      return this;
    }

    /**
     * Lambda execution role.
     * <p>
     * This is the role that will be assumed by the function upon execution. It controls the permissions that the function will have. The Role must be
     * assumable by the 'lambda.amazonaws.com' service principal.
     * <p>
     * The default Role automatically has permissions granted for Lambda execution. If you provide a Role, you must add the relevant AWS managed
     * policies yourself.
     * <p>
     * The relevant managed policies are "service-role/AWSLambdaBasicExecutionRole" and "service-role/AWSLambdaVPCAccessExecutionRole".
     * <p>
     * Default: - A unique role will be generated for this lambda function. Both supplied and generated roles can always be changed by calling
     * `addToRolePolicy`.
     * <p>
     *
     * @param role Lambda execution role. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder role(final IRole role) {
      this.props.role(role);
      return this;
    }

    /**
     * The list of security groups to associate with the Lambda's network interfaces.
     * <p>
     * Only used if 'vpc' is supplied.
     * <p>
     * Default: - If the function is placed within a VPC and a security group is not specified, either by this or securityGroup prop, a dedicated
     * security group will be created for this function.
     * <p>
     *
     * @param securityGroups The list of security groups to associate with the Lambda's network interfaces. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder securityGroups(final List<? extends ISecurityGroup> securityGroups) {
      this.props.securityGroups(securityGroups);
      return this;
    }

    /**
     * The function execution time (in seconds) after which Lambda terminates the function.
     * <p>
     * Because the execution time affects cost, set this value based on the function's expected execution time.
     * <p>
     * Default: Duration.seconds(3)
     * <p>
     *
     * @param timeout The function execution time (in seconds) after which Lambda terminates the function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder timeout(final Duration timeout) {
      this.props.timeout(timeout == null ? Duration.seconds(FUNCTION_DEFAULT_TIMEOUT_IN_SECONDS) : timeout);
      return this;
    }

    /**
     * Enable AWS X-Ray Tracing for Lambda Function.
     * <p>
     * Default: Tracing.Disabled
     * <p>
     *
     * @param tracing Enable AWS X-Ray Tracing for Lambda Function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder tracing(final Tracing tracing) {
      this.props.tracing(tracing);
      return this;
    }

    /**
     * VPC network to place Lambda network interfaces.
     * <p>
     * Specify this if the Lambda function needs to access resources in a VPC. This is required when <code>vpcSubnets</code> is specified.
     * <p>
     * Default: - Function is not placed within a VPC.
     * <p>
     *
     * @param vpc VPC network to place Lambda network interfaces. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder vpc(final IVpc vpc) {
      this.props.vpc(vpc);
      return this;
    }

    /**
     * Where to place the network interfaces within the VPC.
     * <p>
     * This requires <code>vpc</code> to be specified in order for interfaces to actually be placed in the subnets. If <code>vpc</code> is not
     * specify, this will raise an error.
     * <p>
     * Note: Internet access for Lambda Functions requires a NAT Gateway, so picking public subnets is not allowed (unless
     * <code>allowPublicSubnet</code> is set to <code>true</code>).
     * <p>
     * Default: - the Vpc default strategy if not specified
     * <p>
     *
     * @param vpcSubnets Where to place the network interfaces within the VPC. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder vpcSubnets(final SubnetSelection vpcSubnets) {
      this.props.vpcSubnets(vpcSubnets);
      return this;
    }

    /**
     * The source code of your Lambda function.
     * <p>
     * You can point to a file in an Amazon Simple Storage Service (Amazon S3) bucket or specify your source code as inline text.
     * <p>
     *
     * @param code The source code of your Lambda function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder code(final Code code) {
      this.props.code(code);
      return this;
    }

    /**
     * The name of the method within your code that Lambda calls to execute your function.
     * <p>
     * The format includes the file name. It can also include namespaces and other qualifiers, depending on the runtime. For more information, see
     * https://docs.aws.amazon.com/lambda/latest/dg/foundation-progmodel.html.
     * <p>
     * Use <code>Handler.FROM_IMAGE</code> when defining a function from a Docker image.
     * <p>
     * NOTE: If you specify your source code as inline text by specifying the ZipFile property within the Code property, specify index.function_name
     * as the handler.
     * <p>
     *
     * @param handler The name of the method within your code that Lambda calls to execute your function. This parameter is required.
     * @return {@code this}
     */
    public CustomRuntime2Function.Builder handler(final String handler) {
      this.props.handler(handler);
      return this;
    }

    /**
     * @return a newly built instance of {@link CustomRuntime2Function}.
     */
    @Override
    public CustomRuntime2Function build() {
      final FunctionProps functionProps = this.props.build();

      checkArgument(functionProps.getCode() != null, "'code' is required");
      checkArgument(isNoneBlank(functionProps.getHandler()), "'handler' is required");

      checkArgument(functionProps.getTimeout() != null, "'timeout' is required");
      checkArgument(isNoneBlank(functionProps.getDescription()), "'description' is required");
      checkArgument(isNotEmpty(functionProps.getEnvironment()), "'environment' is required");

      checkArgument(functionProps.getMemorySize() != null, "'memorySize' is required");
      checkArgument(functionProps.getMemorySize().intValue() >= 128 && functionProps.getMemorySize().intValue() <= 3008,
          "'memorySize' must be between 128 and 3008 (inclusive)");

      checkArgument(functionProps.getRetryAttempts() != null, "'retryAttempts' is required");
      checkArgument(functionProps.getRetryAttempts().intValue() >= 0 && functionProps.getRetryAttempts().intValue() <= 2,
          "'retryAttempts' must be between 0 and 2 (inclusive)");

      checkArgument(functionProps.getOnFailure() != null, "'onFailure' is required");
      checkArgument(functionProps.getOnSuccess() != null, "'onSuccess' is required");

      return new CustomRuntime2Function(this.scope, this.id, functionProps);
    }
  }
}
