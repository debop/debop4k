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

package debop4k.core.lazyseq

import debop4k.core.lastChar
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 * LazySeqMinMaxTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqMinMaxTest : AbstractLazySeqTest() {

  @Test
  fun testReturnNullOnEmptySeqByLambda() {
    val empty = emptyLazySeq<Int>()

    val min = empty.min { a, b -> a - b }
    val max = empty.max { a, b -> a - b }

    assertThat(min).isNull()
    assertThat(max).isNull()
  }

  @Test
  fun testReturnNullOnEmptySeq() {
    val empty = emptyLazySeq<Int>()

    val min = empty.min(Comparator { a, b -> a - b })
    val max = empty.max(Comparator { a, b -> a - b })

    assertThat(min).isNull()
    assertThat(max).isNull()

    val minBy = empty.minBy(Math::abs)
    val maxBy = empty.maxBy(Math::abs)

    assertThat(minBy).isNull()
    assertThat(maxBy).isNull()
  }

  @Test
  fun testSingleSeq() {
    val single = lazySeqOf(1)

    val min: Int? = single.min { a, b -> a - b }
    val max: Int? = single.max { a, b -> a - b }

    assertThat(min).isEqualTo(1)
    assertThat(max).isEqualTo(1)

    val minBy = single.minBy(Math::abs)
    val maxBy = single.maxBy(Math::abs)

    assertThat(minBy).isEqualTo(1)
    assertThat(maxBy).isEqualTo(1)
  }

  @Test
  fun testFixedSeq() {
    val fixed = lazySeqOf(3, -2, 8, 5, -4, 11, 2, 1)

    val min: Int? = fixed.min { a, b -> a - b }
    val max: Int? = fixed.max { a, b -> a - b }

    assertThat(min).isEqualTo(-4)
    assertThat(max).isEqualTo(11)

    val minBy = fixed.minBy(Math::abs)
    val maxBy = fixed.maxBy(Math::abs)

    assertThat(minBy).isEqualTo(1)
    assertThat(maxBy).isEqualTo(11)
  }

  @Test
  fun testLazyFiniteSeq() {
    val lazy = lazy()

    val min: Int? = lazy.min { a, b -> a - b }
    val max: Int? = lazy.max { a, b -> a - b }

    assertThat(min).isEqualTo(-4)
    assertThat(max).isEqualTo(11)

    val minBy = lazy.minBy(Math::abs)
    val maxBy = lazy.maxBy(Math::abs)

    assertThat(minBy).isEqualTo(1)
    assertThat(maxBy).isEqualTo(11)
  }

  @Test
  fun testFindShortestAndLogestString() {
    val single = lazySeqOf(*loremIpsum())

    val min = single.min { a, b -> a.length - b.length }
    val max = single.max { a, b -> a.length - b.length }

    assertThat(min).isEqualTo("id")
    assertThat(max).isEqualTo("consectetur")

    val minBy = single.minBy(String::length)
    val maxBy = single.maxBy(String::length)

    assertThat(minBy).isEqualTo("id")
    assertThat(maxBy).isEqualTo("consectetur")
  }

  @Test
  fun testMinMaxByStringAlphabetically() {
    val single = lazySeqOf(*loremIpsum())

    val min = single.min(String::compareTo)
    val max = single.max(String::compareTo)

    assertThat(min).isEqualTo("adipiscing")
    assertThat(max).isEqualTo("sit")
  }

  @Test
  fun testMinMaxByLastCharacter() {
    val single = lazySeqOf(*loremIpsum())

    val min = single.minBy { s -> s.lastChar }
    val max = single.maxBy { s -> s.lastChar }

    assertThat(min).isEqualTo("ligula")
    assertThat(max).isEqualTo("sit")
  }
}