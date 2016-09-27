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
 * FirstRetryNoDelayBackoffKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class FirstRetryNoDelayBackoffKotlinTest : AbstractRetryKotlinTest() {

  @Test
  fun firstRetryShouldHaveNoDelay() {
    //given
    val backoff = FixedIntervalBackoff(1000).withFirstRetryNoDelay()

    //when
    val first = backoff.delayMillis(retry(1))
    val second = backoff.delayMillis(retry(2))
    val third = backoff.delayMillis(retry(3))

    //then
    assertThat(first).isEqualTo(0)
    assertThat(second).isEqualTo(1000)
    assertThat(third).isEqualTo(1000)
  }

  @Test
  fun secondRetryShouldCalculateDelayAsIfItWasFirst() {
    //given
    val backoff = ExponentialDelayBackoff(100, 2.0).withFirstRetryNoDelay()

    //when
    val first = backoff.delayMillis(retry(1))
    val second = backoff.delayMillis(retry(2))
    val third = backoff.delayMillis(retry(3))
    val fourth = backoff.delayMillis(retry(4))

    //then
    assertThat(first).isEqualTo(0)
    assertThat(second).isEqualTo(100)
    assertThat(third).isEqualTo(200)
    assertThat(fourth).isEqualTo(400)
  }
}