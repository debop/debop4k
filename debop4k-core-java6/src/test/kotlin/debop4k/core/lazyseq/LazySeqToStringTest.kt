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
import org.junit.Test

/**
 * LazySeqToStringTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqToStringTest : AbstractLazySeqTest() {

  @Test fun testEmptySeqToString() {
    assertThat(emptySeq.toString()).isEqualTo("[]")
  }

  @Test fun testFixedSeqToString() {
    assertThat(lazySeqOf('x').toString()).isEqualTo("[x]")
    assertThat(lazySeqOf('x', 'y').toString()).isEqualTo("[x, y]")
    assertThat(lazySeqOf('x', 'y', 'z').toString()).isEqualTo("[x, y, z]")
  }

  @Test fun testInfiniteWithFirstElementToString() {
    assertThat(LazySeq.numbers(1).toString()).isEqualTo("[1, ?]")

    val numbers = LazySeq.numbers(0)
    numbers[4]
    assertThat(numbers.toString()).isEqualTo("[0, 1, 2, 3, 4, ?]")
  }

  @Test fun testMixedSeqToString() {
    val notFull = lazySeqOf(1, 2) { lazySeqOf(3) }
    assertThat(notFull.toString()).isEqualTo("[1, 2, ?]")
  }

  @Test fun testTerminatedSeqToString() {
    val notFull: LazySeq<Int> = lazySeqOf(1, 2) { emptyLazySeq() }

    assertThat(notFull.toString()).isEqualTo("[1, 2, ?]")

    notFull.take(100)
    assertThat(notFull.toString()).isEqualTo("[1, 2, ?]")
  }
}