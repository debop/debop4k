/*
 * Copyright (c) 2016. KESTI co, ltd
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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import debop4k.core.collections.permutations.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ZipTest : AbstractPermutationTest() {

  private val firstSupplier = mock<() -> Permutation<String>>()
  private val secondSupplier = mock<() -> Permutation<Int>>()

  @Test fun testZipEmptyWithEmptySeq() {
    val empty = emptyIntPermutation
    val zipped = empty.zip(emptyIntPermutation) { a, b -> a + b }
    assertThat(zipped).isEmpty()
  }

  @Test fun testZipEmptyWithNonEmptySeq() {
    val zipped = emptyIntPermutation.zip(permutationOf(1)) { a, b -> a + b }
    assertThat(zipped).isEmpty()

    val reversed = permutationOf(1).zip(emptyIntPermutation) { a, b -> a + b }
    assertThat(reversed).isEmpty()
  }

  @Test fun testZipTwoFiniteSeqOfSameSize() {
    val first = permutationOf("A", "B", "C")
    val second = permutationOf(1, 2, 3)

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(permutationOf("A1", "B2", "C3"))
  }

  @Test fun testTwoLazySeqOfSameSize() {
    val first = cons("A") { cons("B") { permutationOf("C") } }
    val second = cons(1) { cons(2) { permutationOf(3) } }

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(permutationOf("A1", "B2", "C3"))
  }

  @Test fun testZipTwoFiniteSeqOfDiffSize() {
    val first = permutationOf("A", "B", "C")
    val second = permutationOf(1, 2, 3, 4)

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(permutationOf("A1", "B2", "C3"))
  }

  @Test fun testZipTwoFiniteSeqOfDiffSize2() {
    val first = permutationOf("A", "B", "C", "D")
    val second = permutationOf(1, 2, 3)

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(permutationOf("A1", "B2", "C3"))
  }

  @Test fun testTwoLazySeqOfDiffSize() {
    val first = cons("A") { cons("B") { permutationOf("C") } }
    val second = cons(1) { cons(2) { cons(3) { permutationOf(4) } } }

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(permutationOf("A1", "B2", "C3"))
  }

  @Test fun testTwoLazySeqOfDiffSize2() {
    val first = cons("A") { cons("B") { cons("C") { permutationOf("D") } } }
    val second = cons(1) { cons(2) { permutationOf(3) } }

    val zipped = first.zip(second) { s, i -> s + i.toString() }
    assertThat(zipped).isEqualTo(permutationOf("A1", "B2", "C3"))
  }

  @Test
  fun testZipTwoInfiniteSeq() {
    val naturals = numbers(1)
    val primes = primes()
    val zipped = naturals.zip(primes) { n, p -> "$n:$p" }

    assertThat(zipped.take(5)).isEqualTo(permutationOf("1:2", "2:3", "3:5", "4:7", "5:11"))

    val fixedPrimes = primes().take(5)
    val zippedFixed = naturals.zip(fixedPrimes) { n, p -> "$n:$p" }

    assertThat(zippedFixed).isEqualTo(permutationOf("1:2", "2:3", "3:5", "4:7", "5:11"))
  }

  @Test fun testNotEvaluateTailWhenZippingTwoSequence() {
    val first = cons("A", firstSupplier)
    val second = cons(1, secondSupplier)

    first.zip(second) { c, i -> c + i }

    verifyZeroInteractions(firstSupplier)
    verifyZeroInteractions(secondSupplier)
  }
}