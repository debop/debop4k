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

@file:JvmName("KodaTimes")

package debop4k.core.kodatimes

import org.eclipse.collections.api.set.ImmutableSet
import org.joda.time.*
import org.joda.time.base.AbstractInstant
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import java.sql.Timestamp
import java.util.*

val EPOCH: DateTime = DateTime(0)

// yyyy-MM-dd'T'HH:mm:ss.SSSZZ
val JODA_DEFAULT_DATETIME_FORMATTER: DateTimeFormatter = ISODateTimeFormat.dateTime()

/** 기본 [DateTimeFormatter] */
private val defaultFormatter: DateTimeFormatter = ISODateTimeFormat.dateTime()

/** Timestamp 값을 [DateTime] 으로 변환합니다 */
fun Long.toDateTime(): DateTime = DateTime(this)

@JvmOverloads
fun String?.parse(formatter: DateTimeFormatter = defaultFormatter): DateTime
    = if (this.isNullOrBlank()) DateTime(0) else DateTime.parse(this, formatter)

fun DateTime.weekyearAndWeekOfWeekyear(): Pair<Int, Int>
    = Pair(this.weekyear, this.weekOfWeekyear)

fun DateTime.monthAndWeekOfMonth(): Pair<Int, Int> {
  val result = weekOfWeekyear - startOfMonth().weekOfWeekyear + 1
  if (result > 0)
    return Pair(monthOfYear, result)

  return Pair(this.plusMonths(1).monthOfYear, 1)
}

fun utcNow(): DateTime = DateTime.now(DateTimeZone.UTC)

fun DateTime.toISOString(): String = JODA_DEFAULT_DATETIME_FORMATTER.print(this)
fun DateTime.toISODateHourMinuteSecond(): String = ISODateTimeFormat.dateHourMinuteSecond().print(this)

fun DateTime?.asIsoFormatDateTimeString(): String? {
  return if (this != null) ISODateTimeFormat.dateTime().print(this) else null
}

fun String?.asIsoFormatDateTime(): DateTime? {
  return if (!isNullOrBlank())
    DateTime.parse(this, ISODateTimeFormat.dateTime().withOffsetParsed())
  else
    null
}

fun DateTime?.asIsoFormatDateHMSString(): String? {
  return if (this != null) ISODateTimeFormat.dateHourMinuteSecond().print(this) else null
}

fun String?.asIsoFormatDateHMS(): DateTime? {
  return if (!isNullOrBlank())
    DateTime.parse(this, ISODateTimeFormat.dateHourMinuteSecond().withOffsetParsed())
  else
    null
}

fun String?.asDateTimeByPattern(pattern: String): DateTime? {
  return if (!isNullOrBlank())
    DateTime.parse(this, DateTimeFormat.forPattern(pattern))
  else
    null
}


fun String?.toDateTime(formatter: DateTimeFormatter): DateTime {
  if (this.isNullOrBlank())
    return DateTime(0)
  return DateTime.parse(this, formatter)
}

fun DateTime.startOfDay(): DateTime = this.withTimeAtStartOfDay()
fun DateTime.endOfDay(): DateTime = this.startOfDay().plusDays(1).minusMillis(1)
fun DateTime.startOfWeek(): DateTime = this.minusDays(this.dayOfWeek - DateTimeConstants.MONDAY).withTimeAtStartOfDay()
fun DateTime.endOfWeek(): DateTime = this.startOfWeek().plusDays(7).minusMillis(1)
fun DateTime.startOfMonth(): DateTime = this.withDate(this.year, this.monthOfYear, 1)
fun DateTime.endOfMonth(): DateTime = this.startOfMonth().plusMonths(1).minusMillis(1)
fun DateTime.startOfYear(): DateTime = this.withDate(this.year, 1, 1)
fun DateTime.endOfYear(): DateTime = this.startOfYear().plusYears(1).minusMillis(1)

fun DateTime.getWeekyearAndWeekOfWeekyear(): Pair<Int, Int> {
  return Pair(this.weekyear, this.weekOfWeekyear)
}

fun DateTime.getMonthAndWeekOfMonth(): Pair<Int, Int> {
  val result = this.weekOfWeekyear - this.startOfMonth().weekOfWeekyear + 1
  if (result > 0)
    return Pair(this.monthOfYear, result)

  return Pair(this.plusMonths(1).monthOfYear, 1)
}


val DefaultTimeZone: DateTimeZone get() = DateTimeZone.getDefault()

@JvmOverloads
fun Date?.toDateTime(zone: DateTimeZone = DefaultTimeZone): DateTime? {
  if (this == null)
    return null
  return DateTime(this.time, zone)
}

fun Date.toLocalDateTime(): LocalDateTime = LocalDateTime.fromDateFields(this)
fun Date.toLocalDate(): LocalDate = LocalDate.fromDateFields(this)
fun Date.toLocalTime(): LocalTime = LocalTime.fromDateFields(this)

@JvmOverloads
fun Timestamp?.toDateTime(zone: DateTimeZone = DefaultTimeZone): DateTime? {
  if (this == null)
    return null
  return DateTime(this.time, zone)
}

fun AbstractInstant.dateTimeUTC(): DateTime = this.toDateTime(DateTimeZone.UTC)
fun AbstractInstant.mutableDateTimeUTC(): MutableDateTime = this.toMutableDateTime(DateTimeZone.UTC)

fun Int.millis(): DurationBuilder = DurationBuilder(Period.millis(this))
fun Int.seconds(): DurationBuilder = DurationBuilder(Period.seconds(this))
fun Int.minutes(): DurationBuilder = DurationBuilder(Period.minutes(this))
fun Int.hours(): DurationBuilder = DurationBuilder(Period.hours(this))

fun Int.days(): Period = Period.days(this)
fun Int.weeks(): Period = Period.weeks(this)
fun Int.months(): Period = Period.months(this)
fun Int.years(): Period = Period.years(this)

fun Int.times(builder: DurationBuilder): DurationBuilder = DurationBuilder(builder.period.multipliedBy(this))
fun Int.times(period: Period): Period = period.multipliedBy(this)

/** 해당년도의 일수 (day count) */
fun Int.dayCountOfYear(): Int = asDate(this + 1).minusMillis(1).dayOfYear

fun Int.startTimeOfYear(): DateTime = asDate(this, 1, 1)
fun Int.endTimeOfYear(): DateTime = startTimeOfYear().plusYears(1).minusDays(1)


fun Long.millis(): DurationBuilder = DurationBuilder(Period.millis(this.toInt()))
fun Long.seconds(): DurationBuilder = DurationBuilder(Period.seconds(this.toInt()))
fun Long.minutes(): DurationBuilder = DurationBuilder(Period.minutes(this.toInt()))
fun Long.hours(): DurationBuilder = DurationBuilder(Period.hours(this.toInt()))

fun Long.standardMillis(): Duration = Duration.millis(this)
fun Long.standardSeconds(): Duration = Duration.standardSeconds(this)
fun Long.standardMinutes(): Duration = Duration.standardMinutes(this)
fun Long.standardHours(): Duration = Duration.standardHours(this)
fun Long.standardDays(): Duration = Duration.standardDays(this)

fun Long.days(): Period = Period.days(this.toInt())
fun Long.weeks(): Period = Period.weeks(this.toInt())
fun Long.months(): Period = Period.months(this.toInt())
fun Long.years(): Period = Period.years(this.toInt())

fun Long.times(builder: DurationBuilder): DurationBuilder = DurationBuilder(builder.period.multipliedBy(this.toInt()))
fun Long.times(period: Period): Period = period.multipliedBy(this.toInt())

@JvmOverloads
fun Long.asDateTime(zone: DateTimeZone = DefaultTimeZone): DateTime = DateTime(this, zone)


/**
 * String extensions
 */
fun dateTimeFormat(pattern: String): DateTimeFormatter = DateTimeFormat.forPattern(pattern)

@JvmOverloads
fun String.toDateTime(pattern: String? = null): DateTime? {
  return try {
    if (pattern.isNullOrBlank()) DateTime(this)
    else DateTime.parse(this, dateTimeFormat(pattern!!))
  } catch(ignored: Exception) {
    null
  }
}

fun String?.tryToDateTime(defaultValue: DateTime): DateTime {

  try {
    return this?.toDateTime(ISODateTimeFormat.dateTime()) ?: defaultValue
  } catch(e: Exception) {
    return defaultValue
  }
}

fun String.toInterval(): Interval? {
  return try {
    Interval.parse(this)
  } catch(ignored: Exception) {
    null
  }
}

fun String.toLocalDate(pattern: String? = null): LocalDate? {
  return try {
    if (pattern.isNullOrBlank()) LocalDate(this)
    else LocalDate.parse(this, dateTimeFormat(pattern!!))
  } catch(ignored: Exception) {
    null
  }
}

fun String.toLocalTime(pattern: String? = null): LocalTime? {
  return try {
    if (pattern.isNullOrBlank()) LocalTime(this) else LocalTime.parse(this, dateTimeFormat(pattern!!))
  } catch(ignored: Exception) {
    null
  }
}

fun dateTimeFromJson(json: String): DateTime = DateTime(json)
fun dateTimeOf(year: Int, month: Int, day: Int): DateTime = DateTime(year, month, day, 0, 0)

fun adjustPeriod(a: DateTime, b: DateTime): Pair<DateTime, DateTime> {
  return Pair(a min b, a max b)
}

fun adjustPeriod(m: DateTime, d: Duration): Pair<DateTime, Duration> {
  if (d.millis >= 0)
    return Pair(m, d)
  return Pair(m.plus(d), d.negated())
}

fun DateTime.trimToYear(): DateTime = asDate(this.year, 1, 1)
@JvmOverloads fun DateTime.trimToMonth(monthOfYear: Int = 1): DateTime = asDate(this.year, monthOfYear, 1)
@JvmOverloads fun DateTime.trimToDay(dayOfMonth: Int = 1): DateTime = asDate(this.year, this.monthOfYear, dayOfMonth)
@JvmOverloads fun DateTime.trimToHour(hourOfDay: Int = 0): DateTime
    = trimToDay(this.dayOfMonth).withHourOfDay(hourOfDay)

@JvmOverloads fun DateTime.trimToMinute(minuteOfHour: Int = 0): DateTime
    = trimToHour(this.hourOfDay).withMinuteOfHour(minuteOfHour)

@JvmOverloads fun DateTime.trimToSecond(secondOfMinute: Int = 0): DateTime
    = trimToMinute(this.minuteOfHour).withSecondOfMinute(secondOfMinute)

@JvmOverloads fun DateTime.trimToMillis(millisOfSecond: Int = 0): DateTime
    = this.withMillisOfSecond(millisOfSecond)


operator fun DateTime.minus(builder: DurationBuilder): DateTime = this.minus(builder.period)
operator fun DateTime.plus(builder: DurationBuilder): DateTime = this.plus(builder.period)

fun DateTime.ago(duration: ReadableDuration): DateTime = this.minus(duration)
fun DateTime.since(duration: ReadableDuration): DateTime = this.plus(duration)
fun DateTime.from(duration: ReadableDuration): DateTime = this.plus(duration)
fun fromNow(duration: ReadableDuration): DateTime = now().from(duration)

fun DateTime.tomorrow(): DateTime = this.nextDay()
fun DateTime.yesterday(): DateTime = this.lastDay()

fun DateTime.nextSecond(): DateTime = this.plusSeconds(1)
fun DateTime.nextMinute(): DateTime = this.plusMinutes(1)
fun DateTime.nextHour(): DateTime = this.plusHours(1)
fun DateTime.nextDay(): DateTime = this.plusDays(1)
fun DateTime.nextWeek(): DateTime = this.plusWeeks(1)
fun DateTime.nextMonth(): DateTime = this.plusMonths(1)
fun DateTime.nextYear(): DateTime = this.plusYears(1)

fun DateTime.lastSecond(): DateTime = this.minusSeconds(1)
fun DateTime.lastMinute(): DateTime = this.minusMinutes(1)
fun DateTime.lastHour(): DateTime = this.minusHours(1)
fun DateTime.lastDay(): DateTime = this.minusDays(1)
fun DateTime.lastWeek(): DateTime = this.minusWeeks(1)
fun DateTime.lastMonth(): DateTime = this.minusMonths(1)
fun DateTime.lastYear(): DateTime = this.minusYears(1)

fun DateTime.asTimestamp(): Timestamp = Timestamp(this.millis)
fun DateTime.toTimestamp(): Timestamp = Timestamp(this.millis)
fun DateTime.asUtc(): DateTime = this.toDateTime(DateTimeZone.UTC)
@JvmOverloads
fun DateTime.asLocal(tz: DateTimeZone = DateTimeZone.getDefault()): DateTime = this.toDateTime(tz)

fun DateTime.toIsoFormatString(): String = ISODateTimeFormat.dateTime().print(this)
fun DateTime.toIsoFormatDateString(): String = ISODateTimeFormat.date().print(this)
fun DateTime.toIsoFormatTimeString(): String = ISODateTimeFormat.time().print(this)
fun DateTime.toIsoFormatTimeNoMillisString(): String = ISODateTimeFormat.timeNoMillis().print(this)
fun DateTime.toIsoFormatHMSString(): String = ISODateTimeFormat.dateHourMinuteSecond().print(this)

fun DateTime.toTimestampZoneText(): TimestampZoneText = TimestampZoneText(this)

infix fun DateTime.min(that: DateTime): DateTime {
  return if (this.compareTo(that) < 0) this else that
}

infix fun DateTime.max(that: DateTime): DateTime {
  return if (this.compareTo(that) > 0) this else that
}

fun DateTime.monthInterval(): Interval {
  val start = this.withDayOfMonth(1).withTimeAtStartOfDay()
  return Interval(start, start + 1.months())
}

fun DateTime.dayInterval(): Interval {
  val start = this.startOfDay()
  return Interval(start, start + 1.days())
}

val UnixEpoch = DateTime(0, DateTimeZone.UTC)

fun now(): DateTime = DateTime.now()
fun now(zone: DateTimeZone): DateTime = DateTime.now(zone)
fun nowUtc(): DateTime = DateTime.now(DateTimeZone.UTC)
fun tomorrow(): DateTime = now().tomorrow()
fun yesterday(): DateTime = now().yesterday()
fun today(): DateTime = now().withTimeAtStartOfDay()
fun noon(): DateTime = today() + 12.hours()
fun zeroTime(): DateTime = UnixEpoch

fun nextSecond(): DateTime = now().plusSeconds(1)
fun nextMinute(): DateTime = now().plusMinutes(1)
fun nextHour(): DateTime = now().plusHours(1)
fun nextDay(): DateTime = now().plusDays(1)
fun nextWeek(): DateTime = now().plusWeeks(1)
fun nextMonth(): DateTime = now().plusMonths(1)
fun nextYear(): DateTime = now().plusYears(1)

fun lastSecond(): DateTime = now().minusSeconds(1)
fun lastMinute(): DateTime = now().minusMinutes(1)
fun lastHour(): DateTime = now().minusHours(1)
fun lastDay(): DateTime = now().minusDays(1)
fun lastWeek(): DateTime = now().minusWeeks(1)
fun lastMonth(): DateTime = now().minusMonths(1)
fun lastYear(): DateTime = now().minusYears(1)

fun DateTime.asDate(): DateTime = this.withTimeAtStartOfDay()

@JvmOverloads
fun asDate(year: Int,
           monthOfYear: Int = 1,
           dayOfMonth: Int = 1,
           hourOfDay: Int = 0,
           minuteOfHour: Int = 0,
           secondOfMinute: Int = 0,
           millisOfSecond: Int = 0): DateTime {
  return DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond)
}

@JvmOverloads
fun asDateTime(year: Int,
               monthOfYear: Int = 1,
               dayOfMonth: Int = 1,
               hourOfDay: Int = 0,
               minuteOfHour: Int = 0,
               secondOfMinute: Int = 0,
               millisOfSecond: Int = 0): DateTime {
  return DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond)
}

operator fun LocalDateTime.minus(builder: DurationBuilder): LocalDateTime = this.minus(builder.period)
operator fun LocalDateTime.plus(builder: DurationBuilder): LocalDateTime = this.plus(builder.period)

operator fun LocalDate.minus(builder: DurationBuilder): LocalDate = this.minus(builder.period)
operator fun LocalDate.plus(builder: DurationBuilder): LocalDate = this.plus(builder.period)

operator fun LocalTime.minus(builder: DurationBuilder): LocalTime = this.minus(builder.period)
operator fun LocalTime.plus(builder: DurationBuilder): LocalTime = this.plus(builder.period)


/**
 * [Duration] extensions
 */
val emptyDuration: Duration = Duration.ZERO

fun Duration.days(): Long = this.standardDays
fun Duration.hours(): Long = this.standardHours
fun Duration.minutes(): Long = this.standardMinutes
fun Duration.seconds(): Long = this.standardSeconds

fun Duration.abs(): Duration = if (this < emptyDuration) -this else this
fun Duration.fromNow(): DateTime = now() + this
fun Duration.agoNow(): DateTime = now() - this
fun Duration.afterEpoch(): DateTime = DateTime(0) + this
fun Duration.diff(other: Duration): Duration = this - other

operator fun Duration.unaryMinus(): Duration = this.negated()
operator fun Duration.div(divisor: Int): Duration = this.dividedBy(divisor.toLong())
operator fun Duration.div(divisor: Long): Duration = this.dividedBy(divisor)
operator fun Duration.times(multiplicand: Int): Duration = this.multipliedBy(multiplicand.toLong())
operator fun Duration.times(multiplicand: Long): Duration = this.multipliedBy(multiplicand)

fun Duration.isZero(): Boolean = this.millis == 0L

infix fun Duration.min(that: Duration): Duration = if (this.compareTo(that) < 0) this else that
infix fun Duration.max(that: Duration): Duration = if (this.compareTo(that) > 0) this else that

/**
 * [Period] extensions
 */
fun Period.ago(): DateTime = DateTime.now() - this

fun Period.since(): DateTime = DateTime.now() + this

fun Period.later(): DateTime = DateTime.now() + this
fun Period.from(moment: DateTime): DateTime = moment + this
fun Period.before(moment: DateTime): DateTime = moment - this
val Period.standardDuration: Duration get() = this.toStandardDuration()

fun periodOfYears(y: Int): Period = Period.years(y)
fun periodOfMonths(m: Int): Period = Period.months(m)
fun periodOfWeek(w: Int): Period = Period.weeks(w)
fun periodOfDay(d: Int): Period = Period.days(d)
fun periodOfHours(h: Int): Period = Period.hours(h)
fun periodOfMinutes(m: Int): Period = Period.minutes(m)
fun periodOfSeconds(s: Int): Period = Period.seconds(s)
fun periodOfMillis(m: Int): Period = Period.millis(m)

operator fun Instant.minus(builder: DurationBuilder): Instant = this.minus(builder.period.toStandardDuration())
operator fun Instant.plus(builder: DurationBuilder): Instant = this.plus(builder.period.toStandardDuration())

fun thisSecond(): Interval = now().secondOfMinute().toInterval()
fun thisMinute(): Interval = now().minuteOfHour().toInterval()
fun thisHour(): Interval = now().hourOfDay().toInterval()

fun DateTimeZone.timeZoneOffset(): Int = this.getOffset(0)
fun String.timeZoneOffset(): Int = DateTimeZone.forID(this).timeZoneOffset()
fun Int.timeZoneForOffsetMillis(): DateTimeZone = DateTimeZone.forOffsetMillis(this)

fun availableTimeZone(): ImmutableSet<DateTimeZone> = TimeZones.Zones
fun availableOffsetMillis(): ImmutableSet<Int> = TimeZones.Offsets


//
// [ReadableInstant] .. [ReadableInstant] => [Interval]
//
/**
 * 시각 .. 시각 => [Interval]이 된다.
 */
operator fun ReadableInstant.rangeTo(other: ReadableInstant): Interval = Interval(this, other)

fun ReadableInterval.millis(): Long = this.toDurationMillis()

infix fun ReadableInterval.step(instance: ReadablePeriod): Sequence<DateTime> {
  return generateSequence(start + instance) { it + instance }.takeWhile { it <= end }
}

/** 기간을 초 단위로 열거합니다. */
fun ReadableInterval.seconds(): Sequence<DateTime> {
  return generateSequence(start) { it.plusSeconds(1) }.takeWhile { it <= end }
}

/** 기간을 분 단위로 열거합니다. */
fun ReadableInterval.minutes(): Sequence<DateTime> {
  return generateSequence(start) { it.plusMinutes(1) }.takeWhile { it <= end }
}

/** 기간을 시간 단위로 열거합니다. */
fun ReadableInterval.hours(): Sequence<DateTime> {
  return generateSequence(start) { it.plusHours(1) }.takeWhile { it <= end }
}

/** 기간을 일 단위로 열거합니다. */
fun ReadableInterval.days(): Sequence<DateTime> {
  return generateSequence(start.startOfDay()) { it.plusDays(1) }.takeWhile { it <= end }
}

/** 기간을 주 단위로 열거합니다. */
fun ReadableInterval.weeks(): Sequence<DateTime> {
  return generateSequence(start.startOfWeek()) { it.plusWeeks(1) }.takeWhile { it <= end }
}

/** 기간을 월 단위로 열거합니다. */
fun ReadableInterval.months(): Sequence<DateTime> {
  return generateSequence(start.startOfMonth()) { it.plusMonths(1) }.takeWhile { it <= end }
}


