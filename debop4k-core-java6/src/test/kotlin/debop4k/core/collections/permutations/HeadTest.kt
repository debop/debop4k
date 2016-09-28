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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.util.*

/**
 * HeadTest
 * @author sunghyouk.bae@gmail.com
 */
class HeadTest : AbstractPermutationTest() {

  val supplierMock = mock<() -> Permutation<Int>>()

  @Test
  fun testThrowToEmptySeqHead() {
    val empty = emptyPermutation<Any>()
    assertThatThrownBy {
      empty.head
    }.isInstanceOf(NoSuchElementException::class.java)
  }

  @Test
  fun testGetHeadFromFixedSeq() {
    assertThat(permutationOf(1).head).isEqualTo(1)
    assertThat(permutationOf(2, 3).head).isEqualTo(2)
    assertThat(permutationOf(4, 5, 6).head).isEqualTo(4)
    assertThat(permutationOf(listOf(7, 8, 9, 10)).head).isEqualTo(7)
  }

  @Test
  fun testGetHeadFromDynamicSeq() {
    assertThat(permutationOf(1, tail()).head).isEqualTo(1)
    assertThat(permutationOf(2, 3, tail()).head).isEqualTo(2)
    assertThat(permutationOf(4, 5, 6, tail()).head).isEqualTo(4)
    assertThat(concat(listOf(7, 8, 9, 10), tail()).head).isEqualTo(7)
  }

  @Test
  fun testGetHeadOfTailFromDynamicSeq() {
    assertThat(permutationOf(1, tail()).tail.head).isEqualTo(42)
    assertThat(permutationOf(2, 3, tail()).tail.head).isEqualTo(3)
    assertThat(permutationOf(4, 5, 6, tail()).tail.head).isEqualTo(5)
    assertThat(concat(listOf(7, 8, 9, 10), tail()).tail.head).isEqualTo(8)
  }

  @Test
  fun testNotEvaluateTailIfHeadRequested() {
    val lazy = cons(1, supplierMock)
    lazy.head
    Mockito.verifyZeroInteractions(supplierMock)
  }

  @Test
  fun testEvaluateTailOnlyOnceWhenTailsHeadRequested() {
    val lazy = cons(1, supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(cons(2, supplierMock))

    val tailsHead = lazy.tail.head
    verify(supplierMock).invoke()
    assertThat(tailsHead).isEqualTo(2)
  }

  @Test
  fun testEvaluateTailOnlyTwiceOnAccessingThirdElement() {
    val lazy = cons(1, supplierMock)
    BDDMockito.given(supplierMock.invoke()).willReturn(cons(2, supplierMock))

    val tailsHead = lazy.tail.tail.head

    verify(supplierMock, times(2)).invoke()
    assertThat(tailsHead).isEqualTo(2)
  }

  @Test
  fun testThrowWhenAccessHeadPastEnd() {
    val twoItems = permutationOf(1, 2)
    val tail = twoItems.tail.tail

    assertThatThrownBy {
      tail.head
    }.isInstanceOf(NoSuchElementException::class.java)
  }

  fun tail(): () -> Permutation<Int> = { permutationOf(42) }
}