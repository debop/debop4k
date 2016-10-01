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
import debop4k.timeperiod.utils.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class YearRangeCollectionTest extends AbstractTimePeriodTest {
  @Test
  public void singleYears() {
    final int startYear = 2004;

    YearRangeCollection yearRanges = YearRangeCollection.of(startYear, 1);
    assertThat(yearRanges.getYearCount()).isEqualTo(1);

    assertThat(yearRanges.getStartYear()).isEqualTo(startYear);
    assertThat(yearRanges.getEndYear()).isEqualTo(startYear);

    List<YearRange> years = yearRanges.yearStream();
    assertThat(years.size()).isEqualTo(1);
    log.trace("years={}, expected={}", years, YearRange.of(startYear));
    assertThat(years.get(0).isSamePeriod(YearRange.of(startYear))).isTrue();
  }

  @Test
  public void calenarMonths() {
    final int startYear = 2004;
    final int yearCount = 5;

    YearRangeCollection yearRanges = new YearRangeCollection(startYear, yearCount, TimeCalendar.emptyOffset());

    assertThat(yearRanges.getYearCount()).isEqualTo(yearCount);
    assertThat(yearRanges.getStartYear()).isEqualTo(startYear);
    assertThat(yearRanges.getEndYear()).isEqualTo(startYear + yearCount);
  }

  @Test
  public void yearCounts() {
    int[] yearCounts = new int[]{1, 6, 48, 180, 365};

    DateTime now = Times.now();
    DateTime today = Times.today();

    for (int yearCount : yearCounts) {
      final YearRangeCollection yearRanges = YearRangeCollection.of(now, yearCount);

      DateTime startTime = yearRanges.getCalendar().mapStart(Times.trimToYear(today));
      DateTime endTime = yearRanges.getCalendar().mapEnd(startTime.plusYears(yearCount));

      assertThat(yearRanges.getStart()).isEqualTo(startTime);
      assertThat(yearRanges.getEnd()).isEqualTo(endTime);

      List<YearRange> items = yearRanges.yearStream();

      for (int y = 0; y < yearCount; y++) {
        final YearRange item = items.get(y);
        assertThat(item.getStart()).isEqualTo(startTime.plusYears(y));
        assertThat(item.getEnd()).isEqualTo(yearRanges.getCalendar().mapEnd(startTime.plusYears(y + 1)));

        assertThat(item.unmappedStart()).isEqualTo(startTime.plusYears(y));
        assertThat(item.unmappedEnd()).isEqualTo(startTime.plusYears(y + 1));

        assertThat(item.isSamePeriod(YearRange.of(yearRanges.getStart().plusYears(y)))).isTrue();
      }
    }
  }
}
