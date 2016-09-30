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
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito

/**
 * ForceTest
 * @author sunghyouk.bae@gmail.com
 */
class ForceTest : AbstractPermutationTest() {

  val supplierMock = mock<() -> Permutation<Int>>()

  @Test
  fun testForceEmptySeq() {
    assertThat(emptyPermutation<Int>().force()).isEqualTo(emptyPermutation<Int>())
  }

  @Test
  fun testForceWholeFiniteSeq() {
    val nums = numbers(1.0, 0.5).take(5)
    assertThat(nums.toString()).isEqualTo("[1.0, ?]")

    nums.force()
    assertThat(nums.toString()).isEqualTo("[1.0, 1.5, 2.0, 2.5, 3.0]")
  }

  @Test
  fun testForceOnlyPartOfTheSeq() {
    val nums = numbers(1.0, 0.5)

    nums.take(5).force()
    assertThat(nums.toString()).isEqualTo("[1.0, 1.5, 2.0, 2.5, 3.0, ?]")
  }

  @Test
  fun testDoNothingWhenForcingTwice() {
    val lazy = cons(1, supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(permutationOf(1))

    lazy.force().force()
    verify(supplierMock, times(1)).invoke()
  }
}