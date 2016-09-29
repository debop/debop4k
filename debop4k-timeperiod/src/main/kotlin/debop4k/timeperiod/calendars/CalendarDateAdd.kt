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

package debop4k.timeperiod.calendars

import debop4k.timeperiod.*
import debop4k.timeperiod.models.DayOfWeek
import debop4k.timeperiod.timelines.TimeGapCalculator
import debop4k.timeperiod.timeranges.DayOfWeekHourRange
import debop4k.timeperiod.timeranges.HourRangeInDay
import debop4k.timeperiod.timeranges.WeekRange
import org.eclipse.collections.api.block.procedure.Procedure
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

/**
 * 특정 일자로부터 미래로(Forward)로 특정 기간을 이후의 일자를 계산합니다.
 * Calendar 의 휴일 등을 제외한 특정 기간 산정을 탐색을 통해 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
open class CalendarDateAdd : DateAdd() {

  private val log = LoggerFactory.getLogger(javaClass)

  val calendar: TimeCalendar = TimeCalendar.EMPTY_OFFSET
  val weekDays: FastList<DayOfWeek> = FastList.newList<DayOfWeek>()
  val workingHours: FastList<HourRangeInDay> = FastList.newList<HourRangeInDay>()
  val workingDayHours: FastList<DayOfWeekHourRange> = FastList.newList<DayOfWeekHourRange>()

  override val includePeriods: TimePeriodCollection
    get() = throw UnsupportedOperationException("IncludePeriods는 지원하지 않습니다.")

  fun addWorkingWeekdays(): Unit {
    addWeekdays(*Weekdays)
  }

  fun addWeekendWeekdays(): Unit {
    addWeekdays(*Weekends)
  }

  fun addWeekdays(vararg dayOfWeeks: DayOfWeek) {
    weekDays.addAll(dayOfWeeks.asList())
  }

  override fun add(start: DateTime, offset: Duration, seekBoundary: SeekBoundaryMode): DateTime? {
    log.trace("add... start={}, offset={}, seekBoundary={}", start, offset, seekBoundary)

    if (weekDays.isEmpty && excludePeriods.isEmpty() && workingHours.isEmpty) {
      return start.plus(offset)
    }

    val pair =
        if (offset.millis < 0)
          calculateEnd(start, offset.negated(), SeekDirection.Backward, seekBoundary)
        else
          calculateEnd(start, offset, SeekDirection.Forward, seekBoundary)

    log.trace("add... end={}", pair.first)
    return pair.first
  }

  override fun substract(start: DateTime, offset: Duration, seekBoundary: SeekBoundaryMode): DateTime? {
    log.trace("subtract... start={}, offset={}, seekBoundary={}", start, offset, seekBoundary)

    if (weekDays.isEmpty && excludePeriods.isEmpty() && workingHours.isEmpty) {
      return start.minus(offset)
    }

    val pair = if (offset.millis < 0)
      calculateEnd(start, offset.negated(), SeekDirection.Forward, seekBoundary)
    else
      calculateEnd(start, offset, SeekDirection.Backward, seekBoundary)

    log.trace("subtract... end={}", pair.first)
    return pair.first
  }

  override fun calculateEnd(start: DateTime,
                            offset: Duration?,
                            seekDir: SeekDirection,
                            seekBoundary: SeekBoundaryMode): Pair<DateTime?, Duration?> {
    log.trace("기준 시각으로부터 offset 만큼 떨어진 시각을 구합니다..." + "start={}, offset={}, seekDir={}, seekBoundary={}",
              start, offset, seekDir, seekBoundary)

    require((offset?.millis ?: 0L) >= 0L) { "offset 값은 0 이상이어야 합니다. offset=$offset" }

    var moment = start
    var end: DateTime? = null
    var remaining: Duration? = offset

    var week: WeekRange? = WeekRange(start, calendar)

    while (week != null) {
      includePeriods.clear()
      includePeriods.addAll(getAvailableWeekPeriods(week))

      log.trace("가능한 기간={}", includePeriods)

      val result = super.calculateEnd(moment, remaining, seekDir, seekBoundary)
      end = result.first
      remaining = result.second

      log.trace("완료기간을 구했습니다. end={}, remaining={}", end, remaining)

      if (end != null || remaining == null) {
        log.trace("결과. end={}, remaining={}", end, remaining)
        return Pair(end, remaining)
      }

      if (seekDir === SeekDirection.Forward) {
        week = findNextWeek(week)
        if (week != null)
          moment = week.start
      } else {
        week = findPrevWeek(week)
        if (week != null)
          moment = week.end
      }
    }

    log.trace("결과. end={}, remaining={}", end, remaining)
    return Pair(end, remaining)
  }

  private fun findNextWeek(current: WeekRange): WeekRange? {
    log.trace("current week={}의 이후 week 기간을 구합니다...")

    val next: WeekRange?

    if (excludePeriods.isEmpty()) {
      next = current.nextWeek
    } else {
      val limits = TimeRange(current.end.plusMillis(1))
      val gapCalculator = TimeGapCalculator<TimeRange>(calendar)
      val remainingPeriods = gapCalculator.gaps(excludePeriods, limits)

      if (remainingPeriods.size > 0) {
        next = WeekRange(remainingPeriods[0].start, calendar)
      } else {
        next = null
      }
    }

    log.trace("current week={} 이후 week={}", current, next)
    return next
  }

  private fun findPrevWeek(current: WeekRange): WeekRange? {
    log.trace("current week={}의 이전 week 기간을 구합니다...")

    val prev: WeekRange?

    if (excludePeriods.isEmpty()) {
      prev = current.prevWeek
    } else {
      val limits = TimeRange(end = current.start.minusMillis(1))
      val gapCalculator = TimeGapCalculator<TimeRange>(calendar)
      val remainingPeriods = gapCalculator.gaps(excludePeriods, limits)

      if (remainingPeriods.size > 0) {
        prev = WeekRange(remainingPeriods[remainingPeriods.size - 1].end, calendar)
      } else {
        prev = null
      }
    }

    log.trace("current week={} 이전 week={}", current, prev)
    return prev
  }

  private fun getAvailableWeekPeriods(limits: ITimePeriod): List<ITimePeriod> {
    log.trace("가능한 주간 기간을 추출합니다. limits={}", limits)

    if (weekDays.isEmpty && workingHours.isEmpty && workingDayHours.isEmpty) {
      return TimePeriodCollection.of(limits)
    }

    val filter = CalendarPeriodCollectorFilter()

    weekDays.forEach(Procedure<debop4k.timeperiod.models.DayOfWeek> { each -> filter.addWeekdays(each) })

    filter.collectingHours.addAll(workingHours)
    filter.collectingDayHours.addAll(workingDayHours)

    val weekCollector = CalendarPeriodCollector(filter, limits, SeekDirection.Forward, calendar)
    weekCollector.collectHours()

    log.trace("availableWeekPeriod={}", weekCollector.periods)

    return weekCollector.periods
  }

}