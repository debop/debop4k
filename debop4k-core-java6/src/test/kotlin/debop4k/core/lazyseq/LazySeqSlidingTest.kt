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

import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * LazySeqSlidingTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqSlidingTest : AbstractLazySeqTest() {

  @Test
  fun testEmptySeq() {
    val empty = emptyLazySeq<Any>()

    assertThat(empty.sliding(1)).isEmpty()
    assertThat(empty.sliding(10)).isEmpty()

    assertThatThrownBy {
      empty.sliding(0)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testFixedSeq() {
    val fixed = lazySeqOf(1, 2, 3)

    assertThatThrownBy {
      fixed.sliding(0)
    }.isInstanceOf(IllegalArgumentException::class.java)

    val sliding = fixed.sliding(4)
    assertThat(sliding.head).containsExactly(1, 2, 3)
    assertThat(sliding).hasSize(1)

    val sliding2 = fixed.sliding(3)
    assertThat(sliding2.head).containsExactly(1, 2, 3)
    assertThat(sliding2).hasSize(1)
  }

  @Test
  fun testFixedSeqWithSliding2() {
    val fixed = lazySeqOf(5, 7, 9, 11)

    val sliding = fixed.sliding(3)
    assertThat(sliding[0]).containsExactly(5, 7, 9)
    assertThat(sliding[1]).containsExactly(7, 9, 11)
    assertThat(sliding).hasSize(2)

    val sliding2 = fixed.sliding(2)
    assertThat(sliding2[0]).containsExactly(5, 7)
    assertThat(sliding2[1]).containsExactly(7, 9)
    assertThat(sliding2[2]).containsExactly(9, 11)
    assertThat(sliding2).hasSize(3)
  }

  @Test
  fun testFixedSeqWithSlidingOne() {
    val fixed = lazySeqOf(5, 7, 9, 11)

    val sliding = fixed.sliding(1)
    assertThat(sliding[0]).containsExactly(5)
    assertThat(sliding[1]).containsExactly(7)
    assertThat(sliding[2]).containsExactly(9)
    assertThat(sliding[3]).containsExactly(11)
    assertThat(sliding).hasSize(4)
  }

  @Test
  fun testSingleSeqWithSlidingOne() {
    val fixed = lazySeqOf(3)

    val sliding = fixed.sliding(1)
    assertThat(sliding[0]).containsExactly(3)
    assertThat(sliding).hasSize(1)
  }

  @Test
  fun testInfiniteSeq() {
    val primes = primes()

    val sliding = primes.sliding(3)

    assertThat(sliding[0]).containsExactly(2, 3, 5)
    assertThat(sliding[1]).containsExactly(3, 5, 7)
    assertThat(sliding[2]).containsExactly(5, 7, 11)
    assertThat(sliding[3]).containsExactly(7, 11, 13)
  }
}