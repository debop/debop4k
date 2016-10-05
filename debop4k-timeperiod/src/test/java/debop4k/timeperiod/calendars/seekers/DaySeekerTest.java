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

package debop4k.timeperiod.calendars.seekers;

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.calendars.CalendarVisitorFilter;
import debop4k.timeperiod.calendars.SeekDirection;
import debop4k.timeperiod.timeranges.DayRange;
import debop4k.timeperiod.timeranges.DayRangeCollection;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static debop4k.core.kodatimes.KodaTimex.asDate;
import static debop4k.core.kodatimes.KodaTimex.asDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class DaySeekerTest extends AbstractTimePeriodTest {

  private static final Logger log = LoggerFactory.getLogger(DaySeekerTest.class);

  @Test
  public void simpleForward() {

    final DayRange start = new DayRange();
    final DaySeeker daySeeker = new DaySeeker();

    DayRange day1 = daySeeker.findDay(start, 0);
    assertThat(day1.isSamePeriod(start)).isTrue();

    DayRange day2 = daySeeker.findDay(start, 1);
    assertThat(day2).isNotNull();
    assertThat(day2.isSamePeriod(start.nextDay())).isTrue();

    for (int i = -10; i < 20; i++) {
      Integer dayCount = i * 5;
      DayRange day = daySeeker.findDay(start, dayCount);
      assertThat(day).isNotNull();
      assertThat(day.isSamePeriod(start.addDays(dayCount))).isTrue();
    }
  }

  @Test
  public void simpleBackward() {

    final DayRange start = new DayRange();
    final DaySeeker daySeeker = new DaySeeker(new CalendarVisitorFilter(), SeekDirection.Backward);

    DayRange day1 = daySeeker.findDay(start, 0);
    assertThat(day1).isNotNull();
    assertThat(day1.isSamePeriod(start)).isTrue();

    DayRange day2 = daySeeker.findDay(start, 1);
    assertThat(day2).isNotNull();
    assertThat(day2.isSamePeriod(start.prevDay())).isTrue();

    for (int i = -10; i < 20; i++) {
      Integer offset = i * 5;
      DayRange day = daySeeker.findDay(start, offset);
      assertThat(day).isNotNull();
      assertThat(day.isSamePeriod(start.addDays(-offset))).isTrue();
    }
  }

  @Test
  public void seekDirectionTest() {
    final DayRange start = new DayRange();
    final DaySeeker daySeeker = new DaySeeker();

    for (int i = -10; i < 20; i++) {
      Integer offset = i * 5;
      DayRange day = daySeeker.findDay(start, offset);
      assertThat(day).isNotNull();
      assertThat(day.isSamePeriod(start.addDays(offset))).isTrue();
    }

    final DaySeeker backwardSeeker = new DaySeeker(new CalendarVisitorFilter(), SeekDirection.Backward);

    for (int i = -10; i < 20; i++) {
      Integer offset = i * 5;
      DayRange day = backwardSeeker.findDay(start, offset);
      assertThat(day).isNotNull();
      assertThat(day.isSamePeriod(start.addDays(-offset))).isTrue();
    }
  }

  @Test
  public void minDateTest() {
    DaySeeker daySeeker = new DaySeeker();
    DayRange day = daySeeker.findDay(new DayRange(TimeSpec.MinPeriodTime), -10);
    assertThat(day).isNull();
  }

  @Test
  public void maxDateTest() {
    DaySeeker daySeeker = new DaySeeker();
    DayRange day = daySeeker.findDay(new DayRange(TimeSpec.MaxPeriodTime), 10);
    assertThat(day).isNull();
  }

  @Test
  public void seekWeekendHolidayTest() {

    DayRange start = new DayRange(asDate(2011, 2, 15));

    CalendarVisitorFilter filter = new CalendarVisitorFilter();
    filter.addWorkingWeekdays();
    filter.getExcludePeriods().add(new DayRangeCollection(asDateTime(2011, 2, 27), 14)); // 14 daysView -> week 9
    // and week 10

    DaySeeker daySeeker = new DaySeeker(filter);

    DayRange day1 = daySeeker.findDay(start, 3);
    assertThat(day1).isEqualTo(new DayRange(2011, 2, 18));

    DayRange day2 = daySeeker.findDay(start, 4);                // 주말 (19, 20) 제외
    assertThat(day2).isEqualTo(new DayRange(2011, 2, 21));

    DayRange day3 = daySeeker.findDay(start, 10);                // 주말 (19, 20) 제외, 2.27부터 14일간 휴가
    assertThat(day3).isEqualTo(new DayRange(2011, 3, 15));
  }

}
