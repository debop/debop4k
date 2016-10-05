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
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HourRangeCollectionTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(HourRangeCollectionTest.class);

  @Test
  public void singleHour() {
    DateTime startTime = new DateTime(2004, 2, 22, 17, 0);
    HourRangeCollection hours = new HourRangeCollection(startTime, 1, TimeCalendar.EMPTY_OFFSET);

    assertThat(hours.getHourCount()).isEqualTo(1);
    assertThat(hours.getStartYear()).isEqualTo(startTime.getYear());
    assertThat(hours.getStartMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
    assertThat(hours.getStartDayOfMonth()).isEqualTo(startTime.getDayOfMonth());
    assertThat(hours.getStartHourOfDay()).isEqualTo(startTime.getHourOfDay());

    assertThat(hours.getEndYear()).isEqualTo(startTime.getYear());
    assertThat(hours.getEndMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
    assertThat(hours.getEndDayOfMonth()).isEqualTo(startTime.getDayOfMonth());
    assertThat(hours.getEndHourOfDay()).isEqualTo(startTime.getHourOfDay() + 1);

    final List<HourRange> items = hours.hours();
    assertThat(items.size()).isEqualTo(1);
    assertThat(items.get(0).isSamePeriod(new HourRange(startTime, TimeCalendar.EMPTY_OFFSET))).isTrue();
  }

  @Test
  public void calendarHoursTeset() {
    final DateTime startTime = new DateTime(2004, 2, 11, 22, 0);
    final int hourCount = 4;
    HourRangeCollection hours = new HourRangeCollection(startTime, hourCount, TimeCalendar.EMPTY_OFFSET);

    assertThat(hours.getHourCount()).isEqualTo(hourCount);
    assertThat(hours.getStartYear()).isEqualTo(startTime.getYear());
    assertThat(hours.getStartMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
    assertThat(hours.getStartDayOfMonth()).isEqualTo(startTime.getDayOfMonth());
    assertThat(hours.getStartHourOfDay()).isEqualTo(startTime.getHourOfDay());

    assertThat(hours.getEndYear()).isEqualTo(startTime.getYear());
    assertThat(hours.getEndMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
    assertThat(hours.getEndDayOfMonth()).isEqualTo(startTime.getDayOfMonth() + 1);
    assertThat(hours.getEndHourOfDay()).isEqualTo((startTime.getHourOfDay() + hourCount) % 24);

    final List<HourRange> items = hours.hours();
    assertThat(items.size()).isEqualTo(hourCount);
    for (int h = 0; h < hourCount; h++) {
      assertThat(items.get(h).isSamePeriod(new HourRange(startTime.plusHours(h), TimeCalendar.EMPTY_OFFSET))).isTrue();
    }
  }

  @Test
  public void hoursTest() {

    final int[] hourCounts = new int[]{1, 24, 48, 64, 128};
    final DateTime now = Times.now();

    for (int hourCount : hourCounts) {

      final HourRangeCollection hours = HourRangeCollection.of(now, hourCount);
      final DateTime startTime = KodaTimex.trimToMinute(now).plus(hours.getCalendar().getStartOffset());
      final DateTime endTime = startTime.plusHours(hourCount).plus(hours.getCalendar().getEndOffset());

      assertThat(hours.getStart()).isEqualTo(startTime);
      assertThat(hours.getEnd()).isEqualTo(endTime);
      assertThat(hours.getHourCount()).isEqualTo(hourCount);

      final List<HourRange> items = hours.hours();
      assertThat(items.size()).isEqualTo(hourCount);

      for (int h = 0; h < hourCount; h++) {
        assertThat(items.get(h).getStart()).isEqualTo(startTime.plusHours(h));
        assertThat(items.get(h).getEnd()).isEqualTo(hours.getCalendar().mapEnd(startTime.plusHours(h + 1)));
        assertThat(items.get(h).getUnmappedEnd()).isEqualTo(startTime.plusHours(h + 1));
      }
    }
  }
}
