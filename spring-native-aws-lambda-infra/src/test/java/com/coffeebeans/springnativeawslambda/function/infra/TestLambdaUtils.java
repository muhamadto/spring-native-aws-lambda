package com.coffeebeans.springnativeawslambda.function.infra;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestLambdaUtils {

  @NotNull
  public static Path getTestLambdaCodePath(@NotNull final Path tempDir) throws IOException {
    final Path lambdaCodePath = tempDir.resolve("lambda-package.zip");

    final File file = lambdaCodePath.toFile();

    if (file.exists()) {
      return lambdaCodePath;
    }

    final boolean isCreated = file.createNewFile();

    if (!isCreated) {
      throw new IOException("Failed to create lambda package");
    }
    return lambdaCodePath;
  }
}