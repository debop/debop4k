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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * GetTest
 * @author sunghyouk.bae@gmail.com
 */
class GetTest : AbstractPermutationTest() {

  @Test
  fun testThrowWhenNegativeGet() {
    val naturals = numbers(1)

    assertThatThrownBy {
      naturals.get(-1)
    }.isInstanceOf(IndexOutOfBoundsException::class.java)
  }

  @Test
  fun testThrowWhenTryingToAccessFirstOfEmptySeq() {
    val empty = emptyPermutation<Int>()

    assertThatThrownBy {
      empty.get(0)
    }.isInstanceOf(IndexOutOfBoundsException::class.java)
  }

  @Test
  fun testThrowWhenTryingToAccessPastEndOfLongerSeq() {
    val seq = permutationOf(1, 2, 3)

    assertThatThrownBy {
      seq.get(3)
    }.isInstanceOf(IndexOutOfBoundsException::class.java)
  }

  @Test
  fun testReturnHeadWhenGettingFirstElement() {
    assertThat(permutationOf("a")[0]).isEqualTo("a")
    assertThat(permutationOf("b", "c", "d")[0]).isEqualTo("b")
    assertThat(cons("w") { emptyPermutation() }.head).isEqualTo("w")
  }

  @Test
  fun testReturnLastElementOfFixedSeq() {
    assertThat(permutationOf('a')[0]).isEqualTo('a')
    assertThat(permutationOf('a', 'b')[1]).isEqualTo('b')
    assertThat(permutationOf('a', 'b', 'c')[2]).isEqualTo('c')
  }

  @Test
  fun testGetElementFarInTheSeq() {
    val naturals = numbers(0)
    val natual = naturals[100000]
    assertThat(natual).isEqualTo(100000)
  }
}