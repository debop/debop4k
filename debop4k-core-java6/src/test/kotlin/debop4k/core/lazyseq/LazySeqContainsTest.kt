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

import debop4k.core.lazyseq.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * LazySeqContainsTest
 * @author sunghyouk.bae@gmail.com
 */
class LazySeqContainsTest : AbstractLazySeqTest() {

  @Test
  fun testReturnInfiniteSequenceOfNatualNumbers() {
    val naturals = LazySeq.numbers(1)
    assertThat(naturals).contains(17)
    assertThat(naturals.take(1000)).doesNotContain(-1, 0)
  }

  @Test
  fun testReturnInfiniteSequenceOfPowersOfTwo() {
    val powersOfTwo = LazySeq.iterate(1) { it * 2 }   // generateSequence(1) { it * 2 }
    assertThat(powersOfTwo).contains(16, 1024, 65536)
  }

  @Test
  fun testReturnInfiniteSequenceOfPowersOfGrowingStrings() {
    val strings = LazySeq.iterate("") { s -> s + s.length.toString() }
    assertThat(strings).contains("012345678910121416")
    log.debug("strings.take(15)={}", strings.take(15).mkString())
  }

  @Test
  fun testReturnStreamOfPrimes() {
    val primes = primes()
    assertThat(primes.take(10)).isEqualTo(lazySeqOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29))
    assertThat(primes).contains(997)
    assertThat(primes.take(1000)).doesNotContain(4, 6, 8, 9, 10, 12, 14, 15, 16)
  }
}