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

package debop4k.core.collections.permutations

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ReduceTest : AbstractPermutationTest() {

  val sum: (Int, Int) -> Int get() = { a, b -> a + b }
  val product: (Int, Int) -> Int get() = { a, b -> a * b }

  @Test fun testSumEmptySeq() {
    assertThat(emptyIntPermutation.reduce(0, sum)).isEqualTo(0)
  }

  @Test fun testSumFixedSeq() {
    val fixed = permutationOf(1, 2, 4, 7)
    val sum = fixed.reduce(0, sum)
    assertThat(sum).isEqualTo(1 + 2 + 4 + 7)
  }

  @Test fun testSumLazyFixedSeq() {
    val lazy = cons(1) { cons(2) { cons(4) { permutationOf(7) } } }
    val sum = lazy.reduce(0, sum)
    assertThat(sum).isEqualTo(1 + 2 + 4 + 7)
  }

  @Test fun testProductEmptySeq() {
    assertThat(emptyIntPermutation.reduce(product)).isNull()
    assertThat(emptyIntPermutation.reduce(1, product)).isEqualTo(1)
  }

  @Test fun testProductFixedSeq() {
    val fixed = permutationOf(1, 2, 4, 7)
    val product = fixed.reduce(1, product)
    assertThat(product).isEqualTo(1 * 2 * 4 * 7)
  }

  @Test fun testProductLazyFixedSeq() {
    val lazy = cons(1) { cons(2) { cons(4) { permutationOf(7) } } }
    val product = lazy.reduce(1, product)
    assertThat(product).isEqualTo(1 * 2 * 4 * 7)
  }

  @Test fun testZeroTotalSumOfStringLengthWithEmptySeq() {
    val totalSum = emptyPermutation<String>().reduce(0) { acc, i -> acc + i.length }
    assertThat(totalSum).isEqualTo(0)
  }

  @Test fun testTotalSumOfStringLengthWithFixedSeq() {
    val fixed = permutationOf("1", "22", "4444", "7777777")
    val totalSum = fixed.reduce(0) { acc, i -> acc + i.length }
    assertThat(totalSum).isEqualTo(1 + 2 + 4 + 7)
  }

  @Test fun testTotalSumOfStringLengthWithLazySeq() {
    val lazy = cons("1") { cons("22") { cons("4444") { permutationOf("7777777") } } }
    val totalSum = lazy.reduce(0) { acc, i -> acc + i.length }
    assertThat(totalSum).isEqualTo(1 + 2 + 4 + 7)
  }
}