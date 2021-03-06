/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.shell;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.accumulo.core.util.Base64;
import org.apache.accumulo.shell.ShellUtil;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.io.Text;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.ImmutableList;

public class ShellUtilTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder(new File(System.getProperty("user.dir") + "/target"));

  // String with 3 lines, with one empty line
  private static final String FILEDATA = "line1\n\nline2";

  @Test
  public void testWithoutDecode() throws IOException {
    File testFile = new File(folder.getRoot(), "testFileNoDecode.txt");
    FileUtils.writeStringToFile(testFile, FILEDATA);
    List<Text> output = ShellUtil.scanFile(testFile.getAbsolutePath(), false);
    assertEquals(ImmutableList.of(new Text("line1"), new Text("line2")), output);
  }

  @Test
  public void testWithDecode() throws IOException {
    File testFile = new File(folder.getRoot(), "testFileWithDecode.txt");
    FileUtils.writeStringToFile(testFile, FILEDATA);
    List<Text> output = ShellUtil.scanFile(testFile.getAbsolutePath(), true);
    assertEquals(
        ImmutableList.of(new Text(Base64.decodeBase64("line1".getBytes(UTF_8))), new Text(Base64.decodeBase64("line2".getBytes(UTF_8)))),
        output);
  }

  @Test(expected = FileNotFoundException.class)
  public void testWithMissingFile() throws FileNotFoundException {
    ShellUtil.scanFile("missingFile.txt", false);
  }
}
