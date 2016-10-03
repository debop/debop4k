/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.core.io;

import debop4k.core.AbstractCoreTest;
import debop4k.core.collections.Arrayx;
import debop4k.core.utils.Resourcex;
import debop4k.core.utils.Stringx;
import kotlin.text.Charsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class IOStreamxTest extends AbstractCoreTest {

  @Test
  @SneakyThrows({IOException.class})
  public void readStream() {
    InputStream is = Resourcex.getClassPathResourceStream("logback-test.xml");
    try {
      assertThat(is).isNotNull();
      String xml = IOStreamx.toString(is);
      assertThat(xml).isNotEmpty();
      assertThat(xml).contains("logger");
    } finally {
      is.close();
    }
  }

  @Test
  @SneakyThrows({IOException.class})
  public void copyStreams() {
    String source = Stringx.replicate(sampleText, 1000);
    InputStream input = IOStreamx.toInputStream(source);
    try {
      String copied = IOStreamx.toString(input);
      assertThat(copied).isEqualTo(source);
    } finally {
      input.close();
    }
  }

  @Test
  public void inputStreamWithBytes() throws Exception {
    byte[] sourceBytes = Arrayx.getRandomBytes(64 * 1024);
    InputStream input = IOStreamx.toInputStream(sourceBytes);
    try {
      byte[] copied = IOStreamx.toByteArray(input);
      assertThat(copied).isEqualTo(sourceBytes);
    } finally {
      input.close();
    }
  }

  @Test
  public void inputStreamWithString() throws Exception {
    String sourceText = Stringx.replicate(sampleText, 1000);
    InputStream input = IOStreamx.toInputStream(sourceText);
    try {
      String copiedText = IOStreamx.toString(input);
      assertThat(copiedText).isEqualTo(sourceText);
    } finally {
      input.close();
    }
  }

  @Test
  public void outputStreamWithByte() throws Exception {
    byte[] sourceBytes = Arrayx.getRandomBytes(64 * 1024);
    FastByteArrayOutputStream output = IOStreamx.toOutputStream(sourceBytes);
    try {
      byte[] copied = output.toByteArrayUnsafe();
      assertThat(copied).isEqualTo(sourceBytes);
    } finally {
      output.close();
    }
  }

  @Test
  public void outputStreamWithString() throws Exception {
    String sourceText = Stringx.replicate(sampleText, 1000);
    FastByteArrayOutputStream output = IOStreamx.toOutputStream(sourceText);
    try {
      String copiedText = Stringx.toUtf8String(output.toByteArrayUnsafe());
      assertThat(copiedText).isEqualTo(sourceText);
    } finally {
      output.close();
    }
  }

  @Test
  public void inputStreamToStringList() throws Exception {
    InputStream input = Resourcex.getClassPathResourceStream("logback-test.xml");
    try {
      List<String> lines = IOStreamx.toStringList(input);
      assertThat(lines).isNotNull();
      assertThat(lines.size()).isGreaterThan(0);
    } finally {
      if (input != null)
        input.close();
    }
  }

  @Test
  public void bytesToStringList() {
    byte[] bytes = Resourcex.getBytes("logback-test.xml");
    List<String> lines = IOStreamx.toStringList(bytes, Charsets.UTF_8);

    assertThat(lines).isNotNull();
    assertThat(lines.size()).isGreaterThan(0);
    log.trace("lines={}", lines);
  }
}
