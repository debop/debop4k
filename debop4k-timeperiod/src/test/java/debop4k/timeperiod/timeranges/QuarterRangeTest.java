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

import debop4k.core.kodatimes.KodaTimex;
import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.TimeCalendar;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.Quarter;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static debop4k.core.kodatimes.KodaTimex.asDate;
import static debop4k.timeperiod.TimeCalendar.EMPTY_OFFSET;
import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;


public class QuarterRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(QuarterRangeTest.class);

  @Test
  public void initValues() {
    DateTime now = KodaTimex.now();
    DateTime firstQuarter = startTimeOfQuarter(now.getYear(), Quarter.FIRST);
    DateTime secondQuarter = startTimeOfQuarter(now.getYear(), Quarter.SECOND);

    QuarterRange quarterRange = new QuarterRange(now.getYear(), Quarter.FIRST, EMPTY_OFFSET);

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
    DateTime now = KodaTimex.now();
    int currentYear = now.getYear();

    assertThat(new QuarterRange(asDate(currentYear, 1, 1)).getQuarter()).isEqualTo(Quarter.FIRST);
    assertThat(new QuarterRange(asDate(currentYear, 3, 31)).getQuarter()).isEqualTo(Quarter.FIRST);

    assertThat(new QuarterRange(asDate(currentYear, 4, 1)).getQuarter()).isEqualTo(Quarter.SECOND);
    assertThat(new QuarterRange(asDate(currentYear, 6, 30)).getQuarter()).isEqualTo(Quarter.SECOND);

    assertThat(new QuarterRange(asDate(currentYear, 7, 1)).getQuarter()).isEqualTo(Quarter.THIRD);
    assertThat(new QuarterRange(asDate(currentYear, 9, 30)).getQuarter()).isEqualTo(Quarter.THIRD);

    assertThat(new QuarterRange(asDate(currentYear, 10, 1)).getQuarter()).isEqualTo(Quarter.FOURTH);
    assertThat(new QuarterRange(asDate(currentYear, 12, 31)).getQuarter()).isEqualTo(Quarter.FOURTH);
  }

  @Test
  public void startMonth() {
    final DateTime now = KodaTimex.now();
    final int currentYear = now.getYear();

    assertThat(new QuarterRange(currentYear, Quarter.FIRST).getStartMonthOfYear()).isEqualTo(1);
    assertThat(new QuarterRange(currentYear, Quarter.SECOND).getStartMonthOfYear()).isEqualTo(4);
    assertThat(new QuarterRange(currentYear, Quarter.THIRD).getStartMonthOfYear()).isEqualTo(7);
    assertThat(new QuarterRange(currentYear, Quarter.FOURTH).getStartMonthOfYear()).isEqualTo(10);
  }

  @Test
  public void isMultipleCalendarYearsTest() {
    final DateTime now = KodaTimex.now();
    final int currentYear = now.getYear();

    assertThat(new QuarterRange(currentYear, Quarter.FIRST).isMultipleCalendarYears()).isFalse();
  }

  @Test
  public void calendarQuarter() {
    final DateTime now = KodaTimex.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = EMPTY_OFFSET;

    QuarterRange q1 = new QuarterRange(currentYear, Quarter.FIRST, calendar);

    assertThat(q1.isReadonly()).isTrue();
    assertThat(q1.getQuarter()).isEqualTo(Quarter.FIRST);
    assertThat(q1.getStart()).isEqualTo(asDate(currentYear, 1, 1));
    assertThat(q1.getEnd()).isEqualTo(asDate(currentYear, 4, 1));

    QuarterRange q2 = new QuarterRange(currentYear, Quarter.SECOND, calendar);

    assertThat(q2.isReadonly()).isTrue();
    assertThat(q2.getQuarter()).isEqualTo(Quarter.SECOND);
    assertThat(q2.getStart()).isEqualTo(asDate(currentYear, 4, 1));
    assertThat(q2.getEnd()).isEqualTo(asDate(currentYear, 7, 1));

    QuarterRange q3 = new QuarterRange(currentYear, Quarter.THIRD, calendar);

    assertThat(q3.isReadonly()).isTrue();
    assertThat(q3.getQuarter()).isEqualTo(Quarter.THIRD);
    assertThat(q3.getStart()).isEqualTo(asDate(currentYear, 7, 1));
    assertThat(q3.getEnd()).isEqualTo(asDate(currentYear, 10, 1));

    QuarterRange q4 = new QuarterRange(currentYear, Quarter.FOURTH, calendar);

    assertThat(q4.isReadonly()).isTrue();
    assertThat(q4.getQuarter()).isEqualTo(Quarter.FOURTH);
    assertThat(q4.getStart()).isEqualTo(asDate(currentYear, 10, 1));
    assertThat(q4.getEnd()).isEqualTo(asDate(currentYear + 1, 1, 1));
  }


  @Test
  public void getMonthsTest() {
    final DateTime now = KodaTimex.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = EMPTY_OFFSET;

    QuarterRange q1 = new QuarterRange(currentYear, Quarter.FIRST, calendar);
    List<MonthRange> months = q1.months();
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
    final DateTime now = KodaTimex.now();
    final int currentYear = now.getYear();
    final TimeCalendar calendar = EMPTY_OFFSET;

    QuarterRange q1 = new QuarterRange(currentYear, Quarter.FIRST, calendar);

    QuarterRange prevQ1 = q1.addQuarters(-1);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.FOURTH);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart());

    prevQ1 = q1.addQuarters(-2);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.THIRD);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-2 * TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(-1 * TimeSpec.MonthsPerQuarter));

    prevQ1 = q1.addQuarters(-3);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.SECOND);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-3 * TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(-2 * TimeSpec.MonthsPerQuarter));

    prevQ1 = q1.addQuarters(-4);
    assertThat(prevQ1.getQuarter()).isEqualTo(Quarter.FIRST);
    assertThat(prevQ1.getStart()).isEqualTo(q1.getStart().plusMonths(-4 * TimeSpec.MonthsPerQuarter));
    assertThat(prevQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(-3 * TimeSpec.MonthsPerQuarter));

    QuarterRange nextQ1 = q1.addQuarters(1);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.SECOND);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(2 * TimeSpec.MonthsPerQuarter));

    nextQ1 = q1.addQuarters(2);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.THIRD);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(2 * TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(3 * TimeSpec.MonthsPerQuarter));

    nextQ1 = q1.addQuarters(3);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.FOURTH);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(3 * TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(4 * TimeSpec.MonthsPerQuarter));

    nextQ1 = q1.addQuarters(4);
    assertThat(nextQ1.getQuarter()).isEqualTo(Quarter.FIRST);
    assertThat(nextQ1.getStart()).isEqualTo(q1.getStart().plusMonths(4 * TimeSpec.MonthsPerQuarter));
    assertThat(nextQ1.getEnd()).isEqualTo(q1.getStart().plusMonths(5 * TimeSpec.MonthsPerQuarter));

  }
}
