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

package debop4k.core.examples

import debop4k.core.AbstractCoreKotlinTest
import org.junit.Test

/**
 * LiteralFunctionExamples
 * @author sunghyouk.bae@gmail.com
 */
class LiteralFunctionExamples : AbstractCoreKotlinTest() {

  @Test
  fun makeIterate() {

    val sequence = generateSequence(0) {
      if (it < 100) {
        Thread.sleep(100)
        it + 1
      } else
        it
    }
    val list: Sequence<Int> = sequence.takeWhile { it < 100 }

    list.forEach { println("$it") }
  }
}