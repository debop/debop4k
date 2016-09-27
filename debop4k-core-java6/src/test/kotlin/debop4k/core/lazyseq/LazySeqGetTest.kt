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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * LazySeqGetTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqGetTest : AbstractLazySeqTest() {

  @Test
  fun testThrowWhenNegativeGet() {
    val naturals = LazySeq.numbers(1)

    assertThatThrownBy {
      naturals.get(-1)
    }.isInstanceOf(IndexOutOfBoundsException::class.java)
  }

  @Test
  fun testThrowWhenTryingToAccessFirstOfEmptySeq() {
    val empty = emptyLazySeq<Int>()

    assertThatThrownBy {
      empty.get(0)
    }.isInstanceOf(IndexOutOfBoundsException::class.java)
  }

  @Test
  fun testThrowWhenTryingToAccessPastEndOfLongerSeq() {
    val seq = lazySeqOf(1, 2, 3)

    assertThatThrownBy {
      seq.get(3)
    }.isInstanceOf(IndexOutOfBoundsException::class.java)
  }

  @Test
  fun testReturnHeadWhenGettingFirstElement() {
    assertThat(lazySeqOf("a")[0]).isEqualTo("a")
    assertThat(lazySeqOf("b", "c", "d")[0]).isEqualTo("b")
    assertThat(LazySeq.cons("w") { emptyLazySeq() }.head).isEqualTo("w")
  }

  @Test
  fun testReturnLastElementOfFixedSeq() {
    assertThat(lazySeqOf('a')[0]).isEqualTo('a')
    assertThat(lazySeqOf('a', 'b')[1]).isEqualTo('b')
    assertThat(lazySeqOf('a', 'b', 'c')[2]).isEqualTo('c')
  }

  @Test
  fun testGetElementFarInTheSeq() {
    val naturals = LazySeq.numbers(0)
    val natual = naturals[100000]
    assertThat(natual).isEqualTo(100000)
  }
}