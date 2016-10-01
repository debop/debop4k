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

package debop4k.timeperiod.base;

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.TimePeriodContainer;
import debop4k.timeperiod.TimeRange;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimes.asDate;
import static org.assertj.core.api.Assertions.assertThat;

public class TimePeriodcontainerTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimePeriodcontainerTest.class);

  @Test
  public void testConstructor() {
    TimeRange period1 = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));
    TimeRange period2 = new TimeRange(asDate(2011, 4, 22), asDate(2011, 4, 25));

    TimePeriodContainer container = TimePeriodContainer.of(period1, period2);
    log.trace("container={}", container);
    assertThat(container.size()).isEqualTo(2);

    TimePeriodContainer container2 = TimePeriodContainer.of(period1, period2, container);
    assertThat(container2.size()).isEqualTo(2);
  }
}
