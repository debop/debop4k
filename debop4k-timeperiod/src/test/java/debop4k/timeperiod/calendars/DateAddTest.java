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

package debop4k.timeperiod.calendars;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.utils.Durations;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.asDate;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DateAddTest extends AbstractTimePeriodTest {

  @Test
  public void noPeriodTest() {
    DateTime test = asDate(2011, 4, 12);
    DateAdd dateAdd = new DateAdd();

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(-1))).isEqualTo(test.plusDays(-1));

    assertThat(dateAdd.subtract(test, Durations.dayOf(1))).isEqualTo(test.plusDays(-1));
    assertThat(dateAdd.subtract(test, Durations.dayOf(-1))).isEqualTo(test.plusDays(1));
  }

  @Test
  public void periodLimitsAdd() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 25));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 30), (DateTime) null); // 4월 30일 이후

    DateAdd dateAdd = new DateAdd();

    // 예외기간을 설정합니다. 4월 20일 ~ 4월25일, 4월 30일 이후
    dateAdd.getExcludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Day)).isEqualTo(test.plus(Durations.Day));

    // 4월 12일에 8일을 더하면 4월 20일이지만, 20~25일까지 제외되므로, 4월 25일이 된다.
    assertThat(dateAdd.add(test, Durations.dayOf(8))).isEqualTo(period1.getEnd());

    // 4월 12에 20일을 더하면 4월 20~25일을 제외한 후 계산하면 4월 30 이후가 된다. (5월 3일).
    // 하지만 4월 30 이후는 모두 제외되므로 결과값은 null이다.
    assertThat(dateAdd.add(test, Durations.dayOf(20))).isNull();

    assertThat(dateAdd.subtract(test, Durations.dayOf(3))).isEqualTo(test.minus(Durations.dayOf(3)));
  }

  @Test
  @BenchmarkOptions()
  public void periodLimitsSubtract() {
    DateTime test = asDate(2011, 4, 30);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 25));
    ITimePeriod period2 = new TimeRange(null, asDate(2011, 4, 6)); // 4월 6일까지

    DateAdd dateAdd = new DateAdd();

    // 예외기간을 설정합니다. 4월 6일 이전, 4월 20일 ~ 4월 25일
    dateAdd.getExcludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.subtract(test, Durations.Day)).isEqualTo(test.minus(Durations.Day));

    // 4월 30일로부터 5일 전이면 4월 25일이지만, 예외기간이므로 4월20일이 된다.
    assertThat(dateAdd.subtract(test, Durations.dayOf(5))).isEqualTo(period1.getStart());

    // 4월 30일로부터 20일 전이면, 5월 전이 4월20일이므로, 4월 5일이 된다. 근데, 4월 6일 이전은 모두 제외기간이므로 null을 반환한다.
    assertThat(dateAdd.subtract(test, Durations.dayOf(20))).isNull();
  }

  @Test
  public void includeOutsideMax() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(asDate(2011, 4, 20), (DateTime) null);

    DateAdd dateAdd = new DateAdd();
    dateAdd.getIncludePeriods().add(period);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(period.getStart());
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(period.getStart().plusDays(1));

    assertThat(dateAdd.subtract(test, Durations.Zero)).isNull();
    assertThat(dateAdd.subtract(test, Durations.dayOf(1))).isNull();
  }

  @Test
  public void includeOutsideMin() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(null, asDate(2011, 4, 10));

    DateAdd dateAdd = new DateAdd();
    dateAdd.getIncludePeriods().add(period);

    assertThat(dateAdd.add(test, Durations.Zero)).isNull();
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isNull();

    assertThat(dateAdd.subtract(test, Durations.Zero)).isEqualTo(period.getEnd());
    assertThat(dateAdd.subtract(test, Durations.dayOf(1))).isEqualTo(period.getEnd().plusDays(-1));
  }

  @Test
  public void allExcluded() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(asDate(2011, 4, 10), asDate(2011, 4, 20));
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(period);
    dateAdd.getExcludePeriods().add(period);

    assertThat(dateAdd.add(test, Durations.Zero)).isNull();
    assertThat(dateAdd.add(test, Durations.year(2011))).isNull();
    assertThat(dateAdd.subtract(test, Durations.year(2011))).isNull();
  }

  @Test
  public void allExcluded2() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 10), asDate(2011, 4, 20));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 10), asDate(2011, 4, 15));
    ITimePeriod period3 = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));

    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(period1);

    dateAdd.getExcludePeriods().add(period2);
    dateAdd.getExcludePeriods().add(period3);

    assertThat(dateAdd.add(test, Durations.Zero)).isNull();
    assertThat(dateAdd.add(test, Durations.year(2011))).isNull();
    assertThat(dateAdd.subtract(test, Durations.year(2011))).isNull();
  }

  @Test
  public void allExcluded3() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 10), asDate(2011, 4, 20));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));

    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(2))).isEqualTo(test.plusDays(2));
    assertThat(dateAdd.add(test, Durations.dayOf(3))).isNull();
  }

  @Test
  public void periodMoment() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(test);
    DateAdd dateAdd = DateAdd.of();

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);

    dateAdd.getIncludePeriods().add(period);
    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);

    dateAdd.getExcludePeriods().add(period);
    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);

    dateAdd.getIncludePeriods().clear();
    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);

    dateAdd.getExcludePeriods().clear();
    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
  }

  @Test
  public void includeTest() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(asDate(2011, 4, 1), TimeSpec.MaxPeriodTime);
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(period);
    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(365))).isEqualTo(test.plusDays(365));
  }

  @Test
  public void includeSplit() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 1), asDate(2011, 4, 15));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 24));

    DateAdd dateAdd = new DateAdd();
    dateAdd.getIncludePeriods().add(period1);
    dateAdd.getIncludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(3))).isEqualTo(period2.getStart());
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isEqualTo(period2.getStart().plusDays(2));
    assertThat(dateAdd.add(test, Durations.dayOf(6))).isEqualTo(period2.getStart().plusDays(3));
    assertThat(dateAdd.add(test, Durations.dayOf(7))).isNull();

    // NOTE: SeekBoundaryMode에 따라 결과가 달라집니다.
    assertThat(dateAdd.add(test, Durations.dayOf(7), SeekBoundaryMode.Fill)).isEqualTo(period2.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(7), SeekBoundaryMode.Next)).isNull();
  }

  @Test
  public void excludeTest() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));

    DateAdd dateAdd = new DateAdd();
    dateAdd.getExcludePeriods().add(period);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(2))).isEqualTo(test.plusDays(2));
    assertThat(dateAdd.add(test, Durations.dayOf(3))).isEqualTo(period.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(3, 0, 0, 0, 1))).isEqualTo(period.getEnd().plusMillis(1));
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isEqualTo(period.getEnd().plusDays(2));
  }

  @Test
  public void excludeSplit() {
    DateTime test = asDate(2011, 4, 12);
    ITimePeriod period1 = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));
    ITimePeriod period2 = new TimeRange(asDate(2011, 4, 22), asDate(2011, 4, 25));

    DateAdd dateAdd = new DateAdd();
    dateAdd.getExcludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(2))).isEqualTo(test.plusDays(2));
    assertThat(dateAdd.add(test, Durations.dayOf(3))).isEqualTo(period1.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(4))).isEqualTo(period1.getEnd().plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isEqualTo(period2.getEnd());
    assertThat(dateAdd.add(test, Durations.dayOf(6))).isEqualTo(period2.getEnd().plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(7))).isEqualTo(period2.getEnd().plusDays(2));
  }

  @Test
  public void includeEqualsExclude() {
    DateTime test = asDate(2011, 3, 5);
    ITimePeriod period1 = new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 10));
    ITimePeriod period2 = new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 10));

    DateAdd dateAdd = new DateAdd();
    dateAdd.getIncludePeriods().add(period1);
    dateAdd.getExcludePeriods().add(period2);

    assertThat(dateAdd.add(test, Durations.Zero)).isNull();
    assertThat(dateAdd.add(test, Durations.millisOf(1))).isNull();
    assertThat(dateAdd.add(test, Durations.millisOf(-1))).isNull();

    assertThat(dateAdd.subtract(test, Durations.Zero)).isNull();
    assertThat(dateAdd.subtract(test, Durations.millisOf(1))).isNull();
    assertThat(dateAdd.subtract(test, Durations.millisOf(-1))).isNull();
  }

  @Test
  public void includeExclude() {
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 17), asDate(2011, 4, 20)));

    // setup some periods to exclude
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 22), asDate(2011, 3, 25)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 4, 1), asDate(2011, 4, 7)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 16)));

    // positive
    DateTime periodStart = asDate(2011, 3, 19);

    assertThat(dateAdd.add(periodStart, Durations.Hour)).isEqualTo(periodStart.plusHours(1));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(4))).isEqualTo(asDate(2011, 3, 26));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(17))).isEqualTo(asDate(2011, 4, 14));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(20))).isEqualTo(asDate(2011, 4, 18));

    assertThat(dateAdd.add(periodStart, Durations.dayOf(22), SeekBoundaryMode.Fill)).isEqualTo(asDate(2011, 4, 20));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(22), SeekBoundaryMode.Next)).isNull();
    assertThat(dateAdd.add(periodStart, Durations.dayOf(22))).isNull();

    // negative
    DateTime periodEnd = asDate(2011, 4, 18);
    assertThat(dateAdd.add(periodEnd, Durations.hourOf(-1))).isEqualTo(periodEnd.plusHours(-1));
//    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-4))).isEqualTo(asDate(2011, 4, 13));
//    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-17))).isEqualTo(asDate(2011, 3, 22));
//    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-20))).isEqualTo(asDate(2011, 3, 19));
//    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-22), SeekBoundaryMode.Fill)).isEqualTo(asDate(2011, 3, 17));
//    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-22), SeekBoundaryMode.Next)).isNull();
//    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-22))).isNull();
  }

  @Test
  public void includeExclude2() {
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 5)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 10)));
    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 15)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 15), asDate(2011, 3, 20)));
    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 20), asDate(2011, 3, 25)));

    DateTime periodStart = asDate(2011, 3, 1);
    DateTime periodEnd = asDate(2011, 3, 25);

    // add from start
    assertThat(dateAdd.add(periodStart, Durations.Zero)).isEqualTo(periodStart);
    assertThat(dateAdd.add(periodStart, Durations.dayOf(1))).isEqualTo(asDate(2011, 3, 2));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(3))).isEqualTo(asDate(2011, 3, 4));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(4))).isEqualTo(asDate(2011, 3, 10));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(5))).isEqualTo(asDate(2011, 3, 11));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(8))).isEqualTo(asDate(2011, 3, 14));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(9))).isEqualTo(asDate(2011, 3, 20));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(10))).isEqualTo(asDate(2011, 3, 21));

    assertThat(dateAdd.add(periodStart, Durations.dayOf(14), SeekBoundaryMode.Fill)).isEqualTo(asDate(2011, 3, 25));
    assertThat(dateAdd.add(periodStart, Durations.dayOf(14), SeekBoundaryMode.Next)).isNull();
    assertThat(dateAdd.add(periodStart, Durations.dayOf(14))).isNull();

    // add from endInclusive
    assertThat(dateAdd.add(periodEnd, Durations.Zero)).isEqualTo(periodEnd);
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-1))).isEqualTo(periodEnd.plusDays(-1));
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-5))).isEqualTo(asDate(2011, 3, 15));
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-6))).isEqualTo(asDate(2011, 3, 14));
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-10))).isEqualTo(asDate(2011, 3, 5));
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-11))).isEqualTo(asDate(2011, 3, 4));

    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-14), SeekBoundaryMode.Fill)).isEqualTo(asDate(2011, 3, 1));
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-14), SeekBoundaryMode.Next)).isNull();
    assertThat(dateAdd.add(periodEnd, Durations.dayOf(-14))).isNull();

    // subtract from endInclusive
    assertThat(dateAdd.subtract(periodEnd, Durations.Zero)).isEqualTo(periodEnd);
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(1))).isEqualTo(periodEnd.plusDays(-1));
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(5))).isEqualTo(asDate(2011, 3, 15));
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(6))).isEqualTo(asDate(2011, 3, 14));
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(10))).isEqualTo(asDate(2011, 3, 5));
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(11))).isEqualTo(asDate(2011, 3, 4));

    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(14), SeekBoundaryMode.Fill)).isEqualTo(asDate(2011, 3, 1));
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(14), SeekBoundaryMode.Next)).isNull();
    assertThat(dateAdd.subtract(periodEnd, Durations.dayOf(14))).isNull();

    // subtract from start
    assertThat(dateAdd.subtract(periodStart, Durations.Zero)).isEqualTo(periodStart);
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-1))).isEqualTo(asDate(2011, 3, 2));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-3))).isEqualTo(asDate(2011, 3, 4));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-4))).isEqualTo(asDate(2011, 3, 10));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-5))).isEqualTo(asDate(2011, 3, 11));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-8))).isEqualTo(asDate(2011, 3, 14));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-9))).isEqualTo(asDate(2011, 3, 20));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-10))).isEqualTo(asDate(2011, 3, 21));

    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-14), SeekBoundaryMode.Fill)).isEqualTo(asDate(2011, 3, 25));
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-14), SeekBoundaryMode.Next)).isNull();
    assertThat(dateAdd.subtract(periodStart, Durations.dayOf(-14))).isNull();
  }

  @Test
  public void includeExclude3() {
    DateAdd dateAdd = new DateAdd();

    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 10)));
    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 15)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 15), asDate(2011, 3, 20)));

    DateTime test = asDate(2011, 3, 10);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(test.plusDays(1));
    assertThat(dateAdd.add(test, Durations.dayOf(5), SeekBoundaryMode.Fill)).isEqualTo(test.plusDays(5));
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isNull();
  }

  @Test
  public void includeExclude4() {
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 20)));

    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 15)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 15), asDate(2011, 3, 20)));

    DateTime test = asDate(2011, 3, 10);

    assertThat(dateAdd.add(test, Durations.Zero)).isNull();
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isNull();
    assertThat(dateAdd.add(test, Durations.dayOf(5))).isNull();
  }

  @Test
  public void includeExclude5() {
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 20)));

    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 15)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 15), asDate(2011, 3, 30)));

    DateTime test = asDate(2011, 3, 10);

    assertThat(dateAdd.add(test, Durations.Zero)).isNull();
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isNull();
    assertThat(dateAdd.add(test, Durations.dayOf(-1))).isNull();

    assertThat(dateAdd.subtract(test, Durations.Zero)).isNull();
    assertThat(dateAdd.subtract(test, Durations.dayOf(1))).isNull();
    assertThat(dateAdd.subtract(test, Durations.dayOf(-1))).isNull();
  }

  @Test
  public void includeExclude6() {
    DateAdd dateAdd = new DateAdd();

    dateAdd.getIncludePeriods().add(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 20)));

    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 12)));
    dateAdd.getExcludePeriods().add(new TimeRange(asDate(2011, 3, 18), asDate(2011, 3, 30)));

    DateTime test = asDate(2011, 3, 10);

    assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(asDate(2011, 3, 12));
    assertThat(dateAdd.add(test, Durations.dayOf(1))).isEqualTo(asDate(2011, 3, 13));
  }
}
