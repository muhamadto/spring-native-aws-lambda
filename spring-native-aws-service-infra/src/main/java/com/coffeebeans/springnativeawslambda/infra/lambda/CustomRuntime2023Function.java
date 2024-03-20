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

package com.coffeebeans.springnativeawslambda.infra.lambda;

import com.coffeebeans.springnativeawslambda.infra.CoffeeBeansConstruct;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.constructs.Construct;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2023;

/**
 * A lambda function with runtime provided.al2023 (custom runtime al2023). Creating function with any other
 * runtime (e.g. passed in the {@link FunctionProps} or via any other means will be ignored).
 */

@Getter
public class CustomRuntime2023Function extends Construct implements CoffeeBeansConstruct {

    private static final int FUNCTION_DEFAULT_TIMEOUT_IN_SECONDS = 10;
    private static final int FUNCTION_DEFAULT_MEMORY_SIZE = 512;
    private static final int FUNCTION_DEFAULT_RETRY_ATTEMPTS = 2;

    private final software.amazon.awscdk.services.lambda.Function function;


    /**
     * @param scope This parameter is required.
     * @param id    This parameter is required.
     * @param props This parameter is required.
     */
    public CustomRuntime2023Function(@NotNull final Construct scope,
                                     @NotNull final String id,
                                     @NotNull final FunctionProps props) {
        super(scope, id);

        checkArgument(isNoneBlank(props.getHandler()), "'handler' is required");
        checkArgument(isNoneBlank(props.getDescription()), "'description' is required");
        checkArgument(isNotEmpty(props.getEnvironment()), "'environment' is required");

        final Duration timeout = props.getTimeout() == null
                ? Duration.seconds(FUNCTION_DEFAULT_TIMEOUT_IN_SECONDS)
                : props.getTimeout();

        final Number memorySize = props.getMemorySize() == null
                ? FUNCTION_DEFAULT_MEMORY_SIZE
                : props.getMemorySize();

        final Number retryAttempts = props.getRetryAttempts() == null
                ? FUNCTION_DEFAULT_RETRY_ATTEMPTS
                : props.getRetryAttempts();

        checkArgument(props.getMemorySize().intValue() >= 128
                        && props.getMemorySize().intValue() <= 3008,
                "'memorySize' must be between 128 and 3008 (inclusive)");

        checkArgument(props.getRetryAttempts().intValue() >= 0
                        && props.getRetryAttempts().intValue() <= 2,
                "'retryAttempts' must be between 0 and 2 (inclusive)");


        function = Function.Builder.create(this, id)
                .runtime(PROVIDED_AL2023)
                .functionName(props.getFunctionName())
                .description(props.getDescription())
                .code(props.getCode())
                .handler(props.getHandler())
                .role(props.getRole())
                .vpc(props.getVpc())
                .environment(props.getEnvironment())
                .deadLetterTopic(props.getDeadLetterTopic())
                .timeout(timeout)
                .memorySize(memorySize)
                .retryAttempts(retryAttempts)
                .build();

    }
}
