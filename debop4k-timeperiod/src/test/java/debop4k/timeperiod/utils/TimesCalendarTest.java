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
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.joda.time.DateTime;
import org.junit.Test;

import static debop4k.timeperiod.models.DayOfWeek.*;
import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TimesCalendarTest extends AbstractTimePeriodTest {

  @Test
  public void getYearOfTest() {
    assertThat(yearOf(new DateTime(2000, 1, 1, 0, 0))).isEqualTo(2000);
    assertThat(yearOf(new DateTime(2000, 4, 1, 0, 0))).isEqualTo(2000);
    assertThat(yearOf(2000, 12)).isEqualTo(2000);

    assertThat(yearOf(testNow)).isEqualTo(testNow.getYear());
  }

  @Test
  public void nextHalfyear() {
    assertThat(Times.nextHalfyear(2000, Halfyear.First)).isEqualTo(new YearHalfyear(2000, Halfyear.Second));
    assertThat(Times.nextHalfyear(2000, Halfyear.Second)).isEqualTo(new YearHalfyear(2001, Halfyear.First));
  }

  @Test
  public void prevHalfyear() {
    assertThat(Times.prevHalfyear(2000, Halfyear.First)).isEqualTo(new YearHalfyear(1999, Halfyear.Second));
    assertThat(Times.prevHalfyear(2000, Halfyear.Second)).isEqualTo(new YearHalfyear(2000, Halfyear.First));
  }

  @Test
  public void addHalfyear() {

    assertThat(Times.addHalfyear(2000, Halfyear.First, 1).getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(Times.addHalfyear(2000, Halfyear.Second, 1).getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(Times.addHalfyear(2000, Halfyear.First, -1).getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(Times.addHalfyear(2000, Halfyear.Second, -1).getHalfyear()).isEqualTo(Halfyear.First);

    assertThat(Times.addHalfyear(2000, Halfyear.First, 2).getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(Times.addHalfyear(2000, Halfyear.Second, 2).getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(Times.addHalfyear(2000, Halfyear.First, -2).getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(Times.addHalfyear(2000, Halfyear.Second, -2).getHalfyear()).isEqualTo(Halfyear.Second);

    assertThat(Times.addHalfyear(2000, Halfyear.First, 5).getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(Times.addHalfyear(2000, Halfyear.Second, 5).getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(Times.addHalfyear(2000, Halfyear.First, -5).getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(Times.addHalfyear(2000, Halfyear.Second, -5).getHalfyear()).isEqualTo(Halfyear.First);

    assertThat(Times.addHalfyear(2008, Halfyear.First, 1).getYear()).isEqualTo(2008);
    assertThat(Times.addHalfyear(2008, Halfyear.Second, 1).getYear()).isEqualTo(2009);

    assertThat(Times.addHalfyear(2008, Halfyear.First, -1).getYear()).isEqualTo(2007);
    assertThat(Times.addHalfyear(2008, Halfyear.Second, -1).getYear()).isEqualTo(2008);

    assertThat(Times.addHalfyear(2008, Halfyear.First, 2).getYear()).isEqualTo(2009);
    assertThat(Times.addHalfyear(2008, Halfyear.Second, 2).getYear()).isEqualTo(2009);

    assertThat(Times.addHalfyear(2008, Halfyear.First, 3).getYear()).isEqualTo(2009);
    assertThat(Times.addHalfyear(2008, Halfyear.Second, 3).getYear()).isEqualTo(2010);
  }

  @Test
  public void getHalfyearOfMonthTest() {
    for (int month : TimeSpec.FirstHalfyearMonths)
      assertThat(halfyearOfMonth(month)).isEqualTo(Halfyear.First);

    for (int month : TimeSpec.SecondHalfyearMonths)
      assertThat(halfyearOfMonth(month)).isEqualTo(Halfyear.Second);
  }

  @Test
  public void getMonthsOfHalfyear() {
    assertThat(monthsOfHalfyear(Halfyear.First)).isEqualTo(TimeSpec.FirstHalfyearMonths);
    assertThat(monthsOfHalfyear(Halfyear.Second)).isEqualTo(TimeSpec.SecondHalfyearMonths);
  }

  @Test
  public void nextQuarterTest() {
    assertThat(nextQuarter(2000, Quarter.First).getQuarter()).isEqualTo(Quarter.Second);
    assertThat(nextQuarter(2000, Quarter.Second).getQuarter()).isEqualTo(Quarter.Third);
    assertThat(nextQuarter(2000, Quarter.Third).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(nextQuarter(2000, Quarter.Fourth).getQuarter()).isEqualTo(Quarter.First);
  }

  @Test
  public void previousQuarterTest() {
    assertThat(prevQuarter(2000, Quarter.First).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(prevQuarter(2000, Quarter.Second).getQuarter()).isEqualTo(Quarter.First);
    assertThat(prevQuarter(2000, Quarter.Third).getQuarter()).isEqualTo(Quarter.Second);
    assertThat(prevQuarter(2000, Quarter.Fourth).getQuarter()).isEqualTo(Quarter.Third);
  }

  @Test
  public void addQuarterTest() {

    assertThat(addQuarter(2000, Quarter.First, 1).getQuarter()).isEqualTo(Quarter.Second);
    assertThat(addQuarter(2000, Quarter.Second, 1).getQuarter()).isEqualTo(Quarter.Third);
    assertThat(addQuarter(2000, Quarter.Third, 1).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(addQuarter(2000, Quarter.Fourth, 1).getQuarter()).isEqualTo(Quarter.First);

    assertThat(addQuarter(2000, Quarter.First, -1).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(addQuarter(2000, Quarter.Second, -1).getQuarter()).isEqualTo(Quarter.First);
    assertThat(addQuarter(2000, Quarter.Third, -1).getQuarter()).isEqualTo(Quarter.Second);
    assertThat(addQuarter(2000, Quarter.Fourth, -1).getQuarter()).isEqualTo(Quarter.Third);

    assertThat(addQuarter(2000, Quarter.First, 2).getQuarter()).isEqualTo(Quarter.Third);
    assertThat(addQuarter(2000, Quarter.Second, 2).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(addQuarter(2000, Quarter.Third, 2).getQuarter()).isEqualTo(Quarter.First);
    assertThat(addQuarter(2000, Quarter.Fourth, 2).getQuarter()).isEqualTo(Quarter.Second);

    assertThat(addQuarter(2000, Quarter.First, -2).getQuarter()).isEqualTo(Quarter.Third);
    assertThat(addQuarter(2000, Quarter.Second, -2).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(addQuarter(2000, Quarter.Third, -2).getQuarter()).isEqualTo(Quarter.First);
    assertThat(addQuarter(2000, Quarter.Fourth, -2).getQuarter()).isEqualTo(Quarter.Second);
  }

  @Test
  public void getQuarterOfMonthTest() {
    for (int m : TimeSpec.FirstQuarterMonths)
      assertThat(quarterOfMonth(m)).isEqualTo(Quarter.First);

    for (int m : TimeSpec.SecondQuarterMonths)
      assertThat(quarterOfMonth(m)).isEqualTo(Quarter.Second);

    for (int m : TimeSpec.ThirdQuarterMonths)
      assertThat(quarterOfMonth(m)).isEqualTo(Quarter.Third);

    for (int m : TimeSpec.FourthQuarterMonths)
      assertThat(quarterOfMonth(m)).isEqualTo(Quarter.Fourth);
  }

  @Test
  public void getMonthsOfQuarterTest() {
    assertThat(monthsOfQuarter(Quarter.First)).isEqualTo(TimeSpec.FirstQuarterMonths);
    assertThat(monthsOfQuarter(Quarter.Second)).isEqualTo(TimeSpec.SecondQuarterMonths);
    assertThat(monthsOfQuarter(Quarter.Third)).isEqualTo(TimeSpec.ThirdQuarterMonths);
    assertThat(monthsOfQuarter(Quarter.Fourth)).isEqualTo(TimeSpec.FourthQuarterMonths);
  }

  @Test
  public void nextMonthTest() {
    for (int i = 1; i <= TimeSpec.MonthsPerYear; i++)
      assertThat(nextMonth(2000, i).getMonth()).isEqualTo(i % TimeSpec.MonthsPerYear + 1);
  }

  @Test
  public void prevMonthTest() {
    for (int i = 1; i <= TimeSpec.MonthsPerYear; i++)
      assertThat(prevMonth(2000, i).getMonth()).isEqualTo(
          (i - 1) <= 0 ? TimeSpec.MonthsPerYear + i - 1 : i - 1);
  }

  @Test
  public void addMonthTest() {

    for (int i = 1; i <= TimeSpec.MonthsPerYear; i++)
      assertThat(addMonth(2000, i, 1).getMonth()).isEqualTo(i % TimeSpec.MonthsPerYear + 1);

    for (int i = 1; i <= TimeSpec.MonthsPerYear; i++)
      assertThat(addMonth(2000, i, -1).getMonth()).isEqualTo(
          (i - 1) <= 0 ? TimeSpec.MonthsPerYear + i - 1 : i - 1);

    final int threeYears = 3 * TimeSpec.MonthsPerYear;

    for (int i = 1; i <= threeYears; i++) {
      YearMonth ym = addMonth(2013, 1, i);
      assertThat(ym.getYear()).isEqualTo(2013 + i / TimeSpec.MonthsPerYear);
      assertThat(ym.getMonth()).isEqualTo(i % TimeSpec.MonthsPerYear + 1);
    }
  }

  @Test
  public void weekOfYearTest() {

    ITimePeriod period = new TimeRange(asDate(2007, 12, 31), asDate(2009, 12, 31));

    Periods.dayStream(period)
           .forEach(new Procedure<ITimePeriod>() {
             @Override
             public void value(ITimePeriod p) {
               DateTime moment = p.getStart();
               YearWeek expected = new YearWeek(moment.getWeekyear(), moment.getWeekOfWeekyear());
               YearWeek actual = weekOfYear(moment);

               assertThat(actual).isEqualTo(expected);
             }
           });
  }

  @Test
  public void dayStartTest() {
    assertThat(dayStart(testDate)).isEqualTo(testDate.withTimeAtStartOfDay());
    assertThat(dayStart(testDate).getMillisOfDay()).isEqualTo(0);

    assertThat(dayStart(testNow)).isEqualTo(testNow.withTimeAtStartOfDay());
    assertThat(dayStart(testNow).getMillisOfDay()).isEqualTo(0);
  }

  @Test
  public void nextDayOfWeekTest() {
    assertThat(nextDayOfWeek(MONDAY)).isEqualTo(TUESDAY);
    assertThat(nextDayOfWeek(TUESDAY)).isEqualTo(WEDNESDAY);
    assertThat(nextDayOfWeek(WEDNESDAY)).isEqualTo(THURSDAY);
    assertThat(nextDayOfWeek(THURSDAY)).isEqualTo(FRIDAY);
    assertThat(nextDayOfWeek(FRIDAY)).isEqualTo(SATURDAY);
    assertThat(nextDayOfWeek(SATURDAY)).isEqualTo(SUNDAY);
    assertThat(nextDayOfWeek(SUNDAY)).isEqualTo(MONDAY);
  }

  @Test
  public void previousDayOfWeekTest() {
    assertThat(prevDayOfWeek(MONDAY)).isEqualTo(SUNDAY);
    assertThat(prevDayOfWeek(TUESDAY)).isEqualTo(MONDAY);
    assertThat(prevDayOfWeek(WEDNESDAY)).isEqualTo(TUESDAY);
    assertThat(prevDayOfWeek(THURSDAY)).isEqualTo(WEDNESDAY);
    assertThat(prevDayOfWeek(FRIDAY)).isEqualTo(THURSDAY);
    assertThat(prevDayOfWeek(SATURDAY)).isEqualTo(FRIDAY);
    assertThat(prevDayOfWeek(SUNDAY)).isEqualTo(SATURDAY);
  }

  @Test
  public void addDayOfWeektest() {
    assertThat(addDayOfWeek(MONDAY, 6)).isEqualTo(SUNDAY);
    assertThat(addDayOfWeek(MONDAY, 7)).isEqualTo(MONDAY);
    assertThat(addDayOfWeek(MONDAY, 8)).isEqualTo(TUESDAY);

    assertThat(addDayOfWeek(MONDAY, 14)).isEqualTo(MONDAY);
    assertThat(addDayOfWeek(TUESDAY, 14)).isEqualTo(TUESDAY);

    assertThat(addDayOfWeek(MONDAY, -14)).isEqualTo(MONDAY);
    assertThat(addDayOfWeek(TUESDAY, -14)).isEqualTo(TUESDAY);
  }
}
