/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.kyligence.zenml.toolkit;

import io.kyligence.zenml.toolkit.ZenMlToolkitCLI;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Path;

@SpringBootTest
public class ZenMlToolkitCLITest {
    private static final String destDirPath = Path.of(FileUtils.getTempDirectoryPath(),
            "zen-ml-toolkit").toString();

    @BeforeAll
    public static void setup() {
        File destDir = new File(destDirPath);
        FileUtils.deleteQuietly(destDir);
        destDir.mkdirs();
    }

    @AfterAll
    public static void clean() {
        FileUtils.deleteQuietly(new File(destDirPath));
    }

    @Test
    public void testExecute() {
        String tdsPath = "src/test/resources/sources/tableau/superstore.tds";
        String[] args = new String[]{"-i", tdsPath, "-o", destDirPath};
        ZenMlToolkitCLI cli = new ZenMlToolkitCLI();
        cli.execute(args);
        Path destPath = Path.of(destDirPath, "superstore.zen.yml");
        File destFile = destPath.toFile();
        Assertions.assertTrue(destFile.exists());
    }
}
