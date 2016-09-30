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

package debop4k.core.retry.backoff

import debop4k.core.retry.AbstractRetryKotlinTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown
import org.junit.Test

/**
 * ExponentialDelayBackoffKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class ExponentialDelayBackoffKotlinTest : AbstractRetryKotlinTest() {
  @Test
  @Throws(Exception::class)
  fun shouldThrowWhenNotPositiveInitialDelay() {
    //given
    val initialDelayMillis = 0

    try {
      //when
      ExponentialDelayBackoff(initialDelayMillis.toLong(), 2.0)
      failBecauseExceptionWasNotThrown(IllegalArgumentException::class.java)
    } catch (e: IllegalArgumentException) {
      //then
      assertThat(e.message).endsWith("0")
    }

  }

  @Test
  @Throws(Exception::class)
  fun shouldReturnPowersOfTwo() {
    //given
    val backoff = ExponentialDelayBackoff(1, 2.0)

    //when
    val first = backoff.delayMillis(retry(1))
    val second = backoff.delayMillis(retry(2))
    val third = backoff.delayMillis(retry(3))
    val fourth = backoff.delayMillis(retry(4))

    //then
    assertThat(first).isEqualTo(1)
    assertThat(second).isEqualTo(2)
    assertThat(third).isEqualTo((2 * 2).toLong())
    assertThat(fourth).isEqualTo(2 * 2 * 2.toLong())
  }
}