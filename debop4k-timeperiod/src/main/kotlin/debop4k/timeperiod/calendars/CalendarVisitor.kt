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
import debop4k.timeperiod.timeranges.*
import debop4k.timeperiod.utils.hasPureInsideWith
import org.slf4j.LoggerFactory

/**
 * 특정 기간에 대한 필터링 정보를 기반으로 기간들을 필터링 할 수 있도록 특정 기간을 탐색하는 Visitor입니다.
 * Created by debop
 */
abstract class CalendarVisitor<out F : ICalendarVisitorFilter, in C : ICalendarVisitorContext>(
    val filter: F,
    val limits: ITimePeriod,
    val seekDirection: SeekDirection = SeekDirection.Forward,
    val calendar: ITimeCalendar = DefaultTimeCalendar) {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    val MAX_PERIOD = TimeRange(MinPeriodTime, MaxPeriodTime.minusYears(1))
  }

  val isForward: Boolean
    get() = seekDirection == SeekDirection.Forward

  protected fun startPeriodVisit(context: C): Unit {
    startPeriodVisit(limits, context)
  }

  protected fun startPeriodVisit(period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitYears(yearsToVisit: Sequence<YearRange>, period: ITimePeriod, context: C): Unit {
    yearsToVisit.forEach { year ->
      val canVisit = year.overlapsWith(period) && onVisitYear(year, context) && enterMonths(year, context)

      if (canVisit) {
        val monthsToVisit =
            if (isForward) year.monthSequence()
            else year.monthSequence().sortedByDescending { it.end }
        visitMonths(monthsToVisit, period, context)
      }
    }
  }

  private fun visitMonths(monthsToVisit: Sequence<MonthRange>, period: ITimePeriod, context: C): Unit {
    monthsToVisit.forEach { m ->
      val canVisit = m.overlapsWith(period) && onVisitMonth(m, context) && enterDays(m, context)
      if (canVisit) {
        val daysToVisit =
            if (isForward) m.daySequence()
            else m.daySequence().sortedByDescending { it.end }
        visitDays(daysToVisit, period, context)
      }
    }
  }

  private fun visitDays(daysToVisit: Sequence<DayRange>, period: ITimePeriod, context: C): Unit {
    daysToVisit.forEach { day ->
      val canVisit = day.overlapsWith(period) && onVisitDay(day, context) && enterHours(day, context)
      if (canVisit) {
        val hoursToVisit =
            if (isForward) day.hourSequence()
            else day.hourSequence().sortedByDescending { it.end }

        visitHours(hoursToVisit, period, context)
      }
    }
  }

  private fun visitHours(hoursToVisit: Sequence<HourRange>, period: ITimePeriod, context: C): Unit {
    hoursToVisit.forEach { hour ->
      val canVisit = hour.overlapsWith(period) && onVisitHour(hour, context)
      if (canVisit) {
        enterMinutes(hour, context)
      }
    }
  }

  open protected fun startYearVisit(year: YearRange, context: C, direction: SeekDirection): YearRange? {
    onVisitStart()

    val offset = direction.value
    var current = year
    var lastVisited: YearRange? = null

    while (lastVisited == null && MAX_PERIOD.hasPureInsideWith(current)) {
      if (!onVisitYear(current, context)) {
        lastVisited = current
      } else {
        current = current.addYears(offset)
      }
    }
    onVisitEnd()

    log.trace("마지막 탐색 Year. lastVisited={}", lastVisited)
    return lastVisited
  }

  open protected fun startMonthVisit(month: MonthRange, context: C, direction: SeekDirection): MonthRange? {
    onVisitStart()

    val offset = direction.value
    var current = month
    var lastVisited: MonthRange? = null

    while (lastVisited == null && MAX_PERIOD.hasPureInsideWith(current)) {
      if (!onVisitMonth(current, context)) {
        lastVisited = current
      } else {
        current = current.addMonths(offset)
      }
    }
    onVisitEnd()

    log.trace("마지막 탐색 Month. lastVisited={}", lastVisited)
    return lastVisited
  }

  open protected fun startDayVisit(day: DayRange, context: C, direction: SeekDirection): DayRange? {
    onVisitStart()

    val offset = direction.value
    var current = day
    var lastVisited: DayRange? = null

    while (lastVisited == null && current.hasPureInsideWith(MAX_PERIOD)) {
      if (!onVisitDay(current, context)) {
        lastVisited = current
      } else {
        current = current.addDays(offset)
      }
    }
    onVisitEnd()

    log.trace("Day 단위 탐색을 완료했습니다. lastVisited={}", lastVisited)
    return lastVisited
  }

  open protected fun startHourVisit(hour: HourRange, context: C, direction: SeekDirection): HourRange? {
    onVisitStart()

    val offset = direction.value
    var current = hour
    var lastVisited: HourRange? = null

    while (lastVisited == null && current.hasPureInsideWith(MAX_PERIOD)) {
      if (!onVisitHour(current, context)) {
        lastVisited = current
      } else {
        current = current.addHours(offset)
      }
    }
    onVisitEnd()

    log.trace("Hour 단위 탐색 완료. lastVisited={}", lastVisited)
    return lastVisited
  }

  open protected fun onVisitStart(): Unit {
    // Nothing to do.
  }

  open protected fun onVisitEnd(): Unit {
    // Nothing to do.
  }

  protected fun checkLimits(target: ITimePeriod): Boolean {
    return limits.hasInside(target)
  }

  protected fun checkExcludePeriods(target: ITimePeriod): Boolean {
    return filter.excludePeriods.isEmpty()
           || filter.excludePeriods.overlapPeriods(target).isEmpty()
  }

  open protected fun enterYears(years: YearRangeCollection, context: C): Boolean = true
  open protected fun enterMonths(year: YearRange, context: C): Boolean = true
  open protected fun enterDays(month: MonthRange, context: C): Boolean = true
  open protected fun enterHours(day: DayRange, context: C): Boolean = true
  open protected fun enterMinutes(hour: HourRange, context: C): Boolean = true

  open protected fun onVisitYears(years: YearRangeCollection, context: C): Boolean = true
  open protected fun onVisitYear(year: YearRange, context: C): Boolean = true
  open protected fun onVisitMonth(month: MonthRange, context: C): Boolean = true
  open protected fun onVisitDay(day: DayRange, context: C): Boolean = true
  open protected fun onVisitHour(hour: HourRange, context: C): Boolean = true
  open protected fun onVisitMinute(minute: MinuteRange, context: C): Boolean = true

  open protected fun isMatchingYear(yr: YearRange, context: C): Boolean {
    return (filter.years.isEmpty || filter.years.contains(yr.year)) &&
           checkExcludePeriods(yr)
  }

  open protected fun isMatchingMonth(mr: MonthRange, context: C): Boolean {
    if (!filter.years.isEmpty && !filter.years.contains(mr.year))
      return false
    else if (!filter.monthOfYears.isEmpty && !filter.monthOfYears.contains(mr.monthOfYear))
      return false
    else
      return checkExcludePeriods(mr)
  }

  open protected fun isMatchingDay(dr: DayRange, context: C): Boolean {
    if (!filter.years.isEmpty && !filter.years.contains(dr.year))
      return false
    else if (!filter.monthOfYears.isEmpty && !filter.monthOfYears.contains(dr.monthOfYear))
      return false
    else if (!filter.dayOfMonths.isEmpty && !filter.dayOfMonths.contains(dr.dayOfMonth))
      return false
    else if (!filter.weekOfDays.isEmpty && !filter.weekOfDays.contains(dr.dayOfWeek))
      return false
    else
      return checkExcludePeriods(dr)
  }

  open protected fun isMatchingHour(hr: HourRange, context: C): Boolean {
    if (!filter.years.isEmpty && !filter.years.contains(hr.year))
      return false
    else if (!filter.monthOfYears.isEmpty && !filter.monthOfYears.contains(hr.monthOfYear))
      return false
    else if (!filter.dayOfMonths.isEmpty && !filter.dayOfMonths.contains(hr.dayOfMonth))
      return false
    else if (!filter.weekOfDays.isEmpty && !filter.weekOfDays.contains(DayOfWeek.of(hr.start.dayOfWeek)))
      return false
    else if (!filter.hourOfDays.isEmpty && !filter.hourOfDays.contains(hr.hourOfDay))
      return false
    else
      return checkExcludePeriods(hr)
  }

  open protected fun isMatchingMinute(mr: MinuteRange, context: C): Boolean {
    if (!filter.years.isEmpty && !filter.years.contains(mr.year))
      return false
    else if (!filter.monthOfYears.isEmpty && !filter.monthOfYears.contains(mr.monthOfYear))
      return false
    else if (!filter.dayOfMonths.isEmpty && !filter.dayOfMonths.contains(mr.dayOfMonth))
      return false
    else if (!filter.weekOfDays.isEmpty && !filter.weekOfDays.contains(DayOfWeek.of(mr.start.dayOfWeek)))
      return false
    else if (!filter.hourOfDays.isEmpty && !filter.hourOfDays.contains(mr.hourOfDay))
      return false
    else if (!filter.minuteOfHours.isEmpty && !filter.minuteOfHours.contains(mr.minuteOfHour))
      return false
    else
      return checkExcludePeriods(mr)
  }

}
