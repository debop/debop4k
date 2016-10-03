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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByteBufferStreamTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class ByteBufferStreamTest extends AbstractCoreTest {

  @Test
  @SneakyThrows(IOException.class)
  public void byteArrayToInputStream() {
    byte[] bytes = Arrayx.getRandomBytes(16 * 1024);

    InputStream input = new ByteBufferInputStream(bytes);
    try {
      byte[] copied = IOStreamx.toByteArray(input);
      assertThat(copied).isEqualTo(bytes);
    } finally {
      input.close();
    }
  }

  @Test
  @SneakyThrows(IOException.class)
  public void copyByteBufferStream() {
    byte[] bytes = Arrayx.getRandomBytes(16 * 1024);

    InputStream input = new ByteBufferInputStream(bytes);
    FastByteArrayOutputStream output = IOStreamx.fastByteArrayOutputStreamOf();
    try {
      IOStreamx.copy(input, output);
      byte[] copied = output.toByteArrayUnsafe();

      assertThat(copied).isEqualTo(bytes);
    } finally {
      output.close();
      input.close();
    }
  }
}
