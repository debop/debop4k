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

import debop4k.timeperiod.*;
import debop4k.timeperiod.models.PeriodUnit;
import debop4k.timeperiod.timeranges.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TimesPeriodTest extends AbstractTimePeriodTest {

  public static final int PeriodCount = 24;
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimesPeriodTest.class);

  private DateTime startTime = new DateTime(2008, 4, 10, 5, 33, 24, 345);
  private DateTime endTime = new DateTime(2018, 10, 20, 13, 43, 12, 599);
  private Duration duration = new Duration(startTime, endTime);

  @Test
  public void getTimeBlockByDuration() {
    TimeBlock block = new TimeBlock(startTime, duration);
    assertThat(block.getStart()).isEqualTo(startTime);
    assertThat(block.getEnd()).isEqualTo(endTime);
    assertThat(block.getDuration()).isEqualTo(duration);
  }

  @Test
  public void getTimeBlockByStartAndEnd() {
    TimeBlock block = new TimeBlock(startTime, endTime);
    assertThat(block.getStart()).isEqualTo(startTime);
    assertThat(block.getEnd()).isEqualTo(endTime);
    assertThat(block.getDuration()).isEqualTo(duration);
  }

  @Test
  public void getTimeRangeByDuration() {
    TimeRange range = new TimeRange(startTime, duration);
    assertThat(range.getStart()).isEqualTo(startTime);
    assertThat(range.getEnd()).isEqualTo(endTime);
    assertThat(range.getDuration()).isEqualTo(duration);

    range = new TimeRange(startTime, Durations.negate(duration));
    assertThat(range.getStart()).isEqualTo(startTime.minus(duration));
    assertThat(range.getEnd()).isEqualTo(endTime.minus(duration));
    assertThat(range.getDuration()).isEqualTo(duration);
  }

  @Test
  public void getTimeRangeByStartAndEnd() {
    TimeRange range = new TimeRange(startTime, endTime);
    assertThat(range.getStart()).isEqualTo(startTime);
    assertThat(range.getEnd()).isEqualTo(endTime);
    assertThat(range.getDuration()).isEqualTo(duration);
  }

  @Test
  public void getPeriodOfTest() {
    for (PeriodUnit unit : PeriodUnit.values()) {
      if (unit == PeriodUnit.ALL || unit == PeriodUnit.MILLISECOND)
        continue;

      DateTime moment = startTime;
      ITimePeriod period = Periods.periodOf(moment, unit);

      assertThat(period.hasInside(moment)).isTrue();
      assertThat(period.hasInside(endTime)).isFalse();


      log.trace("[{}] : period[{}] hasInside=[{}]", unit, period, moment);
    }
  }

  @Test
  public void getPeriodOfWithCalendar() {
    for (PeriodUnit unit : PeriodUnit.values()) {
      if (unit == PeriodUnit.ALL || unit == PeriodUnit.MILLISECOND)
        continue;

      DateTime moment = startTime;
      ITimeCalendar calendar = TimeCalendar.EMPTY_OFFSET;
      ITimePeriod period = Periods.periodOf(moment, unit, calendar);

      assertThat(period.hasInside(moment)).isTrue();
      assertThat(period.hasInside(endTime)).isFalse();


      log.trace("[{}] : period[{}] hasInside=[{}]", unit, period, moment);
    }
  }

  @Test
  public void getPeriodsOfTest() {
    for (PeriodUnit unit : PeriodUnit.values()) {
      if (unit == PeriodUnit.ALL || unit == PeriodUnit.MILLISECOND)
        continue;

      for (int count = 1; count < 5; count++) {
        DateTime moment = startTime;
        ITimeCalendar calendar = TimeCalendar.EMPTY_OFFSET;
        ITimePeriod period = Periods.periodOf(moment, unit, count, calendar);

        log.trace("unit={}, period={}, moment={}", unit, period, moment);
        assertThat(period.hasPeriod()).isTrue();
        assertThat(period.hasInside(moment)).isTrue();
        assertThat(period.hasInside(endTime)).isFalse();


        log.trace("[{}] : period[{}] hasInside=[{}]", unit, period, moment);
      }
    }
  }

  @Test
  public void getYearRangeTest() {
    YearRange yearRange = new YearRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfYear(startTime);

    assertThat(yearRange.getStart()).isEqualTo(start);
    assertThat(yearRange.getStartYear()).isEqualTo(start.getYear());
    assertThat(yearRange.getEnd()).isEqualTo(start.plusYears(1));
    assertThat(yearRange.getEndYear()).isEqualTo(start.plusYears(1).getYear());
  }

  @Test
  public void getYearRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      YearRangeCollection yearRanges = new YearRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfYear(startTime);

      assertThat(yearRanges.getStart()).isEqualTo(start);
      assertThat(yearRanges.getStartYear()).isEqualTo(start.getYear());
      assertThat(yearRanges.getEnd()).isEqualTo(start.plusYears(i));
      assertThat(yearRanges.getEndYear()).isEqualTo(start.plusYears(i).getYear());
    }
  }

  @Test
  public void getHalfyearRangeTest() {
    HalfyearRange hy = new HalfyearRange(startTime, TimeCalendar.EMPTY_OFFSET);

    DateTime start = startTimeOfHalfyear(startTime);
    assertThat(hy.getStart()).isEqualTo(start);
    assertThat(hy.getEnd()).isEqualTo(hy.nextHalfyear().getStart());
  }

  @Test
  public void getHalfyearRangesTest() {

    for (int i = 1; i < PeriodCount; i++) {
      HalfyearRangeCollection hys = new HalfyearRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);

      DateTime start = startTimeOfHalfyear(startTime);
      assertThat(hys.getStart()).isEqualTo(start);
      assertThat(hys.getEnd()).isEqualTo(start.plusMonths(i * TimeSpec.MonthsPerHalfyear));
      assertThat(hys.getHalfyearCount()).isEqualTo(i);
    }
  }

  @Test
  public void getQuarterRangeTest() {
    QuarterRange qr = new QuarterRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfQuarter(startTime);

    assertThat(qr.getStart()).isEqualTo(start);
    assertThat(qr.getEnd()).isEqualTo(qr.nextQuarter().getStart());
  }

  @Test
  public void getQuarterRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      QuarterRangeCollection quarters = new QuarterRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfQuarter(startTime);

      assertThat(quarters.getStart()).isEqualTo(start);
      assertThat(quarters.getEnd()).isEqualTo(start.plusMonths(i * TimeSpec.MonthsPerQuarter));
      assertThat(quarters.getQuarterCount()).isEqualTo(i);
    }
  }

  @Test
  public void getMonthRangeTest() {
    MonthRange mr = new MonthRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfMonth(startTime);

    assertThat(mr.getStart()).isEqualTo(start);
    assertThat(mr.getEnd()).isEqualTo(mr.getNextMonth().getStart());
  }

  @Test
  public void getMonthRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      MonthRangeCollection mrs = new MonthRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfMonth(startTime);

      assertThat(mrs.getStart()).isEqualTo(start);
      assertThat(mrs.getEnd()).isEqualTo(start.plusMonths(i));
      assertThat(mrs.getMonthCount()).isEqualTo(i);
    }
  }

  @Test
  public void getWeekRangeTest() {
    WeekRange wr = new WeekRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfWeek(startTime);

    assertThat(wr.getStart()).isEqualTo(start);
    assertThat(wr.getEnd()).isEqualTo(wr.getNextWeek().getStart());
  }

  @Test
  public void getWeekRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      WeekRangeCollection wks = new WeekRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfWeek(startTime);

      assertThat(wks.getStart()).isEqualTo(start);
      assertThat(wks.getEnd()).isEqualTo(start.plusWeeks(i));
      assertThat(wks.getWeekCount()).isEqualTo(i);
    }
  }

  @Test
  public void getDayRangeTest() {
    DayRange dr = new DayRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfDay(startTime);

    assertThat(dr.getStart()).isEqualTo(start);
    assertThat(dr.getEnd()).isEqualTo(dr.nextDay().getStart());
  }

  @Test
  public void getDayRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      DayRangeCollection drs = new DayRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfDay(startTime);

      assertThat(drs.getStart()).isEqualTo(start);
      assertThat(drs.getEnd()).isEqualTo(start.plusDays(i));
      assertThat(drs.getDayCount()).isEqualTo(i);
    }
  }

  @Test
  public void getHourRangeTest() {
    HourRange hr = new HourRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfHour(startTime);

    assertThat(hr.getStart()).isEqualTo(start);
    assertThat(hr.getEnd()).isEqualTo(hr.nextHour().getStart());
  }

  @Test
  public void getHourRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      HourRangeCollection drs = new HourRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfHour(startTime);

      assertThat(drs.getStart()).isEqualTo(start);
      assertThat(drs.getEnd()).isEqualTo(start.plusHours(i));
      assertThat(drs.getHourCount()).isEqualTo(i);
    }
  }

  @Test
  public void getMinuteRangeTest() {
    MinuteRange hr = new MinuteRange(startTime, TimeCalendar.EMPTY_OFFSET);
    DateTime start = startTimeOfMinute(startTime);

    assertThat(hr.getStart()).isEqualTo(start);
    assertThat(hr.getEnd()).isEqualTo(hr.getNextMinute().getStart());
  }

  @Test
  public void getMinuteRangesTest() {
    for (int i = 1; i < PeriodCount; i++) {
      MinuteRangeCollection drs = new MinuteRangeCollection(startTime, i, TimeCalendar.EMPTY_OFFSET);
      DateTime start = startTimeOfMinute(startTime);

      assertThat(drs.getStart()).isEqualTo(start);
      assertThat(drs.getEnd()).isEqualTo(start.plusMinutes(i));
      assertThat(drs.getMinuteCount()).isEqualTo(i);
    }
  }

}
