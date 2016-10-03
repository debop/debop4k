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

package debop4k.examples.basic

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InCheckExample : AbstractExampleTest() {

  fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
  fun isNotDigit(c: Char) = c !in '0'..'9'

  fun recognize(c: Char) = when (c) {
    in '0'..'9'              -> "$c is a digit"
    in 'a'..'z', in 'A'..'Z' -> "$c is a letter"
    else                     -> "I don't know ... $c"
  }

  @Test
  fun inCheck() {
    assertThat(isLetter('q')).isTrue();
    assertThat(isLetter('?')).isFalse();

    assertThat(isNotDigit('a')).isTrue();
    assertThat(isNotDigit('0')).isFalse();

    assertThat(recognize('5')).contains("digit")
    assertThat(recognize('a')).contains("letter")
    assertThat(recognize('@')).contains("don't")
  }
}