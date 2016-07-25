/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.timeperiod.timeranges

import debop4k.core.ToStringHelper
import debop4k.core.kodatimes.*
import debop4k.core.utils.hashOf
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.TimeRange
import debop4k.timeperiod.models.DayOfWeek
import debop4k.timeperiod.utils.assertValidPeriod
import org.joda.time.DateTime
import org.joda.time.Duration


/**
 * Calendar 기반의 [TimeRange]
 *
 * @author sunghyouk.bae@gmail.com
 */
@Suppress("EqualsOrHashCode")
open class CalendarTimeRange(val period: ITimePeriod,
                             val calendar: ITimeCalendar = DefaultTimeCalendar)
: TimeRange(calendar.mapStart(period.start),
            calendar.mapEnd(period.end),
            true) {

  constructor(calendar: ITimeCalendar = DefaultTimeCalendar) : this(TimeRange.AnyTime, calendar)
  constructor(start: DateTime, end: DateTime, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(TimeRange(start, end), calendar)

  init {
    assertValidPeriod(calendar.mapStart(period.start), calendar.mapEnd(period.end))
  }

  override fun copy(offset: Duration): CalendarTimeRange {
    return CalendarTimeRange(super.copy(offset), calendar)
  }

  val startYear: Int get() = start.year
  val startMonthOfYear: Int get() = start.monthOfYear
  val startDayOfMonth: Int get() = start.dayOfMonth
  val startDayOfWeek: DayOfWeek get() = DayOfWeek.of(start.dayOfWeek)
  val startHourOfDay: Int get() = start.hourOfDay
  val startMinuteOfHour: Int get() = start.minuteOfHour
  val startSecondOfMinute: Int get() = start.secondOfMinute

  val endYear: Int get() = end.year
  val endMonthOfYear: Int get() = end.monthOfYear
  val endDayOfMonth: Int get() = end.dayOfMonth
  val endDayOfWeek: DayOfWeek get() = DayOfWeek.of(end.dayOfWeek)
  val endHourOfDay: Int get() = end.hourOfDay
  val endMinuteOfHour: Int get() = end.minuteOfHour
  val endSecondOfMinute: Int get() = end.secondOfMinute

  val mappedStart: DateTime get() = calendar.mapStart(start)
  val mappedEnd: DateTime get() = calendar.mapEnd(end)
  val unmappedStart: DateTime get() = calendar.unmapStart(start)
  val unmappedEnd: DateTime get() = calendar.unmapEnd(end)

  val startYearOfStart: DateTime get() = start.trimToMonth()
  val startYearOfEnd: DateTime get() = end.trimToMonth()
  val startMonthOfStart: DateTime get() = start.trimToDay()
  val startMonthOfEnd: DateTime get() = end.trimToDay()
  val startDayOfStart: DateTime get() = start.trimToHour()
  val startDayOfEnd: DateTime get() = end.trimToHour()
  val startHourOfStart: DateTime get() = start.trimToMinute()
  val startHourOfEnd: DateTime get() = end.trimToMinute()
  val startMinuteOfStart: DateTime get() = start.trimToSecond()
  val startMinuteOfEnd: DateTime get() = end.trimToSecond()
  val startSecondOfStart: DateTime get() = start.trimToMillis()
  val startSecondOfEnd: DateTime get() = end.trimToMillis()

  override fun hashCode(): Int {
    return hashOf(super.hashCode(), calendar)
  }

  override fun toString(): String {
    return ToStringHelper(this)
        .add("period", period)
        .add("calendar", calendar)
        .toString()
  }
}