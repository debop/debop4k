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
 * DistinctTest
 * @author sunghyouk.bae@gmail.com
 */
class DistinctTest : AbstractPermutationTest() {

  @Test
  fun testReturnEmptySeqWhenCalledOnEmptySeq() {
    assertThat(emptyPermutation<Any>().distinct()).isEmpty()
  }

  @Test
  fun testReturnSeqWithOneElementWhenOnSeqWithOneElement() {
    val single = permutationOf(9)
    val dist = single.distinct()
    assertThat(dist).isEqualTo(permutationOf(9))
  }

  @Test
  fun testReturnTwoElementsWhenSeqWithTwoDifferentElements() {
    val twoDistinct = permutationOf(9, 7)
    val dist = twoDistinct.distinct()
    assertThat(dist.toList()).containsOnly(7, 9)
  }

  @Test
  fun testReturnOneElementWhenSeqWithTwoSameElement() {
    val twoSame = permutationOf(9, 9)
    val dist = twoSame.distinct()
    assertThat(dist).isEqualTo(permutationOf(9))
  }

  @Test
  fun testReturnAllElementsOnFixedAlreadyDistinctSeq() {
    val oneToFive = numbers(1).take(5)
    val dist = oneToFive.distinct()
    assertThat(dist.toList()).isEqualTo(listOf(1, 2, 3, 4, 5))
  }
}