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

import debop4k.core.ToStringHelper
import debop4k.core.kodatimes.asDate
import debop4k.core.kodatimes.months
import debop4k.core.kodatimes.now
import debop4k.core.kodatimes.years
import debop4k.core.loggerOf
import debop4k.core.utils.hashOf
import debop4k.core.utils.min
import debop4k.timeperiod.*
import debop4k.timeperiod.utils.quarterOfMonth
import debop4k.timeperiod.utils.startTimeOfWeek
import debop4k.timeperiod.utils.yearOf
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * 특정 기간의 [Duration] 을 계산합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
open class DateDiff @JvmOverloads constructor(val start: DateTime,
                                              val end: DateTime = now(),
                                              val calendar: ITimeCalendar = DefaultTimeCalendar) {

  val log = loggerOf(javaClass)

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(start: DateTime,
           end: DateTime = now(),
           calendar: ITimeCalendar = DefaultTimeCalendar): DateDiff {
      return DateDiff(start, end, calendar)
    }
  }

  val difference: Duration = Duration(start, end)
  val isEmpty: Boolean = (difference == Duration.ZERO)

  val startYear: Int get() = calendar.year(start)
  val endYear: Int get() = calendar.year(end)
  val startMonthOfYear: Int get() = calendar.monthOfYear(start)
  val endMonthOfYear: Int get() = calendar.monthOfYear(end)

  private val _cache: ConcurrentHashMap<String, Long> = ConcurrentHashMap.newMap()

  val years: Long get() = _cache.getIfAbsentPut("yearCount", calcYears())
  val quarters: Long get() = _cache.getIfAbsentPut("quarters", calcQuarters())
  val months: Long get() = _cache.getIfAbsentPut("months", calcMonths())
  val weeks: Long get() = _cache.getIfAbsentPut("weeks", calcWeeks())
  val days: Long get() = difference.standardDays
  val hours: Long get() = difference.standardHours
  val minutes: Long get() = difference.standardMinutes
  val seconds: Long get() = difference.standardSeconds

  val elapsedYears: Long get() = years
  val elapsedQuarters: Long get() = quarters
  val elapsedMonths: Long get() = months - elapsedYears * MonthsPerYear
  val elapsedStartDays: DateTime
    get() = start
        .plusYears(elapsedYears.toInt())
        .plusMonths(elapsedMonths.toInt())
  val elapsedDays: Long get() = Duration(elapsedStartDays, end).standardDays

  val elapsedStartHours: DateTime
    get() = elapsedStartDays.plusDays(elapsedDays.toInt())

  val elapsedHours: Long get() = Duration(elapsedStartHours, end).standardHours

  val elapsedStartMinutes: DateTime
    get() = elapsedStartHours.plusHours(elapsedHours.toInt())

  val elapsedMinutes: Long get() = Duration(elapsedStartMinutes, end).standardMinutes

  val elapsedStartSeconds: DateTime
    get() = elapsedStartMinutes.plusMinutes(elapsedMinutes.toInt())

  val elapsedSeconds: Long get() = Duration(elapsedStartSeconds, end).standardSeconds

  private fun calcYears(): Long {
    if (isEmpty) return 0L

    val compareDay = end.dayOfMonth min calendar.daysInMonth(startYear, endMonthOfYear)
    var compareDate = asDate(startYear, endMonthOfYear, compareDay).plusMillis(end.millisOfDay)

    if (end > start) {
      if (!start.year().isLeap) {
        if (compareDate < start) {
          compareDate += 1.years()
        }
      } else {
        if (compareDate < start.minusDays(1)) {
          compareDate += 1.years()
        }
      }
    } else if (compareDate > start) {
      compareDate = compareDate.minusYears(1)
    }

    return (endYear - calendar.year(compareDate)).toLong()
  }

  private fun calcQuarters(): Long {
    if (isEmpty) return 0L

    val y1: Int = yearOf(startYear, startMonthOfYear, calendar)
    val q1: Int = quarterOfMonth(startMonthOfYear).value

    val y2: Int = yearOf(endYear, endMonthOfYear, calendar)
    val q2: Int = quarterOfMonth(endMonthOfYear).value

    val diff = (y2 * QuartersPerYear + q2) - (y1 * QuartersPerYear + q1)
    return diff.toLong()
  }

  private fun calcMonths(): Long {
    if (isEmpty) return 0L

    val compareDay = end.dayOfMonth min calendar.daysInMonth(startYear, startMonthOfYear)
    var compareDate = asDate(startYear, startMonthOfYear, compareDay).plusMillis(end.millisOfDay)

    if (end > start) {
      if (!start.year().isLeap) {
        if (compareDate < start) {
          compareDate += 1.months()
        }
      } else if (compareDate < start.minusDays(1)) {
        compareDate += 1.months()
      }
    } else if (compareDate > start) {
      compareDate = compareDate.minusMonths(1)
    }

    val diff = (endYear * MonthsPerYear + endMonthOfYear) -
               (calendar.year(compareDate) * MonthsPerYear + calendar.monthOfYear(compareDate))

    return diff.toLong()
  }

  private fun calcWeeks(): Long {
    if (isEmpty) return 0L

    val w1 = start.startTimeOfWeek()
    val w2 = end.startTimeOfWeek()

    return if (w1 == w2) 0L else Duration(w1, w2).standardDays / DaysPerWeek
  }

  private fun roundEx(n: Double): Double {
    val rounded = if (n > 0.0) Math.round(n) else -Math.round(-n)
    return rounded.toDouble()
  }

  override fun hashCode(): Int {
    return hashOf(start, end, calendar)
  }

  override fun toString(): String {
    return ToStringHelper(this)
        .add("start", start)
        .add("end", end)
        .add("difference", difference)
        .add("calendar", calendar)
        .toString()
  }
}