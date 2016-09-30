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

package debop4k.core.collections.permutations

import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions

class ForEachTest : AbstractPermutationTest() {


  val consumerMock = mock<(Int) -> Unit>()

  @Test
  fun testDoNothingOnEmptySeq() {
    val empty = emptyPermutation<Int>()

    empty.forEach(consumerMock)
    verifyZeroInteractions(consumerMock)
  }

  @Test
  fun testCallConsumerForSingleEmenetInSeq() {
    val single = permutationOf(1)

    single.forEach(consumerMock)
    verify(consumerMock).invoke(1)
    verifyZeroInteractions(consumerMock)
  }

  @Test
  fun testCallConsumerForMultipleElementsOfFixedSeq() {
    val fixed = permutationOf(2, 3, 4)

    fixed.forEach(consumerMock)

    verify(consumerMock).invoke(2)
    verify(consumerMock).invoke(3)
    verify(consumerMock).invoke(4)
    verifyZeroInteractions(consumerMock)
  }

  @Test
  fun testCallConsumerForMultipleElementsOfSubstream() {
    val fixed = permutationOf(2, 3, 4, 5, 6, 7).take(3)

    fixed.forEach(consumerMock)

    verify(consumerMock).invoke(2)
    verify(consumerMock).invoke(3)
    verify(consumerMock).invoke(4)
    verifyZeroInteractions(consumerMock)
  }

  @Test
  fun testCallConsumerForEachElementOfLazilyCreatedButNotInfiniteSeq() {
    val fixed = cons(5) { cons(6) { permutationOf(7) } }

    fixed.forEach(consumerMock)
    verify(consumerMock).invoke(5)
    verify(consumerMock).invoke(6)
    verify(consumerMock).invoke(7)
    verifyZeroInteractions(consumerMock)
  }
}