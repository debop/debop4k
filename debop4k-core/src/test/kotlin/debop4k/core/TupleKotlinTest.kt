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

package debop4k.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Tuple Test
 * @author sunghyouk.bae@gmail.com
 */
class TupleKotlinTest : AbstractCoreKotlinTest() {

  @Test
  fun testTuple1() {
    val t = tupleOf(1)
    val (a) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(t.toList()).isEqualTo(listOf(a))
  }

  @Test
  fun testTuple2() {
    val t = tupleOf(1, 2)
    val (a, b) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(b).isEqualTo(t.second).isEqualTo(2)
    assertThat(t.toList()).isEqualTo(listOf(a, b))

    t.toList().forEachIndexed { i, any ->
      assertThat(t[i]).isEqualTo(any?.toString()?.toInt())
    }
  }

  @Test
  fun testTuple3() {
    val t = tupleOf(1, 2, 3)
    val (a, b, c) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(b).isEqualTo(t.second).isEqualTo(2)
    assertThat(c).isEqualTo(t.third).isEqualTo(3)
    assertThat(t.toList()).isEqualTo(listOf(a, b, c))

    t.toList().forEachIndexed { i, any ->
      assertThat(t[i]).isEqualTo(any?.toString()?.toInt())
    }
  }

  @Test
  fun testTuple4() {
    val t = tupleOf(1, 2, 3, 4)
    val (a, b, c, d) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(b).isEqualTo(t.second).isEqualTo(2)
    assertThat(c).isEqualTo(t.third).isEqualTo(3)
    assertThat(d).isEqualTo(t.fourth).isEqualTo(4)
    assertThat(t.toList()).isEqualTo(listOf(a, b, c, d))

    t.toList().forEachIndexed { i, any ->
      assertThat(t[i]).isEqualTo(any?.toString()?.toInt())
    }
  }

  @Test
  fun testTuple9() {
    val t = tupleOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val (a, b, c, d, e, f, g, h, i) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(b).isEqualTo(t.second).isEqualTo(2)
    assertThat(c).isEqualTo(t.third).isEqualTo(3)
    assertThat(d).isEqualTo(t.fourth).isEqualTo(4)
    assertThat(e).isEqualTo(t.fifth).isEqualTo(5)
    assertThat(f).isEqualTo(t.sixth).isEqualTo(6)
    assertThat(g).isEqualTo(t.seventh).isEqualTo(7)
    assertThat(h).isEqualTo(t.eighth).isEqualTo(8)
    assertThat(i).isEqualTo(t.ninth).isEqualTo(9)
    assertThat(t.toList()).isEqualTo(listOf(a, b, c, d, e, f, g, h, i))

    t.toList().forEachIndexed { i, any ->
      assertThat(t[i]).isEqualTo(any?.toString()?.toInt())
    }
  }

  @Test
  fun testTuple10() {
    val t = tupleOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val (a, b, c, d, e, f, g, h, i, j) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(b).isEqualTo(t.second).isEqualTo(2)
    assertThat(c).isEqualTo(t.third).isEqualTo(3)
    assertThat(d).isEqualTo(t.fourth).isEqualTo(4)
    assertThat(e).isEqualTo(t.fifth).isEqualTo(5)
    assertThat(f).isEqualTo(t.sixth).isEqualTo(6)
    assertThat(g).isEqualTo(t.seventh).isEqualTo(7)
    assertThat(h).isEqualTo(t.eighth).isEqualTo(8)
    assertThat(i).isEqualTo(t.ninth).isEqualTo(9)
    assertThat(j).isEqualTo(t.ten).isEqualTo(10)
    assertThat(t.toList()).isEqualTo(listOf(a, b, c, d, e, f, g, h, i, j))

    t.toList().forEachIndexed { i, any ->
      assertThat(t[i]).isEqualTo(any?.toString()?.toInt())
    }
  }

  @Test
  fun testTuple13() {
    val t = tupleOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
    val (a, b, c, d, e, f, g, h, i, j, k, l, m) = t
    assertThat(a).isEqualTo(t.first).isEqualTo(1)
    assertThat(b).isEqualTo(t.second).isEqualTo(2)
    assertThat(c).isEqualTo(t.third).isEqualTo(3)
    assertThat(d).isEqualTo(t.fourth).isEqualTo(4)
    assertThat(e).isEqualTo(t.fifth).isEqualTo(5)
    assertThat(f).isEqualTo(t.sixth).isEqualTo(6)
    assertThat(g).isEqualTo(t.seventh).isEqualTo(7)
    assertThat(h).isEqualTo(t.eighth).isEqualTo(8)
    assertThat(i).isEqualTo(t.ninth).isEqualTo(9)
    assertThat(j).isEqualTo(t.ten).isEqualTo(10)
    assertThat(k).isEqualTo(t.eleven).isEqualTo(11)
    assertThat(l).isEqualTo(t.twelve).isEqualTo(12)
    assertThat(m).isEqualTo(t.thirteen).isEqualTo(13)
    assertThat(t.toList()).isEqualTo(listOf(a, b, c, d, e, f, g, h, i, j, k, l, m))

    t.toList().forEachIndexed { i, v ->
      assertThat(t[i]).isEqualTo(v?.toString()?.toInt())
    }
  }
}