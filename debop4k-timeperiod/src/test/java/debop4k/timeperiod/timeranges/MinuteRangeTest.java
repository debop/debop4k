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
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class MinuteRangeTest extends AbstractTimePeriodTest {
  @Test
  public void initValues() {
    DateTime now = Times.now();
    DateTime firstMin = Times.trimToSecond(now);
    DateTime secondMin = firstMin.plusMinutes(1);

    MinuteRange minRange = new MinuteRange(now, TimeCalendar.emptyOffset());

    assertThat(minRange.getStart().getYear()).isEqualTo(firstMin.getYear());
    assertThat(minRange.getStart().getMonthOfYear()).isEqualTo(firstMin.getMonthOfYear());
    assertThat(minRange.getStart().getDayOfMonth()).isEqualTo(firstMin.getDayOfMonth());
    assertThat(minRange.getStart().getMinuteOfHour()).isEqualTo(firstMin.getMinuteOfHour());
    assertThat(minRange.getStart().getSecondOfMinute()).isEqualTo(0);
    assertThat(minRange.getStart().getMillisOfSecond()).isEqualTo(0);

    assertThat(minRange.getEnd().getYear()).isEqualTo(secondMin.getYear());
    assertThat(minRange.getEnd().getMonthOfYear()).isEqualTo(secondMin.getMonthOfYear());
    assertThat(minRange.getEnd().getDayOfMonth()).isEqualTo(secondMin.getDayOfMonth());
    assertThat(minRange.getEnd().getMinuteOfHour()).isEqualTo(secondMin.getMinuteOfHour());
    assertThat(minRange.getEnd().getSecondOfMinute()).isEqualTo(0);
    assertThat(minRange.getEnd().getMillisOfSecond()).isEqualTo(0);
  }

  @Test
  public void defaultCalendar() {
    DateTime now = Times.now();
    DateTime today = Times.today();

    for (int m = 0; m < TimeSpec.MinutesPerHour; m++) {
      MinuteRange minRange = new MinuteRange(today.plusMinutes(m));

      assertThat(minRange.getYear()).isEqualTo(today.getYear());
      assertThat(minRange.getMonthOfYear()).isEqualTo(today.getMonthOfYear());
      assertThat(minRange.getDayOfMonth()).isEqualTo(today.getDayOfMonth());
      assertThat(minRange.getHourOfDay()).isEqualTo(today.getHourOfDay());
      assertThat(minRange.getMinuteOfHour()).isEqualTo(m);
      assertThat(minRange.getStart()).isEqualTo(minRange.getCalendar().mapStart(today.plusMinutes(m)));
      assertThat(minRange.getEnd()).isEqualTo(minRange.getCalendar().mapEnd(today.plusMinutes(m + 1)));
    }
  }

  @Test
  public void constructorTest() {
    DateTime now = Times.now();

    MinuteRange minRange = new MinuteRange(now);
    assertThat(minRange.getYear()).isEqualTo(now.getYear());
    assertThat(minRange.getMonthOfYear()).isEqualTo(now.getMonthOfYear());
    assertThat(minRange.getDayOfMonth()).isEqualTo(now.getDayOfMonth());
    assertThat(minRange.getHourOfDay()).isEqualTo(now.getHourOfDay());
    assertThat(minRange.getMinuteOfHour()).isEqualTo(now.getMinuteOfHour());

    minRange = new MinuteRange(now);
    assertThat(minRange.getYear()).isEqualTo(now.getYear());
    assertThat(minRange.getMonthOfYear()).isEqualTo(now.getMonthOfYear());
    assertThat(minRange.getDayOfMonth()).isEqualTo(now.getDayOfMonth());
    assertThat(minRange.getHourOfDay()).isEqualTo(now.getHourOfDay());
    assertThat(minRange.getMinuteOfHour()).isEqualTo(now.getMinuteOfHour());
  }

  @Test
  public void addHourTest() {
    MinuteRange minRange = new MinuteRange();

    assertThat(minRange.prevMinute().getMinuteOfHour()).isEqualTo(minRange.getStart().plusMinutes(-1).getMinuteOfHour());
    assertThat(minRange.nextMinute().getMinuteOfHour()).isEqualTo(minRange.getStart().plusMinutes(1).getMinuteOfHour());

    minRange = new MinuteRange(TimeCalendar.emptyOffset());

    assertThat(minRange.addMinutes(0)).isEqualTo(minRange);

    MinuteRange prevRange = minRange.prevMinute();
    assertThat(minRange.addMinutes(-1).getYear()).isEqualTo(prevRange.getYear());
    assertThat(minRange.addMinutes(-1).getMonthOfYear()).isEqualTo(prevRange.getMonthOfYear());
    assertThat(minRange.addMinutes(-1).getDayOfMonth()).isEqualTo(prevRange.getDayOfMonth());
    assertThat(minRange.addMinutes(-1).getHourOfDay()).isEqualTo(prevRange.getHourOfDay());
    assertThat(minRange.addMinutes(-1).getMinuteOfHour()).isEqualTo(prevRange.getMinuteOfHour());

    MinuteRange nextRange = minRange.nextMinute();
    assertThat(minRange.addMinutes(1).getYear()).isEqualTo(nextRange.getYear());
    assertThat(minRange.addMinutes(1).getMonthOfYear()).isEqualTo(nextRange.getMonthOfYear());
    assertThat(minRange.addMinutes(1).getDayOfMonth()).isEqualTo(nextRange.getDayOfMonth());
    assertThat(minRange.addMinutes(1).getHourOfDay()).isEqualTo(nextRange.getHourOfDay());
    assertThat(minRange.addMinutes(1).getMinuteOfHour()).isEqualTo(nextRange.getMinuteOfHour());

    for (int m = -100; m < 100; m += 5) {
      MinuteRange range = minRange.addMinutes(m);
      MinuteRange range2 = minRange.addMinutes(m);
      assertThat(range.getYear()).isEqualTo(range2.getYear());
      assertThat(range.getMonthOfYear()).isEqualTo(range2.getMonthOfYear());
      assertThat(range.getDayOfMonth()).isEqualTo(range2.getDayOfMonth());
      assertThat(range.getHourOfDay()).isEqualTo(range2.getHourOfDay());
      assertThat(range.getMinuteOfHour()).isEqualTo(range2.getMinuteOfHour());
    }
  }

  @Test
  public void getMinutesTest() {
    HourRange hourRange = new HourRange();
    List<MinuteRange> minutes = hourRange.minuteStream();

    assertThat(minutes.size()).isEqualTo(TimeSpec.MinutesPerHour);

    for (int i = 0; i < TimeSpec.MinutesPerHour; i++) {
      MinuteRange minute = minutes.get(i);

      assertThat(minute.getStart()).isEqualTo(hourRange.getStart().plusMinutes(i));
      assertThat(minute.unmappedStart()).isEqualTo(hourRange.getStart().plusMinutes(i));

      assertThat(minute.getEnd()).isEqualTo(minute.getCalendar().mapEnd(hourRange.getStart().plusMinutes(i + 1)));
      assertThat(minute.unmappedEnd()).isEqualTo(hourRange.getStart().plusMinutes(i + 1));
    }
  }
}
