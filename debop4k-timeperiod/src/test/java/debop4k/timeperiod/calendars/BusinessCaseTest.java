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

package debop4k.timeperiod.calendars;

import debop4k.core.Loggingx;
import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.timeranges.*;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimex.trimToSecond;
import static org.assertj.core.api.Assertions.assertThat;

public class BusinessCaseTest extends AbstractTimePeriodTest {

  private static final Logger log = Loggingx.loggerOf(BusinessCaseTest.class);

  @Test
  public void timeRangeCalendarTimeRange() {
    final DateTime now = Times.now();

    for (int i = 0; i < 500; i++) {
      DateTime current = now.plusDays(i);
      TimeRange currentFiveSeconds = new TimeRange(trimToSecond(current, 15), trimToSecond(current, 20));

      assertThat(new YearRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new HalfyearRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new QuarterRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new MonthRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new WeekRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new DayRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new HourRange(current).hasInside(currentFiveSeconds)).isTrue();
      assertThat(new MinuteRange(current).hasInside(currentFiveSeconds)).isTrue();
    }

    TimeRange anytime = new TimeRange();

    assertThat(new YearRange().hasInside(anytime)).isFalse();
    assertThat(new HalfyearRange().hasInside(anytime)).isFalse();
    assertThat(new QuarterRange().hasInside(anytime)).isFalse();
    assertThat(new MonthRange().hasInside(anytime)).isFalse();
    assertThat(new WeekRange().hasInside(anytime)).isFalse();
    assertThat(new DayRange().hasInside(anytime)).isFalse();
    assertThat(new HourRange().hasInside(anytime)).isFalse();
    assertThat(new MinuteRange().hasInside(anytime)).isFalse();
  }
}
