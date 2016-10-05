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

package debop4k.core.utils.codecs;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.core.utils.Stringx.toUtf8Bytes;
import static debop4k.core.utils.Stringx.toUtf8String;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * CodecxTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class CodecxKtTest extends AbstractCoreTest {

  static String plainText = "동해물과 백두산이 $% https://github.com/debop/debop4s?title=hello-world&x=. java 8 is good";

  @Test
  public void encodeBase64Bytes() {
    byte[] plainBytes = toUtf8Bytes(plainText);
    byte[] base64 = Codecx.encodeBase64(plainBytes);
    byte[] decoded = Codecx.decodeBase64(base64);

    assertThat(decoded).isEqualTo(plainBytes);
  }

  @Test
  public void encodeBase64String() {
    String base64Text = Codecx.encodeBase64AsString(toUtf8Bytes(plainText));
    String decodedText = Codecx.decodeBase64AsString(base64Text);

    assertThat(decodedText).isEqualTo(plainText);
  }

  StringEncoder[] encoders = new StringEncoder[]{
      Codecx.getBase64(),
      Codecx.getHexDecimal(),
      Codecx.getGzipBase64(),
      Codecx.getGzipHexDecimal(),
      Codecx.getDeflaterBase64(),
      Codecx.getDeflaterHexDecimal(),
      Codecx.getSnappyBase64(),
      Codecx.getSnappyHexDecimal(),
//      Codecx.getRc2Base64(),
//      Codecx.getRc2Hex(),
      Codecx.getDeflaterBase64(),
      Codecx.getDeflaterHexDecimal(),
//      Codecx.getTripleDESBase64(),
//      Codecx.getTripleDESHex()
  };

  @Test
  public void testEncoders() {
    byte[] bytes = toUtf8Bytes(plainText);

    for (StringEncoder encoder : encoders) {
      String encodedStr = encoder.encode(bytes);
      byte[] decodedBytes = encoder.decode(encodedStr);
      String decodedStr = toUtf8String(decodedBytes);
      assertThat(decodedStr).isEqualTo(plainText);
    }
  }
}
