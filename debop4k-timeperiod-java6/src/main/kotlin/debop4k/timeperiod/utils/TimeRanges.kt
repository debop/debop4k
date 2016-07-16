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
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime


@JvmOverloads
fun yearStream(year: Int, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): FastList<YearRange> {
  val start = YearRange(year, calendar)
  val years = FastList.newList<YearRange>(yearCount)
  for (y in 0 until yearCount) {
    years.add(start.addYears(y))
  }
  return years
}

@JvmOverloads
fun yearStream(start: DateTime, yearCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): FastList<YearRange> {
  return yearStream(start.year, yearCount, calendar)
}

@JvmOverloads
fun monthStream(year: Int, monthOfYear: Int, monthCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): FastList<MonthRange> {
  val start = MonthRange(year, monthOfYear)
  val months = FastList.newList<MonthRange>(monthCount)
  for (m in 0 until monthCount) {
    months.add(start.addMonths(m))
  }
  return months
}

@JvmOverloads
fun monthStream(startTime: DateTime, monthCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): FastList<MonthRange> {
  return monthStream(startTime.year, startTime.monthOfYear, monthCount, calendar)
}

@JvmOverloads
fun weekStream(startTime: DateTime, weekCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<WeekRange> {
  val start = WeekRange(startTime, calendar)

  return (0 until weekCount).map { w ->
    start.addWeeks(w)
  }.asSequence()
}

@JvmOverloads
fun dayStream(startTime: DateTime, dayCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<DayRange> {
  val start = DayRange(startTime, calendar)

  return (0 until dayCount).map { d ->
    start.addDays(d)
  }.asSequence()
}

@JvmOverloads
fun hourStream(startTime: DateTime, hourCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<HourRange> {
  val start = HourRange(startTime, calendar)

  return (0 until hourCount).map { h ->
    start.addHours(h)
  }.asSequence()
}

@JvmOverloads
fun minuteStream(startTime: DateTime, minuteCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Sequence<MinuteRange> {
  val start = MinuteRange(startTime, calendar)

  return (0 until minuteCount).map { m ->
    start.addMinutes(m)
  }.asSequence()
}
