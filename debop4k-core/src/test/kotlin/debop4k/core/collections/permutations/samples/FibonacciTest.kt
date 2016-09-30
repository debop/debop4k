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

package debop4k.core.collections.permutations.samples

import debop4k.core.collections.permutations.AbstractPermutationTest
import debop4k.core.collections.permutations.Permutation
import debop4k.core.collections.permutations.cons
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * FibonacciTest
 * @author sunghyouk.bae@gmail.com
 */
class FibonacciTest : AbstractPermutationTest() {

  fun lastTwoFib(first: Int, second: Int): Permutation<Int> {
    return cons(first) { lastTwoFib(second, first + second) }
  }

  @Test
  fun testGenerateFibonacciSequence() {
    val fib = lastTwoFib(0, 1)
    assertThat(fib.startsWith(listOf(0, 1, 1, 2, 3, 5, 8, 13, 21)))
  }

  @Test
  fun testReturnGreaterFibonacciNumber() {
    val fib = lastTwoFib(0, 1)
    assertThat(fib[5]).isEqualTo(5)
    assertThat(fib[45]).isEqualTo(1134903170)
  }

  fun fibonacciSequence(first: Int, second: Int): Sequence<Int> {
    return generateSequence(first to second) { it.second to it.first + it.second }.map { it.first }
  }

  @Test
  fun testGenerateFibonacciSequenceBySequence() {
    val fib = fibonacciSequence(0, 1)
    assertThat(fib.take(9).toList()).isEqualTo(listOf(0, 1, 1, 2, 3, 5, 8, 13, 21))
  }

  @Test
  fun testReturnGreaterFibonacciNumberBySequence() {
    val fib = fibonacciSequence(0, 1)
    assertThat(fib.take(6).last()).isEqualTo(5)
    assertThat(fib.take(46).last()).isEqualTo(1134903170)
  }


}