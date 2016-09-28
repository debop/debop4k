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
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import debop4k.core.collections.permutations.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito

class AnyMatchTest : AbstractPermutationTest() {

  val supplierMock = mock<() -> Permutation<String>>()

  @Test
  fun testEmptySeq() {
    assertThat(emptyPermutation<String>().anyMatch { x -> false }).isFalse()
  }

  @Test
  fun testSingleSeq() {
    val single = permutationOf<Int>(1)

    assertThat(single.anyMatch { x -> true }).isTrue()
    assertThat(single.anyMatch { it > 0 }).isTrue()
    assertThat(single.anyMatch { x -> false }).isFalse()
    assertThat(single.anyMatch { it < 0 }).isFalse()
    assertThat(single.anyMatch { it % 2 == 0 }).isFalse()
    assertThat(single.anyMatch { it % 2 == 1 }).isTrue()
  }

  @Test
  fun testFiniteSeqWithPredicate() {
    val fixed = permutationOf(5, 10, 15)

    assertThat(fixed.anyMatch { it % 5 == 0 }).isTrue()
    assertThat(fixed.anyMatch { it <= 10 }).isTrue()
    assertThat(fixed.anyMatch { it > 100 }).isFalse()
  }

  @Test
  fun testFiniteSeqWithStringMatches() {
    val fixed = permutationOf("a", "bc", "def")

    assertThat(fixed.anyMatch(String::isEmpty)).isFalse()
    assertThat(fixed.anyMatch(String::isBlank)).isFalse()
    assertThat(fixed.anyMatch(String::isNotEmpty)).isTrue()
  }

  @Test
  fun testLazyFiniteSeq() {
    val lazy = cons(3) { cons(2) { permutationOf(8) } }

    assertThat(lazy.anyMatch { it > 0 }).isTrue()
    assertThat(lazy.anyMatch { it <= 0 }).isFalse()
    assertThat(lazy.anyMatch { it % 2 == 0 }).isTrue()

    val lazy2 = cons(3) { cons(-5) { permutationOf(9) } }

    assertThat(lazy2.anyMatch { it > 0 }).isTrue()
    assertThat(lazy2.anyMatch { it <= 0 }).isTrue()
    assertThat(lazy2.anyMatch { it % 2 == 0 }).isFalse()
    assertThat(lazy2.anyMatch { it % 2 != 0 }).isTrue()
  }

  @Test
  fun testWithPrimes() {
    val primes = primes()

    assertThat(primes.anyMatch { it % 2 != 0 }).isTrue()
    assertThat(primes.anyMatch { it < 1000 }).isTrue()
  }

  @Test
  fun testNotEvaluateTailIfHeadNotMatchPredicate() {
    val lazy = cons("a", supplierMock)
    assertThat(lazy.anyMatch { !it.isEmpty() })
    verifyZeroInteractions(supplierMock)
  }


  @Test
  fun testEvaluateTailIfHeadMatchPredicate() {
    val lazy = cons("", supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(permutationOf("b"))

    assertThat(lazy.anyMatch { !it.isEmpty() })
    verify(supplierMock).invoke()
  }
}