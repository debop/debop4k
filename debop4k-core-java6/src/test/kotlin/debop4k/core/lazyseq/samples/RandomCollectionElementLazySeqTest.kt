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

package debop4k.core.lazyseq.samples

import debop4k.core.lazyseq.AbstractLazySeqTest
import debop4k.core.lazyseq.LazySeq
import org.junit.Test
import java.util.*

class RandomCollectionElementLazySeqTest : AbstractLazySeqTest() {

  val random = Random(System.currentTimeMillis())

  @Test
  fun testRandomCollection() {
    val charStream = LazySeq.continually { randomChar() }

    val uniqueCharStream = charStream.distinct()

    uniqueCharStream.take(25).forEach(::println)
  }

  fun randomChar(): Char {
    return 'A' + (random.nextDouble() * ('Z' - 'A' + 1)).toInt()
  }
}
