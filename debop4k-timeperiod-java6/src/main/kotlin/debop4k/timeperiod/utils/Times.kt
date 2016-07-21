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

@file:JvmName("Times")

package debop4k.timeperiod.utils

import debop4k.core.areEquals
import debop4k.core.kodatimes.*
import debop4k.timeperiod.*
import debop4k.timeperiod.models.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.util.*


fun DateTime.localDate(): LocalDate = this.toLocalDate()
fun DateTime.localTime(): LocalTime = this.toLocalTime()


fun yearOf(year: Int, monthOfYear: Int): Int = if (monthOfYear >= 1) year else year - 1
@JvmOverloads
fun DateTime.yearOf(calendar: ITimeCalendar = TimeCalendar.DEFAULT): Int = yearOf(this.year, this.monthOfYear)

fun daysOfYear(year: Int): Int = asDate(year + 1).minusDays(1).dayOfYear

fun addHalfyear(year: Int, halfyear: Halfyear, delta: Int): YearHalfyear {
  if (delta == 0)
    return YearHalfyear(year, halfyear)

  return YearHalfyear.of(startTimeOfHalfyear(year, halfyear).plusMonths(delta * MonthsPerHalfyear))
}

fun YearHalfyear.addHalfyear(delta: Int): YearHalfyear = TODO()

fun nextHalfyear(year: Int, halfyear: Halfyear): YearHalfyear = addHalfyear(year, halfyear, 1)
fun YearHalfyear.nextHalfyear(): YearHalfyear = this.addHalfyear(1)
fun prevHalfyear(year: Int, halfyear: Halfyear): YearHalfyear = addHalfyear(year, halfyear, -1)
fun YearHalfyear.prevHalfyear(): YearHalfyear = this.addHalfyear(-1)

fun halfyearOfMonth(monthOfYear: Int) = if (FirstHalfyearMonths.contains(monthOfYear)) Halfyear.First else Halfyear.Second

fun addQuarter(year: Int, quarter: Quarter, delta: Int): YearQuarter = TODO()
fun YearQuarter.addQuarter(delta: Int): YearQuarter = TODO()

fun nextQuarter(year: Int, quarter: Quarter): YearQuarter = addQuarter(year, quarter, 1)
fun YearQuarter.nextQuarter(delta: Int): YearQuarter = this.addQuarter(1)
fun prevQuarter(year: Int, quarter: Quarter): YearQuarter = addQuarter(year, quarter, -1)
fun YearQuarter.prevQuarter(delta: Int): YearQuarter = this.addQuarter(-1)

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
  calendar.timeInMillis = this.getMillis()
  calendar.minimalDaysInFirstWeek = 1
  calendar.firstDayOfWeek = Calendar.MONDAY

  return MonthWeek(this.getMonthOfYear(), calendar.get(Calendar.WEEK_OF_MONTH))
}

@JvmOverloads
fun DateTime.weekOfYear(calendar: ITimeCalendar = TimeCalendar.DEFAULT): YearWeek = YearWeek.of(this)

fun lastWeekOfYear(year: Int): YearWeek {
  var lastDay = asDate(year, 12, 31)
  while (lastDay.weekyear > year) {
    lastDay = lastDay.minusDays(1)
  }
  return YearWeek(year, lastDay.weekOfWeekyear)
}

fun startOfYearWeek(weekyear: Int, weekOfWeekyear: Int): DateTime = TODO()
fun YearWeek.startOfYearWeek(): DateTime = TODO()

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

fun DateTime.isSameYear(other: DateTime?): Boolean = this.year == (other?.year ?: Long.MIN_VALUE)
fun DateTime.isSameHalfyear(other: DateTime?): Boolean = TODO()
fun DateTime.isSameQuarter(other: DateTime?): Boolean = TODO()
fun DateTime.isSameMonth(other: DateTime?): Boolean = TODO()
fun DateTime.isSameWeek(other: DateTime?): Boolean = TODO()
fun DateTime.isSameDay(other: DateTime?): Boolean = TODO()
fun DateTime.isSameHour(other: DateTime?): Boolean = TODO()
fun DateTime.isSameMinute(other: DateTime?): Boolean = TODO()
fun DateTime.isSameSecond(other: DateTime?): Boolean = TODO()
fun DateTime.isSameDateTime(other: DateTime?): Boolean = areEquals(this, other)

fun currentYear(): DateTime = asDate(now().year, 1, 1)
fun currentHalfyear(): DateTime = TODO()
fun currentQuarter(): DateTime = TODO()
fun currentMonth(): DateTime = TODO()
fun currentWeek(): DateTime = TODO()
fun currentDay(): DateTime = today()
fun currentHour(): DateTime = TODO()
fun currentMinute(): DateTime = TODO()
fun currentSecond(): DateTime = TODO()


fun startTimeOfYear(year: Int): DateTime = asDate(year, 1, 1)
fun DateTime.startTimeOfYear(): DateTime = startTimeOfYear(this.year)
fun endTimeOfYear(year: Int): DateTime = startTimeOfYear(year).plusYears(1).minusMillis(1)
fun DateTime.endTimeOfYear(): DateTime = endTimeOfYear(this.year)

fun startTimeOfHalfyear(year: Int, monthOfYear: Int): DateTime = TODO()
fun startTimeOfHalfyear(year: Int, halfyear: Halfyear): DateTime = TODO()
fun YearHalfyear.startTimeOfHalfyear(): DateTime = TODO()
fun DateTime.startTimeOfHalfyear(): DateTime = startTimeOfHalfyear(this.year, this.monthOfYear)

fun endTimeOfHalfyear(year: Int, monthOfYear: Int): DateTime = TODO()
fun endTimeOfHalfyear(year: Int, halfyear: Halfyear): DateTime = TODO()
fun YearHalfyear.endTimeOfHalfyear(): DateTime = TODO()
fun DateTime.endTimeOfHalfyear(): DateTime = endTimeOfHalfyear(this.year, this.monthOfYear)

fun startTimeOfQuarter(year: Int, monthOfYear: Int): DateTime = TODO()
fun startTimeOfQuarter(year: Int, quarter: Quarter): DateTime = TODO()
fun YearQuarter.startTimeOfQuarter(): DateTime = TODO()
fun DateTime.startTimeOfQuarter(): DateTime = startTimeOfQuarter(this.year, this.monthOfYear)

fun endTimeOfQuarter(year: Int, monthOfYear: Int): DateTime = TODO()
fun endTimeOfQuarter(year: Int, quarter: Quarter): DateTime = TODO()
fun YearQuarter.endTimeOfQuarter(): DateTime = TODO()
fun DateTime.endTimeOfQuarter(): DateTime = endTimeOfQuarter(this.year, this.monthOfYear)

fun startTimeOfMonth(year: Int, monthOfYear: Int): DateTime = asDate(year, monthOfYear, 1)
fun startTimeOfMonth(year: Int, month: Month): DateTime = startTimeOfMonth(year, month.value)
fun YearMonth.startTimeOfMonth(): DateTime = startTimeOfMonth(this.year, this.monthOfYear)
fun DateTime.startTimeOfMonth(): DateTime = startTimeOfMonth(this.year, this.monthOfYear)

fun endTimeOfMonth(year: Int, monthOfYear: Int): DateTime = startTimeOfMonth(year, monthOfYear).plusMonths(1).minusMillis(1)
fun endTimeOfMonth(year: Int, month: Month): DateTime = endTimeOfMonth(year, month.value)
fun YearMonth.endTimeOfMonth(): DateTime = endTimeOfMonth(this.year, this.monthOfYear)
fun DateTime.endTimeOfMonth(): DateTime = endTimeOfMonth(this.year, this.monthOfYear)

fun startTimeOfWeek(weekyear: Int, weekOfWeekyear: Int): DateTime =
    DateTime().withWeekyear(weekyear).withWeekOfWeekyear(weekOfWeekyear)

fun YearWeek.startTimeOfWeek(): DateTime = startTimeOfWeek(this.weekyear, this.weekOfWeekyear)
fun DateTime.startTimeOfWeek(): DateTime = startTimeOfWeek(this.weekyear, this.weekOfWeekyear)

fun endTimeOfWeek(weekyear: Int, weekOfWeekyear: Int): DateTime
    = startTimeOfWeek(weekyear, weekOfWeekyear).plusWeeks(1).minusMillis(1)

fun YearWeek.endTimeOfWeek(): DateTime = endTimeOfWeek(this.weekyear, this.weekOfWeekyear)
fun DateTime.endTimeOfWeek(): DateTime = endTimeOfWeek(this.weekyear, this.weekOfWeekyear)

fun DateTime.startTimeOfDay(): DateTime = this.withTimeAtStartOfDay()
fun DateTime.endTimeOfDay(): DateTime = startTimeOfDay().plusDays(1).minusMillis(1)

fun DateTime.startTimeOfHour(): DateTime = startTimeOfDay().plusHours(this.hourOfDay)
fun DateTime.endTimeOfHour(): DateTime = startTimeOfHour().plusHours(1).minusMillis(1)

fun DateTime.startTimeOfMinute(): DateTime = startTimeOfHour().plusMinutes(this.minuteOfHour)
fun DateTime.endTimeOfMinute(): DateTime = startTimeOfMinute().plusMinutes(1).minusMillis(1)

fun DateTime.startTimeOfSecond(): DateTime = startTimeOfHour().plusSeconds(this.secondOfMinute)
fun DateTime.endTimeOfSecond(): DateTime = startTimeOfSecond().plusSeconds(1).minusMillis(1)

fun halfyearOf(monthOfYear: Int): Halfyear = Halfyear.ofMonth(monthOfYear)
fun DateTime.halfyearOf(): Halfyear = Halfyear.ofMonth(this.monthOfYear)

fun Quarter.startMonthOfQuarter(): Int = this.ordinal * MonthsPerQuarter + 1
fun Quarter.endMonthOfQuarter(): Int = this.value * MonthsPerQuarter

fun quarterOf(monthOfYear: Int): Quarter = Quarter.ofMonth(monthOfYear)
fun DateTime.quarterOf(): Quarter = Quarter.ofMonth(this.monthOfYear)

/** 현 일자의 다음주 같은 요일 */
fun DateTime.nextDayOfWeek(): DateTime = this.plusWeeks(1)

fun DateTime.prevDayOfWeek(): DateTime = this.minusWeeks(1)

fun DateTime?.hasDate(): Boolean = this != null && this.withTimeAtStartOfDay().millis > 0

fun DateTime.setDatepart(date: DateTime): DateTime {
  return date.startTimeOfDay().withMillisOfDay(this.millisOfDay)
}

@JvmOverloads
fun DateTime.setDatepart(year: Int, monthOfYear: Int = 1, dayOfMonth: Int = 1): DateTime {
  return asDate(year, monthOfYear, dayOfMonth).withMillisOfDay(this.millisOfDay)
}

fun DateTime.setYear(year: Int): DateTime = this.withYear(year)
fun DateTime.setMonth(monthOfYear: Int): DateTime = this.withMonthOfYear(monthOfYear)
fun DateTime.setDay(dayOfMonth: Int): DateTime = this.withDayOfMonth(dayOfMonth)

fun DateTime.setTimepart(time: DateTime): DateTime {
  return this.startTimeOfDay().withMillisOfDay(time.millisOfDay)
}

@JvmOverloads
fun DateTime.setTimepart(hourOfDay: Int, minuteOfHour: Int = 0, secnodOfMinute: Int = 0, millisOfSeocond: Int = 0): DateTime {
  return this.startTimeOfDay().withTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond)
}

fun DateTime.getTimepart(): Int = this.millisOfDay
fun DateTime?.hasTimepart(): Boolean = this != null && this.millisOfDay > 0

fun combine(date: DateTime, time: DateTime): DateTime = date.setTimepart(time)
fun combine(date: LocalDate, time: LocalTime): DateTime = date.toDateTime(time)

fun DateTime.setHour(hourOfDay: Int): DateTime = this.withHourOfDay(hourOfDay)
fun DateTime.setMinute(minuteOfHour: Int): DateTime = this.withMinuteOfHour(minuteOfHour)
fun DateTime.setSecond(secondOfMinute: Int): DateTime = this.withSecondOfMinute(secondOfMinute)
fun DateTime.setMillis(millisOfSeocond: Int): DateTime = this.withMillisOfSecond(millisOfSeocond)
fun DateTime.setMillisOfDay(millsOfDay: Int): DateTime = this.withMillisOfDay(millisOfDay)


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

