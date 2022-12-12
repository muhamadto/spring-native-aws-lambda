package com.coffeebeans.cdk;

import static com.coffeebeans.cdk.StackUtils.createStack;
import static com.coffeebeans.cdk.TagUtils.TAG_VALUE_COST_CENTRE;
import static com.coffeebeans.cdk.TestLambdaUtils.getTestLambdaCodePath;
import static org.assertj.core.api.Assertions.assertThat;
import static software.amazon.awscdk.services.sqs.DeduplicationScope.MESSAGE_GROUP;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import software.amazon.awscdk.App;
import software.amazon.awscdk.services.sqs.Queue;

class ApiBaseStackTest {

  private static final String ENV = "test";

  private ApiBaseStack apiBaseStack;

  private final App app = new App();

  private MockedStatic<TagUtils> tagUtilsStaticMock;

  @TempDir
  private static Path TEMP_DIR;

  @BeforeEach
  void setUp() throws IOException {

    final Path lambdaCodePath = getTestLambdaCodePath(TEMP_DIR);

    this.apiBaseStack = createStack(app, ENV, "test-stack", lambdaCodePath.toString(), ENV, "test-cdk-bucket");

    this.tagUtilsStaticMock = Mockito.mockStatic(TagUtils.class);
  }

  @Test
  void should_create_and_return_queue() {
    final String queueId = "test-queue";
    final Map<String, String> tags = TagUtils.createTags(ENV, TAG_VALUE_COST_CENTRE);

    final Queue actual = apiBaseStack.createQueue(queueId, tags);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .hasFieldOrProperty("deadLetterQueue")
        .extracting("fifo")
        .isEqualTo(false);

    tagUtilsStaticMock.verify(() -> TagUtils.addTags(actual, tags));
  }

  @Test
  void should_create_and_return_fifo_queue() {
    final String queueId = "test-queue";
    final Map<String, String> tags = TagUtils.createTags(ENV, TAG_VALUE_COST_CENTRE);

    final Queue actual = apiBaseStack.createFifoQueue(queueId, true, MESSAGE_GROUP, tags);

    assertThat(actual)
        .isNotNull()
        .hasFieldOrProperty("queueName")
        .hasFieldOrProperty("queueArn")
        .hasFieldOrProperty("queueUrl")
        .hasFieldOrProperty("deadLetterQueue")
        .extracting("fifo")
        .isEqualTo(true);

    tagUtilsStaticMock.verify(() -> TagUtils.addTags(actual, tags));
  }

  @AfterEach
  void tearDown() {
    this.tagUtilsStaticMock.close();
    this.apiBaseStack = null;
  }
}