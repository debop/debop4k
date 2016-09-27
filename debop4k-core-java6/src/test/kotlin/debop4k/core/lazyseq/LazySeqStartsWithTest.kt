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

import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * LazySeqStartsWithTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqStartsWithTest : AbstractLazySeqTest() {

  @Test
  fun testEmptySeq() {
    assertThat(emptySeq.startsWith(emptyLazySeq())).isTrue()

    assertThat(emptySeq.startsWith(lazySeqOf(1))).isFalse()
    assertThat(emptySeq.startsWith(lazySeqOf(1, 2))).isFalse()
  }

  @Test
  fun testFixedSeqStartsWithEmptySeq() {
    assertThat(lazySeqOf(1).startsWith(emptyIntSeq)).isTrue()
    assertThat(lazySeqOf(1, 2).startsWith(emptyIntSeq)).isTrue()
  }

  @Test
  fun testStartsWithSelfFixedSeq() {
    val fixed = lazySeqOf(1, 2, 3)
    assertThat(fixed.startsWith(fixed)).isTrue()

    assertThat(fixed.startsWith(listOf())).isTrue()
    assertThat(fixed.startsWith(listOf(1))).isTrue()
    assertThat(fixed.startsWith(listOf(1, 2))).isTrue()
    assertThat(fixed.startsWith(listOf(1, 2, 3))).isTrue()
    assertThat(fixed.startsWith(listOf(1, 2, 3, 4))).isFalse()
  }

  @Test
  fun testStartsWithInfiniteSeq() {
    assertThat(primes().startsWith(listOf(2, 3, 5, 7))).isTrue()
    assertThat(primes().startsWith(listOf(2, 3, 4))).isFalse()
  }
}