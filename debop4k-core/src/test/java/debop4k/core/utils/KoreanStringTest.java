/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

package debop4k.core.utils;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * KoreanExTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 15.
 */
@Slf4j
public class KoreanStringTest extends AbstractCoreTest {

  @Test
  public void extractJaso() {
    String jaso = "ㄷㅗㅇㅎㅐㅁㅜㄹㄱㅘ ㅂㅐㄱㄷㅜㅅㅏㄴㅇㅣ HELLO WORLD";
    assertThat(KoreanString.getJasoLetter("동해물과 백두산이 Hello World")).isEqualToIgnoringCase(jaso);
  }

  @Test
  public void extractChosung() {
    char[] chosungs = KoreanString.getChosung("배성혁");
    assertThat(chosungs[0]).isEqualTo('ㅂ');
    assertThat(chosungs[1]).isEqualTo('ㅅ');
    assertThat(chosungs[2]).isEqualTo('ㅎ');
    assertThat(chosungs).hasSize(3).containsExactly('ㅂ', 'ㅅ', 'ㅎ');
  }
}
