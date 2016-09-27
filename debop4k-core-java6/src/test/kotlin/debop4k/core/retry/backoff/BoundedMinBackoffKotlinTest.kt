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

package debop4k.core.retry.backoff

import debop4k.core.retry.AbstractRetryKotlinTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * BoundedMinBackoffKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class BoundedMinBackoffKotlinTest : AbstractRetryKotlinTest() {

  @Test
  @Throws(Exception::class)
  fun shouldReturnOriginalBackoffDelayIfAboveMin() {
    val backoff = ExponentialDelayBackoff(1000, 2.0).withMinDelay()

    assertThat(backoff.delayMillis(retry(1))).isEqualTo(1000)
    assertThat(backoff.delayMillis(retry(2))).isEqualTo(2000)
    assertThat(backoff.delayMillis(retry(3))).isEqualTo(4000)
    assertThat(backoff.delayMillis(retry(4))).isEqualTo(8000)
  }

  @Test
  @Throws(Exception::class)
  fun shouldCapBackoffAtDefaultLevel() {
    val backoff = ExponentialDelayBackoff(1, 2.0).withMinDelay()

    assertThat(backoff.delayMillis(retry(1))).isEqualTo(DEFAULT_MIN_DELAY_MILLIS)
  }

  @Test
  @Throws(Exception::class)
  fun shouldCapBackoffAtGivenLevel() {
    val backoff = ExponentialDelayBackoff(1, 2.0).withMaxDelay(250)

    assertThat(backoff.delayMillis(retry(100))).isEqualTo(250)
  }

  @Test
  @Throws(Exception::class)
  fun shouldApplyBothMinAndMaxBound() {
    val backoff = ExponentialDelayBackoff(1, 2.0).withMinDelay(5).withMaxDelay(10)

    assertThat(backoff.delayMillis(retry(2))).isEqualTo(5)
    assertThat(backoff.delayMillis(retry(3))).isEqualTo(5)
    assertThat(backoff.delayMillis(retry(4))).isEqualTo(8)
    assertThat(backoff.delayMillis(retry(5))).isEqualTo(10)
  }
}