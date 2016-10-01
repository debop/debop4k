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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.list.MutableList;
import org.joda.time.DateTime;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.endTimeOfMonth;
import static debop4k.timeperiod.utils.Times.startTimeOfYear;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DayRangeTest extends AbstractTimePeriodTest {

  @Test
  public void initValues() {
    DateTime now = Times.now();
    DateTime firstDay = Times.startTimeOfDay(now);
    DateTime secondDay = firstDay.plusDays(1);

    DayRange dr = new DayRange(now, TimeCalendar.emptyOffset());

    assertThat(dr.getStart()).isEqualTo(firstDay);
    assertThat(dr.getEnd()).isEqualTo(secondDay);
  }

  @Test
  public void defaultCalendarTest() {
    DateTime yearStart = startTimeOfYear(Times.now());

    for (int m = 1; m <= TimeSpec.MonthsPerYear; m++) {
      DateTime monthStart = asDate(yearStart.getYear(), m, 1);
      DateTime monthEnd = endTimeOfMonth(yearStart.getYear(), m);

      for (int day = monthStart.getDayOfMonth(); day < monthEnd.getDayOfMonth(); day++) {

        DayRange dr = new DayRange(monthStart.plusDays(day - monthStart.getDayOfMonth()));

        assertThat(dr.getYear()).isEqualTo(yearStart.getYear());
        assertThat(dr.getMonthOfYear()).isEqualTo(monthStart.getMonthOfYear());
      }
    }
  }

  @Test
  public void constructorTest() {
    DateTime now = Times.now();

    DayRange dr = new DayRange(now);
    assertThat(dr.getStart()).isEqualTo(now.withTimeAtStartOfDay());

    dr = new DayRange(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());
    assertThat(dr.getStart()).isEqualTo(now.withTimeAtStartOfDay());
  }

  @Test
  public void dayOfWeekTest() {
    DateTime now = Times.now();
    DayRange dr = new DayRange(now, TimeCalendar.of());
    assertThat(dr.getDayOfWeek()).isEqualTo(TimeCalendar.of().dayOfWeek(now));
  }

  @Test
  public void addDaysTest() {

    final DateTime now = Times.now();
    final DateTime today = Times.today();
    final DayRange dr = new DayRange(now);


    assertThat(dr.prevDay().getStart()).isEqualTo(today.minusDays(1));
    assertThat(dr.nextDay().getStart()).isEqualTo(today.plusDays(1));

    assertThat(dr.addDays(0)).isEqualTo(dr);

    for (int i = -60; i < 120; i++) {
      assertThat(dr.addDays(i).getStart()).isEqualTo(today.plusDays(i));
    }
  }

  @Test
  public void getHoursTest() {
    final DayRange dr = new DayRange();
    MutableList<HourRange> hours = dr.hourStream();

    final int[] index = new int[]{0};
    hours.forEach(new Procedure<HourRange>() {
      @Override
      public void value(HourRange h) {
        assertThat(h.getStart()).isEqualTo(dr.getStart().plusHours(index[0]));
        assertThat(h.getEnd()).isEqualTo(h.getCalendar().mapEnd(h.getStart().plusHours(1)));
        index[0]++;
      }
    });
    assertThat(index[0]).isEqualTo(TimeSpec.HoursPerDay);
  }
}
