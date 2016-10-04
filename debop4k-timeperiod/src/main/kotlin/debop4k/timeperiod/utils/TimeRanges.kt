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
fun yearRangeSequence(year: Int, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<YearRange> {
  require(yearCount >= 0)
  val start = YearRange(year, calendar)

  return generateSequence(start) { it.addYears(1) }.take(yearCount)
}

@JvmOverloads
fun yearRangeSequence(start: DateTime, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<YearRange> {
  require(yearCount >= 0)
  return yearRangeSequence(start.year, yearCount, calendar)
}

@JvmOverloads
fun halfyearRangeSequence(startTime: DateTime,
                          halfyearCount: Int,
                          calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HalfyearRange> {
  require(halfyearCount >= 0)
  val start = HalfyearRange(startTime.year, startTime.monthOfYear, calendar)

  return generateSequence(start) { it.addHalfyears(1) }.take(halfyearCount)
}


@JvmOverloads
fun quarterRangeSequence(startTime: DateTime,
                         quarterCount: Int,
                         calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<QuarterRange> {
  require(quarterCount >= 0)
  val start = QuarterRange(startTime.year, startTime.quarterOf(), calendar)

  return generateSequence(start) { it.addQuarters(1) }.take(quarterCount)
}

@JvmOverloads
fun monthRangeSequence(year: Int,
                       monthOfYear: Int,
                       monthCount: Int,
                       calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MonthRange> {
  require(monthCount >= 0)
  val start = MonthRange(year, monthOfYear, calendar)

  return generateSequence(start) { it.addMonths(1) }.take(monthCount)
}

@JvmOverloads
fun monthRangeSequence(startTime: DateTime,
                       monthCount: Int,
                       calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MonthRange> {
  require(monthCount >= 0)
  return monthRangeSequence(startTime.year, startTime.monthOfYear, monthCount, calendar)
}

@JvmOverloads
fun weekRangeSequence(startTime: DateTime, weekCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<WeekRange> {
  require(weekCount >= 0)
  val start = WeekRange(startTime, calendar)

  return generateSequence(start) { it.addWeeks(1) }.take(weekCount)
}

@JvmOverloads
fun dayRangeSequence(startTime: DateTime, dayCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<DayRange> {
  require(dayCount >= 0)
  val start = DayRange(startTime, calendar)

  return generateSequence(start) { it.addDays(1) }.take(dayCount)
}

@JvmOverloads
fun hourRangeSequence(startTime: DateTime,
                      hourCount: Int,
                      calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HourRange> {
  require(hourCount >= 0)
  val start = HourRange(startTime, calendar)
  return generateSequence(start) { it.addHours(1) }.take(hourCount)
}

@JvmOverloads
fun minuteRangeSequence(startTime: DateTime,
                        minuteCount: Int,
                        calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MinuteRange> {
  require(minuteCount >= 0)
  val start = MinuteRange(startTime, calendar)

  return generateSequence(start) { it.addMinutes(1) }.take(minuteCount)
}
