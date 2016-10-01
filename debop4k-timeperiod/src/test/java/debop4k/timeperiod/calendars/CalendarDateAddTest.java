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

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.models.Timepart;
import debop4k.timeperiod.timeranges.DayRange;
import debop4k.timeperiod.timeranges.HourRangeInDay;
import debop4k.timeperiod.utils.Durations;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimes.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CalendarDateAddTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(CalendarDateAddTest.class);

  @Test
  public void noPeriodTest() {
    final CalendarDateAdd calendarDateAdd = new CalendarDateAdd();
    final DateTime now = now();

    for (int i = -10; i < 20; i++) {
      int offset = i * 5;
      assertThat(calendarDateAdd.add(now, Durations.dayOf(offset))).isEqualTo(now.plusDays(offset));
      assertThat(calendarDateAdd.add(now, Durations.dayOf(-offset))).isEqualTo(now.plusDays(-offset));

      assertThat(calendarDateAdd.subtract(now, Durations.dayOf(offset))).isEqualTo(now.plusDays(-offset));
      assertThat(calendarDateAdd.subtract(now, Durations.dayOf(-offset))).isEqualTo(now.plusDays(offset));
    }
  }

  @Test
  public void periodLimitsAdd() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 25));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 30), (DateTime) null); // 4월 30일 이후

    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    // 예외기간을 설정합니다. 4월 20일 ~ 4월25일, 4월 30일 이후
    dateAdd.getExcludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Day)).isEqualTo(test.plus(Durations.Day));

    // 4월 12일에 8일을 더하면 4월 20일이지만, 20~25일까지 제외되므로, 4월 25일이 된다.
    assertThat(dateAdd.add(test, Durations.dayOf(8))).isEqualTo(period1.getEnd());

    // 4월 12에 20일을 더하면 4월 20~25일을 제외한 후 계산하면 4월 30 이후가 된다. (5월 3일).
    // 하지만 4월 30 이후는 모두 제외되므로 결과값은 null이다.
    assertThat(dateAdd.add(test, Durations.dayOf(20))).isNull();

    assertThat(dateAdd.subtract(test, Durations.dayOf(3))).isEqualTo(test.minus(Durations.dayOf(3)));
  }

  @Test
  public void periodLimitsSubtract() {
    DateTime test = asDate(2011, 4, 30);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 25));
    ITimePeriod period2 = new TimeRange(null, asDate(2011, 4, 6)); // 4월 6일까지

    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    // 예외기간을 설정합니다. 4월 6일 이전, 4월 20일 ~ 4월 25일
    dateAdd.getExcludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.subtract(test, Durations.Day)).isEqualTo(test.minus(Durations.Day));

    // 4월 30일로부터 5일 전이면 4월 25일이지만, 예외기간이므로 4월20일이 된다.
    assertThat(dateAdd.subtract(test, Durations.dayOf(5))).isEqualTo(period1.getStart());

    // 4월 30일로부터 20일 전이면, 5월 전이 4월20일이므로, 4월 5일이 된다. 근데, 4월 6일 이전은 모두 제외기간이므로 null을 반환한다.
    assertThat(dateAdd.subtract(test, Durations.dayOf(20))).isNull();
  }

  @Test
  public void excludeTest() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));

    CalendarDateAdd dateAdd = CalendarDateAdd.of();
    dateAdd.getExcludePeriods().add(period);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(2))).isEqualTo(test.plusDays(2));
    assertThat(dateAdd.add(test, Durations.dayOf(3))).isEqualTo(period.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(3, 0, 0, 0, 1))).isEqualTo(period.getEnd().plusMillis(1));
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isEqualTo(period.getEnd().plusDays(2));
  }

  @Test
  public void excludeSplit() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 22), asDate(2011, 4, 25));

    CalendarDateAdd dateAdd = CalendarDateAdd.of();
    dateAdd.getExcludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(2))).isEqualTo(test.plusDays(2));
    assertThat(dateAdd.add(test, Durations.dayOf(3))).isEqualTo(period1.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(4))).isEqualTo(period1.getEnd().plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isEqualTo(period2.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(6))).isEqualTo(period2.getEnd().plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(7))).isEqualTo(period2.getEnd().plusDays(2));
  }

  @Test
  public void calendarDateAddSeekBoundaryMode() {
    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    dateAdd.addWorkingWeekdays();
    dateAdd.getExcludePeriods().add(new DayRange(asDate(2011, 4, 4), dateAdd.getCalendar()));
    dateAdd.getWorkingHours().add(new HourRangeInDay(8, 18));

    DateTime start = asDateTime(2011, 4, 1, 9, 0);

    assertThat(dateAdd.add(start, Durations.hourOf(29), SeekBoundaryMode.Fill)).isEqualTo(asDateTime(2011, 4, 6, 18));
    assertThat(dateAdd.add(start, Durations.hourOf(29), SeekBoundaryMode.Next)).isEqualTo(asDateTime(2011, 4, 7, 8));
    assertThat(dateAdd.add(start, Durations.hourOf(29))).isEqualTo(asDateTime(2011, 4, 7, 8));
  }

  @Test
  public void calendarDateAdd1() {
    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    dateAdd.addWorkingWeekdays();
    dateAdd.getExcludePeriods().add(new DayRange(asDate(2011, 4, 4), dateAdd.getCalendar()));
    dateAdd.getWorkingHours().add(new HourRangeInDay(8, 18));

    DateTime start = asDateTime(2011, 4, 1, 9, 0);

    assertThat(dateAdd.add(start, Durations.hourOf(22))).isEqualTo(asDateTime(2011, 4, 6, 11));
    assertThat(dateAdd.add(start, Durations.hourOf(22), SeekBoundaryMode.Fill)).isEqualTo(asDateTime(2011, 4, 6, 11));

    assertThat(dateAdd.add(start, Durations.hourOf(29))).isEqualTo(asDateTime(2011, 4, 7, 8));
    assertThat(dateAdd.add(start, Durations.hourOf(29), SeekBoundaryMode.Fill)).isEqualTo(asDateTime(2011, 4, 6, 18));
  }

  @Test
  public void calendarDateAdd2() {
    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    dateAdd.addWorkingWeekdays();
    dateAdd.getExcludePeriods().add(new DayRange(asDate(2011, 4, 4), dateAdd.getCalendar()));
    dateAdd.getWorkingHours().add(new HourRangeInDay(8, 12));
    dateAdd.getWorkingHours().add(new HourRangeInDay(13, 18));

    DateTime start = asDateTime(2011, 4, 1, 9, 0);

    assertThat(dateAdd.add(start, Durations.hourOf(3))).isEqualTo(asDateTime(2011, 4, 1, 13));
    assertThat(dateAdd.add(start, Durations.hourOf(4))).isEqualTo(asDateTime(2011, 4, 1, 14));
    assertThat(dateAdd.add(start, Durations.hourOf(8))).isEqualTo(asDateTime(2011, 4, 5, 8));
  }

  @Test
  public void calendarDateAdd3() {
    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    dateAdd.addWorkingWeekdays();
    dateAdd.getExcludePeriods().add(new DayRange(asDate(2011, 4, 4), dateAdd.getCalendar()));
    dateAdd.getWorkingHours().add(new HourRangeInDay(new Timepart(8, 30), new Timepart(12)));
    dateAdd.getWorkingHours().add(new HourRangeInDay(new Timepart(13, 30), new Timepart(18)));

    DateTime start = asDateTime(2011, 4, 1, 9, 0);

    assertThat(dateAdd.add(start, Durations.hourOf(3))).isEqualTo(asDateTime(2011, 4, 1, 13, 30));
    assertThat(dateAdd.add(start, Durations.hourOf(4))).isEqualTo(asDateTime(2011, 4, 1, 14, 30));
    assertThat(dateAdd.add(start, Durations.hourOf(8))).isEqualTo(asDateTime(2011, 4, 5, 9));
  }

  @Test
  public void emptyStartWeek() {
    CalendarDateAdd dateAdd = CalendarDateAdd.of();

    // 주중(월~금)을 working time 으로 추가
    dateAdd.addWorkingWeekdays();

    DateTime start = asDateTime(2011, 4, 2, 13, 0, 0);
    Duration offset = Durations.hourOf(20);

    // 4월 2일(토), 4월 3일(일) 제외하면 4월 4일 0시부터 20시간
    assertThat(dateAdd.add(start, Durations.hourOf(20))).isEqualTo(asDateTime(2011, 4, 4, 20));

    // 4월 2일(토), 4월 3일(일) 제외하면 4월 4일 0시부터 24시간
    assertThat(dateAdd.add(start, Durations.hourOf(24))).isEqualTo(asDateTime(2011, 4, 5));

    // 4월 2일(토), 4월 3일(일) 제외하면, 4월 4일부터 5일이면 주말인 4월 9일(토), 4월 10일(일) 제외한 4월 11일!!!
    assertThat(dateAdd.add(start, Durations.dayOf(5))).isEqualTo(asDateTime(2011, 4, 11));
  }

}
