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

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.DayOfWeek;
import debop4k.timeperiod.models.Month;
import debop4k.timeperiod.models.Timepart;
import debop4k.timeperiod.timeranges.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.timeperiod.utils.Times.asDate;
import static debop4k.timeperiod.utils.Times.asDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CalendarPeriodCollectorTest extends AbstractTimePeriodTest {

  @Test
  public void collectYearsTest() {
    CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

    filter.getYears().add(2006);
    filter.getYears().add(2007);
    filter.getYears().add(2012);

    ITimePeriod limits = CalendarTimeRange.of(asDate(2001, 1, 1), asDate(2019, 12, 31));
    CalendarPeriodCollector collector = CalendarPeriodCollector.of(filter, limits);

    collector.collectYears();

    log.trace("Collect yearsView... periods=[{}]", collector.getPeriods());

    for (int i = 0; i < collector.getPeriods().size(); i++) {
      ITimePeriod period = collector.getPeriods().get(i);
      log.trace("period=[{}]", period);
      assertThat(period.isSamePeriod(YearRange.of((int) filter.getYears().get(i)))).isTrue();
    }
  }

  @Test
  public void collectMonthsTest() {
    CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

    filter.getMonthOfYears().add(Month.January.getValue());

    ITimePeriod limits = CalendarTimeRange.of(asDate(2010, 1, 1), asDate(2011, 12, 31));
    CalendarPeriodCollector collector = CalendarPeriodCollector.of(filter, limits);

    collector.collectMonths();

    log.trace("Collect monthsView... periods=[{}]", collector.getPeriods());

    assertThat(collector.getPeriods().size()).isEqualTo(2);
    assertThat(collector.getPeriods().get(0).isSamePeriod(new MonthRange(2010, 1))).isTrue();
    assertThat(collector.getPeriods().get(1).isSamePeriod(new MonthRange(2011, 1))).isTrue();
  }

  @Test
  public void collectDaysTest() {
    CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

    // 1월의 금요일만 추출
    filter.getMonthOfYears().add(Month.January.getValue());
    filter.getWeekOfDays().add(DayOfWeek.FRIDAY);

    ITimePeriod limits = CalendarTimeRange.of(asDate(2010, 1, 1), asDate(2011, 12, 31));
    CalendarPeriodCollector collector = CalendarPeriodCollector.of(filter, limits);

    collector.collectDays();


    for (ITimePeriod period : collector.getPeriods()) {
      log.trace("Day=[{}]", period);
    }

    assertThat(collector.getPeriods().size()).isEqualTo(9);

    assertThat(collector.getPeriods().get(0).isSamePeriod(new DayRange(2010, 1, 1))).isTrue();
    assertThat(collector.getPeriods().get(1).isSamePeriod(new DayRange(2010, 1, 8))).isTrue();
    assertThat(collector.getPeriods().get(2).isSamePeriod(new DayRange(2010, 1, 15))).isTrue();
    assertThat(collector.getPeriods().get(3).isSamePeriod(new DayRange(2010, 1, 22))).isTrue();
    assertThat(collector.getPeriods().get(4).isSamePeriod(new DayRange(2010, 1, 29))).isTrue();

    assertThat(collector.getPeriods().get(5).isSamePeriod(new DayRange(2011, 1, 7))).isTrue();
    assertThat(collector.getPeriods().get(6).isSamePeriod(new DayRange(2011, 1, 14))).isTrue();
    assertThat(collector.getPeriods().get(7).isSamePeriod(new DayRange(2011, 1, 21))).isTrue();
    assertThat(collector.getPeriods().get(8).isSamePeriod(new DayRange(2011, 1, 28))).isTrue();
  }

  @Test
  public void collectHoursTest() {
    CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

    // 1월의 금요일의 08:00~18:00 추출
    filter.getMonthOfYears().add(Month.January.getValue());
    filter.getWeekOfDays().add(DayOfWeek.FRIDAY);
    filter.getCollectingHours().add(HourRangeInDay.of(8, 18));

    ITimePeriod limits = CalendarTimeRange.of(asDate(2010, 1, 1), asDate(2011, 12, 31));
    CalendarPeriodCollector collector = CalendarPeriodCollector.of(filter, limits);

    collector.collectHours();

    for (ITimePeriod period : collector.getPeriods()) {
      log.trace("Hours=[{}]", period);
    }

    assertThat(collector.getPeriods().size()).isEqualTo(9);

    assertThat(collector.getPeriods().get(0).isSamePeriod(HourRangeCollection.of(2010, 1, 1, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(1).isSamePeriod(HourRangeCollection.of(2010, 1, 8, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(2).isSamePeriod(HourRangeCollection.of(2010, 1, 15, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(3).isSamePeriod(HourRangeCollection.of(2010, 1, 22, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(4).isSamePeriod(HourRangeCollection.of(2010, 1, 29, 8, 10))).isTrue();

    assertThat(collector.getPeriods().get(5).isSamePeriod(HourRangeCollection.of(2011, 1, 7, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(6).isSamePeriod(HourRangeCollection.of(2011, 1, 14, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(7).isSamePeriod(HourRangeCollection.of(2011, 1, 21, 8, 10))).isTrue();
    assertThat(collector.getPeriods().get(8).isSamePeriod(HourRangeCollection.of(2011, 1, 28, 8, 10))).isTrue();
  }

  @Test
  public void collectHoursWithMinutes() {
    CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

    // 1월의 금요일의 08:30~18:30 추출
    filter.getMonthOfYears().add(Month.January.getValue());
    filter.getWeekOfDays().add(DayOfWeek.FRIDAY);
    filter.getCollectingHours().add(new HourRangeInDay(Timepart.of(8, 30), Timepart.of(18, 50)));

    ITimePeriod limits = CalendarTimeRange.of(asDate(2010, 1, 1), asDate(2011, 12, 31));
    CalendarPeriodCollector collector = CalendarPeriodCollector.of(filter, limits);

    collector.collectHours();

    for (ITimePeriod period : collector.getPeriods()) {
      log.trace("Hours=[{}]", period);
    }

    assertThat(collector.getPeriods().size()).isEqualTo(9);

    assertThat(collector.getPeriods().get(0)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2010, 1, 1, 8, 30),
                                                           asDateTime(2010, 1, 1, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(1)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2010, 1, 8, 8, 30),
                                                           asDateTime(2010, 1, 8, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(2)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2010, 1, 15, 8, 30),
                                                           asDateTime(2010, 1, 15, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(3)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2010, 1, 22, 8, 30),
                                                           asDateTime(2010, 1, 22, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(4)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2010, 1, 29, 8, 30),
                                                           asDateTime(2010, 1, 29, 18, 50)))).isTrue();

    assertThat(collector.getPeriods().get(5)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2011, 1, 7, 8, 30),
                                                           asDateTime(2011, 1, 7, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(6)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2011, 1, 14, 8, 30),
                                                           asDateTime(2011, 1, 14, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(7)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2011, 1, 21, 8, 30),
                                                           asDateTime(2011, 1, 21, 18, 50)))).isTrue();
    assertThat(collector.getPeriods().get(8)
                        .isSamePeriod(CalendarTimeRange.of(asDateTime(2011, 1, 28, 8, 30),
                                                           asDateTime(2011, 1, 28, 18, 50)))).isTrue();
  }

  @Test
  // @Ignore("CalendarPeriodCollector 가 부정기적으로 계산이 틀리다... 병렬 수행에서 문제가 있는 듯 하다.")
  public void collectExcludePeriod() {

    final int workingDays2011 = 365 - 2 - (51 * 2) - 1;
    final int workingDaysMarch2011 = 31 - 8; // total daysView - weekend daysView

    YearRange year2011 = YearRange.of(2011);

    CalendarPeriodCollectorFilter filter1 = new CalendarPeriodCollectorFilter();
    filter1.addWorkingWeekdays();

    // FIXME: 여기서 예외가 부정기적으로 발생한다. 아마 CalendarPeriodCollector가 병렬로 작업해서 그런 것 같다.
    CalendarPeriodCollector collector1 = CalendarPeriodCollector.of(filter1, year2011);
    collector1.collectDays();
    assertThat(collector1.getPeriods().size()).isEqualTo(workingDays2011);

    // 3월 제외 (23일 제외)
    CalendarPeriodCollectorFilter filter2 = new CalendarPeriodCollectorFilter();
    filter2.addWorkingWeekdays();
    filter2.getExcludePeriods().add(new MonthRange(2011, 3));

    CalendarPeriodCollector collector2 = CalendarPeriodCollector.of(filter2, year2011);
    collector2.collectDays();
    assertThat(collector2.getPeriods().size()).isEqualTo(workingDays2011 - workingDaysMarch2011);


    // 2011 년 26주차 ~ 27주차 (여름휴가)
    CalendarPeriodCollectorFilter filter3 = CalendarPeriodCollectorFilter.of();
    filter3.addWorkingWeekdays();
    filter3.getExcludePeriods().add(new MonthRange(2011, 3));
    filter3.getExcludePeriods().add(WeekRangeCollection.of(2011, 26, 2));

    CalendarPeriodCollector collector3 = CalendarPeriodCollector.of(filter3, year2011);
    collector3.collectDays();
    assertThat(collector3.getPeriods().size())
        .isEqualTo(workingDays2011 - workingDaysMarch2011 - 2 * TimeSpec.WeekDaysPerWeek);
  }
}
