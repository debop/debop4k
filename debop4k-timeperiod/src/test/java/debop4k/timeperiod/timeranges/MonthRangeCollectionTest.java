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
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.YearMonth;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static debop4k.core.kodatimes.KodaTimex.trimToDay;
import static org.assertj.core.api.Assertions.assertThat;


public class MonthRangeCollectionTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(MonthRangeCollectionTest.class);

  @Test
  public void singleMonths() {
    final int startYear = 2004;
    final int startMonth = 6;

    MonthRangeCollection monthRanges = new MonthRangeCollection(startYear, startMonth, 1);
    assertThat(monthRanges.getMonthCount()).isEqualTo(1);

    List<MonthRange> months = monthRanges.months();
    assertThat(months.size()).isEqualTo(1);
    assertThat(months.get(0).isSamePeriod(new MonthRange(startYear, startMonth))).isTrue();

    assertThat(monthRanges.getStartYear()).isEqualTo(startYear);
    assertThat(monthRanges.getEndYear()).isEqualTo(startYear);
    assertThat(monthRanges.getStartMonthOfYear()).isEqualTo(startMonth);
    assertThat(monthRanges.getEndMonthOfYear()).isEqualTo(startMonth);
  }

  @Test
  public void calenarMonths() {
    final int startYear = 2004;
    final int startMonth = 11;
    final int monthCount = 5;

    MonthRangeCollection monthRanges = new MonthRangeCollection(startYear, startMonth, monthCount);

    assertThat(monthRanges.getMonthCount()).isEqualTo(monthCount);
    assertThat(monthRanges.getStartYear()).isEqualTo(startYear);
    assertThat(monthRanges.getStartMonthOfYear()).isEqualTo(startMonth);
    assertThat(monthRanges.getEndYear()).isEqualTo(startYear + 1);
    assertThat(monthRanges.getEndMonthOfYear()).isEqualTo((startMonth + monthCount - 1) % TimeSpec.MonthsPerYear);
  }

  @Test
  public void monthCounts() {
    int[] monthCounts = new int[]{1, 6, 48, 180, 365};

    final DateTime now = KodaTimex.now();
    final DateTime today = KodaTimex.today();

    for (final int monthCount : monthCounts) {
      final MonthRangeCollection monthRanges = new MonthRangeCollection(now, monthCount);

      final DateTime startTime = monthRanges.getCalendar().mapStart(trimToDay(today));
      final DateTime endTime = monthRanges.getCalendar().mapEnd(startTime.plusMonths(monthCount));

      assertThat(monthRanges.getStart()).isEqualTo(startTime);
      assertThat(monthRanges.getEnd()).isEqualTo(endTime);

      final List<MonthRange> items = monthRanges.months();

      for (int m = 0; m < monthCount; m++) {
        final MonthRange item = items.get(m);
        assertThat(item.getStart()).isEqualTo(startTime.plusMonths(m));
        assertThat(item.getEnd()).isEqualTo(monthRanges.getCalendar().mapEnd(startTime.plusMonths(m + 1)));

        assertThat(item.getUnmappedStart()).isEqualTo(startTime.plusMonths(m));
        assertThat(item.getUnmappedEnd()).isEqualTo(startTime.plusMonths(m + 1));

        assertThat(item.isSamePeriod(new MonthRange(monthRanges.getStart().plusMonths(m)))).isTrue();

        YearMonth ym = Times.addMonth(now.getYear(), now.getMonthOfYear(), m);
        assertThat(item.isSamePeriod(new MonthRange(ym))).isTrue();
      }
    }
  }
}
