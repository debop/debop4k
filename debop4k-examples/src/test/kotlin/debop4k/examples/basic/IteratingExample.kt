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
 */

package debop4k.examples.basic

import debop4k.examples.AbstractExampleTest
import org.junit.Test
import java.util.*

class IteratingExample : AbstractExampleTest() {

  @Test
  fun iteratingMap() {
    val binaryReps = TreeMap<Char, String>()

    for (c in 'A'..'F') {
      val binary = Integer.toBinaryString(c.toInt())
      binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
      log.debug("$letter = $binary")
    }

    binaryReps.forEach { letter, binary ->
      log.debug("$letter = $binary")
    }
  }

  @Test
  fun iteratingWithIndex() {
    val list = arrayListOf("10", "11", "1001")

    for ((index, element) in list.withIndex()) {
      log.debug("$index: $element")
    }

    list.withIndex().forEach { (index, element) ->
      log.debug("$index: $element")
    }
  }

}