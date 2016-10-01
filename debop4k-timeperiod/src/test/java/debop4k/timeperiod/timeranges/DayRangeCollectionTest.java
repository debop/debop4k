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
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.utils.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DayRangeCollectionTest extends AbstractTimePeriodTest {

  @Test
  public void singleDays() {
    final DateTime start = Times.asDate(2004, 2, 22);
    DayRangeCollection days = DayRangeCollection.of(start, 1);

    assertThat(days.getDayCount()).isEqualTo(1);

    assertThat(days.getStartYear()).isEqualTo(start.getYear());
    assertThat(days.getStartMonthOfYear()).isEqualTo(start.getMonthOfYear());
    assertThat(days.getStartDayOfMonth()).isEqualTo(start.getDayOfMonth());

    assertThat(days.getEndYear()).isEqualTo(start.getYear());
    assertThat(days.getEndMonthOfYear()).isEqualTo(start.getMonthOfYear());
    assertThat(days.getEndDayOfMonth()).isEqualTo(start.getDayOfMonth());

    List<DayRange> dayList = days.dayStream();
    assertThat(dayList.size()).isEqualTo(1);
    assertThat(dayList.get(0).isSamePeriod(new DayRange(start))).isTrue();
  }

  @Test
  public void calendarDays() {
    final int dayCount = 5;

    final DateTime start = Times.asDate(2004, 2, 22);
    final DateTime end = start.plusDays(dayCount - 1);
    DayRangeCollection days = DayRangeCollection.of(start, dayCount);

    assertThat(days.getDayCount()).isEqualTo(dayCount);

    assertThat(days.getStartYear()).isEqualTo(start.getYear());
    assertThat(days.getStartMonthOfYear()).isEqualTo(start.getMonthOfYear());
    assertThat(days.getStartDayOfMonth()).isEqualTo(start.getDayOfMonth());

    assertThat(days.getEndYear()).isEqualTo(end.getYear());
    assertThat(days.getEndMonthOfYear()).isEqualTo(end.getMonthOfYear());
    assertThat(days.getEndDayOfMonth()).isEqualTo(end.getDayOfMonth());

    List<DayRange> dayList = days.dayStream();
    assertThat(dayList.size()).isEqualTo(dayCount);

    for (int i = 0; i < dayCount; i++) {
      assertThat(dayList.get(i).isSamePeriod(new DayRange(start.plusDays(i)))).isTrue();
    }
  }

  @Test
  public void calendarHoursTest() {

    final int[] dayCounts = new int[]{1, 6, 48, 180, 480};

    for (int dayCount : dayCounts) {
      DateTime now = Times.now();
      DayRangeCollection days = DayRangeCollection.of(now, dayCount);

      DateTime startTime = now.withTimeAtStartOfDay().plus(days.getCalendar().getStartOffset());
      DateTime endTime = startTime.plusDays(dayCount).plus(days.getCalendar().getEndOffset());

      assertThat(days.getStart()).isEqualTo(startTime);
      assertThat(days.getEnd()).isEqualTo(endTime);

      assertThat(days.getDayCount()).isEqualTo(dayCount);

      List<HourRange> items = days.hourStream();
      assertThat(items.size()).isEqualTo(dayCount * TimeSpec.HoursPerDay);

      for (int i = 0; i < items.size(); i++) {
        assertThat(items.get(i).getStart()).isEqualTo(startTime.plusHours(i));
        assertThat(items.get(i).getEnd()).isEqualTo(days.getCalendar().mapEnd(startTime.plusHours(i + 1)));
        assertThat(items.get(i).isSamePeriod(new HourRange(days.getStart().plusHours(i)))).isTrue();
      }
    }
  }
}
