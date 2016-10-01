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
@file:JvmName("TimeRanges")

package debop4k.timeperiod.utils

import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.timeranges.*
import org.joda.time.DateTime


@JvmOverloads
fun yearSequence(year: Int, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<YearRange> {
  require(yearCount >= 0)
  val start = YearRange(year, calendar)

  return generateSequence(start) { it.addYears(1) }.take(yearCount)
//  return (0 until yearCount).map { y ->
//    start.addYears(y)
//  }.asSequence()
}

@JvmOverloads
fun yearSequence(start: DateTime, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<YearRange> {
  require(yearCount >= 0)
  return yearSequence(start.year, yearCount, calendar)
}

@JvmOverloads
fun halfyearSequence(startTime: DateTime,
                     halfyearCount: Int,
                     calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HalfyearRange> {
  require(halfyearCount >= 0)
  val start = HalfyearRange(startTime.year, startTime.monthOfYear, calendar)

  return generateSequence(start) { it.addHalfyears(1) }.take(halfyearCount)

//  return (0 until halfyearCount).map { hy ->
//    start.addHalfyears(hy)
//  }.asSequence()
}


@JvmOverloads
fun quarterSequence(startTime: DateTime,
                    quarterCount: Int,
                    calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<QuarterRange> {
  require(quarterCount >= 0)
  val start = QuarterRange(startTime.year, startTime.quarterOf(), calendar)

  return generateSequence(start) { it.addQuarters(1) }.take(quarterCount)

//  return (0 until quarterCount).map { q ->
//    start.addQuarters(q)
//  }.asSequence()
}

@JvmOverloads
fun monthSequence(year: Int,
                  monthOfYear: Int,
                  monthCount: Int,
                  calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MonthRange> {
  require(monthCount >= 0)
  val start = MonthRange(year, monthOfYear, calendar)

  return generateSequence(start) { it.addMonths(1) }.take(monthCount)

//  return (0 until monthCount).map { m ->
//    start.addMonths(m)
//  }.asSequence()
}

@JvmOverloads
fun monthSequence(startTime: DateTime,
                  monthCount: Int,
                  calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MonthRange> {
  require(monthCount >= 0)
  return monthSequence(startTime.year, startTime.monthOfYear, monthCount, calendar)
}

@JvmOverloads
fun weekSequence(startTime: DateTime, weekCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<WeekRange> {
  require(weekCount >= 0)
  val start = WeekRange(startTime, calendar)

  return generateSequence(start) { it.addWeeks(1) }.take(weekCount)
//  return (0 until weekCount).map { w ->
//    start.addWeeks(w)
//  }.asSequence()
}

@JvmOverloads
fun daySequence(startTime: DateTime, dayCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<DayRange> {
  require(dayCount >= 0)
  val start = DayRange(startTime, calendar)

  return generateSequence(start) { it.addDays(1) }.take(dayCount)
//  return (0 until dayCount).map { d ->
//    start.addDays(d)
//  }.asSequence()
}

@JvmOverloads
fun hourSequence(startTime: DateTime,
                 hourCount: Int,
                 calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HourRange> {
  require(hourCount >= 0)
  val start = HourRange(startTime, calendar)
  return generateSequence(start) { it.addHours(1) }.take(hourCount)
//  return (0 until hourCount).map { h ->
//    start.addHours(h)
//  }.asSequence()
}

@JvmOverloads
fun minuteSequence(startTime: DateTime,
                   minuteCount: Int,
                   calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MinuteRange> {
  require(minuteCount >= 0)
  val start = MinuteRange(startTime, calendar)

  return generateSequence(start) { it.addMinutes(1) }.take(minuteCount)
//  return (0 until minuteCount).map { m ->
//    start.addMinutes(m)
//  }.asSequence()
}
