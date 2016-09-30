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

import debop4k.core.collections.permutations.samples.piSeriesEstimation
import org.apache.commons.lang3.StringUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.Test

/**
 * LazySeqTabulateTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqTabulateTest : AbstractPermutationTest() {

  @Test
  fun testGrowingStrings() {
    val strings = tabulate(0) { StringUtils.repeat("*", it) }
    assertThat(strings.take(6)).isEqualTo(permutationOf("", "*", "**", "***", "****", "*****"))

    val seq = generateSequence("") { StringUtils.repeat("*", it.length + 1) }
    assertThat(seq.take(6).toList()).isEqualTo(listOf("", "*", "**", "***", "****", "*****"))
  }

  @Test
  fun testCreateInfiniteStream() {
    val piSeries = piSeriesEstimation()
    val piEstimation = piSeries.limit(1000).reduce { acc: Double, x: Double -> acc + x }!! * 4
    assertThat(piEstimation).isEqualTo(Math.PI, offset(0.01))
  }

  @Test
  fun testCreateInfiniteStreamWithTransforming() {
    val piSeries = piSeriesEstimation().map { it * 4.0 }
    val piEstimation = piSeries.limit(1000).reduce { acc: Double, x: Double -> acc + x }
    assertThat(piEstimation).isEqualTo(Math.PI, offset(0.01))
  }
}