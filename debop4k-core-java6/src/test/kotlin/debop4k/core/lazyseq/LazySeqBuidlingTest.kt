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
import java.util.Arrays.*
import java.util.Collections.*

/**
 * LazySeqBuidlingTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqBuidlingTest : AbstractLazySeqTest() {

  @Test
  fun testCreateEmptySeq() {
    assertThat(emptyLazySeq<Any>()).isEmpty()
    assertThat(emptyLazySeq<Any>().isEmpty()).isTrue()
    assertThat(emptyLazySeq<Any>().size).isZero()
  }

  @Test
  fun testCreateEmptyVarargSeq() {
    assertThat(lazySeqOf<Any>()).isEmpty()
    assertThat(lazySeqOf<Any>().isEmpty()).isTrue()
    assertThat(lazySeqOf<Any>().size).isZero()
  }

  @Test
  fun testCreateEmptyCollectionsSeq() {
    assertThat(lazySeqOf<Any>(emptyList())).isEmpty()
    assertThat(lazySeqOf<Any>(emptyList()).isEmpty()).isTrue()
    assertThat(lazySeqOf<Any>(emptyList()).size).isZero()
  }

  @Test
  fun testCreateEmptyIteratorSeq() {
    assertThat(lazySeqOf<Any>(emptyIterator())).isEmpty()
    assertThat(lazySeqOf<Any>(emptyIterator()).isEmpty()).isTrue()
    assertThat(lazySeqOf<Any>(emptyIterator()).size).isZero()
  }

  @Test
  fun testCreateSeqWithOneElement() {
    assertThat(lazySeqOf<Int>(1)).hasSize(1)
    assertThat(lazySeqOf<Int>(1).isEmpty()).isFalse()
    assertThat(lazySeqOf<Int>(1).size).isEqualTo(1)
  }

  @Test
  fun testCreateSeqWithTwoElements() {
    assertThat(lazySeqOf<Int>(2, 3)).hasSize(2)
    assertThat(lazySeqOf<Int>(2, 3).isEmpty()).isFalse()
    assertThat(lazySeqOf<Int>(2, 3).size).isEqualTo(2)
  }

  @Test
  fun testCreateSeqWithThreeElements() {
    assertThat(lazySeqOf<Int>(4, 5, 6)).hasSize(3)
    assertThat(lazySeqOf<Int>(4, 5, 6).isEmpty()).isFalse()
    assertThat(lazySeqOf<Int>(4, 5, 6).size).isEqualTo(3)
  }

  @Test
  fun testCreateSeqWithSeveralElements() {
    assertThat(lazySeqOf<Int>(7, 8, 9, 1, 2, 3, 4, 5, 6)).hasSize(9)
    assertThat(lazySeqOf<Int>(7, 8, 9, 1, 2, 3, 4, 5, 6).isEmpty()).isFalse()
    assertThat(lazySeqOf<Int>(7, 8, 9, 1, 2, 3, 4, 5, 6).size).isEqualTo(9)
  }

  @Test
  fun testCreateSeqWithSeveralElementsFromIterable() {
    assertThat(lazySeqOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6))).hasSize(9)
    assertThat(lazySeqOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6)).isEmpty()).isFalse()
    assertThat(lazySeqOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6)).size).isEqualTo(9)
  }

  @Test
  fun testCreateSeqWithSeveralElementsFromIterator() {
    assertThat(lazySeqOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6).iterator())).hasSize(9)
    assertThat(lazySeqOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6).iterator()).isEmpty()).isFalse()
    assertThat(lazySeqOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6).iterator()).size).isEqualTo(9)
  }

  @Test
  fun testCreateInfiniteSeqStartingFromFewFixedElements() {
    val infinite = lazySeqOf(1, 2, 3) { LazySeq.numbers(4) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqStartingFromIterableAndConcreteSeq() {
    val infinite = LazySeq.concat(asList(1, 2, 3)) { LazySeq.numbers(4) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqStartingFromIteratorAndConcreteSeq() {
    val infinite = LazySeq.concat(asList(1, 2, 3).iterator()) { LazySeq.numbers(4) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqWithConsAndSupplier() {
    val infinite = LazySeq.cons(1) { LazySeq.numbers(2) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqWithConsAndConcreteSeq() {
    val infinite = LazySeq.cons(1, LazySeq.numbers(2))
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }
}