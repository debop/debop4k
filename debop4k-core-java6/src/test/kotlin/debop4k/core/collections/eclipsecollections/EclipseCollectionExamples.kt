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

package debop4k.core.collections.eclipsecollections

import debop4k.core.collections.toIntArrayList
import io.kotlintest.specs.FunSpec
import org.eclipse.collections.impl.list.mutable.FastList
import org.slf4j.LoggerFactory
import java.util.concurrent.*

/**
 * EclipseCollectionExamples
 * @author debop sunghyouk.bae@gmail.com
 */
class EclipseCollectionExamples : FunSpec() {

  private val log = LoggerFactory.getLogger(javaClass)

  val executor: ExecutorService = Executors.newFixedThreadPool(4)

  init {
    test("select method") {
      val xs = FastList.wrapCopy(1, 2, 3, 4, 5)
      val even = xs.select { it % 2 == 0 }
      even.all { it % 2 == 0 } shouldEqual true
    }

    test("asParallel") {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }

      val even = xs.asParallel(executor, 4).select { it % 2 == 0 }.toList()
      even.size shouldEqual 500

      even.detect { it % 2 == 0 } shouldEqual 0
      even.detectIndex { it % 2 == 0 } shouldEqual 0
      even.detectLastIndex { it % 2 == 0 } shouldEqual 499
      even.detect { it % 2 == 1 } shouldEqual null
      even.select { it % 2 == 1 }.size shouldEqual 0
    }

    test("partition") {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }

      val evenOdd = xs.partition { it % 2 == 0 }
      evenOdd.selected.size shouldEqual 500
      evenOdd.rejected.size shouldEqual 500
    }

    test("build IntArrayList") {
      val xs = (0 until 1000).toIntArrayList()

      xs.size() shouldEqual 1000
      xs.asLazy().select { it % 2 == 0 }.size() shouldEqual 500
    }
  }
}