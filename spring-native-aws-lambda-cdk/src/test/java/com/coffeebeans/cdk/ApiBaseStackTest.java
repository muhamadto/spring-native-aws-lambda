package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.StackUtils.createStack;
import static com.coffeebeans.cdk.TestLambdaUtils.getTestLambdaCodePath;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.services.sqs.DeduplicationScope.MESSAGE_GROUP;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.services.sqs.Queue;

class ApiBaseStackTest {

  private static final String ENV = "test";

  private ApiBaseStack apiBaseStack;

  private final App app = new App();

  @TempDir
  private static Path TEMP_DIR;

  @BeforeEach
  void setUp() throws IOException {

    final Path lambdaCodePath = getTestLambdaCodePath(TEMP_DIR);

    this.apiBaseStack = createStack(app, "test-stack", lambdaCodePath.toString(), ENV, "test-cdk-bucket", ENV);
  }

  @Test
  void should_create_and_return_queue() {
    final String queueId = "test-queue";

    final Queue actual = apiBaseStack.createQueue(queueId);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .hasFieldOrProperty("deadLetterQueue")
        .extracting("fifo")
        .isEqualTo(false);
  }

  @Test
  void should_create_and_return_fifo_queue() {
    final String queueId = "test-queue";

    final Queue actual = apiBaseStack.createFifoQueue(queueId, true, MESSAGE_GROUP);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .hasFieldOrProperty("deadLetterQueue")
        .extracting("fifo")
        .isEqualTo(true);

  }
}