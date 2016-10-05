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

package debop4k.core.collections.permutations

import debop4k.core.utils.toList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*
import java.util.stream.*

/**
 * StreamTest
 * @author sunghyouk.bae@gmail.com
 */
class StreamTest : AbstractPermutationTest() {

  val expectedElements = listOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
  val expectedElementAsString = listOf("5", "6", "7", "8", "9", "10", "11", "12", "13", "14")

  private fun stackedStream(permutation: Permutation<Int>): Stream<Int> {
    return permutation.toStream()
        .map { it + 1 }
        .flatMap { listOf(0, it - 1).stream() }
        .filter { it != 0 }
        .skip(4)
        .limit(10)
        .sorted()
        .distinct()
  }

  @Test
  fun testInvokeMultipleOperations() {
    val numbers = numbers(1).take(20)
    val collected: Permutation<Int> = stackedStream(numbers).toPermutation()

    assertThat(collected).isEqualTo(permutationOf(expectedElements))
  }

  @Test
  fun testInvokeMultpleOperationOnPermutation() {
    val numbers = numbers(1).take(20)
    val collected = stackedStream(numbers).toList()

    assertThat(collected).isEqualTo(permutationOf(expectedElements))
  }

  @Test
  fun testInvokeMultpleOperationAndCustomCollector() {
    val numbers = numbers(1).take(20)
    val intStream = stackedStream(numbers)

    val collected = intStream.collect({ ArrayList<String>() },
                                      { list, item -> list.add(item.toString()) },
                                      { list, items -> list.addAll(items) })

    assertThat(collected).isEqualTo(permutationOf(expectedElementAsString))
  }

  @Test
  fun testInvokeTerminalOperations() {
    val numbers = numbers(1).take(20)
    val min = stackedStream(numbers).min { a, b -> a - b }

    assertThat(min).isEqualTo(Optional.of(expectedElements.min()))
  }

}