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
import debop4k.timeperiod.models.Quarter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static debop4k.timeperiod.TimeCalendar.emptyOffset;
import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class QuarterRangeTest extends AbstractTimePeriodTest {

  @Test
  public void initValues() {
    DateTime now = now();
    DateTime firstQuarter = startTimeOfQuarter(now.getYear(), Quarter.First);
    DateTime secondQuarter = startTimeOfQuarter(now.getYear(), Quarter.Second);

    QuarterRange quarterRange = new QuarterRange(now.getYear(), Quarter.First, emptyOffset());

    assertThat(quarterRange.getStart().getYear()).isEqualTo(firstQuarter.getYear());
    assertThat(quarterRange.getStart().getMonthOfYear()).isEqualTo(firstQuarter.getMonthOfYear());
    assertThat(quarterRange.getStart().getDayOfMonth()).isEqualTo(firstQuarter.getDayOfMonth());
    assertThat(quarterRange.getStart().getHourOfDay()).isEqualTo(0);
    assertThat(quarterRange.getStart().getMinuteOfHour()).isEqualTo(0);
    assertThat(quarterRange.getStart().getMinuteOfHour()).isEqualTo(0);
    assertThat(quarterRange.getStart().getSecondOfMinute()).isEqualTo(0);
    assertThat(quarterRange.getStart().getMillisOfSecond()).isEqualTo(0);

    assertThat(quarterRange.getEnd().getYear()).isEqualTo(secondQuarter.getYear());
    assertThat(quarterRange.getEnd().getMonthOfYear()).isEqualTo(secondQuarter.getMonthOfYear());
    assertThat(quarterRange.getEnd().getDayOfMonth()).isEqualTo(secondQuarter.getDayOfMonth());
    assertThat(quarterRange.getEnd().getHourOfDay()).isEqualTo(0);
    assertThat(quarterRange.getEnd().getMinuteOfHour()).isEqualTo(0);
    assertThat(quarterRange.getEnd().getMinuteOfHour()).isEqualTo(0);
    assertThat(quarterRange.getEnd().getSecondOfMinute()).isEqualTo(0);
    assertThat(quarterRange.getEnd().getMillisOfSecond()).isEqualTo(0);
  }

  @Test
  public void defaultCalendarTest() {
    DateTime yearStart = startTimeOfYear(currentYear());

    for (Quarter quarter : Quarter.values()) {
      int offset = quarter.getValue() - 1;
      QuarterRange quarterRange = new QuarterRange(yearStart.plusMonths(TimeSpec.MonthsPerQuarter * offset));

      assertThat(quarterRange.getStart())
          .isEqualTo(quarterRange.getCalendar().mapStart(yearStart.plusMonths(TimeSpec.MonthsPerQuarter * offset)));
      assertThat(quarterRange.getEnd())
          .isEqualTo(quarterRange.getCalendar().mapEnd(yearStart.plusMonths(TimeSpec.MonthsPerQuarter * (offset + 1))));
    }
  }

  @Test
  public void momentTest() {
    DateTime now = now();
    int currentYear = now.getYear();

    assertThat(new QuarterRange(asDate(currentYear, 1, 1)).getQuarter()).isEqualTo(Quarter.First);
    assertThat(new QuarterRange(asDate(currentYear, 3, 31)).getQuarter()).isEqualTo(Quarter.First);

    assertThat(new QuarterRange(asDate(currentYear, 4, 1)).getQuarter()).isEqualTo(Quarter.Second);
    assertThat(new QuarterRange(asDate(currentYear, 6, 30)).getQuarter()).isEqualTo(Quarter.Second);

    assertThat(new QuarterRange(asDate(currentYear, 7, 1)).getQuarter()).isEqualTo(Quarter.Third);
    assertThat(new QuarterRange(asDate(currentYear, 9, 30)).getQuarter()).isEqualTo(Quarter.Third);

    assertThat(new QuarterRange(asDate(currentYear, 10, 1)).getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(new QuarterRange(asDate(currentYear, 12, 31)).getQuarter()).isEqualTo(Quarter.Fourth);
  }

  @Test
  public void startMonth() {
    final DateTime now = now();
    final int currentYear = now.getYear();

    assertThat(new QuarterRange(currentYear, Quarter.First).getStartMonthOfYear()).isEqualTo(1);
    assertThat(new QuarterRange(currentYear, Quarter.Second).getStartMonthOfYear()).isEqualTo(4);
    assertThat(new QuarterRange(currentYear, Quarter.Third).getStartMonthOfYear()).isEqualTo(7);
    assertThat(new QuarterRange(currentYear, Quarter.Fourth).getStartMonthOfYear()).isEqualTo(10);
  }

  @Test
  public void isMultipleCalendarYearsTest() {
    final DateTime now = now();
    final int currentYear = now.getYear();

    assertThat(new QuarterRange(currentYear, Quarter.First).isMultipleCalendarYears()).isFalse();
  }

  @Test
  public void calendarQuarter() {
    final DateTime now = now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = emptyOffset();

    QuarterRange q1 = new QuarterRange(currentYear, Quarter.First, calendar);

    assertThat(q1.isReadonly()).isTrue();
    assertThat(q1.getQuarter()).isEqualTo(Quarter.First);
    assertThat(q1.getStart()).isEqualTo(asDate(currentYear, 1, 1));
    assertThat(q1.getEnd()).isEqualTo(asDate(currentYear, 4, 1));

    QuarterRange q2 = new QuarterRange(currentYear, Quarter.Second, calendar);

    assertThat(q2.isReadonly()).isTrue();
    assertThat(q2.getQuarter()).isEqualTo(Quarter.Second);
    assertThat(q2.getStart()).isEqualTo(asDate(currentYear, 4, 1));
    assertThat(q2.getEnd()).isEqualTo(asDate(currentYear, 7, 1));

    QuarterRange q3 = new QuarterRange(currentYear, Quarter.Third, calendar);

    assertThat(q3.isReadonly()).isTrue();
    assertThat(q3.getQuarter()).isEqualTo(Quarter.Third);
    assertThat(q3.getStart()).isEqualTo(asDate(currentYear, 7, 1));
    assertThat(q3.getEnd()).isEqualTo(asDate(currentYear, 10, 1));

    QuarterRange q4 = new QuarterRange(currentYear, Quarter.Fourth, calendar);

    assertThat(q4.isReadonly()).isTrue();
    assertThat(q4.getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(q4.getStart()).isEqualTo(asDate(currentYear, 10, 1));
    assertThat(q4.getEnd()).isEqualTo(asDate(currentYear + 1, 1, 1));
  }


  @Test
  public void getMonthsTest() {
    final DateTime now = now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = emptyOffset();

    QuarterRange q1 = new QuarterRange(currentYear, Quarter.First, calendar);
    List<MonthRange> months = q1.monthStream();
    assertThat(months.size()).isEqualTo(TimeSpec.MonthsPerQuarter);

    int index = 0;
    for (MonthRange month : months) {
      assertThat(month.getStart()).isEqualTo(q1.getStart().plusMonths(index));
      assertThat(month.getEnd()).isEqualTo(calendar.mapEnd(month.getStart().plusMonths(1)));
      index++;
    }
  }

  @Test
  public void addQuatersTest() {
    final DateTime now = now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = emptyOffset();

    QuarterRange q1 = new QuarterRange(currentYear, Quarter.First, calendar);

    QuarterRange prevQ1 = q1.addQuarters(-1);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart());

    prevQ1 = q1.addQuarters(-2);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.Third);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-2 * TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(-1 * TimeSpec.MonthsPerQuarter));

    prevQ1 = q1.addQuarters(-3);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.Second);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-3 * TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(-2 * TimeSpec.MonthsPerQuarter));

    prevQ1 = q1.addQuarters(-4);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.First);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-4 * TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(-3 * TimeSpec.MonthsPerQuarter));

    QuarterRange nextQ1 = q1.addQuarters(1);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.Second);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(2 * TimeSpec.MonthsPerQuarter));

    nextQ1 = q1.addQuarters(2);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.Third);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(2 * TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(3 * TimeSpec.MonthsPerQuarter));

    nextQ1 = q1.addQuarters(3);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.Fourth);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(3 * TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(4 * TimeSpec.MonthsPerQuarter));

    nextQ1 = q1.addQuarters(4);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.First);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(4 * TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(5 * TimeSpec.MonthsPerQuarter));

  }
}
