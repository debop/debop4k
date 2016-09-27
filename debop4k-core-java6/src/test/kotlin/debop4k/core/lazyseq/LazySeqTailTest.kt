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

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.util.*

/**
 * LazySeqTailTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqTailTest : AbstractLazySeqTest() {

  @Test
  fun testAccessTailForEmptySeq() {
    assertThatThrownBy {
      emptySeq.tail
    }.isInstanceOf(NoSuchElementException::class.java)
  }

  @Test
  fun testAccessTailForSingleSeq() {
    assertThat(lazySeqOf(1).tail).isEmpty()
  }

  @Test
  fun testAccessTailForFixedSeq() {
    assertThat(lazySeqOf(1).tail).isEmpty()
    assertThat(lazySeqOf(2, 3).tail).isEqualTo(lazySeqOf(3))
    assertThat(lazySeqOf(4, 5, 6).tail).isEqualTo(lazySeqOf(5, 6))
    assertThat(lazySeqOf(7, 8, 9, 10).tail).isEqualTo(lazySeqOf(8, 9, 10))
  }

  @Test
  fun testAccessTailForInfiniteSeq() {
    assertThat(LazySeq.numbers(1).tail.limit(4)).isEqualTo(lazySeqOf(2, 3, 4, 5))
  }
}