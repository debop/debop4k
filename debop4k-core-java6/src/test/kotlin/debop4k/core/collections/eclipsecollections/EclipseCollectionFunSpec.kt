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

import debop4k.core.collections.eclipseCollections.*
import io.kotlintest.matchers.be
import io.kotlintest.specs.FunSpec
import org.eclipse.collections.impl.map.mutable.UnifiedMap
import org.eclipse.collections.impl.set.mutable.UnifiedSet
import org.slf4j.LoggerFactory

/**
 * EclipseCollectionFunSpec
 * @author sunghyouk.bae@gmail.com
 */
class EclipseCollectionFunSpec : FunSpec() {

  private val log = LoggerFactory.getLogger(javaClass)

  init {

    test("Primitive ArrayList extension function") {
      val intArrays = intArrayListOf(1, 2, 3, 4, 5)
      intArrays.size() shouldEqual 5
      intArrays.contains(4) shouldEqual true
      intArrays.contains(0) shouldEqual false


      val longArrays = longArrayListOf(1, 2, 3, 4, 5)
      longArrays.size() shouldEqual 5
      longArrays.contains(4) shouldEqual true
      longArrays.contains(0) shouldEqual false

      val floatArrays = floatArrayListOf(1F, 2F, 3F, 4F, 5F)
      floatArrays.size() shouldEqual 5
      floatArrays.contains(4F) shouldEqual true
      floatArrays.contains(0F) shouldEqual false

      val doubleArrays = doubleArrayListOf(1.0, 2.0, 3.0, 4.0, 5.0)
      doubleArrays.size() shouldEqual 5
      doubleArrays.contains(4.0) shouldEqual true
      doubleArrays.contains(0.0) shouldEqual false
    }

    test("primitive list asList") {
      val ints: List<Int> = intArrayListOf(1, 2, 3, 4, 5).asList()
      ints.size shouldEqual 5
      ints shouldEqual listOf(1, 2, 3, 4, 5)

      val longs: List<Long> = longArrayListOf(1, 2, 3, 4, 5).asList()
      longs.size shouldEqual 5
      longs shouldEqual listOf<Long>(1, 2, 3, 4, 5)
    }

    test("fastListOf") {
      val empty = fastListOf<Any>()
      empty.size shouldEqual 0

      val ints = fastListOf(1, 2, 3, 4, 5)
      ints.size should be gt 0
      ints shouldEqual listOf(1, 2, 3, 4, 5)
    }

    test("unifiedSetOf") {
      val set: UnifiedSet<Int> = unifiedSetOf(1, 2, 3, 3, 3)
      set.size shouldEqual 3
      set shouldEqual setOf(1, 2, 3)
    }

    test("unifiedMapOf") {
      val map: UnifiedMap<Int, String> = unifiedMapOf(1 to "a", 2 to "b", 3 to "c")
      map.size shouldEqual 3
      map[1] shouldEqual "a"
      map[2] shouldEqual "b"
      map[3] shouldEqual "c"
      map[4] shouldEqual null
    }
  }
}