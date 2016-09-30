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
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import debop4k.core.collections.permutations.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito

/**
 * NoneMatchTest
 * @author sunghyouk.bae@gmail.com
 */
class NoneMatchTest : AbstractPermutationTest() {

  private val supplierMock = mock<() -> Permutation<String>>()

  @Test
  fun testAlwaysTruePredicateWithEmptySeq() {
    assertThat(emptyPermutation.noneMatch { true }).isTrue()
  }

  @Test
  fun testSingleElementSeq() {
    val single = permutationOf(1)
    assertThat(single.noneMatch { false }).isTrue()
    assertThat(single.noneMatch { it < 0 }).isTrue()

    assertThat(single.noneMatch { it % 2 != 0 }).isFalse()
  }

  @Test
  fun testMultiElementsSeq() {
    val fixed = permutationOf(5, 10, 15)

    assertThat(fixed.noneMatch { it % 5 != 0 }).isTrue()
    assertThat(fixed.noneMatch { it > 10 }).isFalse()
  }

  @Test
  fun testMultiElementsSeqWithString() {
    val fixed = permutationOf("a", "bc", "def")

    assertThat(fixed.noneMatch { !it.isEmpty() }).isFalse()
  }

  @Test
  fun testLazySeq() {
    val lazy = cons(3) {
      cons(2) {
        permutationOf(8)
      }
    }
    assertThat(lazy.noneMatch { it < 0 }).isTrue()
    assertThat(lazy.noneMatch { it % 2 == 0 }).isFalse()
  }

  @Test
  fun testInfiniteSeq() {
    val primes = primes()
    assertThat(primes.noneMatch { it % 2 == 0 }).isFalse()
    assertThat(primes.noneMatch { it > 1000 }).isFalse()
  }

  @Test
  fun testNotEvaluateTailIfHeadMatchesPredicate() {
    val lazy = cons("a", supplierMock)
    lazy.noneMatch { !it.isEmpty() }
    verifyZeroInteractions(supplierMock)
  }

  @Test
  fun testEvaluateTailOnlyOnceWhenHeadMatchesButSecondNotMatches() {
    val lazy = cons("a", supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(permutationOf(""))

    lazy.noneMatch(String::isEmpty)

    verify(supplierMock, times(1)).invoke()
  }
}