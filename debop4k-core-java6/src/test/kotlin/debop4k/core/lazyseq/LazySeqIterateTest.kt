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

import debop4k.core.functional.constant
import debop4k.core.functional.identity
import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * LazySeqIterateTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqIterateTest : AbstractLazySeqTest() {

  @Test
  fun testReturnInfiniteSequenceOfNaturalNumbers() {
    val naturals = LazySeq.numbers(1)
    assertThat(naturals.head).isEqualTo(1)
    assertThat(naturals.tail.head).isEqualTo(2)
    assertThat(naturals.drop(1000).head).isEqualTo(1001)
  }

  @Test
  fun testReturnInfiniteSequenceOfPowersOfTwo() {
    val powersOfTwo = LazySeq.iterate(1) { it * 2 }
    assertThat(powersOfTwo[0]).isEqualTo(1)
    assertThat(powersOfTwo[1]).isEqualTo(2)
    assertThat(powersOfTwo[2]).isEqualTo(4)
    assertThat(powersOfTwo.drop(10).head).isEqualTo(1024)
  }

  @Test
  fun testReturnInfiniteSequenceOfPowersOfGrowingStrings() {
    val strings = LazySeq.iterate("") { it + it.length }
    assertThat(strings.head).isEqualTo("")
    assertThat(strings[1]).isEqualTo("0")
    assertThat(strings[2]).isEqualTo("01")
    assertThat(strings[3]).isEqualTo("012")
    assertThat(strings[4]).isEqualTo("0123")
  }

  @Test
  fun testReturnConstantSeqWithIdentity() {
    val constant = LazySeq.iterate(1, identity<Int>())
    assertThat(constant.head).isEqualTo(1)
    assertThat(constant[0]).isEqualTo(1)
    assertThat(constant[1]).isEqualTo(1)
    assertThat(constant[2]).isEqualTo(1)
    assertThat(constant[3]).isEqualTo(1)
  }

  @Test
  fun testReturnConstantSeqWithConstant() {
    val constant = LazySeq.iterate(1, constant(1))
    assertThat(constant.head).isEqualTo(1)
    assertThat(constant[0]).isEqualTo(1)
    assertThat(constant[1]).isEqualTo(1)
    assertThat(constant[2]).isEqualTo(1)
    assertThat(constant[3]).isEqualTo(1)
  }

  @Test
  fun testStreamOfPrimes() {
    val primes = primes()
    assertThat(primes.take(10)).isEqualTo(lazySeqOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29))
  }
}