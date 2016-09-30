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
import org.junit.Test

/**
 * BoundedMaxBackoffKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class BoundedMaxBackoffKotlinTest : AbstractRetryKotlinTest() {

  @Test
  @Throws(Exception::class)
  fun shouldReturnOriginalBackoffDelayIfBelowMax() {
    val backoff = ExponentialDelayBackoff(1, 2.0).withMaxDelay()

    assertThat(backoff.delayMillis(retry(1))).isEqualTo(1)
    assertThat(backoff.delayMillis(retry(2))).isEqualTo(2)
    assertThat(backoff.delayMillis(retry(3))).isEqualTo(4)
    assertThat(backoff.delayMillis(retry(4))).isEqualTo(8)
  }

  @Test
  @Throws(Exception::class)
  fun shouldCapBackoffAtDefaultLevel() {
    val backoff = ExponentialDelayBackoff(1, 2.0).withMaxDelay()

    assertThat(backoff.delayMillis(retry(100))).isEqualTo(DEFAULT_MAX_DELAY_MILLIS)
  }

  @Test
  @Throws(Exception::class)
  fun shouldCapBackoffAtGivenLevel() {
    val backoff = ExponentialDelayBackoff(1, 2.0).withMaxDelay(1234)

    assertThat(backoff.delayMillis(retry(100))).isEqualTo(1234)
  }
}