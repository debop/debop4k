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

package debop4k.timeperiod.timeranges;

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.TimeCalendar;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MonthRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(MonthRangeTest.class);

  @Test
  public void initValues() {
    DateTime now = now();
    DateTime firstMonth = startTimeOfMonth(now);
    DateTime secondMonth = firstMonth.plusMonths(1);

    MonthRange monthRange = new MonthRange(now, TimeCalendar.EMPTY_OFFSET);

    assertThat(monthRange.getStart()).isEqualTo(firstMonth);
    assertThat(monthRange.getEnd()).isEqualTo(secondMonth);
  }

  @Test
  public void defaultCalendarTest() {
    DateTime yearStart = startTimeOfYear(now());

    for (int m = 0; m < TimeSpec.MonthsPerYear; m++) {

      MonthRange monthRange = new MonthRange(yearStart.plusMonths(m));
      assertThat(monthRange.getYear()).isEqualTo(yearStart.getYear());
      assertThat(monthRange.getMonthOfYear()).isEqualTo(m + 1);

      assertThat(monthRange.getUnmappedStart()).isEqualTo(yearStart.plusMonths(m));
      assertThat(monthRange.getUnmappedEnd()).isEqualTo(yearStart.plusMonths(m + 1));
    }
  }


  @Test
  public void getDaysTest() {
    final DateTime now = now();
    final MonthRange monthRange = new MonthRange();
    List<DayRange> days = monthRange.days();

    int index = 0;
    for (DayRange day : days) {
      assertThat(day.getStart()).isEqualTo(monthRange.getStart().plusDays(index));
      assertThat(day.getEnd()).isEqualTo(day.getCalendar().mapEnd(day.getStart().plusDays(1)));
      index++;
    }
    assertThat(index).isEqualTo(daysInMonth(now.getYear(), now.getMonthOfYear()));
  }

  @Test
  public void addMonthsTest() {

    final DateTime now = Times.now();
    final DateTime startMonth = startTimeOfMonth(now);
    final MonthRange monthRange = new MonthRange(now);


    assertThat(monthRange.getPrevMonth().getStart())
        .isEqualTo(startMonth.minusMonths(1));
    assertThat(monthRange.getNextMonth().getStart())
        .isEqualTo(startMonth.plusMonths(1));

    assertThat(monthRange.addMonths(0)).isEqualTo(monthRange);

    for (int m = -60; m < 120; m++) {
      assertThat(monthRange.addMonths(m).getStart())
          .isEqualTo(startMonth.plusMonths(m));
    }
  }
}
