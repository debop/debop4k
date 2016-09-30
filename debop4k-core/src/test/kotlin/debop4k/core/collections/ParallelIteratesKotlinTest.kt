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

package debop4k.core.collections

import debop4k.core.AbstractCoreKotlinTest
import org.assertj.core.api.Assertions
import org.eclipse.collections.api.multimap.MutableMultimap
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.junit.Test

/**
 * ParallelIteratesKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class ParallelIteratesKotlinTest : AbstractCoreKotlinTest() {

  private val xs: FastList<Int>
    get() {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }
      return xs
    }

  private val xss: FastList<List<Int>> = fastListOf(fastListOf(0, 1, 2),
                                                    fastListOf(3, 4, 5),
                                                    fastListOf(6, 7, 8))

  @Test fun `parallel filter`() {
    val even = xs.parFilter { it % 2 == 0 }
    val odd = xs.parFilter { it % 2 == 1 }

    Assertions.assertThat(even).hasSize(500)
    Assertions.assertThat(odd).hasSize(500)
  }

  @Test fun `parallel reject`() {
    val odd = xs.parReject { it % 2 == 0 }
    val even = xs.parReject { it % 2 == 1 }

    Assertions.assertThat(even).hasSize(500)
    Assertions.assertThat(odd).hasSize(500)
  }

  @Test fun `parallel count`() {
    Assertions.assertThat(xs.parCount { it % 2 == 0 }).isEqualTo(500)
  }

  @Test fun `parallel forEach`() {
    val even = IntArrayList()
    xs.parForEach { if (it % 2 == 0) even.add(it) }

    Assertions.assertThat(even.size()).isEqualTo(500)
    Assertions.assertThat(even.asList().take(3)).isEqualTo(listOf(0, 2, 4))
  }

  @Test fun `parallel forEach with Index`() {
    val even = IntArrayList()
    xs.parForEachWithIndex { v, i ->
      if (v % 2 == 0) {
        even.add(v)
        log.debug("v=$v, i=$i")
      }
    }

    Assertions.assertThat(even.size()).isEqualTo(500)
    Assertions.assertThat(even.asList().take(3)).isEqualTo(listOf(0, 2, 4))
  }

  @Test fun `parallel map`() {
    val times = xs.parMap { it * 2 }
    Assertions.assertThat(times).hasSize(1000)
    Assertions.assertThat(times.take(5)).isEqualTo(listOf(0, 2, 4, 6, 8))
  }

  @Test fun `parallel flatMap`() {
    val fm = xss.parFlatMap { x: List<Int> -> x.map { it * 2 } }

    Assertions.assertThat(fm.size).isEqualTo(9)
    log.debug("flatMap=$fm")
    Assertions.assertThat(fm.last()).isEqualTo(16)
  }


  @Test fun `parallel filter map`() {
    val fm = xs.parFilterMap({ it % 2 == 0 }, { it * 2 })

    Assertions.assertThat(fm.size).isEqualTo(500)
    Assertions.assertThat(fm.take(5)).isEqualTo(listOf(0, 4, 8, 12, 16))
    Assertions.assertThat(fm.last()).isEqualTo(1996)
  }

  @Test fun `parallel group by`() {
    val groupBy: MutableMultimap<Int, Int> = xs.parGroupBy { it % 2 }

    Assertions.assertThat(groupBy.keysView().size()).isEqualTo(2)
    Assertions.assertThat(groupBy[0].size).isEqualTo(500)
    Assertions.assertThat(groupBy[1].size).isEqualTo(500)
  }

  @Test fun `parallel aggregate by`() {
    val agg: MutableMap<Int, Int> = xs.parAggregateBy({ 1 }, { 0 }, { acc, item -> acc + item + 1 })

    Assertions.assertThat(agg.keys.size).isEqualTo(1)
    log.debug("agg=${agg[1]}")
    Assertions.assertThat(agg[1]).isEqualTo(500500)

    val agg2 = xs.parAggregateBy({ it % 10 }, { 0 }, { acc, item -> acc + item + 1 })
    Assertions.assertThat(agg2.keys.size).isEqualTo(10)
    log.debug("agg2=${agg2.values}")

    (0 until 10).forEach { i ->
      Assertions.assertThat(agg2[i]).isEqualTo(49600 + 100 * i)
    }
  }
}