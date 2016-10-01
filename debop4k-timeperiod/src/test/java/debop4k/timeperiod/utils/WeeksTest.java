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
import debop4k.timeperiod.timeranges.WeekRange;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class WeeksTest extends AbstractTimePeriodTest {

  public static final DateTime[] testTimes = new DateTime[]{
      asDate(2003, 12, 28)
  };

  @Test
  public void getYearAndWeek() {
    for (DateTime moment : testTimes) {
      YearWeek yw = YearWeek.of(moment);
      assertThat(yw.getWeekyear()).isEqualTo(moment.getWeekyear());
      assertThat(yw.getWeekOfWeekyear()).isEqualTo(moment.getWeekOfWeekyear());
    }
  }

  @Test
  public void getYearAndWeekTest() {
    for (int year = 2000; year < 2100; year++) {
      DateTime startDay = startTimeOfYear(year);
      DateTime endDay = endTimeOfYear(year - 1);

      YearWeek startYW = YearWeek.of(startDay);
      YearWeek endYW = YearWeek.of(endDay);

      if (startDay.getDayOfWeek() == TimeSpec.FirstDayOfWeek.getValue())
        assertThat(endYW.equals(startYW)).isFalse();
      else
        assertThat(endYW.equals(startYW)).isTrue();
    }
  }

  @Test
  public void getStartWeekRangeOfYear() {
    for (int year = 2000; year < 2100; year++) {
      WeekRange startWeekRange = new WeekRange(year, 1);
      log.trace("year={}, startWeekRange={}", year, startWeekRange);

      assertThat(new Duration(asDate(year - 1, 12, 28), startWeekRange.startDayOfStart()).getStandardDays()).isGreaterThan(0);
      assertThat(new Duration(asDate(year, 1, 3), startWeekRange.startDayOfEnd()).getStandardDays()).isGreaterThan(0);
    }
  }

  @Test
  public void getEndYearAndWeekTest() {
    for (int year = 1980; year < 2100; year++) {
      YearWeek yw = lastWeekOfYear(year);
      assertThat(year).isEqualTo(yw.getWeekyear());
      assertThat(yw.getWeekOfWeekyear()).isGreaterThanOrEqualTo(52);
    }
  }

  @Test
  public void getEndWeekRangeOfYear() {
    for (int year = 2000; year < 2100; year++) {
      WeekRange startWeekRange = startWeekRangeOfYear(year);
      WeekRange endWeekRange = endWeekRangeOfYear(year - 1);

      log.trace("year=[{}], startWeek=[{}], endWeek=[{}]",
                year, startWeekRange.startDayOfStart(), endWeekRange.startDayOfStart());

      assertThat(new Duration(asDate(year - 1, 12, 28), startWeekRange.startDayOfStart()).getStandardDays())
          .isGreaterThan(0);
      assertThat(new Duration(asDate(year, 1, 3), startWeekRange.startDayOfEnd()).getStandardDays())
          .isGreaterThan(0);

      assertThat(endWeekRange.startDayOfStart().plusWeeks(1)).isEqualTo(startWeekRange.startDayOfStart());
      assertThat(endWeekRange.startDayOfEnd().plusDays(1)).isEqualTo(startWeekRange.startDayOfStart());
    }
  }

  @Test
  public void getWeekRangeTest() {
    for (int year = 2000; year < 2100; year++) {
      DateTime endDay = endTimeOfYear(year - 1);
      DateTime startDay = startTimeOfYear(year);

      YearWeek endDayYearWeek = YearWeek.of(endDay);
      assertThat(endDayYearWeek.getWeekyear()).isGreaterThanOrEqualTo(year - 1);

      YearWeek startDayYearWeek = YearWeek.of(startDay);
      assertThat(startDayYearWeek.getWeekyear()).isLessThanOrEqualTo(year);

      // 해당일자가 속한 주차의 일자들을 구한다. 년말/년초 구간은 꼭 7일이 아닐 수 있다.
      WeekRange endDayWeekRange = new WeekRange(endDayYearWeek);
      WeekRange startDayWeekRange = new WeekRange(startDayYearWeek);

      assertThat(endDayWeekRange.hasPeriod()).isTrue();
      assertThat(startDayWeekRange.hasPeriod()).isTrue();

      log.trace("start day weeksView=[{}]", startDayWeekRange);

      if (endDayYearWeek.equals(startDayYearWeek)) {
        assertThat(startDayWeekRange).isEqualTo(endDayWeekRange);
      } else {
        assertThat(startDayWeekRange).isNotEqualTo(endDayWeekRange);
      }
    }
  }

  @Test
  public void addWeekOfYearsTest() {
    for (int year = 2000; year < 2100; year++) {
      final int step = 2;
      final int maxAddWeeks = 40;

      YearWeek prevResult = null;
      YearWeek maxWeek = maxWeekOfYear(year);

      for (int week = 1; week < maxWeek.getWeekOfWeekyear(); week += step) {
        for (int addWeeks = -maxAddWeeks; addWeeks <= maxAddWeeks; addWeeks += step) {
          YearWeek current = new YearWeek(year, week);
          YearWeek result = addWeeks(current, addWeeks);

          log.trace("current={}, result={}, addWeeks={}", current, result, addWeeks);
          if (addWeeks != 0 && prevResult != null) {
            if (result.getWeekyear() == prevResult.getWeekyear())
              assertThat(result.getWeekOfWeekyear()).isEqualTo(prevResult.getWeekOfWeekyear() + step);
          }

          assertThat(result.getWeekOfWeekyear()).isGreaterThan(0);
          assertThat(result.getWeekOfWeekyear()).isLessThanOrEqualTo(TimeSpec.MaxWeeksPerYear);

          prevResult = result;
        }
      }
    }
  }
}
