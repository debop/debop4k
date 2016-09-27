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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito

class LazySeqAllMatchTest : AbstractLazySeqTest() {

//  @Mock
//  private val supplierMock: () -> LazySeq<String> = uninitialized()

  val supplierMock = mock<() -> LazySeq<String>>()

  @Test
  fun testEmptySeq() {
    assertThat(emptyLazySeq<String>().allMatch { x -> false }).isTrue()
  }

  @Test
  fun testSingleSeq() {
    val single = lazySeqOf<Int>(1)
    val all = single.allMatch { x -> true }
    assertThat(all).isTrue()

    val gtZero = single.allMatch { it > 0 }
    assertThat(gtZero).isTrue()

    val allNot = single.allMatch { x -> false }
    assertThat(allNot).isFalse()

    val ltZero = single.allMatch { it < 0 }
    assertThat(ltZero).isFalse()

    assertThat(single.allMatch { it % 2 == 0 }).isFalse()
    assertThat(single.allMatch { it % 2 == 1 }).isTrue()
  }

  @Test
  fun testFiniteSeqWithPredicate() {
    val fixed = lazySeqOf(5, 10, 15)

    assertThat(fixed.allMatch { it % 5 == 0 }).isTrue()
    assertThat(fixed.allMatch { it <= 10 }).isFalse()
  }

  @Test
  fun testFiniteSeqWithStringMatches() {
    val fixed = lazySeqOf("a", "bc", "def")

    assertThat(fixed.allMatch(String::isEmpty)).isFalse()
    assertThat(fixed.allMatch(String::isBlank)).isFalse()
    assertThat(fixed.allMatch(String::isNotEmpty)).isTrue()
  }

  @Test
  fun testLazyFiniteSeq() {
    val lazy = LazySeq.cons(3) { LazySeq.cons(2) { lazySeqOf(8) } }

    assertThat(lazy.allMatch { it > 0 }).isTrue()
    assertThat(lazy.allMatch { it <= 0 }).isFalse()
    assertThat(lazy.allMatch { it % 2 == 0 }).isFalse()

    val lazy2 = LazySeq.cons(3) { LazySeq.cons(-5) { lazySeqOf(9) } }

    assertThat(lazy2.allMatch { it > 0 }).isFalse()
    assertThat(lazy2.allMatch { it <= 0 }).isFalse()
    assertThat(lazy2.allMatch { it % 2 == 0 }).isFalse()
    assertThat(lazy2.allMatch { it % 2 != 0 }).isTrue()
  }

  @Test
  fun testWithPrimes() {
    val primes = primes()

    assertThat(primes.allMatch { it % 2 != 0 }).isFalse()
    assertThat(primes.allMatch { it < 1000 }).isFalse()
  }

  @Test
  fun testNotEvaluateTailIfHeadNotMatchPredicate() {
    val lazy = LazySeq.cons("a", supplierMock)

    assertThat(lazy.allMatch(String::isEmpty))
    // supplierMock 은 호출되지 않는다.
    Mockito.verifyZeroInteractions(supplierMock)
  }


  @Test
  fun testEvaluateTailIfHeadMatchPredicate() {
    val lazy = LazySeq.cons("", supplierMock)

    BDDMockito.given(supplierMock.invoke()).willReturn(lazySeqOf("b"))

    assertThat(lazy.allMatch(String::isEmpty))
    verify(supplierMock).invoke()
  }
}