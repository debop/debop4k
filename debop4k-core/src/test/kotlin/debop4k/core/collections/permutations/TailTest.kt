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
import java.util.*

/**
 * TailTest
 * @author sunghyouk.bae@gmail.com
 */
class TailTest : AbstractPermutationTest() {

  @Test
  fun testAccessTailForEmptySeq() {
    assertThatThrownBy {
      emptyPermutation.tail
    }.isInstanceOf(NoSuchElementException::class.java)
  }

  @Test
  fun testAccessTailForSingleSeq() {
    assertThat(permutationOf(1).tail).isEmpty()
  }

  @Test
  fun testAccessTailForFixedSeq() {
    assertThat(permutationOf(1).tail).isEmpty()
    assertThat(permutationOf(2, 3).tail).isEqualTo(permutationOf(3))
    assertThat(permutationOf(4, 5, 6).tail).isEqualTo(permutationOf(5, 6))
    assertThat(permutationOf(7, 8, 9, 10).tail).isEqualTo(permutationOf(8, 9, 10))
  }

  @Test
  fun testAccessTailForInfiniteSeq() {
    assertThat(numbers(1).tail.limit(4)).isEqualTo(permutationOf(2, 3, 4, 5))
  }
}