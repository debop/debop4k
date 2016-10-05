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

package debop4k.core.retry.backoff;

import debop4k.core.retry.AbstractRetryTest;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Random;

import static debop4k.core.retry.backoff.Backoffx.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * RandomBackoffTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
public class RandomBackoffTest extends AbstractRetryTest {

  @Mock
  private Random randomMock;

  @Test
  public void shouldApplyRandomUniformDistributionWithDefaultRange() throws Exception {
    //given
    final Backoff backoff = new FixedIntervalBackoff().withUniformJitter();

    //when
    final long delay = backoff.delayMillis(anyRetry());

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo(DEFAULT_PERIOD_MILLIS - DEFAULT_RANDOM_RANGE_MILLIS)
        .isLessThanOrEqualTo(DEFAULT_PERIOD_MILLIS + DEFAULT_RANDOM_RANGE_MILLIS);
  }

  @Test
  public void shouldApplyRandomUniformDistribution() throws Exception {
    //given
    final int range = 300;
    final Backoff backoff = new FixedIntervalBackoff().withUniformJitter(range);

    //when
    final long delay = backoff.delayMillis(anyRetry());

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo(DEFAULT_PERIOD_MILLIS - range)
        .isLessThanOrEqualTo(DEFAULT_PERIOD_MILLIS + range);
  }

  @Test
  public void shouldApplyRandomUniformDistributionWithCustomRandomSource() throws Exception {
    //given
    final Backoff backoff = new UniformRandomBackoff(new FixedIntervalBackoff(), randomMock);
    given(randomMock.nextDouble()).willReturn(0.5);

    //when
    final long delay = backoff.delayMillis(anyRetry());

    //then
    assertThat(delay).isEqualTo(DEFAULT_PERIOD_MILLIS);
  }

  @Test
  public void shouldApplyRandomProportionalDistributionWithDefaultRange() throws Exception {
    //given
    final Backoff backoff = new FixedIntervalBackoff().withProportionalJitter();

    //when
    final long delay = backoff.delayMillis(anyRetry());

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo((long) (DEFAULT_PERIOD_MILLIS * (1 - DEFAULT_MULTIPLIER)))
        .isLessThan((long) (DEFAULT_PERIOD_MILLIS * (1 + DEFAULT_MULTIPLIER)));
  }

  @Test
  public void shouldApplyRandomProportionalDistribution() throws Exception {
    //given
    final double range = 0.3;
    final Backoff backoff = new FixedIntervalBackoff().withProportionalJitter(range);

    //when
    final long delay = backoff.delayMillis(anyRetry());

    //then
    assertThat(delay)
        .isGreaterThanOrEqualTo((long) (DEFAULT_PERIOD_MILLIS * (1 - range)))
        .isLessThan((long) (DEFAULT_PERIOD_MILLIS * (1 + range)));
  }

  @Test
  public void shouldApplyRandomProportionalDistributionWithCustomRandomSource() throws Exception {
    //given
    final Backoff backoff = new ProportionalRandomBackoff(new FixedIntervalBackoff(), randomMock);
    given(randomMock.nextDouble()).willReturn(0.5);

    //when
    final long delay = backoff.delayMillis(anyRetry());

    //then
    assertThat(delay).isEqualTo(DEFAULT_PERIOD_MILLIS);
  }

}
