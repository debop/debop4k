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

class LazySeqScanTest : AbstractLazySeqTest() {

  @Test fun testInitialElementWithEmptySeq() {
    val scanned: LazySeq<Int> = emptyIntSeq.scan(0) { acc, c -> acc + c }
    assertThat(scanned).isEqualTo(lazySeqOf(0))
  }

  @Test fun testScannedFixedSeq() {
    val fixed = lazySeqOf(1, 2, 3, 4)

    val scanned = fixed.scan(0) { acc, c -> acc + c }
    assertThat(scanned).isEqualTo(lazySeqOf(0, 1, 3, 6, 10))
  }

  @Test fun testScannedFixedSeqOfStrings() {
    val fixed = LazySeq.continually("*").take(5)

    val scanned = fixed.scan("") { acc, c -> acc + c }
    assertThat(scanned).isEqualTo(lazySeqOf("", "*", "**", "***", "****", "*****"))
  }

  @Test fun testScanInfiniteSeq() {
    val primes = primes()

    val scanned = primes.scan(1) { acc, c -> acc * c }

    assertThat(scanned.take(4)).isEqualTo(lazySeqOf(1, 1 * 2, 1 * 2 * 3, 1 * 2 * 3 * 5))
  }
}