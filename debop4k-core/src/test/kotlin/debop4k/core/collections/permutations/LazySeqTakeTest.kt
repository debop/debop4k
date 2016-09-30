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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * LazySeqTakeTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqTakeTest : AbstractPermutationTest() {

  private val supplierMock = mock<() -> Permutation<Int>>()

  @Test
  fun testTakeWithNegativeArg() {
    val seq = permutationOf(1, 2, 3)

    assertThatThrownBy {
      seq.limit(-1)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testTakeWithEmptySeq() {
    assertThat(emptyPermutation.limit(10)).isEmpty()
  }

  @Test
  fun testTakeWithFixedSeq() {
    val fixed = permutationOf(1, 2, 3, 4, 5)
    val limited = fixed.limit(3)
    assertThat(limited).isEqualTo(permutationOf(1, 2, 3))
  }

  @Test
  fun testTakeWithInfiniteSeq() {
    val limited = numbers(1).limit(3)
    assertThat(limited).isEqualTo(permutationOf(1, 2, 3))
  }

  @Test
  fun testNotEvaluateTail() {
    val infinite = permutationOf(1, supplierMock)
    infinite.take(10)
    verifyZeroInteractions(supplierMock)
  }

}