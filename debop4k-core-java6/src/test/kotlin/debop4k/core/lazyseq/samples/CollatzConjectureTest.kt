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

package debop4k.core.lazyseq.samples

import debop4k.core.lazyseq.AbstractLazySeqTest
import debop4k.core.lazyseq.LazySeq
import debop4k.core.lazyseq.lazySeqOf
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.junit.Test

class CollatzConjectureTest : AbstractLazySeqTest() {

  private val LOOP_COUNT = 1000

  @Test
  fun testProduceWholeCollatzConjecture() {
    val collatz = collatz(10)
    assertThat(collatz.startsWith(listOf(10L, 5L, 16L))).isTrue()
    assertThat(collatz).isEqualTo(lazySeqOf<Long>(10, 5, 16, 8, 4, 2, 1))
  }

  @Test
  fun testVeryLongProgression() {
    for (i in 0..LOOP_COUNT) {
      val collatz = collatz(63728127)
      assertThat(collatz).hasSize(950)
    }
  }

  @Test
  fun xestVeryLongProgressionCached() {
    for (i in 0..LOOP_COUNT) {
      val collatz = cacheCollatz(63728127)
      assertThat(collatz).hasSize(950)
    }
//    assertThat(collatzCache)
//        .containsKey(63728127)
//        .containsKey(16L)
//        .containsKey(8L)
  }

  private fun collatz(from: Long): LazySeq<Long> {
    if (from > 1) {
      val next = if (from % 2 == 0L) from / 2 else from * 3 + 1
      return LazySeq.cons(from) { collatz(next) }
    } else {
      return lazySeqOf(1)
    }
  }

  val collatzCache = ConcurrentHashMap<Long, LazySeq<Long>>().apply { put(1, lazySeqOf<Long>(1)) }

  fun cacheCollatz(from: Long): LazySeq<Long> {
    return collatzCache.getIfAbsentPut(from) { from ->
      val next = if (from % 2L == 0L) from / 2 else from * 3 + 1
      LazySeq.cons(from) { cacheCollatz(next) }
    }
  }
}