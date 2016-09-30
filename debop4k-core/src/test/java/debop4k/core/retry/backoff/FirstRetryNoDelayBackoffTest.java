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

package debop4k.core.retry.backoff;

import debop4k.core.retry.AbstractRetryTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FirstRetryNoDelayBackoffTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
public class FirstRetryNoDelayBackoffTest extends AbstractRetryTest {

  @Test
  public void firstRetryShouldHaveNoDelay() {
    //given
    final Backoff backoff = new FixedIntervalBackoff(1_000).withFirstRetryNoDelay();

    //when
    final long first = backoff.delayMillis(retry(1));
    final long second = backoff.delayMillis(retry(2));
    final long third = backoff.delayMillis(retry(3));

    //then
    assertThat(first).isEqualTo(0);
    assertThat(second).isEqualTo(1_000);
    assertThat(third).isEqualTo(1_000);
  }

  @Test
  public void secondRetryShouldCalculateDelayAsIfItWasFirst() {
    //given
    final Backoff backoff = new ExponentialDelayBackoff(100, 2).withFirstRetryNoDelay();

    //when
    final long first = backoff.delayMillis(retry(1));
    final long second = backoff.delayMillis(retry(2));
    final long third = backoff.delayMillis(retry(3));
    final long fourth = backoff.delayMillis(retry(4));

    //then
    assertThat(first).isEqualTo(0);
    assertThat(second).isEqualTo(100);
    assertThat(third).isEqualTo(200);
    assertThat(fourth).isEqualTo(400);
  }
}
