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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * MkStringTest
 * @author sunghyouk.bae@gmail.com
 */
class MkStringTest : AbstractPermutationTest() {

  @Test
  fun testEmptySeq() {
    assertThat(emptyPermutation.mkString(", ", "[", "]")).isEqualTo("[]")
  }

  @Test
  fun testDisplayWholeElements() {
    assertThat(permutationOf('x').mkString()).isEqualTo("x")
    assertThat(permutationOf('x', 'y').mkString()).isEqualTo("x, y")
    assertThat(permutationOf('x', 'y', 'z').mkString()).isEqualTo("x, y, z")
    assertThat(permutationOf(listOf('x', 'y', 'z')).mkString()).isEqualTo("x, y, z")
    assertThat(numbers(1).take(4).mkString()).isEqualTo("1, 2, 3, 4")
  }

  @Test
  fun testWorkWithArbitraryStartSepAndEndString() {
    assertThat(permutationOf('x', 'y').mkString("-")).isEqualTo("x-y")
    assertThat(permutationOf('x', 'y').mkString(";", "{", "}")).isEqualTo("{x;y}")
    assertThat(permutationOf('x', 'y').mkString("/", "[", "]")).isEqualTo("[x/y]")
  }

  @Test
  fun testInfiniteSeq() {
    val numbers = numbers(1)
    numbers.take(3).forEach { }
    assertThat(numbers.mkString(", ", "[", "]", true)).isEqualTo("[1, 2, 3, ?]")
  }
}