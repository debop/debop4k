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
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.core.retry.backoff.Backoffx.DEFAULT_MAX_DELAY_MILLIS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * BoundedMaxBackoffTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
public class BoundedMaxBackoffTest extends AbstractRetryTest {

  @Test
  public void shouldReturnOriginalBackoffDelayIfBelowMax() throws Exception {
    final Backoff backoff = new ExponentialDelayBackoff(1, 2.0).withMaxDelay();

    assertThat(backoff.delayMillis(retry(1))).isEqualTo(1);
    assertThat(backoff.delayMillis(retry(2))).isEqualTo(2);
    assertThat(backoff.delayMillis(retry(3))).isEqualTo(4);
    assertThat(backoff.delayMillis(retry(4))).isEqualTo(8);
  }

  @Test
  public void shouldCapBackoffAtDefaultLevel() throws Exception {
    final Backoff backoff = new ExponentialDelayBackoff(1, 2.0).withMaxDelay();

    assertThat(backoff.delayMillis(retry(100))).isEqualTo(DEFAULT_MAX_DELAY_MILLIS);
  }

  @Test
  public void shouldCapBackoffAtGivenLevel() throws Exception {
    final Backoff backoff = new ExponentialDelayBackoff(1, 2.0).withMaxDelay(1234);

    assertThat(backoff.delayMillis(retry(100))).isEqualTo(1234);
  }
}
