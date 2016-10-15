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

package debop4k.data.ignite.examples

import debop4k.core.collections.parForEach
import debop4k.core.uninitialized
import debop4k.core.utils.use
import debop4k.data.ignite.AbstractIgniteTest
import debop4k.data.ignite.getCache
import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.lang.IgniteCallable
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import java.util.*

class SimpleExample : AbstractIgniteTest() {

  companion object {

    @JvmField var ignite: Ignite = uninitialized()

    @BeforeClass
    @JvmStatic
    fun setupClass() {
      ignite = Ignition.getOrStart(IgniteConfiguration())
    }

    @AfterClass
    @JvmStatic
    fun cleanupClass() {
      ignite.close()
    }
  }

  @Test
  fun testConfiguration() {
    ignite.getCache<String, Any?>("default").use { cache ->
      cache.put("a", "abc")
      log.debug("cache matrics = {}", cache.metrics())
    }
  }

  @Test
  fun testCompute() {
    val calls = ArrayList<IgniteCallable<Int>>()

    // Iterate through all the words in the sentence and create Callable jobs.
    "Count characters using callable".split(" ").forEach { word ->
      calls.add(IgniteCallable { word.length })
    }

    // Execute collection of Callables on the grid.
    val res = ignite.compute().call(calls)

    // Add up all the results.
    val sum = res.map { it }.sum()

    log.debug("Total number of characters is '{}'.", sum)
    assertThat(sum).isEqualTo(28)
  }

  @Test
  fun testDataGrid() {
    val range = 0..100
    ignite.getCache<Int, String>("dataGrid").use { cache ->

      // Store keys in cache (values will end up on different cache nodes).
      range.parForEach {
        cache.put(it, it.toString())
      }

      range.parForEach {
        log.debug("\tGot [key=$it, value=${cache.get(it)}]")
        assertThat(it).isEqualTo(cache.get(it).toInt())
      }
    }
  }

}