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
import net.jpountz.xxhash.XXHashFactory
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.eclipse.collections.api.tuple.Pair
import org.eclipse.collections.impl.factory.Maps
import org.eclipse.collections.impl.tuple.Tuples
import org.junit.Test
import java.util.*

/**
 * HashExKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class HashExKotlinTest : AbstractCoreKotlinTest() {

  @Test
  fun primitiveHashs() {
    val v12 = compute(1, 2)
    val v21 = compute(2, 1)
    val v22 = compute(2, 2)
    assertThat(v12).isNotEqualTo(v21)
    assertThat(v12).isNotEqualTo(v22)
    assertThat(v21).isNotEqualTo(v22)
  }

  @Test
  fun referenceHashs() {
    val h1 = compute("1", null)
    val h2 = compute(null, "1")
    log.debug("h1={}, h2={}", h1, h2)
    assertThat(h1).isNotEqualTo(h2)
  }

  @Test
  fun referenceHash2() {
    val date = Date()
    val h1 = compute(date, null)
    val h2 = compute(null, date)
    assertThat(h1).isNotEqualTo(h2)
  }


  // NOTE: Objects.hash 와 Arrays.hash 는 버그가 있다.
  //
  @Test(expected = AssertionError::class)
  fun primitiveHashByObjectsHash() {
    val h1 = Objects.hash(1, 0)
    val h2 = Objects.hash(0, 31)
    assertThat(h1).isNotEqualTo(h2)
  }

  @Test
  fun primitiveHashEx() {
    val h1 = compute(1, 0)
    val h2 = compute(0, 31)

    // NOTE: integer 값 배열 중에 같은 경우가 있으므로, 이런 경우에는 문자열로 바꾸어서 수행해야 합니다.
    log.trace("h1={}, h2={}", h1, h2)
    assertThat(h1).isNotEqualTo(h2)
  }

  @Test
  fun primitiveHashEx3() {
    val h1 = compute(1, 0, 0)
    val h2 = compute(0, 31, 0, 0)

    // NOTE: integer 값 배열 중에 같은 경우가 있으므로, 이런 경우에는 문자열로 바꾸어서 수행해야 합니다.
    log.trace("h1={}, h2={}", h1, h2)
    assertThat(h1).isNotEqualTo(h2)
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
            fail("중복")
          }
          hashMap.put(hash, Tuples.pair(i, j))
        }
      }
    }
  }

  @Test
  fun testXXHash() {
    val factory = XXHashFactory.fastestInstance()
    val hash32 = factory.newStreamingHash32(0x9747b28c.toInt());

    With.stopwatch {
      val hashMap = Maps.mutable.of<Int, Pair<Int, Int>>()
      for (i in 0..99) {
        for (j in 0..99) {

          val buf = byteArrayOf(i.toByte(), j.toByte())
          hash32.update(buf, 0, buf.size)
          val hash = hash32.value
          if (hashMap.containsKey(hash)) {
            log.debug("중복!!!!! i={}, j={}, pair={}", i, j, hashMap[hash])
            fail("중복")
          }
          hashMap.put(hash, Tuples.pair(i, j))
        }
        hash32.reset()
      }
    }
  }
}