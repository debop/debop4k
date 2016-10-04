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

import debop4k.core.collections.permutations.Permutation;
import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.PeriodUnit;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static debop4k.core.kodatimes.KodaTimes.asDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class TimesForEachTest extends AbstractTimePeriodTest {

  private static final Logger log = LoggerFactory.getLogger(TimesForEachTest.class);

  private DateTime startTime = asDateTime(2008, 4, 10, 5, 33, 24, 345);
  private DateTime endTime = asDateTime(2009, 10, 20, 13, 43, 12, 599);

  private TimeRange period = new TimeRange(startTime, endTime);

  List<PeriodUnit> excludeUnits = Arrays.asList(PeriodUnit.ALL,
                                                PeriodUnit.MINUTE,
                                                PeriodUnit.SECOND,
                                                PeriodUnit.MILLISECOND);

  @Test
  public void foreachYearsTest() {
    int count = 0;
    for (ITimePeriod p : Periods.yearRangeSequence(period)) {
      log.debug("year [{}] = [{}]", count++, p.getStart().getYear());
    }
    assertThat(count).isEqualTo(period.getEnd().getYear() - period.getStart().getYear() + 1);
  }

  @Test
  public void testYearPermutation() {
    int count = 0;
    for (ITimePeriod p : Periods.yearPermutation(period)) {
      log.trace("year [{}] = [{}]", count++, p.getStart().getYear());
    }
    assertThat(count).isEqualTo(period.getEnd().getYear() - period.getStart().getYear() + 1);
  }

  @Test
  public void foreachYearsInSameYearTest() throws Exception {
    ITimePeriod period = Periods.relativeWeekPeriod(startTime, 1);

    FastList<ITimePeriod> years = Periods.yearRangeSequence(period);
    assertThat(years.size()).isEqualTo(1);
    assertThat(years.getFirst()).isEqualTo(period);
  }

  @Test
  public void testSameYearPermutation() {
    ITimePeriod period = Periods.relativeWeekPeriod(startTime, 1);

    Permutation<ITimePeriod> years = Periods.yearPermutation(period);
    assertThat(years.size()).isEqualTo(1);
    assertThat(years.getHead()).isEqualTo(period);
  }

  @Test
  //@Ignore
  public void foreachMonthsTest() {
    int count = 0;
    for (ITimePeriod p : Periods.monthRangeSequence(period)) {
      log.trace("month [{}] = [{}]", count++, p.getStart().getMonthOfYear());
    }

    int months = (int) (period.getDuration().getMillis() / (TimeSpec.MaxDaysPerMonth * TimeSpec.MillisPerDay)) + 1;
    assertThat(count).isEqualTo(months);
  }

  @Test
  //@Ignore
  public void testMonthPermutation() {
    int count = 0;
    for (ITimePeriod p : Periods.monthPermutation(period)) {
      log.trace("month [{}] = [{}]", count++, p.getStart().getMonthOfYear());
    }
    int months = (int) (period.getDuration().getMillis() / (TimeSpec.MaxDaysPerMonth * TimeSpec.MillisPerDay)) + 1;
    assertThat(count).isEqualTo(months);
  }

  @Test
  public void foreachWeeksTest() {
    int count = 0;
    DateTimeFormatter shortDate = DateTimeFormat.shortDate();
    MutableList<ITimePeriod> weeks = Periods.weekRangeSequence(period);

    for (ITimePeriod p : weeks) {
      log.trace("week[{}] = [{}]~[{}], WeekOfYear=({},{})",
                count++, shortDate.print(p.getStart()),
                shortDate.print(p.getEnd()), p.getStart().getWeekyear(), p.getStart().getWeekOfWeekyear());
    }

    assertThat(weeks.get(0).getStart()).isEqualTo(period.getStart());
    assertThat(weeks.get(weeks.size() - 1).getEnd()).isEqualTo(period.getEnd());
  }

  @Test
  public void testWeekPermutation() {
    int count = 0;
    DateTimeFormatter shortDate = DateTimeFormat.shortDate();
    Permutation<ITimePeriod> weeks = Periods.weekPermutation(period);

    for (ITimePeriod p : weeks) {
      log.trace("week[{}] = [{}]~[{}], WeekOfYear=({},{})",
                count++, shortDate.print(p.getStart()),
                shortDate.print(p.getEnd()), p.getStart().getWeekyear(), p.getStart().getWeekOfWeekyear());
    }

    assertThat(weeks.get(0).getStart()).isEqualTo(period.getStart());
    assertThat(weeks.get(weeks.size() - 1).getEnd()).isEqualTo(period.getEnd());
  }

  @Test
  public void foreachDaysTest() {
    MutableList<ITimePeriod> days = Periods.dayRangeSequence(period);

    assertThat(days.get(0).getStart()).isEqualTo(period.getStart());
    assertThat(days.get(days.size() - 1).getEnd()).isEqualTo(period.getEnd());

    log.trace("endInclusive-1=[{}]", days.get(days.size() - 2));
    log.trace("endInclusive  =[{}]", days.get(days.size() - 1));
  }

  @Test
  public void testDayPermutation() {
    Permutation<ITimePeriod> days = Periods.dayPermutation(period);

    assertThat(days.get(0).getStart()).isEqualTo(period.getStart());
    assertThat(days.get(days.size() - 1).getEnd()).isEqualTo(period.getEnd());

    log.trace("endInclusive-1=[{}]", days.get(days.size() - 2));
    log.trace("endInclusive  =[{}]", days.get(days.size() - 1));
  }

  @Test
  public void foreachHoursTest() {
    MutableList<ITimePeriod> hours = Periods.hourRangeSequence(period);

    assertThat(hours.get(0).getStart()).isEqualTo(period.getStart());
    assertThat(hours.get(hours.size() - 1).getEnd()).isEqualTo(period.getEnd());
    assertThat(hours.get(hours.size() - 1).getStart().getMillis())
        .isGreaterThan(hours.get(hours.size() - 2).getEnd().getMillis());
  }

  @Test
  public void testHourPermutation() {
    Permutation<ITimePeriod> hours = Periods.hourPermutation(period);

    assertThat(hours.get(0).getStart()).isEqualTo(period.getStart());
//    assertThat(hours.get(hours.size() - 1).getEnd()).isEqualTo(period.getEnd());
//    assertThat(hours.get(hours.size() - 1).getStart().getMillis())
//        .isGreaterThan(hours.get(hours.size() - 2).getEnd().getMillis());
  }

  @Test
  public void foreachMinuteTest() {
    MutableList<ITimePeriod> minutes = Periods.minuteRangeSequence(period);

    assertThat(minutes.get(0).getStart()).isEqualTo(period.getStart());
    assertThat(minutes.get(minutes.size() - 1).getEnd()).isEqualTo(period.getEnd());
    assertThat(minutes.get(minutes.size() - 1).getStart().getMillis())
        .isGreaterThan(minutes.get(minutes.size() - 2).getEnd().getMillis());
  }

  @Test
  public void testMinutePermutation() {
    Permutation<ITimePeriod> minutes = Periods.minutePermutation(period).take(1000).force();
    assertThat(minutes.get(0).getStart()).isEqualTo(period.getStart());
//    assertThat(minutes.get(minutes.size() - 1).getEnd()).isEqualTo(period.getEnd());
//    assertThat(minutes.get(minutes.size() - 1).getStart().getMillis())
//        .isGreaterThan(minutes.get(minutes.size() - 2).getEnd().getMillis());
  }

  @Test
  public void testMinuteSequences() {
    Sequence<ITimePeriod> minutes = Periods.minuteSequences(period);

    Sequence<ITimePeriod> results = SequencesKt.takeWhile(minutes, x -> x != null);
    Iterator<ITimePeriod> iter = results.iterator();
    while (iter.hasNext()) {
      log.trace("next={}", iter.next());
    }
  }

  @Test
  public void foreachPeriodsTest() {

    for (PeriodUnit periodUnit : PeriodUnit.values()) {
      if (periodUnit == PeriodUnit.ALL ||
          periodUnit == PeriodUnit.SECOND ||
          periodUnit == PeriodUnit.MILLISECOND)
        continue;

      int count = 0;
      for (ITimePeriod p : Periods.periodList(period, periodUnit)) {
        count++;
        if (count == 1000)
          break;
      }
    }
  }

  @Test
  public void testPeriodPermutation() {

    for (PeriodUnit periodUnit : PeriodUnit.values()) {
      if (periodUnit == PeriodUnit.ALL ||
          periodUnit == PeriodUnit.SECOND ||
          periodUnit == PeriodUnit.MILLISECOND)
        continue;

      int count = 0;
      for (ITimePeriod p : Periods.permutations(period, periodUnit)) {
        count++;
        if (count == 1000)
          break;
      }
    }
  }

  @Test
  public void runPeriodTest() {

    for (PeriodUnit unit : PeriodUnit.values()) {
      if (!excludeUnits.contains(unit)) {
        final int[] count = {0};
        List<Integer> results = Periods.periodList(period, unit)
                                       .collect(new Function<ITimePeriod, Integer>() {
                                         @Override
                                         public Integer valueOf(ITimePeriod p) {
                                           return count[0]++;
                                         }
                                       });

        assertThat(results).hasSize(count[0]);
        log.debug("unit={}, results={}", unit, results.size());
      }
    }
  }

  @Test
  public void testRunPermutations() {

    for (PeriodUnit unit : PeriodUnit.values()) {
      if (!excludeUnits.contains(unit)) {
        log.debug("unit={}", unit);
        final int[] count = {0};
        List<Integer> results = Periods.permutations(period, unit)
                                       .map(new Function1<ITimePeriod, Integer>() {
                                         @Override
                                         public Integer invoke(ITimePeriod iTimePeriod) {
                                           return ++count[0];
                                         }
                                       }).take(1000)
                                       .force();

        assertThat(results).hasSize(count[0]);
        log.debug("unit={}, results={}", unit, results.size());
      }
    }
  }

  @Test
  public void runPeriodAsParallelTest() {

    final AtomicInteger count = new AtomicInteger(0);

    Function<ITimePeriod, Integer> increment = new Function<ITimePeriod, Integer>() {
      @Override
      public Integer valueOf(ITimePeriod p) {
        return count.getAndIncrement();
      }
    };

    for (PeriodUnit unit : PeriodUnit.values()) {
      if (!excludeUnits.contains(unit)) {
        count.set(0);
        List<Integer> results = Periods.parMapPeriod(period, unit, p -> count.getAndIncrement());

        assertThat(results.size()).isEqualTo(count.get());
      }
    }
  }

  public TimeRange getPeriod() {return this.period;}
}
