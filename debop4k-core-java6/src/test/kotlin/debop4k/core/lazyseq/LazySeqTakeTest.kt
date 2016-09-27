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
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

/**
 * LazySeqTakeTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqTakeTest : AbstractLazySeqTest() {

  private val supplierMock = mock<() -> LazySeq<Int>>()

  @Test
  fun testTakeWithNegativeArg() {
    val seq = lazySeqOf(1, 2, 3)

    assertThatThrownBy {
      seq.limit(-1)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun testTakeWithEmptySeq() {
    assertThat(emptySeq.limit(10)).isEmpty()
  }

  @Test
  fun testTakeWithFixedSeq() {
    val fixed = lazySeqOf(1, 2, 3, 4, 5)
    val limited = fixed.limit(3)
    assertThat(limited).isEqualTo(lazySeqOf(1, 2, 3))
  }

  @Test
  fun testTakeWithInfiniteSeq() {
    val numbers = LazySeq.numbers(1)
    val limited = numbers.limit(3)
    assertThat(limited).isEqualTo(lazySeqOf(1, 2, 3))
  }

  @Test
  fun testNotEvaluateTail() {
    val infinite = lazySeqOf(1, supplierMock)

    infinite.take(10)

    verifyZeroInteractions(supplierMock)
  }

}