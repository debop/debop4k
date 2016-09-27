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

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito

/**
 * LazySeqContinuallyTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqContinuallyTest : AbstractLazySeqTest() {

  val supplierMock = mock<() -> Int>()

  @Test
  fun testInvokeGeneratorOnlyOnceOnCreation() {
    // given
    BDDMockito.given(supplierMock.invoke()).willReturn(42)

    // when
    LazySeq.continually(supplierMock)

    // then
    BDDMockito.verify(supplierMock).invoke()
  }

  @Test
  fun testInvokeGeneratorTwiceWhenAskingForTail() {
    // given
    BDDMockito.given(supplierMock.invoke()).willReturn(42)

    // when
    LazySeq.continually(supplierMock).tail

    // then
    BDDMockito.verify(supplierMock, Mockito.times(2)).invoke()
  }

  @Test
  fun testNotCallGeneratorForTheSecondTimeWhenAccessingTail() {
    // given
    BDDMockito.given(supplierMock.invoke()).willReturn(42, 43)

    // when
    val cont = LazySeq.continually(supplierMock)
    cont.tail.head
    cont.tail.head

    // then
    BDDMockito.verify(supplierMock, Mockito.times(2)).invoke()
  }

  @Test
  fun testContinuallyProduceSeq() {
    // given
    BDDMockito.given(supplierMock.invoke()).willReturn(42, 43)

    // when
    val cont = LazySeq.continually(supplierMock)

    // then
    assertThat(cont.head).isEqualTo(42)
    assertThat(cont.tail.head).isEqualTo(43)
  }

  @Test
  fun testCreateCycleFromSingleItemIterable() {
    val posNeg = LazySeq.continually(listOf('a'))

    val subSeq = posNeg.take(5)

    assertThat(subSeq).isEqualTo(lazySeqOf('a', 'a', 'a', 'a', 'a'))
  }

  @Test
  fun testCreateCycleFromLongIterable() {
    val posNeg = LazySeq.continually(listOf(1, 2, 3, 2))
    val subSeq = posNeg.take(7)
    assertThat(subSeq).isEqualTo(lazySeqOf(1, 2, 3, 2, 1, 2, 3))
  }

  @Test
  fun testCreateEmptySeqIfCyclingEmptySeq() {
    val contEmpty: LazySeq<Any> = LazySeq.continually(listOf<Any>())
    assertThat(contEmpty).isEmpty()
    assertThat(contEmpty).isInstanceOf(Nil::class.java)
  }

  @Test
  fun testCreateCycleOfSingleElement() {
    val constant = LazySeq.continually('$')
    assertThat(constant.take(4)).isEqualTo(lazySeqOf('$', '$', '$', '$'))
  }
}