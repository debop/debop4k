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
@file:JvmName("Times")

package debop4k.timeperiod.utils

import debop4k.core.kodatimes.asDate
import debop4k.core.kodatimes.months
import debop4k.core.kodatimes.startOfDay
import debop4k.core.kodatimes.today
import debop4k.timeperiod.*
import debop4k.timeperiod.models.*
import debop4k.timeperiod.timeranges.WeekRange
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.util.*

fun now(): DateTime = DateTime.now()
fun today(): DateTime = DateTime.now().withTimeAtStartOfDay()

fun DateTime.localDate(): LocalDate = this.toLocalDate()
fun DateTime.localTime(): LocalTime = this.toLocalTime()

@Suppress("UNUSED_PARAMETER")
@JvmOverloads
fun yearOf(year: Int, monthOfYear: Int, calendar: ITimeCalendar = DefaultTimeCalendar): Int
    = if (monthOfYear >= 1) year else year - 1

@JvmOverloads
fun DateTime.yearOf(calendar: ITimeCalendar = DefaultTimeCalendar): Int
    = yearOf(this.year, this.monthOfYear, calendar)

fun daysOfYear(year: Int): Int = asDate(year + 1).minusDays(1).dayOfYear

fun addHalfyear(year: Int, halfyear: Halfyear, delta: Int): YearHalfyear {
  if (delta == 0)
    return YearHalfyear(year, halfyear)

  return YearHalfyear(startTimeOfHalfyear(year, halfyear).plusMonths(delta * MonthsPerHalfyear))
}

fun YearHalfyear.addHalfyear(delta: Int): YearHalfyear {
  if (delta == 0)
    return YearHalfyear(this)
  val end = startTimeOfHalfyear(year, halfyear).plusMonths(delta * MonthsPerHalfyear)
  return YearHalfyear(end)
}

fun nextHalfyear(year: Int, halfyear: Halfyear): YearHalfyear = addHalfyear(year, halfyear, 1)
fun YearHalfyear.nextHalfyear(): YearHalfyear = this.addHalfyear(1)
fun prevHalfyear(year: Int, halfyear: Halfyear): YearHalfyear = addHalfyear(year, halfyear, -1)
fun YearHalfyear.prevHalfyear(): YearHalfyear = this.addHalfyear(-1)

fun halfyearOfMonth(monthOfYear: Int): Halfyear = if (FirstHalfyearMonths.contains(monthOfYear)) Halfyear.First else Halfyear.Second

fun addQuarter(year: Int, quarter: Quarter, delta: Int): YearQuarter {
  if (delta == 0)
    return YearQuarter(year, quarter)

  return YearQuarter(startTimeOfQuarter(year, quarter).plusMonths(MonthsPerQuarter * delta))
}

fun YearQuarter.addQuarter(delta: Int): YearQuarter
    = addQuarter(year, quarter, delta)

fun nextQuarter(year: Int, quarter: Quarter): YearQuarter = addQuarter(year, quarter, 1)
fun YearQuarter.nextQuarter(): YearQuarter = this.addQuarter(1)
fun prevQuarter(year: Int, quarter: Quarter): YearQuarter = addQuarter(year, quarter, -1)
fun YearQuarter.prevQuarter(): YearQuarter = this.addQuarter(-1)

fun quarterOfMonth(monthOfYear: Int): Quarter = Quarter.ofMonth(monthOfYear)

fun addMonth(year: Int, monthOfYear: Int, delta: Int): YearMonth {
  return YearMonth(year, monthOfYear).addMonth(delta)
}

fun YearMonth.addMonth(delta: Int): YearMonth {
  if (delta == 0) return this.copy()

  val dt = this.startTimeOfMonth() + delta.months()
  return YearMonth(dt.year, dt.monthOfYear)
}

fun nextMonth(year: Int, monthOfYear: Int): YearMonth = addMonth(year, monthOfYear, 1)
fun YearMonth.nextMonth(): YearMonth = this.addMonth(1)
fun prevMonth(year: Int, monthOfYear: Int): YearMonth = addMonth(year, monthOfYear, -1)
fun YearMonth.prevMonth(): YearMonth = this.addMonth(-1)

fun daysInMonth(year: Int, monthOfYear: Int): Int
    = asDate(year, monthOfYear).plusMonths(1).minusDays(1).dayOfMonth

fun DateTime.daysInMonth(): Int = daysInMonth(this.year, this.monthOfYear)
fun YearMonth.daysInMonth(): Int = daysInMonth(this.year, this.monthOfYear)

fun DateTime.startOfWeek(): DateTime {
  val day = this.startOfDay()
  val dayOfWeek = day.dayOfWeek
  return day.minusDays(dayOfWeek - 1)
}

fun DateTime.weekOfMonth(): MonthWeek {
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = this.millis
  calendar.minimalDaysInFirstWeek = 1
  calendar.firstDayOfWeek = Calendar.MONDAY

  return MonthWeek(this.monthOfYear, calendar.get(Calendar.WEEK_OF_MONTH))
}

@Suppress("UNUSED_PARAMETER")
@JvmOverloads
fun DateTime.weekOfYear(calendar: ITimeCalendar = TimeCalendar.DEFAULT): YearWeek = YearWeek.of(this)

fun lastWeekOfYear(year: Int): YearWeek {
  var lastDay = asDate(year, 12, 31)
  while (lastDay.weekyear > year) {
    lastDay = lastDay.minusDays(1)
  }
  return YearWeek(year, lastDay.weekOfWeekyear)
}

fun startOfYearWeek(weekyear: Int, weekOfWeekyear: Int): DateTime {
  return today().withWeekyear(weekyear).withWeekOfWeekyear(weekOfWeekyear)
}

fun YearWeek.startOfYearWeek(): DateTime = startOfYearWeek(this.weekyear, this.weekOfWeekyear)

fun DateTime.dayStart(): DateTime = this.withTimeAtStartOfDay()

fun addDayOfWeek(dow: DayOfWeek, delta: Int = 1): DayOfWeek = dow + delta
fun DayOfWeek.nextDayOfWeek(): DayOfWeek = addDayOfWeek(this, 1)
fun DayOfWeek.prevDayOfWeek(): DayOfWeek = addDayOfWeek(this, -1)


fun DateTime.isSameTime(other: DateTime?, unit: PeriodUnit): Boolean = when (unit) {
  PeriodUnit.YEAR -> isSameYear(other)
  PeriodUnit.HALFYEAR -> isSameHalfyear(other)
  PeriodUnit.QUARTER -> isSameQuarter(other)
  PeriodUnit.MONTH -> isSameMonth(other)
  PeriodUnit.DAY -> isSameDay(other)
  PeriodUnit.HOUR -> isSameHour(other)
  PeriodUnit.MINUTE -> isSameMinute(other)
  PeriodUnit.SECOND -> isSameSecond(other)
  else -> isSameDateTime(other)
}

fun DateTime.isSameYear(other: DateTime?): Boolean
    = other != null && this.year == other.year

fun DateTime.isSameHalfyear(other: DateTime?): Boolean
    = isSameYear(other) && halfyearOf() == other!!.halfyearOf()

fun DateTime.isSameQuarter(other: DateTime?): Boolean
    = isSameYear(other) && quarterOf() == other!!.quarterOf()

fun DateTime.isSameMonth(other: DateTime?): Boolean
    = isSameYear(other) && monthOfYear == other!!.monthOfYear

fun DateTime.isSameWeek(other: DateTime?): Boolean
    = isSameMonth(other) && weekyear == other!!.weekyear && weekOfWeekyear == other.weekOfWeekyear

fun DateTime.isSameDay(other: DateTime?): Boolean
    = isSameYear(other) && dayOfYear == other!!.dayOfYear

fun DateTime.isSameHour(other: DateTime?): Boolean
    = isSameDay(other) && hourOfDay == other!!.hourOfDay

fun DateTime.isSameMinute(other: DateTime?): Boolean
    = isSameDay(other) && minuteOfDay == other!!.minuteOfDay

fun DateTime.isSameSecond(other: DateTime?): Boolean
    = isSameDay(other) && secondOfDay == other!!.secondOfDay

fun DateTime.isSameDateTime(other: DateTime?): Boolean
    = other != null && millis == other.millis

fun currentYear(): DateTime
    = asDate(now().year, 1, 1)

fun currentHalfyear(): DateTime {
  val current = today()
  return startTimeOfHalfyear(current.year, current.halfyearOf())
}

fun currentQuarter(): DateTime {
  val current = today()
  return startTimeOfQuarter(current.year, current.quarterOf())
}

fun currentMonth(): DateTime = today().startTimeOfMonth()
fun currentWeek(): DateTime = today().startTimeOfWeek()
fun currentDay(): DateTime = today().startTimeOfDay()
fun currentHour(): DateTime = today().startTimeOfHour()
fun currentMinute(): DateTime = today().startTimeOfMinute()
fun currentSecond(): DateTime = today().startTimeOfSecond()


fun startTimeOfYear(year: Int): DateTime = asDate(year, 1, 1)

fun DateTime.startTimeOfYear(): DateTime
    = startTimeOfYear(this.year)

fun endTimeOfYear(year: Int): DateTime
    = startTimeOfYear(year).plusYears(1).minusMillis(1)

fun DateTime.endTimeOfYear(): DateTime
    = endTimeOfYear(this.year)

fun startTimeOfHalfyear(year: Int, halfyear: Halfyear): DateTime
    = asDate(year, halfyear.startMonth.value, 1)

fun startTimeOfHalfyear(year: Int, monthOfYear: Int): DateTime
    = startTimeOfHalfyear(year, halfyearOf(monthOfYear))

fun YearHalfyear.startTimeOfHalfyear(): DateTime
    = startTimeOfHalfyear(year, halfyear)

fun DateTime.startTimeOfHalfyear(): DateTime
    = startTimeOfHalfyear(this.year, this.monthOfYear)

fun endTimeOfHalfyear(year: Int, monthOfYear: Int): DateTime
    = startTimeOfHalfyear(year, monthOfYear).plusMonths(MonthsPerHalfyear).minusMillis(1)

fun endTimeOfHalfyear(year: Int, halfyear: Halfyear): DateTime
    = endTimeOfHalfyear(year, halfyear.startMonth.value)

fun YearHalfyear.endTimeOfHalfyear(): DateTime
    = endTimeOfHalfyear(year, halfyear)

fun DateTime.endTimeOfHalfyear(): DateTime
    = endTimeOfHalfyear(this.year, this.monthOfYear)

fun startTimeOfQuarter(year: Int, monthOfYear: Int): DateTime
    = startTimeOfQuarter(year, quarterOf(monthOfYear))

fun startTimeOfQuarter(year: Int, quarter: Quarter): DateTime
    = asDate(year, quarter.startMonth, 1)

fun YearQuarter.startTimeOfQuarter(): DateTime
    = startTimeOfQuarter(year, quarter)

fun DateTime.startTimeOfQuarter(): DateTime
    = startTimeOfQuarter(this.year, this.monthOfYear)

fun endTimeOfQuarter(year: Int, monthOfYear: Int): DateTime
    = endTimeOfQuarter(year, quarterOf(monthOfYear))

fun endTimeOfQuarter(year: Int, quarter: Quarter): DateTime
    = asDate(year, quarter.endMonth + 1, 1).minusMillis(1)

fun YearQuarter.endTimeOfQuarter(): DateTime
    = endTimeOfQuarter(year, quarter)

fun DateTime.endTimeOfQuarter(): DateTime
    = endTimeOfQuarter(this.year, this.monthOfYear)

fun startTimeOfMonth(year: Int, monthOfYear: Int): DateTime = asDate(year, monthOfYear, 1)
fun startTimeOfMonth(year: Int, month: Month): DateTime = startTimeOfMonth(year, month.value)
fun YearMonth.startTimeOfMonth(): DateTime = startTimeOfMonth(this.year, this.monthOfYear)
fun DateTime.startTimeOfMonth(): DateTime = startTimeOfMonth(this.year, this.monthOfYear)

fun endTimeOfMonth(year: Int, monthOfYear: Int): DateTime
    = startTimeOfMonth(year, monthOfYear).plusMonths(1).minusMillis(1)

fun endTimeOfMonth(year: Int, month: Month): DateTime
    = endTimeOfMonth(year, month.value)

fun YearMonth.endTimeOfMonth(): DateTime
    = endTimeOfMonth(this.year, this.monthOfYear)

fun DateTime.endTimeOfMonth(): DateTime
    = endTimeOfMonth(this.year, this.monthOfYear)

fun startTimeOfWeek(weekyear: Int, weekOfWeekyear: Int): DateTime
    = DateTime().withWeekyear(weekyear).withWeekOfWeekyear(weekOfWeekyear)

fun YearWeek.startTimeOfWeek(): DateTime
    = startTimeOfWeek(this.weekyear, this.weekOfWeekyear)

fun DateTime.startTimeOfWeek(): DateTime {
  val day = asDate()
  val dayOfWeek = day.dayOfWeek
  return day.minusDays(dayOfWeek - 1)
}

fun endTimeOfWeek(weekyear: Int, weekOfWeekyear: Int): DateTime
    = startTimeOfWeek(weekyear, weekOfWeekyear).plusWeeks(1).minusMillis(1)

fun YearWeek.endTimeOfWeek(): DateTime
    = endTimeOfWeek(this.weekyear, this.weekOfWeekyear)

fun DateTime.endTimeOfWeek(): DateTime {
  return startTimeOfWeek().plusDays(7)
}

fun DateTime.startTimeOfDay(): DateTime = this.withTimeAtStartOfDay()
fun DateTime.endTimeOfDay(): DateTime = startTimeOfDay().plusDays(1).minusMillis(1)

fun DateTime.startTimeOfHour(): DateTime = startTimeOfDay().plusHours(this.hourOfDay)
fun DateTime.endTimeOfHour(): DateTime = startTimeOfHour().plusHours(1).minusMillis(1)

fun DateTime.startTimeOfMinute(): DateTime = startTimeOfHour().plusMinutes(this.minuteOfHour)
fun DateTime.endTimeOfMinute(): DateTime = startTimeOfMinute().plusMinutes(1).minusMillis(1)

fun DateTime.startTimeOfSecond(): DateTime = startTimeOfMinute().plusSeconds(this.secondOfMinute)
fun DateTime.endTimeOfSecond(): DateTime = startTimeOfSecond().plusSeconds(1).minusMillis(1)

fun halfyearOf(monthOfYear: Int): Halfyear = Halfyear.ofMonth(monthOfYear)
fun DateTime.halfyearOf(): Halfyear = Halfyear.ofMonth(this.monthOfYear)

fun quarterOf(monthOfYear: Int): Quarter = Quarter.ofMonth(monthOfYear)
fun DateTime.quarterOf(): Quarter = Quarter.ofMonth(this.monthOfYear)

/** 현 일자의 다음주 같은 요일 */
fun DateTime.nextDayOfWeek(): DateTime = this.plusWeeks(1)

fun DateTime.prevDayOfWeek(): DateTime = this.minusWeeks(1)

fun DateTime?.hasDate(): Boolean
    = this != null && this.withTimeAtStartOfDay().millis > 0

fun DateTime.setDatepart(date: DateTime): DateTime
    = date.startTimeOfDay().withMillisOfDay(this.millisOfDay)

@JvmOverloads
fun DateTime.setDatepart(year: Int, monthOfYear: Int = 1, dayOfMonth: Int = 1): DateTime
    = asDate(year, monthOfYear, dayOfMonth).withMillisOfDay(this.millisOfDay)

fun DateTime.setYear(year: Int): DateTime = this.withYear(year)
fun DateTime.setMonth(monthOfYear: Int): DateTime = this.withMonthOfYear(monthOfYear)
fun DateTime.setDay(dayOfMonth: Int): DateTime = this.withDayOfMonth(dayOfMonth)

fun DateTime.setTimepart(time: DateTime): DateTime
    = this.startTimeOfDay().withMillisOfDay(time.millisOfDay)

@JvmOverloads
fun DateTime.setTimepart(hourOfDay: Int,
                         minuteOfHour: Int = 0,
                         secondOfMinute: Int = 0,
                         millisOfSecond: Int = 0): DateTime
    = this.startTimeOfDay().withTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond)

fun DateTime.getTimepart(): Int = this.millisOfDay
fun DateTime?.hasTimepart(): Boolean = this != null && this.millisOfDay > 0

fun combine(date: DateTime, time: DateTime): DateTime = date.setTimepart(time)
fun combine(date: LocalDate, time: LocalTime): DateTime = date.toDateTime(time)

fun DateTime.setHour(hourOfDay: Int): DateTime = this.withHourOfDay(hourOfDay)
fun DateTime.setMinute(minuteOfHour: Int): DateTime = this.withMinuteOfHour(minuteOfHour)
fun DateTime.setSecond(secondOfMinute: Int): DateTime = this.withSecondOfMinute(secondOfMinute)
fun DateTime.setMillis(millisOfSeocond: Int): DateTime = this.withMillisOfSecond(millisOfSeocond)
fun DateTime.setMillisOfDay(millisOfDay: Int): DateTime = this.withMillisOfDay(millisOfDay)


fun DateTime.addDate(unit: PeriodUnit, delta: Int): DateTime = when (unit) {
  PeriodUnit.YEAR -> this.plusYears(delta)
  PeriodUnit.HALFYEAR -> this.plusMonths(MonthsPerHalfyear * delta)
  PeriodUnit.QUARTER -> this.plusMonths(MonthsPerQuarter * delta)
  PeriodUnit.MONTH -> this.plusMonths(delta)
  PeriodUnit.WEEK -> this.plusWeeks(delta)
  PeriodUnit.DAY -> this.plusDays(delta)
  PeriodUnit.HOUR -> this.plusHours(delta)
  PeriodUnit.MINUTE -> this.plusMinutes(delta)
  PeriodUnit.SECOND -> this.plusSeconds(delta)
  PeriodUnit.MILLISECOND -> this.plusMillis(delta)

  else -> throw IllegalArgumentException("Unsupported period unit. unit=$unit")
}

fun startWeekRangeOfYear(year: Int): WeekRange = WeekRange(YearWeek(year, 1))
fun endWeekRangeOfYear(year: Int): WeekRange = WeekRange(lastWeekOfYear(year))

fun maxWeekOfYear(year: Int): YearWeek = YearWeek.of(asDate(year, 12, 28))

fun DayOfWeek.isWeekday(): Boolean = WeekdayList.contains(this)
fun DayOfWeek.isWeekend(): Boolean = WeekendList.contains(this)

