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

package debop4k.ignite.examples

import debop4k.core.io.use
import org.apache.ignite.Ignition
import org.apache.ignite.lang.IgniteCallable
import org.junit.Test
import java.util.*

/**
 * SimpleExample
 * @author sunghyouk.bae@gmail.com
 */
class SimpleExample {

  @Test
  fun testConfiguration() {
    Ignition.start().use { ignite ->
      val cache = ignite.getOrCreateCache<String, Any?>("default")
      cache.put("a", "abc")
      println("cache matrics =${cache.metrics()}")
    }
  }

  @Test
  fun testCompute() {
    Ignition.start().use { ignite ->

      val calls = ArrayList<IgniteCallable<Int>>()

      // Iterate through all the words in the sentence and create Callable jobs.
      for (word in "Count characters using callable".split(" "))
        calls.add(IgniteCallable { word.length })

      // Execute collection of Callables on the grid.
      val res = ignite.compute().call(calls)

      // Add up all the results.
      val sum = res.map { it.toInt() }.sum()

      println("Total number of characters is '$sum'.")
    }
  }

  @Test
  fun testDataGrid() {
    Ignition.start().use { ignite ->
      val cache = ignite.getOrCreateCache<Int, String>("myCacheName")

      // Store keys in cache (values will end up on different cache nodes).
      for (i in 0..9)
        cache.put(i, i.toString())

      for (i in 0..9)
        println("Got [key=$i, val=${cache.get(i)}]")
    }
  }
}