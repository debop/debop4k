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
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static debop4k.core.kodatimes.KodaTimex.now;
import static debop4k.core.kodatimes.KodaTimex.trimToMinute;
import static org.assertj.core.api.Assertions.assertThat;

public class HourRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(HourRangeTest.class);

  @Test
  public void initValues() {
    DateTime now = Times.now();
    DateTime firstHour = trimToMinute(now);
    DateTime secondHour = firstHour.plusHours(1);

    HourRange hourRange = new HourRange(now, TimeCalendar.EMPTY_OFFSET);

    assertThat(hourRange.getStart().getYear()).isEqualTo(firstHour.getYear());
    assertThat(hourRange.getStart().getMonthOfYear()).isEqualTo(firstHour.getMonthOfYear());
    assertThat(hourRange.getStart().getDayOfMonth()).isEqualTo(firstHour.getDayOfMonth());
    assertThat(hourRange.getStart().getMinuteOfHour()).isEqualTo(0);
    assertThat(hourRange.getStart().getSecondOfMinute()).isEqualTo(0);
    assertThat(hourRange.getStart().getMillisOfSecond()).isEqualTo(0);

    assertThat(hourRange.getEnd().getYear()).isEqualTo(secondHour.getYear());
    assertThat(hourRange.getEnd().getMonthOfYear()).isEqualTo(secondHour.getMonthOfYear());
    assertThat(hourRange.getEnd().getDayOfMonth()).isEqualTo(secondHour.getDayOfMonth());
    assertThat(hourRange.getEnd().getMinuteOfHour()).isEqualTo(0);
    assertThat(hourRange.getEnd().getSecondOfMinute()).isEqualTo(0);
    assertThat(hourRange.getEnd().getMillisOfSecond()).isEqualTo(0);
  }

  @Test
  public void defaultCalendar() {
    DateTime now = Times.now();
    DateTime today = Times.today();

    for (int h = 0; h < TimeSpec.HoursPerDay; h++) {
      HourRange hourRange = new HourRange(today.plusHours(h));

      assertThat(hourRange.getYear()).isEqualTo(today.getYear());
      assertThat(hourRange.getMonthOfYear()).isEqualTo(today.getMonthOfYear());
      assertThat(hourRange.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
      assertThat(hourRange.getHourOfDay()).isEqualTo(h);
      assertThat(hourRange.getStart()).isEqualTo(hourRange.getCalendar().mapStart(today.plusHours(h)));
      assertThat(hourRange.getEnd()).isEqualTo(hourRange.getCalendar().mapEnd(today.plusHours(h + 1)));
    }
  }

  @Test
  public void constructorTest() {
    DateTime now = Times.now();

    HourRange hourRange = new HourRange(now);
    assertThat(hourRange.getYear()).isEqualTo(now.getYear());
    assertThat(hourRange.getMonthOfYear()).isEqualTo(now.getMonthOfYear());
    assertThat(hourRange.getDayOfMonth()).isEqualTo(now.getDayOfMonth());
    assertThat(hourRange.getHourOfDay()).isEqualTo(now.getHourOfDay());

    hourRange = new HourRange(trimToMinute(now));
    assertThat(hourRange.getYear()).isEqualTo(now.getYear());
    assertThat(hourRange.getMonthOfYear()).isEqualTo(now.getMonthOfYear());
    assertThat(hourRange.getDayOfMonth()).isEqualTo(now.getDayOfMonth());
    assertThat(hourRange.getHourOfDay()).isEqualTo(now.getHourOfDay());
  }

  @Test
  public void addHourTest() {
    HourRange hourRange = new HourRange();

    assertThat(hourRange.prevHour().getHourOfDay()).isEqualTo(hourRange.getStart().plusHours(-1).getHourOfDay());
    assertThat(hourRange.nextHour().getHourOfDay()).isEqualTo(hourRange.getStart().plusHours(1).getHourOfDay());

    hourRange = new HourRange(now(), TimeCalendar.EMPTY_OFFSET);

    assertThat(hourRange.addHours(0)).isEqualTo(hourRange);

    HourRange prevRange = hourRange.prevHour();
    assertThat(hourRange.addHours(-1).getYear()).isEqualTo(prevRange.getYear());
    assertThat(hourRange.addHours(-1).getMonthOfYear()).isEqualTo(prevRange.getMonthOfYear());
    assertThat(hourRange.addHours(-1).getDayOfMonth()).isEqualTo(prevRange.getDayOfMonth());
    assertThat(hourRange.addHours(-1).getHourOfDay()).isEqualTo(prevRange.getHourOfDay());

    HourRange nextRange = hourRange.nextHour();
    assertThat(hourRange.addHours(1).getYear()).isEqualTo(nextRange.getYear());
    assertThat(hourRange.addHours(1).getMonthOfYear()).isEqualTo(nextRange.getMonthOfYear());
    assertThat(hourRange.addHours(1).getDayOfMonth()).isEqualTo(nextRange.getDayOfMonth());
    assertThat(hourRange.addHours(1).getHourOfDay()).isEqualTo(nextRange.getHourOfDay());

    for (int h = -100; h < 100; h += 5) {
      HourRange range = hourRange.addHours(h);
      HourRange range2 = hourRange.addHours(h);
      assertThat(range.getYear()).isEqualTo(range2.getYear());
      assertThat(range.getMonthOfYear()).isEqualTo(range2.getMonthOfYear());
      assertThat(range.getDayOfMonth()).isEqualTo(range2.getDayOfMonth());
      assertThat(range.getHourOfDay()).isEqualTo(range2.getHourOfDay());
    }
  }

  @Test
  public void getMinutesTest() {
    HourRange hourRange = new HourRange();
    List<MinuteRange> minutes = hourRange.minutes();

    assertThat(minutes.size()).isEqualTo(TimeSpec.MinutesPerHour);

    for (int i = 0; i < TimeSpec.MinutesPerHour; i++) {
      MinuteRange minute = minutes.get(i);

      assertThat(minute.getStart()).isEqualTo(hourRange.getStart().plusMinutes(i));
      assertThat(minute.getUnmappedStart()).isEqualTo(hourRange.getStart().plusMinutes(i));

      assertThat(minute.getEnd()).isEqualTo(minute.getCalendar().mapEnd(hourRange.getStart().plusMinutes(i + 1)));
      assertThat(minute.getUnmappedEnd()).isEqualTo(hourRange.getStart().plusMinutes(i + 1));
    }
  }
}
