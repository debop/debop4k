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

import debop4k.core.collections.permutations.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * EqualsHashcodeTest
 * @author sunghyouk.bae@gmail.com
 */
class EqualsHashcodeTest : AbstractPermutationTest() {

  @Test
  fun twoEmptySeq() {
    val first = emptyPermutation<Any>()
    val second = emptyPermutation<Any>()

    assertThat(first.equals(second)).isTrue()
    assertThat(first.hashCode() == second.hashCode()).isTrue()
  }

  @Test
  fun emptySeqAndNotEmptySeq() {
    val first = permutationOf<Int>()
    val second = permutationOf<Int>(1)

    assertThat(first.equals(second)).isFalse()
    assertThat(second.equals(first)).isFalse()
  }

  @Test
  fun testSingleSeq() {
    val first = permutationOf<Int>(1)
    val second = permutationOf<Int>(1)
    val third = permutationOf<Int>(2)

    assertThat(first == second).isTrue()
    assertThat(first == third).isFalse()
    assertThat(second == third).isFalse()
  }

  @Test
  fun testFixedSeqAndLazyFiniteSeq() {
    val first = permutationOf(1, 2, 3, 4)
    val second = cons(1) { cons(2) { cons(3) { permutationOf(4) } } }

    assertThat(first == second).isTrue()
    assertThat(first.hashCode() == second.hashCode()).isTrue()
  }

  @Test
  fun testLazyButFiniteSequence() {
    val first = lazy()
    val second = lazy()

    assertThat(first == second).isTrue()
    assertThat(first.hashCode() == second.hashCode()).isTrue()
  }

  @Test
  fun testFiniteSeqAndInfiniteSeq() {
    val first = permutationOf(2, 3, 5, 7)
    val second = primes()
    val third = primes().take(4)

    assertThat(first == second).isFalse()
    assertThat(first == third).isTrue()
  }

  @Test
  fun testEmptySeqAndInfiniteSeq() {
    val first = emptyPermutation<Int>()
    val second = primes()

    assertThat(first.equals(second)).isFalse()
    assertThat(second.equals(first)).isFalse()
  }
}