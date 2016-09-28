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
 * SizeTest
 * @author sunghyouk.bae@gmail.com
 */
class SizeTest : AbstractPermutationTest() {

  @Test
  fun testEmptySeq() {
    assertThat(emptyPermutation).hasSize(0)
    assertThat(emptyPermutation.size).isEqualTo(0)
  }

  @Test
  fun testFixedSeq() {
    assertThat(permutationOf(1)).hasSize(1)
    assertThat(permutationOf(2, 3)).hasSize(2)
    assertThat(permutationOf('a', 'b', 'c')).hasSize(3)
    assertThat(permutationOf(expectedList)).hasSize(expectedList.size)
  }

  @Test
  fun testLazySeq() {
    val lazy = lazy()

    assertThat(lazy.size).isEqualTo(expectedList.size)
  }
}