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

/**
 * ToStringTest
 * @author sunghyouk.bae@gmail.com
 */
class ToStringTest : AbstractPermutationTest() {

  @Test fun testEmptySeqToString() {
    assertThat(emptyPermutation.toString()).isEqualTo("[]")
  }

  @Test fun testFixedSeqToString() {
    assertThat(permutationOf('x').toString()).isEqualTo("[x]")
    assertThat(permutationOf('x', 'y').toString()).isEqualTo("[x, y]")
    assertThat(permutationOf('x', 'y', 'z').toString()).isEqualTo("[x, y, z]")
  }

  @Test fun testInfiniteWithFirstElementToString() {
    assertThat(numbers(1).toString()).isEqualTo("[1, ?]")

    val numbers = numbers(0)
    numbers[4]
    assertThat(numbers.toString()).isEqualTo("[0, 1, 2, 3, 4, ?]")
  }

  @Test fun testMixedSeqToString() {
    val notFull = permutationOf(1, 2) { permutationOf(3) }
    assertThat(notFull.toString()).isEqualTo("[1, 2, ?]")
  }

  @Test fun testTerminatedSeqToString() {
    val notFull: Permutation<Int> = permutationOf<Int>(1, 2, { emptyPermutation() })

    assertThat(notFull.toString()).isEqualTo("[1, 2, ?]")

    notFull.forEach { it }
    assertThat(notFull.toString()).isEqualTo("[1, 2]")
  }
}