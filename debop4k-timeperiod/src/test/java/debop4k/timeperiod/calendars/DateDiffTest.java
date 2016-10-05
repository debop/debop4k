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
import debop4k.timeperiod.utils.Durations;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimex.asDateTime;
import static debop4k.timeperiod.TimeSpec.*;
import static debop4k.timeperiod.utils.Times.now;
import static org.assertj.core.api.Assertions.assertThat;


public class DateDiffTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(DateDiffTest.class);

  @Test
  public void emptyDataDiff() {
    DateTime now = now();
    DateDiff dateDiff = new DateDiff(now, now);

    assertThat(dateDiff.isEmpty()).isTrue();
    assertThat(dateDiff.getDifference()).isEqualTo(Durations.Zero);

    assertThat(dateDiff.getYears()).isEqualTo(0);
    assertThat(dateDiff.getQuarters()).isEqualTo(0);
    assertThat(dateDiff.getMonths()).isEqualTo(0);
    assertThat(dateDiff.getWeeks()).isEqualTo(0);
    assertThat(dateDiff.getDays()).isEqualTo(0);
    assertThat(dateDiff.getHours()).isEqualTo(0);
    assertThat(dateDiff.getMinutes()).isEqualTo(0);
    assertThat(dateDiff.getSeconds()).isEqualTo(0);

    assertThat(dateDiff.getElapsedYears()).isEqualTo(0);
    assertThat(dateDiff.getElapsedMonths()).isEqualTo(0);
    assertThat(dateDiff.getElapsedDays()).isEqualTo(0);
    assertThat(dateDiff.getElapsedHours()).isEqualTo(0);
    assertThat(dateDiff.getElapsedMinutes()).isEqualTo(0);
    assertThat(dateDiff.getElapsedSeconds()).isEqualTo(0);
  }

  @Test
  public void differenceTest() {
    DateTime date1 = new DateTime(2008, 10, 12, 15, 32, 44, 243);
    DateTime date2 = new DateTime(2010, 1, 3, 23, 22, 9, 345);

    DateDiff dateDiff = DateDiff.of(date1, date2);

    assertThat(dateDiff.getDifference()).isEqualTo(new Duration(date1, date2));
  }

  @Test
  public void yearsTest() {
    int[] years = new int[]{1, 3, 15};

    for (int year : years) {
      DateTime date1 = now();
      DateTime date2 = date1.plusYears(year);
      DateTime date3 = date1.plusYears(-year);

      log.debug("date1=[{}], date2=[{}], date3=[{}]", date1, date2, date3);

      DateDiff dateDiff12 = DateDiff.of(date1, date2);

      assertThat(dateDiff12.getElapsedYears()).isEqualTo(year);
      assertThat(dateDiff12.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff12.getYears()).isEqualTo(year);
      assertThat(dateDiff12.getQuarters()).isEqualTo(year * QuartersPerYear);
      assertThat(dateDiff12.getMonths()).isEqualTo(year * MonthsPerYear);

      int date12Days = (int) Durations.of(date1, date2).getStandardDays();

      assertThat(dateDiff12.getDays()).isEqualTo(date12Days);
      assertThat(dateDiff12.getHours()).isEqualTo(date12Days * HoursPerDay);
      assertThat(dateDiff12.getMinutes()).isEqualTo(date12Days * HoursPerDay * MinutesPerHour);
      assertThat(dateDiff12.getSeconds()).isEqualTo(date12Days * HoursPerDay * MinutesPerHour * SecondsPerMinute);

      DateDiff dateDiff13 = DateDiff.of(date1, date3);

      assertThat(dateDiff13.getElapsedYears()).isEqualTo(-year);
      assertThat(dateDiff13.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff13.getYears()).isEqualTo(-year);
      assertThat(dateDiff13.getQuarters()).isEqualTo(-year * QuartersPerYear);
      assertThat(dateDiff13.getMonths()).isEqualTo(-year * MonthsPerYear);

      int date13Days = (int) Durations.of(date1, date3).getStandardDays();

      assertThat(dateDiff13.getDays()).isEqualTo(date13Days);
      assertThat(dateDiff13.getHours()).isEqualTo(date13Days * HoursPerDay);
      assertThat(dateDiff13.getMinutes()).isEqualTo(date13Days * HoursPerDay * MinutesPerHour);
      assertThat(dateDiff13.getSeconds()).isEqualTo(date13Days * HoursPerDay * MinutesPerHour * SecondsPerMinute);
    }
  }

  @Test
  public void quartersTest() {
    DateTime date1 = new DateTime(2011, 5, 15, 15, 32, 44, 245);
    DateTime date2 = date1.plusMonths(MonthsPerQuarter);
    DateTime date3 = date1.plusMonths(-MonthsPerQuarter);

    DateDiff dateDiff12 = DateDiff.of(date1, date2);
    int days12 = (int) Durations.of(date1, date2).getStandardDays();

    assertThat(dateDiff12.getElapsedYears()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedMonths()).isEqualTo(MonthsPerQuarter);
    assertThat(dateDiff12.getElapsedDays()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedHours()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(0);

    assertThat(dateDiff12.getYears()).isEqualTo(0);
    assertThat(dateDiff12.getQuarters()).isEqualTo(1);
    assertThat(dateDiff12.getMonths()).isEqualTo(MonthsPerQuarter);
    assertThat(dateDiff12.getWeeks()).isEqualTo(14);
    assertThat(dateDiff12.getDays()).isEqualTo(days12);
    assertThat(dateDiff12.getHours()).isEqualTo(days12 * HoursPerDay);
    assertThat(dateDiff12.getMinutes()).isEqualTo(days12 * HoursPerDay * MinutesPerHour);
    assertThat(dateDiff12.getSeconds()).isEqualTo(days12 * HoursPerDay * MinutesPerHour * SecondsPerMinute);

    DateDiff dateDiff13 = DateDiff.of(date1, date3);
    int days13 = (int) Durations.of(date1, date3).getStandardDays();

    assertThat(dateDiff13.getElapsedYears()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedMonths()).isEqualTo(-MonthsPerQuarter);
    assertThat(dateDiff13.getElapsedDays()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedHours()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(0);

    assertThat(dateDiff13.getYears()).isEqualTo(0);
    assertThat(dateDiff13.getQuarters()).isEqualTo(-1);
    assertThat(dateDiff13.getMonths()).isEqualTo(-MonthsPerQuarter);
    assertThat(dateDiff13.getWeeks()).isEqualTo(-12);
    assertThat(dateDiff13.getDays()).isEqualTo(days13);
    assertThat(dateDiff13.getHours()).isEqualTo(days13 * HoursPerDay);
    assertThat(dateDiff13.getMinutes()).isEqualTo(days13 * HoursPerDay * MinutesPerHour);
    assertThat(dateDiff13.getSeconds()).isEqualTo(days13 * HoursPerDay * MinutesPerHour * SecondsPerMinute);
  }

  @Test
  public void monthsTest() {
    DateTime date1 = new DateTime(2011, 5, 15, 15, 32, 44, 245);
    DateTime date2 = date1.plusMonths(1);
    DateTime date3 = date1.plusMonths(-1);

    DateDiff dateDiff12 = DateDiff.of(date1, date2);
    int days12 = (int) Durations.of(date1, date2).getStandardDays();

    assertThat(dateDiff12.getElapsedYears()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedMonths()).isEqualTo(1);
    assertThat(dateDiff12.getElapsedDays()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedHours()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(0);
    assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(0);

    assertThat(dateDiff12.getYears()).isEqualTo(0);
    assertThat(dateDiff12.getQuarters()).isEqualTo(0);
    assertThat(dateDiff12.getMonths()).isEqualTo(1);
    assertThat(dateDiff12.getWeeks()).isEqualTo(5);
    assertThat(dateDiff12.getDays()).isEqualTo(days12);
    assertThat(dateDiff12.getHours()).isEqualTo(days12 * HoursPerDay);
    assertThat(dateDiff12.getMinutes()).isEqualTo(days12 * HoursPerDay * MinutesPerHour);
    assertThat(dateDiff12.getSeconds()).isEqualTo(days12 * HoursPerDay * MinutesPerHour * SecondsPerMinute);

    DateDiff dateDiff13 = DateDiff.of(date1, date3);
    int days13 = (int) Durations.of(date1, date3).getStandardDays();

    assertThat(dateDiff13.getElapsedYears()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedMonths()).isEqualTo(-1);
    assertThat(dateDiff13.getElapsedDays()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedHours()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(0);
    assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(0);

    assertThat(dateDiff13.getYears()).isEqualTo(0);
    assertThat(dateDiff13.getQuarters()).isEqualTo(0);
    assertThat(dateDiff13.getMonths()).isEqualTo(-1);
    assertThat(dateDiff13.getWeeks()).isEqualTo(-4);
    assertThat(dateDiff13.getDays()).isEqualTo(days13);
    assertThat(dateDiff13.getHours()).isEqualTo(days13 * HoursPerDay);
    assertThat(dateDiff13.getMinutes()).isEqualTo(days13 * HoursPerDay * MinutesPerHour);
    assertThat(dateDiff13.getSeconds()).isEqualTo(days13 * HoursPerDay * MinutesPerHour * SecondsPerMinute);
  }

  @Test
  public void weekTest() {
    DateTime date1 = new DateTime(2011, 5, 15, 15, 32, 44, 245);
    DateTime date2 = date1.plusWeeks(1);
    DateTime date3 = date1.plusWeeks(-1);

    DateDiff dateDiff12 = DateDiff.of(date1, date2);
    int days12 = (int) Durations.of(date1, date2).getStandardDays();

    assertThat(dateDiff12.getYears()).isEqualTo(0);
    assertThat(dateDiff12.getQuarters()).isEqualTo(0);
    assertThat(dateDiff12.getMonths()).isEqualTo(0);
    assertThat(dateDiff12.getWeeks()).isEqualTo(1);
    assertThat(dateDiff12.getDays()).isEqualTo(days12);
    assertThat(dateDiff12.getHours()).isEqualTo(days12 * HoursPerDay);
    assertThat(dateDiff12.getMinutes()).isEqualTo(days12 * HoursPerDay * MinutesPerHour);
    assertThat(dateDiff12.getSeconds()).isEqualTo(days12 * HoursPerDay * MinutesPerHour * SecondsPerMinute);

    DateDiff dateDiff13 = DateDiff.of(date1, date3);
    int days13 = (int) Durations.of(date1, date3).getStandardDays();

    assertThat(dateDiff13.getYears()).isEqualTo(0);
    assertThat(dateDiff13.getQuarters()).isEqualTo(0);
    assertThat(dateDiff13.getMonths()).isEqualTo(0);
    assertThat(dateDiff13.getWeeks()).isEqualTo(-1);
    assertThat(dateDiff13.getDays()).isEqualTo(days13);
    assertThat(dateDiff13.getHours()).isEqualTo(days13 * HoursPerDay);
    assertThat(dateDiff13.getMinutes()).isEqualTo(days13 * HoursPerDay * MinutesPerHour);
    assertThat(dateDiff13.getSeconds()).isEqualTo(days13 * HoursPerDay * MinutesPerHour * SecondsPerMinute);
  }

  @Test
  public void daysTest() {
    int[] days = new int[]{1, 3};

    for (int day : days) {

      DateTime date1 = asDateTime(2011, 5, 19, 15, 32, 44, 245);
      DateTime date2 = date1.plusDays(day);
      DateTime date3 = date1.plusDays(-day);

      DateDiff dateDiff12 = DateDiff.of(date1, date2);
      int days12 = (int) Durations.of(date1, date2).getStandardDays();

      assertThat(dateDiff12.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedDays()).isEqualTo(day);
      assertThat(dateDiff12.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff12.getYears()).isEqualTo(0);
      assertThat(dateDiff12.getQuarters()).isEqualTo(0);
      assertThat(dateDiff12.getMonths()).isEqualTo(0);
      assertThat(dateDiff12.getWeeks()).isEqualTo(0);
      assertThat(dateDiff12.getDays()).isEqualTo(days12);
      assertThat(dateDiff12.getHours()).isEqualTo(days12 * HoursPerDay);
      assertThat(dateDiff12.getMinutes()).isEqualTo(days12 * HoursPerDay * MinutesPerHour);
      assertThat(dateDiff12.getSeconds()).isEqualTo(days12 * HoursPerDay * MinutesPerHour * SecondsPerMinute);

      DateDiff dateDiff13 = DateDiff.of(date1, date3);
      int days13 = (int) Durations.of(date1, date3).getStandardDays();

      assertThat(dateDiff13.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedDays()).isEqualTo(-day);
      assertThat(dateDiff13.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff13.getYears()).isEqualTo(0);
      assertThat(dateDiff13.getQuarters()).isEqualTo(0);
      assertThat(dateDiff13.getMonths()).isEqualTo(0);
      assertThat(dateDiff13.getWeeks()).isEqualTo(0);
      assertThat(dateDiff13.getDays()).isEqualTo(days13);
      assertThat(dateDiff13.getHours()).isEqualTo(days13 * HoursPerDay);
      assertThat(dateDiff13.getMinutes()).isEqualTo(days13 * HoursPerDay * MinutesPerHour);
      assertThat(dateDiff13.getSeconds()).isEqualTo(days13 * HoursPerDay * MinutesPerHour * SecondsPerMinute);
    }
  }

  @Test
  public void hoursTest() {

    int[] hours = new int[]{1, 3, 5};

    for (int hour : hours) {

      DateTime date1 = asDateTime(2011, 5, 19, 15, 32, 44, 245);
      DateTime date2 = date1.plusHours(hour);
      DateTime date3 = date1.plusHours(-hour);

      DateDiff dateDiff12 = DateDiff.of(date1, date2);
      int hours12 = (int) Durations.of(date1, date2).getStandardHours();

      assertThat(dateDiff12.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedHours()).isEqualTo(hour);
      assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff12.getYears()).isEqualTo(0);
      assertThat(dateDiff12.getQuarters()).isEqualTo(0);
      assertThat(dateDiff12.getMonths()).isEqualTo(0);
      assertThat(dateDiff12.getWeeks()).isEqualTo(0);
      assertThat(dateDiff12.getDays()).isEqualTo(0);
      assertThat(dateDiff12.getHours()).isEqualTo(hours12);
      assertThat(dateDiff12.getMinutes()).isEqualTo(hours12 * MinutesPerHour);
      assertThat(dateDiff12.getSeconds()).isEqualTo(hours12 * MinutesPerHour * SecondsPerMinute);

      DateDiff dateDiff13 = DateDiff.of(date1, date3);
      int hours13 = (int) Durations.of(date1, date3).getStandardHours();

      assertThat(dateDiff13.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedHours()).isEqualTo(-hour);
      assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff13.getYears()).isEqualTo(0);
      assertThat(dateDiff13.getQuarters()).isEqualTo(0);
      assertThat(dateDiff13.getMonths()).isEqualTo(0);
      assertThat(dateDiff13.getWeeks()).isEqualTo(0);
      assertThat(dateDiff13.getDays()).isEqualTo(0);
      assertThat(dateDiff13.getHours()).isEqualTo(hours13);
      assertThat(dateDiff13.getMinutes()).isEqualTo(hours13 * MinutesPerHour);
      assertThat(dateDiff13.getSeconds()).isEqualTo(hours13 * MinutesPerHour * SecondsPerMinute);
    }
  }

  @Test
  public void minutesTest() {

    int[] minutes = new int[]{1, 3, 5};

    for (int minute : minutes) {

      DateTime date1 = asDateTime(2011, 5, 19, 15, 32, 44, 245);
      DateTime date2 = date1.plusMinutes(minute);
      DateTime date3 = date1.plusMinutes(-minute);

      DateDiff dateDiff12 = DateDiff.of(date1, date2);
      int minute12 = (int) Durations.of(date1, date2).getStandardMinutes();

      assertThat(dateDiff12.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(minute);
      assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff12.getYears()).isEqualTo(0);
      assertThat(dateDiff12.getQuarters()).isEqualTo(0);
      assertThat(dateDiff12.getMonths()).isEqualTo(0);
      assertThat(dateDiff12.getWeeks()).isEqualTo(0);
      assertThat(dateDiff12.getDays()).isEqualTo(0);
      assertThat(dateDiff12.getHours()).isEqualTo(0);
      assertThat(dateDiff12.getMinutes()).isEqualTo(minute12);
      assertThat(dateDiff12.getSeconds()).isEqualTo(minute12 * SecondsPerMinute);

      DateDiff dateDiff13 = DateDiff.of(date1, date3);
      int minute13 = (int) Durations.of(date1, date3).getStandardMinutes();

      assertThat(dateDiff13.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(-minute);
      assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(0);

      assertThat(dateDiff13.getYears()).isEqualTo(0);
      assertThat(dateDiff13.getQuarters()).isEqualTo(0);
      assertThat(dateDiff13.getMonths()).isEqualTo(0);
      assertThat(dateDiff13.getWeeks()).isEqualTo(0);
      assertThat(dateDiff13.getDays()).isEqualTo(0);
      assertThat(dateDiff13.getHours()).isEqualTo(0);
      assertThat(dateDiff13.getMinutes()).isEqualTo(minute13);
      assertThat(dateDiff13.getSeconds()).isEqualTo(minute13 * SecondsPerMinute);
    }
  }


  @Test
  public void secondsTest() {

    int[] seconds = new int[]{1, 3, 5};

    for (int second : seconds) {

      DateTime date1 = asDateTime(2011, 5, 19, 15, 32, 44, 245);
      DateTime date2 = date1.plusSeconds(second);
      DateTime date3 = date1.plusSeconds(-second);

      DateDiff dateDiff12 = DateDiff.of(date1, date2);
      int second12 = (int) Durations.of(date1, date2).getStandardSeconds();

      assertThat(dateDiff12.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(second);

      assertThat(dateDiff12.getYears()).isEqualTo(0);
      assertThat(dateDiff12.getQuarters()).isEqualTo(0);
      assertThat(dateDiff12.getMonths()).isEqualTo(0);
      assertThat(dateDiff12.getWeeks()).isEqualTo(0);
      assertThat(dateDiff12.getDays()).isEqualTo(0);
      assertThat(dateDiff12.getHours()).isEqualTo(0);
      assertThat(dateDiff12.getMinutes()).isEqualTo(0);
      assertThat(dateDiff12.getSeconds()).isEqualTo(second12);

      DateDiff dateDiff13 = DateDiff.of(date1, date3);
      int second13 = (int) Durations.of(date1, date3).getStandardSeconds();

      assertThat(dateDiff13.getElapsedYears()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMonths()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedDays()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedHours()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(0);
      assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(-second);

      assertThat(dateDiff13.getYears()).isEqualTo(0);
      assertThat(dateDiff13.getQuarters()).isEqualTo(0);
      assertThat(dateDiff13.getMonths()).isEqualTo(0);
      assertThat(dateDiff13.getWeeks()).isEqualTo(0);
      assertThat(dateDiff13.getDays()).isEqualTo(0);
      assertThat(dateDiff13.getHours()).isEqualTo(0);
      assertThat(dateDiff13.getMinutes()).isEqualTo(0);
      assertThat(dateDiff13.getSeconds()).isEqualTo(second13);
    }
  }

  @Test
  public void positiveDurationTest() {
    int[] diffs = new int[]{1, 3, 5};

    for (int diff : diffs) {
      DateTime date1 = now();
      DateTime date2 = date1.plusYears(diff).plusMonths(diff).plusDays(diff).plusHours(diff).plusMinutes(diff).plusSeconds(diff);
      DateTime date3 = date1.minusYears(diff).minusMonths(diff).minusDays(diff).minusHours(diff).minusMinutes(diff).minusSeconds(diff);

      DateDiff dateDiff12 = DateDiff.of(date1, date2);

      assertThat(dateDiff12.getElapsedYears()).isEqualTo(diff);
      assertThat(dateDiff12.getElapsedMonths()).isEqualTo(diff);
      assertThat(dateDiff12.getElapsedDays()).isEqualTo(diff);
      assertThat(dateDiff12.getElapsedHours()).isEqualTo(diff);
      assertThat(dateDiff12.getElapsedMinutes()).isEqualTo(diff);
      assertThat(dateDiff12.getElapsedSeconds()).isEqualTo(diff);

      DateDiff dateDiff13 = DateDiff.of(date1, date3);

      assertThat(dateDiff13.getElapsedYears()).isEqualTo(-diff);
      assertThat(dateDiff13.getElapsedMonths()).isEqualTo(-diff);
      assertThat(dateDiff13.getElapsedDays()).isEqualTo(-diff);
      assertThat(dateDiff13.getElapsedHours()).isEqualTo(-diff);
      assertThat(dateDiff13.getElapsedMinutes()).isEqualTo(-diff);
      assertThat(dateDiff13.getElapsedSeconds()).isEqualTo(-diff);

    }
  }
}
