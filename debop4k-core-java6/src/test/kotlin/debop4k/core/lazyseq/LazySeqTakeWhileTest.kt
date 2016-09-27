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
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * LazySeqTakeWhileTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqTakeWhileTest : AbstractLazySeqTest() {

  private val supplierMock = mock<() -> LazySeq<String>>()

  @Test fun testTakingEmptySeq() {
    assertThat(emptySeq.takeWhile { true }).isEmpty()
  }

  @Test fun testTakingFixedSeq() {
    assertThat(lazySeqOf(-3).takeWhile { it > 0 }).isEmpty()
    assertThat(lazySeqOf(1).takeWhile { it > 0 }).isEqualTo(lazySeqOf(1))
    assertThat(lazySeqOf(-1, 2, 3).takeWhile { it > 0 }).isEmpty()
    assertThat(lazySeqOf(-1, 2, 3).takeWhile { it < 0 }).isEqualTo(lazySeqOf(-1))

    val strings = lazySeqOf("a", "b", "cd", "efg")
    assertThat(strings.takeWhile { it.length < 2 }).hasSize(2).isEqualTo(lazySeqOf("a", "b"))
    assertThat(strings.takeWhile { !it.isEmpty() }).hasSize(4).isEqualTo(lazySeqOf("a", "b", "cd", "efg"))
  }

  @Test fun testTakingInfiniteSeq() {
    val primes = primes()
    assertThat(primes.takeWhile { it < 10 }).isEqualTo(lazySeqOf(2, 3, 5, 7))
    assertThat(primes.takeWhile { it > 1 }.take(5)).isEqualTo(lazySeqOf(2, 3, 5, 7, 11))
  }

  @Test fun testNotEvaluateTailIfFailedPredicate() {
    val seq = LazySeq.cons("a", supplierMock)

    seq.takeWhile(String::isEmpty)
    verifyZeroInteractions(supplierMock)
  }
}