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
 * LazySeqMkStringTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqMkStringTest : AbstractLazySeqTest() {

  @Test
  fun testEmptySeq() {
    assertThat(emptyLazySeq<Any>().mkString(", ", "[", "]")).isEqualTo("[]")
  }

  @Test
  fun testDisplayWholeElements() {
    assertThat(lazySeqOf('x').mkString()).isEqualTo("x")
    assertThat(lazySeqOf('x', 'y').mkString()).isEqualTo("x, y")
    assertThat(lazySeqOf('x', 'y', 'z').mkString()).isEqualTo("x, y, z")
    assertThat(lazySeqOf(listOf('x', 'y', 'z')).mkString()).isEqualTo("x, y, z")
    assertThat(LazySeq.numbers(1).take(4).mkString()).isEqualTo("1, 2, 3, 4")
  }

  @Test
  fun testWorkWithArbitraryStartSepAndEndString() {
    assertThat(lazySeqOf('x', 'y').mkString("-")).isEqualTo("x-y")
    assertThat(lazySeqOf('x', 'y').mkString(";", "{", "}")).isEqualTo("{x;y}")
    assertThat(lazySeqOf('x', 'y').mkString("/", "[", "]")).isEqualTo("[x/y]")
  }

  @Test
  fun testInfiniteSeq() {
    val numbers = LazySeq.numbers(1)
    numbers.take(3).forEach { }
    assertThat(numbers.mkString(", ", "[", "]", true)).isEqualTo("[1, 2, 3, ?]")
  }
}