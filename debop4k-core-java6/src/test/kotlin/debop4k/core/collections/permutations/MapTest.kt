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

package debop4k.core.collections.permutations

import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

/**
 * MapTest
 * @author sunghyouk.bae@gmail.com
 */
class MapTest : AbstractPermutationTest() {

  @Mock
  val supplierMock: () -> Permutation<Int> = uninitialized()

  @Test
  fun testReturnEmptyWhenMappingEmpty() {
    assertThat(emptyPermutation<Int>()).isEmpty()
  }

  @Test
  fun testMapFixedSeqWithJustOneElement() {
    val chars = permutationOf('a')
    val toUpper = chars.map(Char::toUpperCase)

    assertThat(toUpper).isEqualTo(permutationOf('A'))
  }

  @Test
  fun testMapFixedSeqWithFewElements() {
    val chars = permutationOf('a', 'b', 'c')
    val toUpper = chars.map(Char::toUpperCase)

    assertThat(toUpper).isEqualTo(permutationOf('A', 'B', 'C'))
  }

  @Test
  fun testMapInfiniteSeq() {
    val naturals = numbers(1)
    val multiplied = naturals.map { it * 10 }
    assertThat(multiplied.take(4)).isEqualTo(permutationOf(10, 20, 30, 40))
  }

  @Test
  fun testNotEvaluateTailOnMap() {
    val seq = cons(17, supplierMock)
    seq.map(Int::toString)
    Mockito.verifyZeroInteractions(supplierMock)
  }

  @Test
  fun testMapHead() {
    val seq = cons(17, supplierMock)
    val strings = seq.map(Int::toString)
    assertThat(strings.head).isEqualToIgnoringCase("17")
  }

}