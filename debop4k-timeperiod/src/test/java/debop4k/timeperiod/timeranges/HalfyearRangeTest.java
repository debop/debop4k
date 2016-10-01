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
import debop4k.timeperiod.models.Halfyear;
import debop4k.timeperiod.models.Quarter;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static debop4k.core.kodatimes.KodaTimes.asDate;
import static debop4k.timeperiod.TimeSpec.*;
import static org.assertj.core.api.Assertions.assertThat;


public class HalfyearRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(HalfyearRangeTest.class);

  @Test
  public void initValues() {
    DateTime now = Times.now();
    DateTime firstHalfyear = Times.startTimeOfHalfyear(now.getYear(), Halfyear.First);
    DateTime secondHalfyear = Times.startTimeOfHalfyear(now.getYear(), Halfyear.Second);

    HalfyearRange hyr = new HalfyearRange(now.getYear(), Halfyear.First, TimeCalendar.EMPTY_OFFSET);

    assertThat(hyr.getStart().getYear()).isEqualTo(firstHalfyear.getYear());
    assertThat(hyr.getStart().getMonthOfYear()).isEqualTo(firstHalfyear.getMonthOfYear());
    assertThat(hyr.getStart().getDayOfMonth()).isEqualTo(firstHalfyear.getDayOfMonth());
    assertThat(hyr.getStart().getHourOfDay()).isEqualTo(0);
    assertThat(hyr.getStart().getMinuteOfHour()).isEqualTo(0);
    assertThat(hyr.getStart().getMinuteOfHour()).isEqualTo(0);
    assertThat(hyr.getStart().getSecondOfMinute()).isEqualTo(0);
    assertThat(hyr.getStart().getMillisOfSecond()).isEqualTo(0);

    assertThat(hyr.getEnd().getYear()).isEqualTo(secondHalfyear.getYear());
    assertThat(hyr.getEnd().getMonthOfYear()).isEqualTo(secondHalfyear.getMonthOfYear());
    assertThat(hyr.getEnd().getDayOfMonth()).isEqualTo(secondHalfyear.getDayOfMonth());
    assertThat(hyr.getEnd().getHourOfDay()).isEqualTo(0);
    assertThat(hyr.getEnd().getMinuteOfHour()).isEqualTo(0);
    assertThat(hyr.getEnd().getMinuteOfHour()).isEqualTo(0);
    assertThat(hyr.getEnd().getSecondOfMinute()).isEqualTo(0);
    assertThat(hyr.getEnd().getMillisOfSecond()).isEqualTo(0);
  }

  @Test
  public void defaultCalendarTest() {
    DateTime yearStart = Times.startTimeOfYear(Times.currentYear());

    for (Halfyear halfyear : Halfyear.values()) {
      int offset = halfyear.getValue() - 1;
      HalfyearRange hyr = new HalfyearRange(yearStart.plusMonths(MonthsPerHalfyear * offset));

      assertThat(hyr.getStart()).isEqualTo(hyr.getCalendar().mapStart(yearStart.plusMonths(MonthsPerHalfyear * offset)));
      assertThat(hyr.getEnd()).isEqualTo(hyr.getCalendar().mapEnd(yearStart.plusMonths(MonthsPerHalfyear * (offset + 1))));
    }
  }

  @Test
  public void momentTest() {
    DateTime now = Times.now();
    int currentYear = now.getYear();
    assertThat(new HalfyearRange().getHalfyear()).isEqualTo(
        now.getMonthOfYear() < 7 ? Halfyear.First : Halfyear.Second);

    assertThat(new HalfyearRange(asDate(currentYear, 1, 1)).getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(new HalfyearRange(asDate(currentYear, 6, 30)).getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(new HalfyearRange(asDate(currentYear, 7, 1)).getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(new HalfyearRange(asDate(currentYear, 12, 31)).getHalfyear()).isEqualTo(Halfyear.Second);
  }

  @Test
  public void startMonth() {
    final DateTime now = Times.now();
    final int currentYear = now.getYear();

    for (int m = 1; m <= MonthsPerYear; m++) {
      assertThat(new HalfyearRange(currentYear, Halfyear.First).getStartMonthOfYear()).isEqualTo(1);
      assertThat(new HalfyearRange(currentYear, Halfyear.Second).getStartMonthOfYear()).isEqualTo(7);
    }
  }

  @Test
  public void isMultipleCalendarYearsTest() {
    final DateTime now = Times.now();
    final int currentYear = now.getYear();

    assertThat(new HalfyearRange(currentYear, Halfyear.First).isMultipleCalendarYears()).isFalse();
  }

  @Test
  public void calendarHalfyear() {
    final DateTime now = Times.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = TimeCalendar.EMPTY_OFFSET;

    HalfyearRange h1 = new HalfyearRange(currentYear, Halfyear.First, calendar);

    assertThat(h1.isReadonly()).isTrue();
    assertThat(h1.getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(h1.getStart()).isEqualTo(asDate(currentYear, 1, 1));
    assertThat(h1.getEnd()).isEqualTo(asDate(currentYear, 7, 1));

    HalfyearRange h2 = new HalfyearRange(currentYear, Halfyear.Second, calendar);

    assertThat(h2.isReadonly()).isTrue();
    assertThat(h2.getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(h2.getStart()).isEqualTo(asDate(currentYear, 7, 1));
    assertThat(h2.getEnd()).isEqualTo(asDate(currentYear + 1, 1, 1));
  }

  @Test
  public void getQuartersTest() {
    final DateTime now = Times.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = TimeCalendar.EMPTY_OFFSET;

    HalfyearRange h1 = new HalfyearRange(currentYear, Halfyear.First, calendar);
    List<QuarterRange> h1Quarters = h1.quarters();

    int h1Index = 0;
    for (QuarterRange h1Quarter : h1Quarters) {
      log.trace("h1Quarter[{}] = [{}]", h1Index, h1Quarter);
      assertThat(h1Quarter.getQuarter()).isEqualTo(h1Index == 0 ? Quarter.FIRST : Quarter.SECOND);
      assertThat(h1Quarter.getStart()).isEqualTo(h1.getStart().plusMonths(h1Index * MonthsPerQuarter));
      assertThat(h1Quarter.getEnd()).isEqualTo(h1Quarter.getCalendar().mapEnd(h1Quarter.getStart().plusMonths(MonthsPerQuarter)));
      h1Index++;
    }

    HalfyearRange h2 = new HalfyearRange(currentYear, Halfyear.Second, calendar);
    List<QuarterRange> h2Quarters = h2.quarters();

    int h2Index = 0;
    for (QuarterRange h2Quarter : h2Quarters) {
      log.trace("h2Quarter[{}] = [{}]", h2Index, h2Quarter);
      assertThat(h2Quarter.getQuarter()).isEqualTo(h2Index == 0 ? Quarter.THIRD : Quarter.FOURTH);
      assertThat(h2Quarter.getStart()).isEqualTo(h2.getStart().plusMonths(h2Index * MonthsPerQuarter));
      assertThat(h2Quarter.getEnd()).isEqualTo(h2Quarter.getCalendar().mapEnd(h2Quarter.getStart().plusMonths(MonthsPerQuarter)));
      h2Index++;
    }
  }

  @Test
  public void getMonthsTest() {
    final DateTime now = Times.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = TimeCalendar.EMPTY_OFFSET;

    HalfyearRange h1 = new HalfyearRange(currentYear, Halfyear.First, calendar);
    List<MonthRange> months = h1.months();
    assertThat(months.size()).isEqualTo(MonthsPerHalfyear);

    int index = 0;
    for (MonthRange month : months) {
      assertThat(month.getStart()).isEqualTo(h1.getStart().plusMonths(index));
      assertThat(month.getEnd()).isEqualTo(calendar.mapEnd(month.getStart().plusMonths(1)));
      index++;
    }
  }

  @Test
  public void addHalfyearsTest() {
    final DateTime now = Times.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = TimeCalendar.EMPTY_OFFSET;

    HalfyearRange h1 = new HalfyearRange(currentYear, Halfyear.First, calendar);

    HalfyearRange prevH1 = h1.addHalfyears(-1);
    log.trace("h1={}, prevH1={}", h1, prevH1);
    assertThat(prevH1.getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(prevH1.getStart()).isEqualTo(h1.getStart().plusMonths(-MonthsPerHalfyear));
    assertThat(prevH1.getEnd()).isEqualTo(h1.getStart());

    prevH1 = h1.addHalfyears(-2);
    assertThat(prevH1.getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(prevH1.getStart()).isEqualTo(h1.getStart().plusMonths(-2 * MonthsPerHalfyear));
    assertThat(prevH1.getEnd()).isEqualTo(h1.getStart().plusMonths(-1 * MonthsPerHalfyear));

    prevH1 = h1.addHalfyears(-3);
    assertThat(prevH1.getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(prevH1.getStart()).isEqualTo(h1.getStart().plusMonths(-3 * MonthsPerHalfyear));
    assertThat(prevH1.getEnd()).isEqualTo(h1.getStart().plusMonths(-2 * MonthsPerHalfyear));

    HalfyearRange nextH1 = h1.addHalfyears(1);
    assertThat(nextH1.getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(nextH1.getStart()).isEqualTo(h1.getStart().plusMonths(MonthsPerHalfyear));
    assertThat(nextH1.getEnd()).isEqualTo(h1.getStart().plusMonths(2 * MonthsPerHalfyear));

    nextH1 = h1.addHalfyears(2);
    assertThat(nextH1.getHalfyear()).isEqualTo(Halfyear.First);
    assertThat(nextH1.getStart()).isEqualTo(h1.getStart().plusMonths(2 * MonthsPerHalfyear));
    assertThat(nextH1.getEnd()).isEqualTo(h1.getStart().plusMonths(3 * MonthsPerHalfyear));

    nextH1 = h1.addHalfyears(3);
    assertThat(nextH1.getHalfyear()).isEqualTo(Halfyear.Second);
    assertThat(nextH1.getStart()).isEqualTo(h1.getStart().plusMonths(3 * MonthsPerHalfyear));
    assertThat(nextH1.getEnd()).isEqualTo(h1.getStart().plusMonths(4 * MonthsPerHalfyear));

  }
}
