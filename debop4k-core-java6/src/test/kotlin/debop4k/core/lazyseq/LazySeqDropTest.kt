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
import com.nhaarman.mockito_kotlin.times
import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito


class LazySeqDropTest : AbstractLazySeqTest() {

  val supplierMock = mock<() -> LazySeq<Int>>()

  @Test
  fun testThrownWhenNegativeStartIndexOnEmptySeq() {
    val empty = emptyLazySeq<Any>()

    assertThatThrownBy {
      val startInclusive = -1L
      empty.drop(startInclusive)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testThrowWhenNegativeStartIndexOnNonEmptySeq() {
    val nonEmpty = lazySeqOf(1, 2, 3)

    assertThatThrownBy {
      val startInclusive = -1L
      nonEmpty.drop(startInclusive)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testThrowWhenNegativeStartIndexOnInfiniteSeq() {
    val infinite = primes()

    assertThatThrownBy {
      val startInclusive = -1L
      infinite.drop(startInclusive)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testReturnEmptySeq() {
    assertThat(emptyLazySeq<Any>().drop(0)).isEmpty()
    assertThat(emptyLazySeq<Any>().drop(5)).isEmpty()
    assertThat(lazySeqOf(1, 2).drop(2)).isEmpty()
  }

  @Test
  fun testReturnSubstreamWithoutPrefix() {
    assertThat(lazySeqOf(1, 2, 3, 4, 5).drop(2)).isEqualTo(lazySeqOf(3, 4, 5))

    val naturals = LazySeq.numbers(0)
    val fromFive = naturals.drop(5)
    assertThat(fromFive.take(4)).isEqualTo(lazySeqOf(5, 6, 7, 8))
  }

  @Test
  fun testNotEvaluateTailExceptFirstElement() {
    // given
    val naturals = lazySeqOf(1, supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(LazySeq.cons(2, { supplierMock.invoke() }))
    // when
    naturals.drop(2)
    // then
    Mockito.verify(supplierMock, times(2)).invoke()
  }

}