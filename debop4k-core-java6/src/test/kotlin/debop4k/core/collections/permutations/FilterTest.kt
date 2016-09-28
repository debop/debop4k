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

package debop4k.core.collections.permutations

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito

/**
 * FilterTest
 * @author sunghyouk.bae@gmail.com
 */
class FilterTest : AbstractPermutationTest() {

  val supplierMock = mock<() -> Permutation<String>>()

  @Test
  fun testEmptySeqFilter() {
    assertThat(emptyPermutation<Any>().filter { false }).isEqualTo(emptyPermutation<Any>())
  }

  @Test
  fun testFixedSeqNoMatches() {
    val fixed = permutationOf(-1, -2, -3)
    val filtered = fixed.filter { it > 0 }
    assertThat(filtered).isEmpty()
  }

  @Test
  fun testFixedLazySeqNoMatches() {
    val fixed = permutationOf(-1, -2) { permutationOf(-3, -4) }
    val filtered = fixed.filter { it > 0 }
    assertThat(filtered).isEmpty()
  }

  @Test
  fun testNotEvaluateTailIfHeadMatches() {
    val generated = permutationOf("A", "BB", supplierMock)
    generated.filter { !it.isEmpty() }

    Mockito.verifyZeroInteractions(supplierMock)
  }

  @Test
  fun testNotEvaluateTailIfHeadNotMatches() {
    // given
    val generated = permutationOf("", supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(permutationOf("C"))

    // when
    generated.filter { !it.isEmpty() }

    // then
    Mockito.verify(supplierMock, times(1)).invoke()
  }

  @Test
  fun testEvaluateTailMultipleTimesToReturnLastElement() {
    val generated =
        cons("a") {
          cons("bb") {
            cons("ccc") {
              emptyPermutation()
            }
          }
        }
    val filtered = generated.filter { it.length >= 3 }
    assertThat(filtered).isEqualTo(permutationOf("ccc"))
  }

  @Test
  fun testFilterServeralItemsFromFiniteSeq() {
    val naturals = numbers(1)
    val filtered = naturals.filter { it % 3 == 0 }

    assertThat(filtered.take(4)).containsExactly(3, 6, 9, 12)
  }
}