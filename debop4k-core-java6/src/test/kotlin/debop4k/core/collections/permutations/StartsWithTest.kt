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
 * StartsWithTest
 * @author sunghyouk.bae@gmail.com
 */
class StartsWithTest : AbstractPermutationTest() {

  @Test
  fun testEmptySeq() {
    assertThat(emptyPermutation.startsWith(emptyPermutation)).isTrue()

    assertThat(emptyPermutation.startsWith(permutationOf(1))).isFalse()
    assertThat(emptyPermutation.startsWith(permutationOf(1, 2))).isFalse()
  }

  @Test
  fun testFixedSeqStartsWithEmptySeq() {
    assertThat(permutationOf(1).startsWith(emptyIntPermutation)).isTrue()
    assertThat(permutationOf(1, 2).startsWith(emptyIntPermutation)).isTrue()
  }

  @Test
  fun testStartsWithSelfFixedSeq() {
    val fixed = permutationOf(1, 2, 3)
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