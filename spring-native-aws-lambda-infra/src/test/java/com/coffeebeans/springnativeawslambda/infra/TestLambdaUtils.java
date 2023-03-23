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

package com.coffeebeans.springnativeawslambda.infra;

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