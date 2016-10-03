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

class WhenExample : AbstractExampleTest() {

  @Test
  fun whenUsingType() {
    assertThat(eval(Sum(Num(1), Num(2)))).isEqualTo(3)
  }

  interface Expr
  data class Num(val value: Int) : Expr
  data class Sum(val left: Expr, val right: Expr) : Expr

  fun eval(e: Expr): Int {
    when (e) {
      is Num -> return e.value
      is Sum -> return eval(e.left) + eval(e.right)
      else   -> throw IllegalArgumentException("Unknown Expression")
    }
  }
}

