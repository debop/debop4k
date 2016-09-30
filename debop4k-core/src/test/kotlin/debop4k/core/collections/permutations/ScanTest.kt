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

import debop4k.core.collections.permutations.samples.primes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ScanTest : AbstractPermutationTest() {

  @Test fun testInitialElementWithEmptySeq() {
    val scanned: Permutation<Int> = emptyIntPermutation.scan(0) { acc, c -> acc + c }
    assertThat(scanned).isEqualTo(permutationOf(0))
  }

  @Test fun testScannedFixedSeq() {
    val fixed = permutationOf(1, 2, 3, 4)

    val scanned = fixed.scan(0) { acc, c -> acc + c }
    assertThat(scanned).isEqualTo(permutationOf(0, 1, 3, 6, 10))
  }

  @Test fun testScannedFixedSeqOfStrings() {
    val fixed = continually("*").take(5)

    val scanned = fixed.scan("") { acc, c -> acc + c }
    assertThat(scanned).isEqualTo(permutationOf("", "*", "**", "***", "****", "*****"))
  }

  @Test fun testScanInfiniteSeq() {
    val primes = primes()

    val scanned = primes.scan(1) { acc, c -> acc * c }

    assertThat(scanned.take(4)).isEqualTo(permutationOf(1, 1 * 2, 1 * 2 * 3, 1 * 2 * 3 * 5))
  }
}