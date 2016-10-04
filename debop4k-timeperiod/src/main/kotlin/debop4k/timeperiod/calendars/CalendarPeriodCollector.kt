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

import debop4k.core.kodatimes.asDate
import debop4k.core.loggerOf
import debop4k.timeperiod.*
import debop4k.timeperiod.timeranges.*

/**
 * Created by debop
 */
open class CalendarPeriodCollector @JvmOverloads constructor(
    filter: CalendarPeriodCollectorFilter,
    limits: ITimePeriod,
    seekDir: SeekDirection = SeekDirection.Forward,
    calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarVisitor<CalendarPeriodCollectorFilter, CalendarPeriodCollectorContext>(
    filter, limits, seekDir, calendar) {

  private val log = loggerOf(javaClass)

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(filter: CalendarPeriodCollectorFilter,
           limits: ITimePeriod,
           seekDir: SeekDirection = SeekDirection.Forward,
           calendar: ITimeCalendar = DefaultTimeCalendar): CalendarPeriodCollector {
      return CalendarPeriodCollector(filter, limits, seekDir, calendar)
    }
  }

  val periods: ITimePeriodCollection = TimePeriodCollection()

  fun collectYears(): Unit = collectByScope(CollectKind.Year)
  fun collectMonths(): Unit = collectByScope(CollectKind.Month)
  fun collectDays(): Unit = collectByScope(CollectKind.Day)
  fun collectHours(): Unit = collectByScope(CollectKind.Hour)
  fun collectMinutes(): Unit = collectByScope(CollectKind.Minute)

  private fun collectByScope(scope: CollectKind): Unit {
    val context = CalendarPeriodCollectorContext(scope)
    startPeriodVisit(context)
  }

  override fun enterYears(years: YearRangeCollection, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Year.value
  }

  override fun enterMonths(year: YearRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Month.value
  }

  override fun enterDays(month: MonthRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Day.value
  }

  override fun enterHours(day: DayRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Hour.value
  }

  override fun enterMinutes(hour: HourRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Minute.value
  }

  override fun onVisitYears(years: YearRangeCollection, context: CalendarPeriodCollectorContext): Boolean {
    log.trace("visit years ... years={}, context={}", years, context)

    if (context.scope != CollectKind.Year) {
      return true
    }

    years.years()
        .filter { isMatchingYear(it, context) && checkLimits(it) }
        .forEach { periods.add(it) }

    return false
  }

  override fun onVisitYear(year: YearRange, context: CalendarPeriodCollectorContext): Boolean {
    log.trace("visit year ... year={}, context={}", year, context)

    if (context.scope != CollectKind.Month) {
      return true
    }

    val monthFilter: (MonthRange) -> Boolean = { isMatchingMonth(it, context) && checkLimits(it) }

    if (filter.collectingMonths.isEmpty) {
      year.months().filter(monthFilter).forEach { periods.add(it) }
    } else {
      filter.collectingMonths.forEach { m ->
        if (m.isSingleMonth) {
          val mr = MonthRange(asDate(year.year, m.startMonthOfYear), year.calendar)
          if (monthFilter(mr)) {
            periods.add(mr)
          }
        } else {
          val mc = MonthRangeCollection(year.year,
                                        m.startMonthOfYear,
                                        m.endMonthOfYear - m.startMonthOfYear,
                                        year.calendar)
          val months = mc.months()
          val isMatch = months.all { isMatchingMonth(it, context) }
          if (isMatch && checkLimits(mc)) {
            periods.addAll(months)
          }
        }
      }
    }
    return false
  }

  override fun onVisitMonth(month: MonthRange, context: CalendarPeriodCollectorContext): Boolean {
    log.trace("visit month... month={}, context={}", month, context)

    if (context.scope != CollectKind.Day) {
      return true
    }

    val dayFilter: (DayRange) -> Boolean = { isMatchingDay(it, context) && checkLimits(it) }

    if (filter.collectingDays.isEmpty) {
      month.days().filter(dayFilter).forEach { periods.add(it) }
    } else {
      filter.collectingDays.forEach { day ->
        val startTime = asDate(month.year, month.monthOfYear, day.startDayOfMonth)

        if (day.isSingleDay) {
          val dayRange = DayRange(startTime, month.calendar)
          if (dayFilter(dayRange)) {
            periods.add(dayRange)
          }
        } else {
          val dc = DayRangeCollection(startTime,
                                      day.endDayOfMonth - day.startDayOfMonth,
                                      month.calendar)
          val days = dc.days()
          val isMatch = days.all { isMatchingDay(it, context) }
          if (isMatch && checkLimits(dc)) {
            periods.addAll(days)
          }
        }
      }
    }
    return false
  }

  override fun onVisitDay(day: DayRange, context: CalendarPeriodCollectorContext): Boolean {
    log.trace("visit day... day={}, context={}", day, context)

    if (filter.collectingHours.isEmpty) {
      day.hours()
          .filter { isMatchingHour(it, context) && checkLimits(it) }
          .forEach { periods.add(it) }
    } else if (isMatchingDay(day, context)) {
      filter.collectingHours.forEach { h ->
        val start = h.start.toDateTime(day.start)
        val end = h.endExclusive.toDateTime(day.start)
        val hc = CalendarTimeRange(start, end, day.calendar)

        if (checkExcludePeriods(hc) && checkLimits(hc)) {
          periods.add(hc)
        }
      }
    }

    return false
  }
}