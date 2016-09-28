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

package debop4k.core.collections.permutations

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Arrays.*
import java.util.Collections.*

class BuidlingTest : AbstractPermutationTest() {

  @Test
  fun testCreateEmptySeq() {
    assertThat(emptyPermutation<Any>()).isEmpty()
    assertThat(emptyPermutation<Any>().isEmpty()).isTrue()
    assertThat(emptyPermutation<Any>().size).isZero()
  }

  @Test
  fun testCreateEmptyVarargSeq() {
    assertThat(permutationOf<Any>()).isEmpty()
    assertThat(permutationOf<Any>().isEmpty()).isTrue()
    assertThat(permutationOf<Any>().size).isZero()
  }

  @Test
  fun testCreateEmptyCollectionsSeq() {
    assertThat(permutationOf<Any>(emptyList())).isEmpty()
    assertThat(permutationOf<Any>(emptyList()).isEmpty()).isTrue()
    assertThat(permutationOf<Any>(emptyList()).size).isZero()
  }

  @Test
  fun testCreateEmptyIteratorSeq() {
    assertThat(permutationOf<Any>(emptyIterator())).isEmpty()
    assertThat(permutationOf<Any>(emptyIterator()).isEmpty()).isTrue()
    assertThat(permutationOf<Any>(emptyIterator()).size).isZero()
  }

  @Test
  fun testCreateSeqWithOneElement() {
    assertThat(permutationOf<Int>(1)).hasSize(1)
    assertThat(permutationOf<Int>(1).isEmpty()).isFalse()
    assertThat(permutationOf<Int>(1).size).isEqualTo(1)
  }

  @Test
  fun testCreateSeqWithTwoElements() {
    assertThat(permutationOf<Int>(2, 3)).hasSize(2)
    assertThat(permutationOf<Int>(2, 3).isEmpty()).isFalse()
    assertThat(permutationOf<Int>(2, 3).size).isEqualTo(2)
  }

  @Test
  fun testCreateSeqWithThreeElements() {
    assertThat(permutationOf<Int>(4, 5, 6)).hasSize(3)
    assertThat(permutationOf<Int>(4, 5, 6).isEmpty()).isFalse()
    assertThat(permutationOf<Int>(4, 5, 6).size).isEqualTo(3)
  }

  @Test
  fun testCreateSeqWithSeveralElements() {
    assertThat(permutationOf<Int>(7, 8, 9, 1, 2, 3, 4, 5, 6)).hasSize(9)
    assertThat(permutationOf<Int>(7, 8, 9, 1, 2, 3, 4, 5, 6).isEmpty()).isFalse()
    assertThat(permutationOf<Int>(7, 8, 9, 1, 2, 3, 4, 5, 6).size).isEqualTo(9)
  }

  @Test
  fun testCreateSeqWithSeveralElementsFromIterable() {
    assertThat(permutationOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6))).hasSize(9)
    assertThat(permutationOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6)).isEmpty()).isFalse()
    assertThat(permutationOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6)).size).isEqualTo(9)
  }

  @Test
  fun testCreateSeqWithSeveralElementsFromIterator() {
    assertThat(permutationOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6).iterator())).hasSize(9)
    assertThat(permutationOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6).iterator()).isEmpty()).isFalse()
    assertThat(permutationOf<Int>(asList(7, 8, 9, 1, 2, 3, 4, 5, 6).iterator()).size).isEqualTo(9)
  }

  @Test
  fun testCreateInfiniteSeqStartingFromFewFixedElements() {
    val infinite = permutationOf(1, 2, 3) { numbers(4) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqStartingFromIterableAndConcreteSeq() {
    val infinite = concat(asList(1, 2, 3)) { numbers(4) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqStartingFromIteratorAndConcreteSeq() {
    val infinite = concat(asList(1, 2, 3).iterator()) { numbers(4) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqWithConsAndSupplier() {
    val infinite = cons(1) { numbers(2) }
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  @Test
  fun testCreateInfiniteSeqWithConsAndConcreteSeq() {
//    val infinite = cons(1, numbers(2))
    val infinite = permutationOf(1) + numbers(2)
    assertThat(infinite.take(10)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }
}