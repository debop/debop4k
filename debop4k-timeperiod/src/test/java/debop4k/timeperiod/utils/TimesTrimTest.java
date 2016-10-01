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
import debop4k.timeperiod.TimeSpec;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimes.*;
import static debop4k.timeperiod.utils.Times.daysInMonth;
import static org.assertj.core.api.Assertions.assertThat;


public class TimesTrimTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimesTrimTest.class);

  @Test
  public void trimMonthTest() {
    assertThat(trimToMonth(testDate)).isEqualTo(asDate(testDate.getYear(), 1, 1));

    for (int m = 0; m < TimeSpec.MonthsPerYear; m++) {
      assertThat(trimToMonth(testDate, m + 1)).isEqualTo(asDate(testDate.getYear(), m + 1, 1));
    }
  }

  @Test
  public void trimDayTest() {
    assertThat(trimToDay(testDate)).isEqualTo(asDate(testDate.getYear(), testDate.getMonthOfYear(), 1));

    int maxDay = daysInMonth(testDate);
    for (int day = 0; day < maxDay; day++) {
      assertThat(trimToDay(testDate, day + 1))
          .isEqualTo(asDate(testDate.getYear(), testDate.getMonthOfYear(), day + 1));
    }
  }

  @Test
  public void trimHourTest() {
    assertThat(trimToHour(testDate)).isEqualTo(asDate(testDate));

    for (int h = 0; h < TimeSpec.HoursPerDay; h++) {
      assertThat(trimToHour(testDate, h)).isEqualTo(asDate(testDate).plusHours(h));
    }
  }

  @Test
  public void trimMimuteTest() {
    assertThat(trimToMinute(testDate)).isEqualTo(asDate(testDate).plusHours(testDate.getHourOfDay()));

    for (int m = 0; m < TimeSpec.MinutesPerHour; m++) {
      assertThat(trimToMinute(testDate, m))
          .isEqualTo(asDate(testDate).plusHours(testDate.getHourOfDay()).plusMinutes(m));
    }
  }

  @Test
  public void trimSecondTest() {
    assertThat(trimToSecond(testDate)).isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                                                   testDate.getMinuteOfHour(),
                                                                   0,
                                                                   0));
    for (int s = 0; s < TimeSpec.SecondsPerMinute; s++) {
      assertThat(trimToSecond(testDate, s))
          .isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                       testDate.getMinuteOfHour(),
                                       s,
                                       0));
    }
  }

  @Test
  public void trimMillisTest() {
    assertThat(trimToMillis(testDate)).isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                                                   testDate.getMinuteOfHour(),
                                                                   testDate.getSecondOfMinute(),
                                                                   0));
    for (int ms = 0; ms < TimeSpec.MillisPerSecond; ms++) {
      assertThat(trimToMillis(testDate, ms))
          .isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                       testDate.getMinuteOfHour(),
                                       testDate.getSecondOfMinute(),
                                       ms));
    }
  }
}
