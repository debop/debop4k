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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.joda.time.DateTime;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class WeekRangeTest extends AbstractTimePeriodTest {

  @Test
  public void initValues() {
    DateTime now = now();
    DateTime firstWeek = startTimeOfWeek(now);
    DateTime secondWeek = firstWeek.plusWeeks(1);

    WeekRange weekRange = new WeekRange(now, TimeCalendar.emptyOffset());

    assertThat(weekRange.getStart()).isEqualTo(firstWeek);
    assertThat(weekRange.getEnd()).isEqualTo(secondWeek);
  }

  @Test
  public void defaultCalendarTest() {
    DateTime yearStart = startTimeOfYear(now());

    for (int w = 1; w < 50; w++) {

      WeekRange weekRange = new WeekRange(yearStart.plusWeeks(w));
      assertThat(weekRange.getYear()).isEqualTo(yearStart.getYear());
      assertThat(weekRange.getWeekOfWeekyear()).isEqualTo(yearStart.plusWeeks(w).getWeekOfWeekyear());

      assertThat(weekRange.unmappedStart()).isEqualTo(startTimeOfWeek(yearStart.plusWeeks(w)));
      assertThat(weekRange.unmappedEnd()).isEqualTo(startTimeOfWeek(yearStart.plusWeeks(w)).plusWeeks(1));
    }
  }


  @Test
  public void getDaysTest() {
    final DateTime now = now();
    final WeekRange weekRange = new WeekRange();
    final MutableList<DayRange> days = weekRange.dayStream();

    int index = 0;
    for (DayRange day : days) {
      assertThat(day.getStart()).isEqualTo(weekRange.getStart().plusDays(index));
      assertThat(day.getEnd()).isEqualTo(day.getCalendar().mapEnd(day.getStart().plusDays(1)));
      index++;
    }
    assertThat(index).isEqualTo(TimeSpec.DaysPerWeek);
  }

  @Test
  public void addMonthsTest() {

    final DateTime now = now();
    final DateTime startWeek = startTimeOfWeek(now);
    final WeekRange weekRange = new WeekRange(now);

    log.trace("startWeek=[{}], weekRange=[{}]", startWeek, weekRange);

    assertThat(weekRange.prevWeek().getStart()).isEqualTo(startWeek.minusWeeks(1));
    assertThat(weekRange.nextWeek().getStart()).isEqualTo(startWeek.plusWeeks(1));

    assertThat(weekRange.addWeeks(0)).isEqualTo(weekRange);

    for (int m = -60; m < 120; m++) {
      assertThat(weekRange.addWeeks(m).getStart())
          .isEqualTo(startWeek.plusWeeks(m));
    }
  }
}
