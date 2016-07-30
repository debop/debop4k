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

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.java.collections.eclipseCollections.*
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.collections.impl.map.mutable.UnifiedMap
import org.eclipse.collections.impl.set.mutable.UnifiedSet
import org.junit.Test

/**
 * EclipseCollectionFunSpec
 * @author sunghyouk.bae@gmail.com
 */
class EclipseCollectionFunSpec : AbstractCoreKotlinTest() {


  @Test fun `Primitive ArrayList extension function`() {
    val intArrays = intArrayListOf(1, 2, 3, 4, 5)
    assertThat(intArrays.size()).isEqualTo(5)
    assertThat(intArrays.contains(4)).isTrue()
    assertThat(intArrays.contains(0)).isFalse()


    val longArrays = longArrayListOf(1, 2, 3, 4, 5)
    assertThat(longArrays.size()).isEqualTo(5)
    assertThat(longArrays.contains(4)).isTrue()
    assertThat(longArrays.contains(0)).isFalse()

    val floatArrays = floatArrayListOf(1F, 2F, 3F, 4F, 5F)
    assertThat(floatArrays.size()).isEqualTo(5)
    assertThat(floatArrays.contains(4F)).isTrue()
    assertThat(floatArrays.contains(0F)).isFalse()

    val doubleArrays = doubleArrayListOf(1.0, 2.0, 3.0, 4.0, 5.0)
    assertThat(doubleArrays.size()).isEqualTo(5)
    assertThat(doubleArrays.contains(4.0)).isTrue()
    assertThat(doubleArrays.contains(0.0)).isFalse()
  }

  @Test fun `primitive list asList`() {
    val ints: List<Int> = intArrayListOf(1, 2, 3, 4, 5).asList()
    assertThat(ints)
        .hasSize(5)
        .isEqualTo(listOf(1, 2, 3, 4, 5))

    val longs: List<Long> = longArrayListOf(1, 2, 3, 4, 5).asList()
    assertThat(longs)
        .hasSize(5)
        .isEqualTo(listOf(1, 2, 3, 4, 5))
  }

  @Test fun fastListOf() {
    val empty = fastListOf<Any>()
    assertThat(empty).isEmpty()

    val ints = fastListOf(1, 2, 3, 4, 5)
    assertThat(ints)
        .hasSize(5)
        .isEqualTo(listOf(1, 2, 3, 4, 5))
  }

  @Test fun unifiedSetOf() {
    val set: UnifiedSet<Int> = unifiedSetOf(1, 2, 3, 3, 3)
    assertThat(set)
        .hasSize(3)
        .isEqualTo(setOf(1, 2, 3))
  }

  @Test fun unifiedMapOf() {
    val map: UnifiedMap<Int, String> = unifiedMapOf(1 to "a", 2 to "b", 3 to "c")

    assertThat(map.size).isEqualTo(3)

    assertThat(map[1]).isEqualTo("a")
    assertThat(map[2]).isEqualTo("b")
    assertThat(map[3]).isEqualTo("c")
    assertThat(map[4]).isNull()
  }

}