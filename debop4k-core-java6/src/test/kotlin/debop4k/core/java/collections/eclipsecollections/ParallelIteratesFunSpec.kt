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

package debop4k.core.java.collections.eclipsecollections

import debop4k.core.java.collections.eclipseCollections.*
import io.kotlintest.specs.FunSpec
import org.eclipse.collections.api.multimap.MutableMultimap
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.slf4j.LoggerFactory

/**
 * ParallelIteratesFunSpec
 * @author debop sunghyouk.bae@gmail.com
 */
class ParallelIteratesFunSpec : FunSpec() {

  private val log = LoggerFactory.getLogger(javaClass)

  private val xs: FastList<Int>
    get() {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }
      return xs
    }

  private val xss: FastList<List<Int>> = fastListOf(fastListOf(0, 1, 2),
                                                    fastListOf(3, 4, 5),
                                                    fastListOf(6, 7, 8))

  init {

    test("parallel filter") {
      val even = xs.parFilter { it % 2 == 0 }
      val odd = xs.parFilter { it % 2 == 1 }

      even.size shouldEqual 500
      odd.size shouldEqual 500
    }

    test("parallel reject") {
      val odd = xs.parReject { it % 2 == 0 }
      val even = xs.parReject { it % 2 == 1 }

      even.size shouldEqual 500
      odd.size shouldEqual 500
    }

    test("parallel count ") {
      xs.parCount { it % 2 == 0 } shouldEqual 500
    }

    test("parallel forEach") {
      val even = IntArrayList()
      xs.parForEach { if (it % 2 == 0) even.add(it) }

      even.size() shouldEqual 500
      even.asList().take(3) shouldEqual listOf(0, 2, 4)
    }

    test("parallel forEach with Index") {
      val even = IntArrayList()
      xs.parForEachWithIndex { v, i ->
        if (v % 2 == 0) {
          even.add(v)
          log.debug("v=$v, i=$i")
        }
      }

      even.size() shouldEqual 500
      even.asList().take(3) shouldEqual listOf(0, 2, 4)
    }

    test("parallel map") {
      val times = xs.parMap { it * 2 }
      times.size shouldEqual 1000
      times.take(5) shouldEqual listOf(0, 2, 4, 6, 8)
    }

    test("parallel flatMap") {
      val fm = xss.parFlatMap { x: List<Int> -> x.map { it * 2 } }

      fm.size shouldEqual 9
      log.debug("flatMap=$fm")
      fm.last() shouldEqual 16
    }


    test("parallel filter map") {
      val fm = xs.parFilterMap({ it % 2 == 0 }, { it * 2 })

      fm.size shouldEqual 500
      fm.take(5) shouldEqual listOf(0, 4, 8, 12, 16)
      fm.last() shouldEqual 1996
    }

    test("parallel group by") {
      val groupBy: MutableMultimap<Int, Int> = xs.parGroupBy { it % 2 }

      groupBy.keysView().size() shouldEqual 2
      groupBy[0].size shouldEqual 500
      groupBy[1].size shouldEqual 500
    }

    test("parallel aggregate by") {
      val agg: MutableMap<Int, Int> = xs.parAggregateBy({ 1 }, { 0 }, { acc, item -> acc + item + 1 })

      agg.keys.size shouldEqual 1
      log.debug("agg=${agg[1]}")
      agg[1] shouldEqual 500500

      val agg2 = xs.parAggregateBy({ it % 10 }, { 0 }, { acc, item -> acc + item + 1 })
      agg2.keys.size shouldEqual 10
      log.debug("agg2=${agg2.values}")

      (0 until 10).forEach { i ->
        agg2[i] shouldEqual 49600 + 100 * i
      }
    }
  }
}