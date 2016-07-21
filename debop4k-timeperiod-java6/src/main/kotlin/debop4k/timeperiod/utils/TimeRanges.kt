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

package debop4k.timeperiod.utils

import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.timeranges.*
import org.joda.time.DateTime


@JvmOverloads
fun yearSequence(year: Int, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<YearRange> {
  val start = YearRange(year, calendar)

  return (0 until yearCount).map { y ->
    start.addYears(y)
  }.asSequence()
}

@JvmOverloads
fun yearSequence(start: DateTime, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<YearRange> {
  return yearSequence(start.year, yearCount, calendar)
}

@JvmOverloads
fun halfyearSequence(startTime: DateTime,
                     halfyearCount: Int,
                     calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HalfyearRange> {

  val start = HalfyearRange()

  return (0 until halfyearCount).map { hy ->
    start.addHalfyears(hy)
  }.asSequence()
}


@JvmOverloads
fun quarterSequence(startTime: DateTime,
                    quarterCount: Int,
                    calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<QuarterRange> {
  val start = QuarterRange(startTime.year, startTime.quarterOf(), calendar)

  return (0 until quarterCount).map { q ->
    start.addQuarters(q)
  }.asSequence()
}

@JvmOverloads
fun monthSequence(year: Int,
                  monthOfYear: Int,
                  monthCount: Int,
                  calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MonthRange> {
  val start = MonthRange(year, monthOfYear)

  return (0 until monthCount).map { m ->
    start.addMonths(m)
  }.asSequence()
}

@JvmOverloads
fun monthSequence(startTime: DateTime,
                  monthCount: Int,
                  calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MonthRange> {
  return monthSequence(startTime.year, startTime.monthOfYear, monthCount, calendar)
}

@JvmOverloads
fun weekSequence(startTime: DateTime, weekCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<WeekRange> {
  val start = WeekRange(startTime, calendar)

  return (0 until weekCount).map { w ->
    start.addWeeks(w)
  }.asSequence()
}

@JvmOverloads
fun daySequence(startTime: DateTime, dayCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<DayRange> {
  val start = DayRange(startTime, calendar)

  return (0 until dayCount).map { d ->
    start.addDays(d)
  }.asSequence()
}

@JvmOverloads
fun hourSequence(startTime: DateTime,
                 hourCount: Int,
                 calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HourRange> {
  val start = HourRange(startTime, calendar)

  return (0 until hourCount).map { h ->
    start.addHours(h)
  }.asSequence()
}

@JvmOverloads
fun minuteSequence(startTime: DateTime,
                   minuteCount: Int,
                   calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MinuteRange> {
  val start = MinuteRange(startTime, calendar)

  return (0 until minuteCount).map { m ->
    start.addMinutes(m)
  }.asSequence()
}
