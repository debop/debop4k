/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.io;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;

import static debop4k.core.utils.Stringx.toUtf8String;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByteBuffersTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class ByteBufferExTest extends AbstractCoreTest {

  @Test
  public void convertToByteArray() {
    ByteBuffer buffer = ByteBufferx.toByteBuffer(sampleBytes);
    String text = toUtf8String(ByteBufferx.toByteArray(buffer));

    assertThat(text).isEqualTo(sampleText);
  }

}
