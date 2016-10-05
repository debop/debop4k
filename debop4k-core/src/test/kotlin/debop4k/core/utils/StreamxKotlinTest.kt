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

package debop4k.core.utils

import debop4k.core.AbstractCoreKotlinTest
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.collections.impl.list.primitive.IntInterval
import org.junit.Test
import java.util.stream.*

/**
 * StreamxKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class StreamxKotlinTest : AbstractCoreKotlinTest() {

  @Test
  fun testAsIntArrayList() {
    val arrayList = IntStream.range(0, 1000).toIntArrayList()
    assertThat(arrayList).isNotNull()
    assertThat(arrayList.size()).isEqualTo(1000)
  }

  @Test
  fun testAsLongArrayList() {
    val arrayList = LongStream.range(0, 1000).toLongArrayList()
    assertThat(arrayList).isNotNull()
    assertThat(arrayList.size()).isEqualTo(1000)
  }


  @Test
  fun testAsDoubleArrayList() {
    val arrayList = DoubleStream.of(0.0, 1.0, 2.0, 3.0, 4.0).toDoubleArrayList()
    assertThat(arrayList).isNotNull()
    assertThat(arrayList.size()).isEqualTo(5)
  }


  @Test
  fun testForEach() {
    var sum = 0
    IntInterval.fromTo(0, 10).forEach { i -> sum += i }
    assertThat(sum).isEqualTo(55)
  }
}