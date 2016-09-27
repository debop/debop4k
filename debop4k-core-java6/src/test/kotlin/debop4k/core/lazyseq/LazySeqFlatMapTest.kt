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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito

class LazySeqFlatMapTest : AbstractLazySeqTest() {

  val supplierMock = mock<() -> LazySeq<Int>>()

  @Test
  fun testReturnEmptySequenceWhenEmptyAsInput() {
    val empty = emptyLazySeq<Int>()
    val flat = empty.flatMap { listOf(it, -it) }
    assertThat(flat).isEmpty()
  }

  @Test
  fun testFlattenHead() {
    val raw = lazySeqOf(1, 2)
    val flat = raw.flatMap { listOf(it, -it) }

    assertThat(flat).isEqualTo(lazySeqOf(1, -1, 2, -2))
  }

  @Test
  fun testLazilyFlattenInfiniteStream() {
    val raw = LazySeq.numbers(1)
    val flat = raw.flatMap { listOf(it, 0, -it) }

    assertThat(flat.take(10)).isEqualTo(lazySeqOf(1, 0, -1, 2, 0, -2, 3, 0, -3, 4))
  }

  @Test
  fun testFlattenHeadOnlyAndNotEvaluateTail() {
    val raw = lazySeqOf(1, supplierMock)
    val flat = raw.flatMap { listOf(it, 0, -it) }

    assertThat(flat[0]).isEqualTo(1)
    assertThat(flat[1]).isEqualTo(0)
    assertThat(flat[2]).isEqualTo(-1)
    Mockito.verifyZeroInteractions(supplierMock)
  }

  @Test
  fun testKeepEvaluatingIfFirstFlatMapResultIfEmpty() {
    val from = -10
    val raw = LazySeq.numbers(from)
    val flat = raw.flatMap { flatMapFunc(it) }

    assertThat(flat.take(10)).isEqualTo(lazySeqOf(2, 3, 4, 5, 6, 7, 0, 0, 0, 0))
  }

  private fun flatMapFunc(i: Int): Iterable<Int> = when (i) {
    in Int.MIN_VALUE..0 -> listOf<Int>()
    1 -> listOf(2)
    2 -> listOf(3, 4)
    3 -> listOf(5, 6, 7)
    else -> listOf(0, 0)
  }
}