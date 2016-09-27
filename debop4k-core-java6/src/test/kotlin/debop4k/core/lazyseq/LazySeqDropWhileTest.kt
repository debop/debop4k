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
 * LazySeqDropWhileTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqDropWhileTest : AbstractLazySeqTest() {

  @Test
  fun testEmptySeq() {
    val empty = emptyLazySeq<Any>()
    val filtered = empty.dropWhile { true }
    assertThat(filtered).isEmpty()
  }

  @Test
  fun testSingleElement() {
    val single = lazySeqOf(1)
    val filtered = single.dropWhile { it > 0 }
    assertThat(filtered).isEmpty()

    val positives = single.dropWhile { it < 0 }
    assertThat(positives).isEqualTo(lazySeqOf(1))
  }

  @Test
  fun testFixedSeq() {
    val fixed = lazySeqOf(2, 4, 6, 8, 10)
    val overFive = fixed.dropWhile { it < 5 }
    assertThat(overFive).isEqualTo(lazySeqOf(6, 8, 10))

    val positives = fixed.dropWhile { it < 0 }
    assertThat(positives).isEqualTo(fixed)
  }

  @Test
  fun testInifiteSeq() {
    val primes = primes()
    val filtered = primes.dropWhile { it < 10 }
    assertThat(filtered.take(5)).isEqualTo(lazySeqOf(11, 13, 17, 19, 23))
  }
}