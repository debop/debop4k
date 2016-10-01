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
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.YearWeek;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class TimesCompareTest extends AbstractTimePeriodTest {

  @Test
  public void isSameYearTest() {
    assertThat(isSameYear(testDate, testNow)).isFalse();
    assertThat(isSameYear(asDate(2000, 4, 1), asDate(2000, 12, 31))).isTrue();
  }

  @Test
  public void isSameHalfyearTest() {
    assertThat(isSameHalfyear(asDate(2000, 1, 1), asDate(2000, 6, 30))).isTrue();
    assertThat(isSameHalfyear(asDate(2000, 7, 1), asDate(2000, 12, 31))).isTrue();
    assertThat(isSameHalfyear(asDate(2000, 1, 1), asDate(2000, 7, 1))).isFalse();
    assertThat(isSameHalfyear(asDate(2000, 7, 1), asDate(2001, 1, 1))).isFalse();
  }

  @Test
  public void isSameQuarterTest() {
    assertThat(isSameQuarter(asDate(2000, 1, 1), asDate(2000, 3, 31))).isTrue();
    assertThat(isSameQuarter(asDate(2000, 4, 1), asDate(2000, 6, 30))).isTrue();
    assertThat(isSameQuarter(asDate(2000, 7, 1), asDate(2000, 9, 30))).isTrue();
    assertThat(isSameQuarter(asDate(2000, 10, 1), asDate(2000, 12, 31))).isTrue();

    assertThat(isSameQuarter(asDate(2000, 1, 1), asDate(2000, 4, 1))).isFalse();
    assertThat(isSameQuarter(asDate(2000, 4, 1), asDate(2000, 7, 1))).isFalse();
    assertThat(isSameQuarter(asDate(2000, 7, 1), asDate(2000, 10, 1))).isFalse();
    assertThat(isSameQuarter(asDate(2000, 10, 1), asDate(2001, 1, 1))).isFalse();
  }

  @Test
  public void isSameMonthTest() {
    assertThat(isSameMonth(testDate, testDiffDate)).isFalse();
    assertThat(isSameMonth(asDate(2000, 10, 1), asDate(2000, 10, 31))).isTrue();
    assertThat(isSameMonth(asDate(2000, 10, 1), asDate(2000, 10, 1))).isTrue();
    assertThat(isSameMonth(asDate(2000, 10, 1), asDate(2000, 11, 1))).isFalse();
    assertThat(isSameMonth(asDate(2000, 10, 1), asDate(2000, 9, 30))).isFalse();
  }

  @Test
  public void isSameWeekTest() {
    DateTime prevWeek = testDate.minusDays(TimeSpec.DaysPerWeek + 1);
    DateTime nextWeek = testDate.plusDays(TimeSpec.DaysPerWeek + 1);

    YearWeek yw = Times.weekOfYear(testDate);

    DateTime startOfWeek = Times.startOfYearWeek(YearWeek.of(yw.getWeekyear(), yw.getWeekOfWeekyear()));

    YearWeek yw2 = Times.weekOfYear(startOfWeek);
    log.debug("yw=[{}], yw2=[{}], startOfWeek=[{}], testDate=[{}]", yw, yw2, startOfWeek, testDate);

    assertThat(isSameWeek(testDate, startOfWeek)).isTrue();
    assertThat(isSameWeek(testDate, testDate)).isTrue();
    assertThat(isSameWeek(testDiffDate, testDiffDate)).isTrue();

    assertThat(isSameWeek(testDate, testDiffDate)).isFalse();
    assertThat(isSameWeek(testDate, prevWeek)).isFalse();
    assertThat(isSameWeek(testDate, nextWeek)).isFalse();
  }

  @Test
  public void isSameDayTest() {
    assertThat(isSameDay(testDate, testDiffDate)).isFalse();
    assertThat(isSameDay(asDate(2000, 10, 19), asDate(2000, 10, 19))).isTrue();
    assertThat(isSameDay(asDate(2000, 10, 19), endTimeOfDay(asDate(2000, 10, 19)))).isTrue();
    assertThat(isSameDay(asDate(1999, 10, 19), asDate(2000, 10, 19))).isFalse();
    assertThat(isSameDay(asDate(2000, 10, 18), asDate(2000, 10, 19))).isFalse();
    assertThat(isSameDay(asDate(2000, 10, 19), asDate(2000, 10, 20))).isFalse();
  }

  @Test
  public void isSameHourTest() {
    assertThat(isSameHour(testDate, testDiffDate)).isFalse();
    assertThat(isSameHour(new DateTime(2000, 10, 19, 18, 0, 0), new DateTime(2000, 10, 19, 18, 0, 0))).isTrue();
    assertThat(isSameHour(new DateTime(2000, 10, 19, 18, 0, 0), new DateTime(2000, 10, 19, 18, 10, 10))).isTrue();
    assertThat(isSameHour(new DateTime(2000, 10, 19, 18, 0, 0), new DateTime(2000, 10, 19, 1, 0, 0))).isFalse();
  }

  @Test
  public void isSameMinuteTest() {
    assertThat(isSameMinute(testDate, testDiffDate)).isFalse();
    assertThat(isSameMinute(new DateTime(2000, 10, 19, 18, 5, 0), new DateTime(2000, 10, 19, 18, 5, 0))).isTrue();
    assertThat(isSameMinute(new DateTime(2000, 10, 19, 18, 10, 0), new DateTime(2000, 10, 19, 18, 10, 10))).isTrue();
    assertThat(isSameMinute(new DateTime(2000, 10, 19, 18, 10, 0), new DateTime(2000, 10, 19, 1, 10, 0))).isFalse();
  }

  @Test
  public void isSameSecondTest() {
    assertThat(isSameSecond(testDate, testDiffDate)).isFalse();
    assertThat(isSameSecond(new DateTime(2000, 10, 19, 18, 5, 0), new DateTime(2000, 10, 19, 18, 5, 0))).isTrue();
    assertThat(isSameSecond(new DateTime(2000, 10, 19, 18, 10, 0), new DateTime(2000, 10, 19, 18, 10, 10))).isFalse();
    assertThat(isSameSecond(new DateTime(2000, 10, 19, 18, 10, 0), new DateTime(2000, 10, 19, 1, 10, 0))).isFalse();
  }
}
