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

import com.nhaarman.mockito_kotlin.mock
import debop4k.core.retry.AbstractRetryKotlinTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import java.util.*

/**
 * RandomBackoffKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RandomBackoffKotlinTest : AbstractRetryKotlinTest() {

  private val randomMock: Random = mock()

  @Test
  fun shouldApplyRandomUniformDistributionWithDefaultRange() {
    //given
    val backoff = FixedIntervalBackoff().withUniformJitter()

    //when
    val delay = backoff.delayMillis(anyRetry())

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo(DEFAULT_PERIOD_MILLIS - DEFAULT_RANDOM_RANGE_MILLIS)
        .isLessThanOrEqualTo(DEFAULT_PERIOD_MILLIS + DEFAULT_RANDOM_RANGE_MILLIS)
  }

  @Test
  @Throws(Exception::class)
  fun shouldApplyRandomUniformDistribution() {
    //given
    val range = 300
    val backoff = FixedIntervalBackoff().withUniformJitter(range.toLong())

    //when
    val delay = backoff.delayMillis(anyRetry())

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo(DEFAULT_PERIOD_MILLIS - range)
        .isLessThanOrEqualTo(DEFAULT_PERIOD_MILLIS + range)
  }

  @Test
  @Throws(Exception::class)
  fun shouldApplyRandomUniformDistributionWithCustomRandomSource() {
    //given
    val backoff = UniformRandomBackoff(FixedIntervalBackoff(), randomMock)
    BDDMockito.given<Double>(randomMock.nextDouble()).willReturn(0.5)

    //when
    val delay = backoff.delayMillis(anyRetry())

    //then
    assertThat(delay).isEqualTo(DEFAULT_PERIOD_MILLIS)
  }

  @Test
  @Throws(Exception::class)
  fun shouldApplyRandomProportionalDistributionWithDefaultRange() {
    //given
    val backoff = FixedIntervalBackoff().withProportionalJitter()

    //when
    val delay = backoff.delayMillis(anyRetry())

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo((DEFAULT_PERIOD_MILLIS * (1 - DEFAULT_MULTIPLIER)).toLong())
        .isLessThan((DEFAULT_PERIOD_MILLIS * (1 + DEFAULT_MULTIPLIER)).toLong())
  }

  @Test
  @Throws(Exception::class)
  fun shouldApplyRandomProportionalDistribution() {
    //given
    val range = 0.3
    val backoff = FixedIntervalBackoff().withProportionalJitter(range)

    //when
    val delay = backoff.delayMillis(anyRetry())

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo((DEFAULT_PERIOD_MILLIS * (1 - range)).toLong())
        .isLessThan((DEFAULT_PERIOD_MILLIS * (1 + range)).toLong())
  }

  @Test
  @Throws(Exception::class)
  fun shouldApplyRandomProportionalDistributionWithCustomRandomSource() {
    //given
    val backoff = ProportionalRandomBackoff(FixedIntervalBackoff(), randomMock)
    BDDMockito.given<Double>(randomMock.nextDouble()).willReturn(0.5)

    //when
    val delay = backoff.delayMillis(anyRetry())

    //then
    assertThat(delay).isEqualTo(DEFAULT_PERIOD_MILLIS)
  }
}