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
@file:JvmName("Periods")

package debop4k.timeperiod.utils

import debop4k.core.NULL_STRING
import debop4k.core.collections.eclipseCollections.parallelCollect
import debop4k.core.kodatimes.*
import debop4k.timeperiod.*
import debop4k.timeperiod.models.PeriodRelation
import debop4k.timeperiod.models.PeriodRelation.*
import debop4k.timeperiod.models.PeriodUnit
import debop4k.timeperiod.timeranges.*
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("Periods")

fun adjustPeriod(start: DateTime, end: DateTime): Pair<DateTime, DateTime>
    = Pair(start min end, start max end)

fun adjustPeriod(start: DateTime, duration: Duration): Pair<DateTime, Duration> {
  if (duration.millis >= 0) return Pair(start, duration)
  return Pair(start + duration, -duration)
}

fun assertValidPeriod(start: DateTime?, end: DateTime?) {
  if (start != null && end != null) {
    assert(start >= end, { "시작시각이 완료시각보다 이전이어야 합니다. start=$start, end=$end" })
  }
}

fun allItemsAreEqual(left: Iterable<ITimePeriod>, right: Iterable<ITimePeriod>): Boolean {
  val l = left.iterator()
  val r = right.iterator()
  while (l.hasNext() && r.hasNext()) {
    if (l.next() != r.next())
      return false
  }
  return !l.hasNext() && !r.hasNext()
}

fun DateTime.timeBlock(duration: Duration): TimeBlock = TimeBlock.of(this, duration)
fun DateTime.timeBlock(end: DateTime): TimeBlock = TimeBlock.of(this, end)

fun DateTime.timeRange(duration: Duration): TimeRange = TimeRange.of(this, duration)
fun DateTime.timeRange(end: DateTime): TimeRange = TimeRange.of(this, end)

fun relativeYearPeriod(year: Int, yearCount: Int): TimeRange {
  return startTimeOfYear(year).relativeYearPeriod(yearCount)
}

fun DateTime.relativeYearPeriod(yearCount: Int): TimeRange {
  val start = this.startTimeOfYear()
  return TimeRange(start, start + yearCount.years())
}

fun DateTime.relativeHalfyearPeriod(halfyearCount: Int): TimeRange {
  val start = this.startTimeOfHalfyear()
  return TimeRange(start, start.plusMonths(MonthsPerHalfyear * halfyearCount))
}


fun DateTime.relativeQuarterPeriod(quarterCount: Int): TimeRange {
  val start = this.startTimeOfQuarter()
  return TimeRange(start, start.plusMonths(MonthsPerQuarter * quarterCount))
}

fun DateTime.relativeMonthPeriod(monthCount: Int): TimeRange {
  val start = this.startTimeOfMonth()
  return TimeRange(start, start + monthCount.months())
}

fun DateTime.relativeWeekPeriod(weekCount: Int): TimeRange {
  val start = this.startTimeOfWeek()
  return TimeRange(start, start + weekCount.weeks())
}

fun DateTime.relativeDayPeriod(dayCount: Int): TimeRange {
  val start = this.startTimeOfDay()
  return TimeRange(start, start + dayCount.days())
}

fun DateTime.relativeHourPeriod(hourCount: Int): TimeRange {
  val start = this.startTimeOfHour()
  return TimeRange(start, start + hourCount.hours())
}

fun DateTime.relativeMinutePeriod(minuteCount: Int): TimeRange {
  val start = this.startTimeOfMinute()
  return TimeRange(start, start + minuteCount.minutes())
}

fun DateTime.relativeSecondPeriod(secondCount: Int): TimeRange {
  val start = this.startTimeOfSecond()
  return TimeRange(start, start.plusSeconds(secondCount))
}


@JvmOverloads
fun DateTime.periodOf(unit: PeriodUnit, calendar: ITimeCalendar = DefaultTimeCalendar): ITimePeriod {
  return when (unit) {
    PeriodUnit.ALL -> TimeRange.AnyTime
    PeriodUnit.YEAR -> YearRange(this, calendar)
    PeriodUnit.HALFYEAR -> HalfyearRange(this, calendar)
    PeriodUnit.QUARTER -> QuarterRange(this, calendar)
    PeriodUnit.MONTH -> MonthRange(this, calendar)
    PeriodUnit.WEEK -> WeekRange(this, calendar)
    PeriodUnit.DAY -> DayRange(this, calendar)
    PeriodUnit.HOUR -> HourRange(this, calendar)
    PeriodUnit.MINUTE -> MinuteRange(this, calendar)
    PeriodUnit.SECOND -> TimeRange.of(this.startTimeOfSecond(), 1.seconds().duration)
    else ->
      throw UnsupportedOperationException("지원하지 않는 Period Unit 입니다. unit=$unit")
  }
}


fun ITimePeriod.hasInsideWith(moment: DateTime): Boolean
    = this.start <= moment && moment <= this.end

fun ITimePeriod.hasInsideWith(target: ITimePeriod): Boolean
    = this.hasInside(target.start) && this.hasInside(target.end)

fun ITimePeriod.hasPureInsideWith(moment: DateTime): Boolean
    = this.start < moment && moment < this.end

fun ITimePeriod.hasPureInsideWith(target: ITimePeriod): Boolean
    = this.hasPureInsideWith(target.start) && this.hasPureInsideWith(target.end)

fun ITimePeriod?.isAnyTime(): Boolean = (this != null) && this.isAnyTime()

fun ITimePeriod?.isNotAnyTime(): Boolean = (this != null) && !this.isAnyTime()

/**
 * 두 [ITimePeriod] 의 관계를 파악합니다.
 */
fun ITimePeriod.relation(target: ITimePeriod): PeriodRelation {
  var relation = NoRelation

  if (this.start > target.end) {
    relation = After
  } else if (this.end < target.start) {
    relation = Before
  } else if (this.isSamePeriod(target)) {
    relation = ExactMatch
  } else if (this.start == target.end) {
    relation = StartTouching
  } else if (this.end == target.start) {
    relation = EndTouching
  } else if (this.hasInside(target)) {
    if (this.start == target.start) {
      relation = EnclosingStartTouching
    } else if (this.end == target.end) {
      relation = EnclosingEndTouching
    } else {
      relation = Enclosing
    }
  } else {
    val insideStart = target.hasInside(this.start)
    val insideEnd = target.hasInside(this.end)

    if (insideStart && insideEnd) {
      if (this.start == target.start) {
        relation = InsideStartTouching
      } else if (this.end == target.end) {
        relation = InsideEndTouching
      } else {
        relation = Inside
      }
    } else if (insideStart) {
      relation = StartInside
    } else if (insideEnd) {
      relation = EndInside
    }
  }

  log.debug("relation={}, period={}, target={}", relation, this, target)
  return relation
}

fun ITimePeriod.intersectWith(target: ITimePeriod): Boolean
    = hasInside(target.start) || hasInside(target.end) || target.hasPureInsideWith(this)

val NotOverlapedRelations: List<PeriodRelation> = listOf(After, StartTouching, EndTouching, Before)

fun ITimePeriod.overlapWith(target: ITimePeriod): Boolean
    = NotOverlapedRelations.contains(this.relation(target))

fun ITimePeriod.intersectBlock(target: ITimePeriod): TimeBlock? {
  var intersection: TimeBlock? = null

  if (this.intersectWith(target)) {
    val start = this.start max target.start
    val end = this.end min target.end
    intersection = TimeBlock.of(start, end, this.readOnly)
  }
  return intersection
}

fun ITimePeriod.intersectRange(target: ITimePeriod): TimeRange? {
  var intersection: TimeRange? = null

  if (this.intersectWith(target)) {
    val start = this.start max target.start
    val end = this.end min target.end
    intersection = TimeRange.of(start, end, this.readOnly)
  }
  return intersection
}

fun ITimePeriod.unionBlock(target: ITimePeriod): TimeBlock {
  val start = this.start min target.start
  val end = this.end max target.end

  return TimeBlock.of(start, end, this.readOnly)
}

fun ITimePeriod.unionRange(target: ITimePeriod): TimeRange {
  val start = this.start min target.start
  val end = this.end max target.end

  return TimeRange.of(start, end, this.readOnly)
}

fun ITimePeriod.assertMutable(): Unit {
  assert(!this.readOnly, { "ITimePeriod 가 읽기전용입니다." })
}

fun ITimePeriod.periodStream(unit: PeriodUnit): FastList<out ITimePeriod> = when (unit) {
  PeriodUnit.YEAR -> this.yearStream()
  PeriodUnit.HALFYEAR -> this.halfyearStream()
  PeriodUnit.QUARTER -> this.quarterStream()
  PeriodUnit.MONTH -> this.monthStream()
  PeriodUnit.WEEK -> this.weekStream()
  PeriodUnit.DAY -> this.dayStream()
  PeriodUnit.HOUR -> this.hourStream()
  PeriodUnit.MINUTE -> this.minuteStream()
  else -> throw UnsupportedOperationException("지원하지 않는 PeriodUnit 입니다. unit=$unit")
}

fun ITimePeriod.yearStream(): FastList<out ITimePeriod> {
  val years = FastList.newList<ITimePeriod>()

  if (this.isAnyTime())
    return years

  if (this.start.isSameYear(this.end)) {
    years.add(TimeRange.of(this))
    return years
  }

  val head = TimeRange(this.start, this.start.endTimeOfYear())
  years.add(head)

  var current = this.start.startTimeOfYear().plusYears(1)
  val endYear = this.end.year
  val oneYear = 1.years()

  while (current.year < endYear) {
    years.add(YearRange(current))
    current += oneYear
  }
  if (current < this.end) {
    years.add(TimeRange(current, this.end))
  }

  return years
}


fun ITimePeriod.halfyearStream(): FastList<out ITimePeriod> {
  val halfyears = FastList.newList<ITimePeriod>()

  if (this.isAnyTime())
    return halfyears

  if (start.isSameHalfyear(end)) {
    halfyears.add(TimeRange(this))
    return halfyears
  }

  var current: DateTime = start.endTimeOfHalfyear()
  val head = TimeRange(start, current)
  halfyears.add(head)

  val endHashCode = end.year * 10 + end.halfyearOf().value

  current += 1.days()
  var currentHashCode = current.year * 10 + current.halfyearOf().value
  val oneHalfyear = MonthsPerHalfyear.months()

  while (currentHashCode < endHashCode) {
    halfyears.add(HalfyearRange(current))

    current += oneHalfyear
    currentHashCode = current.year * 10 + current.halfyearOf().value
  }

  if (current < end) {
    halfyears.add(TimeRange(current.startTimeOfHalfyear(), end))
  }


  return halfyears
}

fun ITimePeriod.quarterStream(): FastList<out ITimePeriod> {
  val quarters = FastList.newList<ITimePeriod>()

  if (this.isAnyTime()) {
    return quarters
  }

  if (start.isSameQuarter(end)) {
    quarters.add(TimeRange(this))
    return quarters
  }

  var current = start.endTimeOfQuarter()
  val head = TimeRange(start, current)
  quarters.add(head)

  val endHashCode = end.year * 10 + end.quarterOf().value

  current += 1.days()
  var currentHashCode = current.year * 10 + current.quarterOf().value
  val oneQuarter = MonthsPerQuarter.months()

  while (currentHashCode < endHashCode) {
    quarters.add(QuarterRange(current))

    current += oneQuarter
    currentHashCode = current.year * 10 + current.quarterOf().value
  }

  if (current < end) {
    quarters.add(TimeRange(current.startTimeOfQuarter(), end))
  }

  return quarters
}

fun ITimePeriod.monthStream(): FastList<out ITimePeriod> {
  val months = FastList.newList<ITimePeriod>()

  if (isAnyTime()) {
    return months
  }

  if (start.isSameMonth(end)) {
    months.add(TimeRange(this))
    return months
  }

  var current = start.endTimeOfMonth()
  val head = TimeRange(start, current)
  months.add(head)

  val endMonth = end.startTimeOfMonth()
  current += 1.days()

  val oneMonth = 1.months()

  while (current < endMonth) {
    months.add(MonthRange(current))
    current += oneMonth
  }
  val last = current.startTimeOfMonth()
  if (last < end) {
    months.add(TimeRange(last, end))
  }

  return months
}

fun ITimePeriod.weekStream(): FastList<out ITimePeriod> {
  val weeks = FastList.newList<ITimePeriod>()
  if (isAnyTime()) {
    return weeks
  }

  if (start.isSameWeek(end)) {
    weeks.add(TimeRange(this))
    return weeks
  }

  var current = start
  val endWeek = current.endTimeOfWeek()
  val head = TimeRange.of(current, endWeek)
  weeks.add(head)

  if (endWeek >= end) {
    return weeks
  }

  val startWeekOfEnd = end.startTimeOfWeek()
  current += 1.weeks()
  current = current.startTimeOfWeek()
  val oneWeek = 1.weeks()

  while (current < startWeekOfEnd) {
    weeks.add(WeekRange(current))
    current += oneWeek
  }

  if (current < end) {
    weeks.add(TimeRange(current, end))
  }

  return weeks;
}

fun ITimePeriod.dayStream(): FastList<out ITimePeriod> {
  val days = FastList.newList<ITimePeriod>()

  if (isAnyTime()) {
    return days
  }

  if (start.isSameDay(end)) {
    days.add(TimeRange(this))
    return days
  }

  val endDay = end.asDate()
  var current = start.asDate() + 1.days()

  val head = TimeRange(start, start.endTimeOfDay())
  days.add(head)

  val oneDay = 1.days()

  while (current < endDay) {
    days += DayRange(current)
    current += oneDay
  }
  if (end.hasTimepart()) {
    days.add(TimeRange(endDay, end))
  }

  return days

}

fun ITimePeriod.hourStream(): FastList<out ITimePeriod> {
  val hours = FastList.newList<ITimePeriod>()

  if (isAnyTime()) {
    return hours
  }

  if (start.isSameHour(end)) {
    hours += TimeRange(this)
    return hours
  }

  val head = TimeRange(start, start.endTimeOfHour())
  hours += head

  var current = start.trimToHour(start.hourOfDay + 1)
  val maxHour = end - 1.hours()
  val oneHour = 1.hours()

  while (current <= maxHour) {
    hours += HourRange(current)
    current += oneHour
  }

  if ((end - end.hourOfDay.hours()).hasTimepart()) {
    hours += TimeRange(end.startTimeOfHour(), end)
  }

  return hours
}

fun ITimePeriod.minuteStream(): FastList<out ITimePeriod> {
  val minutes = FastList.newList<ITimePeriod>()

  if (isAnyTime()) {
    return minutes
  }

  if (start.isSameWeek(end)) {
    minutes += TimeRange(this)
    return minutes
  }

  minutes += TimeRange(start, start.endTimeOfMinute())

  var current = start.trimToMinute(start.minuteOfHour + 1)
  val maxMin = end - 1.minutes()
  val oneMinute = 1.minutes()

  while (current <= maxMin) {
    minutes += MinuteRange(current)
    current += oneMinute
  }

  if (end.minusMinutes(end.minuteOfHour).hasTimepart()) {
    minutes += TimeRange(end.startTimeOfMinute(), end)
  }

  return minutes
}

fun ITimePeriod?.assertHasPeriod(): Unit {
  assert(this != null && this.hasPeriod(), { "기간이 설정되지 않았습니다. period=$this" })
}

fun <R> ITimePeriod.mapPeriod(unit: PeriodUnit, func: (ITimePeriod) -> R): FastList<R> {
  return this.periodStream(unit).collect(func)
}

fun <R> ITimePeriod.mapPeriodAsParallel(unit: PeriodUnit, func: (ITimePeriod) -> R): List<R> {
  return this.periodStream(unit).parallelCollect(func).toList()
}

fun ITimePeriod?.asString(): String = this?.toString() ?: NULL_STRING

fun Collection<ITimePeriod>.toTimePeriodCollection(): TimePeriodCollection
    = TimePeriodCollection.of(this)