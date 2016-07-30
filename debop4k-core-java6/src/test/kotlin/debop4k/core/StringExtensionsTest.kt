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

package debop4k.core

import org.assertj.core.api.Assertions
import org.junit.Test

class StringExtensionsTest : AbstractCoreKotlinTest() {

  @Test
  fun testIsNull() {
    val a: String? = null
    Assertions.assertThat(a.isNull()).isTrue()

    val b: String? = "non null"
    Assertions.assertThat(b.isNull()).isFalse()

    log.debug("Success isNull test")
  }

  @Test
  fun testNonEmpty() {
    val a: String? = null
    Assertions.assertThat(a.nonEmpty()).isFalse()

    val b: String? = "non null"
    Assertions.assertThat(b.nonEmpty()).isTrue()

    log.debug("Success nonEmpty test")
  }
}
