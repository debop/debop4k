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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LazySeqZipTest : AbstractLazySeqTest() {

  private val firstSupplier = mock<() -> LazySeq<String>>()
  private val secondSupplier = mock<() -> LazySeq<Int>>()

  @Test fun testZipEmptyWithEmptySeq() {
    val empty = emptyIntSeq
    val zipped = empty.zip(emptyIntSeq) { a, b -> a + b }
    assertThat(zipped).isEmpty()
  }

  @Test fun testZipEmptyWithNonEmptySeq() {
    val zipped = emptyIntSeq.zip(lazySeqOf(1)) { a, b -> a + b }
    assertThat(zipped).isEmpty()

    val reversed = lazySeqOf(1).zip(emptyIntSeq) { a, b -> a + b }
    assertThat(reversed).isEmpty()
  }

  @Test fun testZipTwoFiniteSeqOfSameSize() {
    val first = lazySeqOf("A", "B", "C")
    val second = lazySeqOf(1, 2, 3)

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(lazySeqOf("A1", "B2", "C3"))
  }

  @Test fun testTwoLazySeqOfSameSize() {
    val first = lazySeqOf("A") { lazySeqOf("B") { lazySeqOf("C") } }
    val second = lazySeqOf(1) { lazySeqOf(2) { lazySeqOf(3) } }

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(lazySeqOf("A1", "B2", "C3"))
  }

  @Test fun testZipTwoFiniteSeqOfDiffSize() {
    val first = lazySeqOf("A", "B", "C")
    val second = lazySeqOf(1, 2, 3, 4)

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(lazySeqOf("A1", "B2", "C3"))
  }

  @Test fun testZipTwoFiniteSeqOfDiffSize2() {
    val first = lazySeqOf("A", "B", "C", "D")
    val second = lazySeqOf(1, 2, 3)

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(lazySeqOf("A1", "B2", "C3"))
  }

  @Test fun testTwoLazySeqOfDiffSize() {
    val first = lazySeqOf("A") { lazySeqOf("B") { lazySeqOf("C") } }
    val second = lazySeqOf(1) { lazySeqOf(2) { lazySeqOf(3) { lazySeqOf(4) } } }

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(lazySeqOf("A1", "B2", "C3"))
  }

  @Test fun testTwoLazySeqOfDiffSize2() {
    val first = lazySeqOf("A") { lazySeqOf("B") { lazySeqOf("C") { lazySeqOf("D") } } }
    val second = lazySeqOf(1) { lazySeqOf(2) { lazySeqOf(3) } }

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(lazySeqOf("A1", "B2", "C3"))
  }

  @Test
  fun testZipTwoInfiniteSeq() {
    val naturals = LazySeq.numbers(1)
    val primes = primes()
    val zipped = naturals.zip(primes) { n, p -> "$n:$p" }

    assertThat(zipped.take(5)).isEqualTo(lazySeqOf("1:2", "2:3", "3:5", "4:7", "5:11"))

    val fixedPrimes = primes().take(5)
    val zippedFixed = naturals.zip(fixedPrimes) { n, p -> "$n:$p" }

    assertThat(zippedFixed).isEqualTo(lazySeqOf("1:2", "2:3", "3:5", "4:7", "5:11"))
  }

  @Test fun testNotEvaluateTailWhenZippingTwoSequence() {
    val first = LazySeq.cons("A", firstSupplier)
    val second = LazySeq.cons(1, secondSupplier)

    first.zip(second) { c, i -> c + i }

    verifyZeroInteractions(firstSupplier)
    verifyZeroInteractions(secondSupplier)
  }
}