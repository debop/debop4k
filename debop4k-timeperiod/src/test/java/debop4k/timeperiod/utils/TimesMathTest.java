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

package debop4k.timeperiod.utils;

import debop4k.timeperiod.AbstractTimePeriodTest;
import kotlin.Pair;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimex.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TimesMathTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimesMathTest.class);
  public final DateTime min = new DateTime(2000, 10, 2, 13, 45, 53, 754);
  public final DateTime max = new DateTime(2002, 9, 3, 7, 14, 22, 234);

  @Test
  public void minTest() {
    assertThat(min(min, max)).isEqualTo(min);
    assertThat(min(min, min)).isEqualTo(min);
    assertThat(min(max, max)).isEqualTo(max);

    assertThat(min(min, null)).isEqualTo(min);
    assertThat(min(null, min)).isEqualTo(min);
    assertThat(min((DateTime) null, null)).isNull();
  }

  @Test
  public void maxTest() {
    assertThat(max(min, max)).isEqualTo(max);
    assertThat(max(min, min)).isEqualTo(min);
    assertThat(max(max, max)).isEqualTo(max);

    assertThat(max(max, null)).isEqualTo(max);
    assertThat(max(null, max)).isEqualTo(max);
    assertThat(max((DateTime) null, null)).isNull();
  }

  @Test
  public void adjustPeriodTest() {

    Pair<DateTime, DateTime> pair = adjustPeriod(max, min);
    assertThat(pair.getFirst()).isEqualTo(min);
    assertThat(pair.getSecond()).isEqualTo(max);

    pair = adjustPeriod(min, max);
    assertThat(pair.getFirst()).isEqualTo(min);
    assertThat(pair.getSecond()).isEqualTo(max);
  }

  @Test
  public void adjustPeriodByDurationTest() {
    DateTime start = min;
    Duration duration = Durations.Day;

    Pair<DateTime, Duration> pair = adjustPeriod(start, duration);
    assertThat(pair.getFirst()).isEqualTo(min);
    assertThat(pair.getSecond()).isEqualTo(Durations.Day);

    pair = adjustPeriod(start, Durations.negate(duration));
    assertThat(pair.getFirst()).isEqualTo(min.minus(Durations.Day));
    assertThat(pair.getSecond()).isEqualTo(Durations.Day);
  }

}

