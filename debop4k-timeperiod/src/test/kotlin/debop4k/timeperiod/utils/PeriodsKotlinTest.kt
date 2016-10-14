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

package debop4k.timeperiod.utils

import debop4k.core.kodatimes.asDateTime
import debop4k.timeperiod.*
import debop4k.timeperiod.models.PeriodUnit
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.format.DateTimeFormat
import org.junit.Test
import java.util.*
import java.util.concurrent.atomic.*

class PeriodsKotlinTest : AbstractTimePeriodKotlinTest() {

  private val startTime = asDateTime(2008, 4, 10, 5, 33, 24, 345)
  private val endTime = asDateTime(2009, 10, 20, 13, 43, 12, 599)

  private val period = TimeRange(startTime, endTime)


  private val excludeUnits = Arrays.asList(PeriodUnit.ALL,
                                           PeriodUnit.MINUTE,
                                           PeriodUnit.SECOND,
                                           PeriodUnit.MILLISECOND)

  @Test
  fun testYearSequences() {
    var count = 0
    for (p in period.yearSequences()) {
      log.debug("year [{}] = [{}]", count++, p.start.year)
    }
    assertThat(count).isEqualTo(period.end.year - period.start.year + 1)
  }

  @Test
  fun testSameSequence() {
    val period = startTime.relativeWeekPeriod(1)

    val years = period.yearSequences().toList()
    assertThat(years.size).isEqualTo(1)
    assertThat(years.first()).isEqualTo(period)
  }

  @Test
      //@Ignore
  fun testMonthSequences() {
    var count = 0
    for (p in period.monthSequences()) {
      log.trace("month [{}] = [{}]", count++, p.start.monthOfYear)
    }
    val months = (period.duration.millis / (MaxDaysPerMonth * MillisPerDay)).toInt() + 1
    assertThat(count).isEqualTo(months)
  }

  @Test
  fun testWeekSequences() {
    var count = 0
    val shortDate = DateTimeFormat.shortDate()
    val weeks = period.weekSequences().toList()

    for (p in weeks) {
      log.trace("week[{}] = [{}]~[{}], WeekOfYear=({},{})",
                count++, shortDate.print(p.start),
                shortDate.print(p.end), p.start.weekyear, p.start.weekOfWeekyear)
    }

    assertThat(weeks[0].start).isEqualTo(period.start)
    assertThat(weeks[weeks.size - 1].end).isEqualTo(period.end)
  }

  @Test
  fun testDayPermutation() {
    val days = period.daySequences().toList()

    assertThat(days[0].start).isEqualTo(period.start)
    assertThat(days[days.size - 1].end).isEqualTo(period.end)

    log.trace("endInclusive-1=[{}]", days[days.size - 2])
    log.trace("endInclusive  =[{}]", days[days.size - 1])
  }

  @Test
  fun testHourSequences() {
    val hours = period.hourSequences().toList()

    assertThat(hours[0].start).isEqualTo(period.start)
    assertThat(hours[hours.size - 1].end).isEqualTo(period.end)
    assertThat(hours[hours.size - 1].start.millis).isGreaterThan(hours[hours.size - 2].end.millis)
  }

  @Test
  fun foreachMinuteTest() {
    val minutes = period.minuteRangeSequence()

    assertThat(minutes[0].start).isEqualTo(period.start)
    assertThat(minutes[minutes.size - 1].end).isEqualTo(period.end)
    assertThat(minutes[minutes.size - 1].start.millis).isGreaterThan(minutes[minutes.size - 2].end.millis)
  }

  @Test
  fun testMinuteSequences() {
    val minutes = period.minuteSequences().toList()

    assertThat(minutes[0].start).isEqualTo(period.start)
    assertThat(minutes[minutes.size - 1].end).isEqualTo(period.end)
    assertThat(minutes[minutes.size - 1].start.millis).isGreaterThan(minutes[minutes.size - 2].end.millis)
  }


  @Test
  fun testPeriodSequences() {

    for (periodUnit in PeriodUnit.values()) {
      if (periodUnit === PeriodUnit.ALL ||
          periodUnit === PeriodUnit.SECOND ||
          periodUnit === PeriodUnit.MILLISECOND)
        continue

      var count = 0
      for (p in period.sequences(periodUnit)) {
        count++
        if (count == 1000)
          break
      }
    }
  }

  @Test
  fun testSequences() {

    for (unit in PeriodUnit.values()) {
      if (!excludeUnits.contains(unit)) {
        val count = intArrayOf(0)
        val results = period.sequences(unit).map { count[0]++ }.toList()

        assertThat(results).hasSize(count[0])
        log.debug("unit={}, results={}", unit, results.size)
      }
    }
  }

  @Test
  fun runPeriodAsParallelTest() {

    val count = AtomicInteger(0)

    val increment: (ITimePeriod) -> Int = { count.andIncrement }

    for (unit in PeriodUnit.values()) {
      if (!excludeUnits.contains(unit)) {
        count.set(0)
        val results = period.parMapPeriod(unit) { increment(it) }

        assertThat(results.size).isEqualTo(count.get())
      }
    }
  }

  fun getPeriod(): TimeRange {
    return this.period
  }
}