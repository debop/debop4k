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

package debop4k.core.compress;

import debop4k.core.AbstractCoreTest;
import debop4k.core.collections.Arrayx;
import debop4k.core.utils.Stringx;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

import static debop4k.core.collections.Arrayx.getEmptyByteArray;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CompressorTest extends AbstractCoreTest {

  static FastList<Compressor> COMPRESSORS = FastList.newListWith(Compressx.getGZIP(),
                                                                 Compressx.getDEFLATER(),
                                                                 Compressx.getSNAPPY(),
                                                                 Compressx.getLZ4(),
                                                                 Compressx.getBZIP2());

  @Test
  public void compressEmptyData() {
    for (Compressor compressor : COMPRESSORS) {
      assertThat(compressor.compress(null)).isEqualTo(Arrayx.getEmptyByteArray());
      assertThat(compressor.decompress(getEmptyByteArray())).isEqualTo(getEmptyByteArray());
    }
  }

  @Test
  public void compressText() {

    Random random = new Random(new Date().getTime());

    FastList<String> builder = FastList.newList();
    for (int i = 0; i < 1000; i++) {
      builder.add(String.format("동해물과 백두산이 %1$d  * !@# Hello World! %1$d%1$d", i));
    }
    String text = builder.makeString();

    //String text = "동해물과 백두산이";
    byte[] bytes = Stringx.toUtf8Bytes(text);

    for (Compressor compressor : COMPRESSORS) {
      log.debug("compressor={}", compressor);

      byte[] compressedBytes = compressor.compress(bytes);
      assertThat(compressedBytes).isNotNull();
      assertThat(compressedBytes.length).isGreaterThan(0);
      log.debug("compressedBytes length={}", compressedBytes.length);

      byte[] decompressedBytes = compressor.decompress(compressedBytes);
      assertThat(decompressedBytes).isNotNull();
      assertThat(decompressedBytes.length).isGreaterThan(0);

      String decompressedText = Stringx.toUtf8String(decompressedBytes);
      assertThat(decompressedText).isEqualTo(decompressedText);
    }
  }

  @Test
  public void compressBytes() {
    byte[] bytes = Arrayx.getRandomBytes(50 * 1025);

    for (Compressor compressor : COMPRESSORS) {
      log.debug("compressor={}", compressor);

      byte[] compressedBytes = compressor.compress(bytes);
      assertThat(compressedBytes.length).isGreaterThan(0);

      byte[] decompressedBytes = compressor.decompress(compressedBytes);
      assertThat(decompressedBytes.length).isGreaterThan(0);

      assertThat(decompressedBytes).isEqualTo(bytes);
    }
  }
}
