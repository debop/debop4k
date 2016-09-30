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

package debop4k.core.utils

import debop4k.core.AbstractCoreKotlinTest
import org.assertj.core.api.Assertions
import org.eclipse.collections.api.tuple.Pair
import org.eclipse.collections.impl.factory.Maps
import org.eclipse.collections.impl.tuple.Tuples
import org.junit.Test
import java.util.*

/**
 * XXHasherKotlinTest
 * @author sunghyouk.bae@gmail.com
 */
class XXHasherKotlinTest : AbstractCoreKotlinTest() {

  @Test
  fun primitiveHashs() {
    val v12 = XXHasher.compute(1, 2)
    val v21 = XXHasher.compute(2, 1)
    val v22 = XXHasher.compute(2, 2)
    Assertions.assertThat(v12).isNotEqualTo(v21)
    Assertions.assertThat(v12).isNotEqualTo(v22)
    Assertions.assertThat(v21).isNotEqualTo(v22)
  }

  @Test
  fun referenceHashs() {
    val h1 = XXHasher.compute("1", null)
    val h2 = XXHasher.compute(null, "1")
    log.debug("h1={}, h2={}", h1, h2)
    Assertions.assertThat(h1).isNotEqualTo(h2)
  }

  @Test
  fun referenceHash2() {
    val date = Date()
    val h1 = XXHasher.compute(date, null)
    val h2 = XXHasher.compute(null, date)
    Assertions.assertThat(h1).isNotEqualTo(h2)
  }


  // NOTE: Objects.hash 와 Arrays.hash 는 버그가 있다.
  //
  @Test(expected = AssertionError::class)
  fun primitiveHashByObjectsHash() {
    val h1 = Objects.hash(1, 0)
    val h2 = Objects.hash(0, 31)
    Assertions.assertThat(h1).isNotEqualTo(h2)
  }

  @Test
  fun primitiveHashEx() {
    val h1 = XXHasher.compute(1, 0)
    val h2 = XXHasher.compute(0, 31)

    // NOTE: integer 값 배열 중에 같은 경우가 있으므로, 이런 경우에는 문자열로 바꾸어서 수행해야 합니다.
    log.trace("h1={}, h2={}", h1, h2)
    Assertions.assertThat(h1).isNotEqualTo(h2)
  }

  @Test
  fun primitiveHashEx3() {
    val h1 = XXHasher.compute(1, 0, 0)
    val h2 = XXHasher.compute(0, 31, 0, 0)

    // NOTE: integer 값 배열 중에 같은 경우가 있으므로, 이런 경우에는 문자열로 바꾸어서 수행해야 합니다.
    log.trace("h1={}, h2={}", h1, h2)
    Assertions.assertThat(h1).isNotEqualTo(h2)
  }


  @Test
  fun testXXHash() {
    With.stopwatch {
      val hashMap = Maps.mutable.of<Int, Pair<Int, Int>>()
      for (i in 0..99) {
        for (j in 0..99) {
          val hash = XXHasher.compute(i, j)

          if (hashMap.containsKey(hash)) {
            log.debug("중복!!!!! i={}, j={}, pair={}", i, j, hashMap[hash])
            Assertions.fail("중복")
          }
          hashMap.put(hash, Tuples.pair(i, j))
        }
      }
    }
  }

  @Test
  fun primitiveHashEx4() {
    val hashMap = Maps.mutable.of<Int, Pair<Int, Int>>()

    With.stopwatch {
      for (i in 0..99) {
        for (j in 0..99) {
          val hash = compute(i, j)
          if (hashMap.containsKey(hash)) {
            log.debug("중복!!!!! i={}, j={}, pair={}", i, j, hashMap[hash])
            Assertions.fail("중복")
          }
          hashMap.put(hash, Tuples.pair(i, j))
        }
      }
    }
  }
}