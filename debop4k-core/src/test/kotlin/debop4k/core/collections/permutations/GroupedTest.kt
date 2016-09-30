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

import debop4k.core.collections.permutations.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * GroupedTest
 * @author sunghyouk.bae@gmail.com
 */
class GroupedTest : AbstractPermutationTest() {

  @Test
  fun testEmptySeq() {
    val empty = emptyPermutation<Any>()

    val grouped = empty.grouped(10)
    assertThat(grouped).isEmpty()
  }

  @Test
  fun testThrowWhenArgumentZero() {
    assertThatThrownBy {
      emptyPermutation<Any>().grouped(0)
    }.isInstanceOf(IllegalArgumentException::class.java)

    assertThatThrownBy {
      permutationOf(1, 2, 3).grouped(0)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testFixedSeqGrouping() {
    val fixed = permutationOf(5, 7, 9)
    val grouped = fixed.grouped(4)

    assertThat(grouped.head).containsExactly(5, 7, 9)
    assertThat(grouped).hasSize(1)
  }

  @Test
  fun testFixedSeqGroupingExactly() {
    val fixed = permutationOf(5, 7, 9, 11)
    val grouped = fixed.grouped(4)

    assertThat(grouped.head).containsExactly(5, 7, 9, 11)
    assertThat(grouped).hasSize(1)
  }

  @Test
  fun testFixedSeqGroupingOneMore() {
    val fixed = permutationOf(5, 7, 9, 11)
    val grouped = fixed.grouped(3)

    assertThat(grouped[0]).containsExactly(5, 7, 9)
    assertThat(grouped[1]).containsExactly(11)
    assertThat(grouped).hasSize(2)
  }

  @Test
  fun testFixedSeqGroupingOneMore2() {
    val fixed = permutationOf(5, 7, 9, 11)
    val grouped = fixed.grouped(2)

    assertThat(grouped[0]).containsExactly(5, 7)
    assertThat(grouped[1]).containsExactly(9, 11)
    assertThat(grouped).hasSize(2)
  }

  @Test
  fun testReturnWindowOfSizeOne() {
    val fixed = permutationOf(5, 7, 9)
    val grouped = fixed.grouped(1)

    assertThat(grouped[0]).containsExactly(5)
    assertThat(grouped[1]).containsExactly(7)
    assertThat(grouped[2]).containsExactly(9)
    assertThat(grouped).hasSize(3)
  }

  @Test
  fun testReturnWindowOfSizeOneForSingleElementSeq() {
    val fixed = permutationOf(3)
    val grouped = fixed.grouped(1)

    assertThat(grouped[0]).containsExactly(3)
    assertThat(grouped).hasSize(1)
  }

  @Test
  fun testReturnWindowOfSizeOneForEmptySeq() {
    val fixed = permutationOf<Any>()
    val grouped = fixed.grouped(1)

    assertThat(grouped).isEmpty()
  }

  @Test
  fun testGroupForInfiniteSeq() {
    val primes = primes()
    val grouped = primes.grouped(3)

    assertThat(grouped[0]).containsExactly(2, 3, 5)
    assertThat(grouped[1]).containsExactly(7, 11, 13)
    assertThat(grouped[2]).containsExactly(17, 19, 23)
  }
}