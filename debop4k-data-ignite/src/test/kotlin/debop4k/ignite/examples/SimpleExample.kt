/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.ignite.examples

import debop4k.core.loggerOf
import debop4k.core.use
import debop4k.data.ignite.getCache
import debop4k.data.ignite.usingIgnite
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.lang.IgniteCallable
import org.junit.Test
import java.util.*

/**
 * SimpleExample
 * @author sunghyouk.bae@gmail.com
 */
class SimpleExample {

  private val log = loggerOf(javaClass)

  private val cfg = IgniteConfiguration()

  @Test
  fun testConfiguration() {
    usingIgnite { ignite ->
      ignite.getCache<String, Any?>("default").use { cache ->
        cache.put("a", "abc")
        println("cache matrics =${cache.metrics()}")
      }
    }
  }

  @Test
  fun testCompute() {
    usingIgnite(cfg) { ignite ->

      val calls = ArrayList<IgniteCallable<Int>>()

      // Iterate through all the words in the sentence and create Callable jobs.
      "Count characters using callable".split(" ").forEach { word ->
        calls.add(IgniteCallable { word.length })
      }

      // Execute collection of Callables on the grid.
      val res = ignite.compute().call(calls)

      // Add up all the results.
      val sum = res.map { it.toInt() }.sum()

      println("Total number of characters is '$sum'.")
    }
  }

  @Test
  fun testDataGrid() {
    usingIgnite(cfg) { ignite ->
      ignite.getCache<Int, String>("dataGrid").use { cache ->

        // Store keys in cache (values will end up on different cache nodes).
        (0..9).forEach {
          cache.put(it, it.toString())
        }

        (0..9).forEach {
          println("\tGot [key=$it, value=${cache.get(it)}]")
        }
      }
    }
  }

}