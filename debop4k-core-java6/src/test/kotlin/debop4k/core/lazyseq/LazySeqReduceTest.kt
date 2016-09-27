/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

package debop4k.core.lazyseq

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LazySeqReduceTest : AbstractLazySeqTest() {

  val sum: (Int, Int) -> Int get() = { a, b -> a + b }
  val product: (Int, Int) -> Int get() = { a, b -> a * b }

  @Test fun testSumEmptySeq() {
    assertThat(emptyIntSeq.reduce(0, sum)).isEqualTo(0)
  }

  @Test fun testSumFixedSeq() {
    val fixed = lazySeqOf(1, 2, 4, 7)
    val sum = fixed.reduce(0, sum)
    assertThat(sum).isEqualTo(1 + 2 + 4 + 7)
  }

  @Test fun testSumLazyFixedSeq() {
    val lazy = lazySeqOf(1) { lazySeqOf(2) { lazySeqOf(4) { lazySeqOf(7) } } }
    val sum = lazy.reduce(0, sum)
    assertThat(sum).isEqualTo(1 + 2 + 4 + 7)
  }

  @Test fun testProductEmptySeq() {
    assertThat(emptyIntSeq.reduce(product)).isNull()
    assertThat(emptyIntSeq.reduce(1, product)).isEqualTo(1)
  }

  @Test fun testProductFixedSeq() {
    val fixed = lazySeqOf(1, 2, 4, 7)
    val product = fixed.reduce(1, product)
    assertThat(product).isEqualTo(1 * 2 * 4 * 7)
  }

  @Test fun testProductLazyFixedSeq() {
    val lazy = lazySeqOf(1) { lazySeqOf(2) { lazySeqOf(4) { lazySeqOf(7) } } }
    val product = lazy.reduce(1, product)
    assertThat(product).isEqualTo(1 * 2 * 4 * 7)
  }

  @Test fun testZeroTotalSumOfStringLengthWithEmptySeq() {
    val totalSum = emptyLazySeq<String>().reduce(0) { acc, i -> acc + i.length }
    assertThat(totalSum).isEqualTo(0)
  }

  @Test fun testTotalSumOfStringLengthWithFixedSeq() {
    val fixed = lazySeqOf("1", "22", "4444", "7777777")
    val totalSum = fixed.reduce(0) { acc, i -> acc + i.length }
    assertThat(totalSum).isEqualTo(1 + 2 + 4 + 7)
  }

  @Test fun testTotalSumOfStringLengthWithLazySeq() {
    val lazy = lazySeqOf("1") { lazySeqOf("22") { lazySeqOf("4444") { lazySeqOf("7777777") } } }
    val totalSum = lazy.reduce(0) { acc, i -> acc + i.length }
    assertThat(totalSum).isEqualTo(1 + 2 + 4 + 7)
  }
}