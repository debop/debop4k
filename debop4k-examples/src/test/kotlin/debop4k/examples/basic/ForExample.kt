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
import org.junit.Test

class ForExample : AbstractExampleTest() {

  fun fizzBuzz(i: Int) = when {
    i % 5 == 0 -> "FizzBuzz "
    i % 3 == 0 -> "Fizz "
    i % 5 == 0 -> "Buzz "
    else       -> "$i "
  }

  @Test
  fun rangeForLoop() {
    for (i in 1..100) {
      log.debug(fizzBuzz(i))
    }
  }

  @Test
  fun rangeForDownTo() {
    for (i in 100 downTo 1 step 2) {
      log.debug(fizzBuzz(i))
    }
  }

}