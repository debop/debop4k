/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.core.result

import debop4k.core.loggerOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ValidationKotlinTest {

  private val log = loggerOf(javaClass)

  @Test
  fun testValidationWithNoError() {
    val r1 = Result.of(1)
    val r2 = Result.of(2)
    val r3 = Result.of(3)

    val validation = Validation(r1, r2, r3)

    assertThat(validation.hasFailure).isFalse()
    assertThat(validation.failures).isEmpty()
  }

  @Test
  fun testValidationWithError() {
    val r1 = Result.of(1)
    val r2 = Result.of { throw Exception("Not a number") }
    val r3 = Result.of(3)
    val r4 = Result.of { throw Exception("Divide by zero") }

    val validation = Validation(r1, r2, r3, r4)

    assertThat(validation.hasFailure).isTrue()
    assertThat(validation.failures)
        .isNotEmpty().hasSize(2)
    assertThat(validation.failures.map { it.message })
        .containsExactly("Not a number", "Divide by zero")
  }
}