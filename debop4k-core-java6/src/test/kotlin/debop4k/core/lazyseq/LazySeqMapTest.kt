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

import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

/**
 * LazySeqMapTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqMapTest : AbstractLazySeqTest() {

  @Mock
  val supplierMock: () -> LazySeq<Int> = uninitialized()

  @Test
  fun testReturnEmptyWhenMappingEmpty() {
    assertThat(LazySeq.empty<Int>()).isEmpty()
  }

  @Test
  fun testMapFixedSeqWithJustOneElement() {
    val chars = lazySeqOf('a')
    val toUpper = chars.map(Char::toUpperCase)

    assertThat(toUpper).isEqualTo(lazySeqOf('A'))
  }

  @Test
  fun testMapFixedSeqWithFewElements() {
    val chars = lazySeqOf('a', 'b', 'c')
    val toUpper = chars.map(Char::toUpperCase)

    assertThat(toUpper).isEqualTo(lazySeqOf('A', 'B', 'C'))
  }

  @Test
  fun testMapInfiniteSeq() {
    val naturals = LazySeq.numbers(1)
    val multiplied = naturals.map { it * 10 }
    assertThat(multiplied.take(4)).isEqualTo(lazySeqOf(10, 20, 30, 40))
  }

  @Test
  fun testNotEvaluateTailOnMap() {
    val seq = LazySeq.cons(17, supplierMock)
    seq.map(Int::toString)
    Mockito.verifyZeroInteractions(supplierMock)
  }

  @Test
  fun testMapHead() {
    val seq = LazySeq.cons(17, supplierMock)
    val strings = seq.map(Int::toString)
    assertThat(strings.head).isEqualToIgnoringCase("17")
  }

}