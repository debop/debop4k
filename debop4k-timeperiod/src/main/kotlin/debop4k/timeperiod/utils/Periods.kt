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

@file:JvmName("Periods")

package debop4k.timeperiod.utils

import debop4k.core.NotSupportedException
import debop4k.core.collections.fastListOf
import debop4k.core.collections.parMap
import debop4k.core.collections.permutations.Permutation
import debop4k.core.collections.permutations.cons
import debop4k.core.collections.permutations.emptyPermutation
import debop4k.core.collections.permutations.permutationOf
import debop4k.core.kodatimes.*
import debop4k.core.loggerOf
import debop4k.core.utils.NULL_STRING
import debop4k.timeperiod.*
import debop4k.timeperiod.models.PeriodRelation
import debop4k.timeperiod.models.PeriodRelation.*
import debop4k.timeperiod.models.PeriodUnit
import debop4k.timeperiod.timeranges.*
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime
import org.joda.time.Duration

private val log = loggerOf("Periods")

fun adjustPeriod(start: DateTime?, end: DateTime?): Pair<DateTime?, DateTime?>
    = Pair(start min end, start max end)

fun adjustPeriod(start: DateTime, duration: Duration): Pair<DateTime, Duration> {
  return if (duration.millis >= 0) Pair(start, duration)
  else Pair(start + duration, -duration)
}

fun assertValidPeriod(start: DateTime?, end: DateTime?) {
  if (start != null && end != null) {
    assert(start <= end, { "시작시각이 완료시각보다 이전이어야 합니다. start=$start, end=$end" })
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

fun DateTime.timeBlock(duration: Duration): TimeBlock = TimeBlock(this, duration)
fun DateTime.timeBlock(end: DateTime): TimeBlock = TimeBlock(this, end)

fun DateTime.timeRange(duration: Duration): TimeRange = TimeRange(this, duration)
fun DateTime.timeRange(end: DateTime): TimeRange = TimeRange(this, end)

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
    PeriodUnit.SECOND -> TimeRange(this.startTimeOfSecond(), 1.seconds().duration)
    else ->
      throw UnsupportedOperationException("지원하지 않는 Period Unit 입니다. unit=$unit")
  }
}

@JvmOverloads
fun DateTime.periodOf(unit: PeriodUnit,
                      periodCount: Int,
                      calendar: ITimeCalendar = DefaultTimeCalendar): CalendarTimeRange {
  when (unit) {
    PeriodUnit.YEAR -> return YearTimeRange(this, periodCount, calendar)
    PeriodUnit.HALFYEAR -> return HalfyearTimeRange(this, periodCount, calendar)
    PeriodUnit.QUARTER -> return QuarterTimeRange(this, periodCount, calendar)
    PeriodUnit.MONTH -> return MonthTimeRange(this, periodCount, calendar)
    PeriodUnit.WEEK -> return WeekTimeRange(this, periodCount, calendar)
    PeriodUnit.DAY -> return DayTimeRange(this, periodCount, calendar)
    PeriodUnit.HOUR -> return HourTimeRange(this, periodCount, calendar)
    PeriodUnit.MINUTE -> return MinuteTimeRange(this, periodCount, calendar)
    PeriodUnit.SECOND -> {
      val start = this.trimToMillis()
      return CalendarTimeRange.of(start, start.plusSeconds(periodCount), calendar)
    }

    else -> throw NotSupportedException("지원하지 않는 PeriodUnit 입니다. unit=" + unit)
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
fun ITimePeriod.relationWith(target: ITimePeriod): PeriodRelation {
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

  log.debug("relationWith={}, period={}, target={}", relation, this, target)
  return relation
}

fun ITimePeriod.intersectWith(target: ITimePeriod): Boolean
    = hasInside(target.start) || hasInside(target.end) || target.hasPureInsideWith(this)

val NotOverlapedRelations: List<PeriodRelation> = listOf(After, StartTouching, EndTouching, Before)

fun ITimePeriod.overlapWith(target: ITimePeriod): Boolean
    = !NotOverlapedRelations.contains(this.relationWith(target))

fun ITimePeriod.intersectBlock(target: ITimePeriod): TimeBlock? {
  var intersection: TimeBlock? = null

  if (this.intersectWith(target)) {
    val start = this.start max target.start
    val end = this.end min target.end
    intersection = TimeBlock(start, end, this.readonly)
  }
  return intersection
}

fun ITimePeriod.intersectRange(target: ITimePeriod): TimeRange? {
  var intersection: TimeRange? = null

  if (this.intersectWith(target)) {
    val start = this.start max target.start
    val end = this.end min target.end
    intersection = TimeRange(start, end, this.readonly)
  }
  return intersection
}

fun ITimePeriod.unionBlock(target: ITimePeriod): TimeBlock {
  val start = this.start min target.start
  val end = this.end max target.end

  return TimeBlock(start, end, this.readonly)
}

fun ITimePeriod.unionRange(target: ITimePeriod): TimeRange {
  val start = this.start min target.start
  val end = this.end max target.end

  return TimeRange(start, end, this.readonly)
}

fun ITimePeriod.assertMutable(): Unit {
  assert(!this.readonly, { "ITimePeriod 가 읽기전용입니다." })
}

fun ITimePeriod.periodSequence(unit: PeriodUnit): FastList<ITimePeriod> = when (unit) {
  PeriodUnit.YEAR -> this.yearSequence()
  PeriodUnit.HALFYEAR -> this.halfyearSequence()
  PeriodUnit.QUARTER -> this.quarterSequence()
  PeriodUnit.MONTH -> this.monthSequence()
  PeriodUnit.WEEK -> this.weekSequence()
  PeriodUnit.DAY -> this.daySequence()
  PeriodUnit.HOUR -> this.hourSequence()
  PeriodUnit.MINUTE -> this.minuteSequence()
  else -> throw UnsupportedOperationException("지원하지 않는 PeriodUnit 입니다. unit=$unit")
}

// TODO: 실제로 Iterator 를 만들던가 Sequence 를 이용하여 만들던가 해야 한다
fun ITimePeriod.yearSequence(): FastList<ITimePeriod> {
  val years = fastListOf<ITimePeriod>()

  if (this.isAnyTime())
    return years

  if (this.start.isSameYear(this.end)) {
    years.add(TimeRange(this))
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


fun ITimePeriod.halfyearSequence(): FastList<ITimePeriod> {
  val halfyears = fastListOf<ITimePeriod>()

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

fun ITimePeriod.quarterSequence(): FastList<ITimePeriod> {
  val quarters = fastListOf<ITimePeriod>()

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

fun ITimePeriod.monthSequence(): FastList<ITimePeriod> {
  val months = fastListOf<ITimePeriod>()

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

fun ITimePeriod.weekSequence(): FastList<ITimePeriod> {
  val weeks = fastListOf<ITimePeriod>()
  if (isAnyTime()) {
    return weeks
  }

  if (start.isSameWeek(end)) {
    weeks.add(TimeRange(this))
    return weeks
  }

  var current = start
  val endWeek = current.endTimeOfWeek()
  val head = TimeRange(current, endWeek)
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

  return weeks
}

fun ITimePeriod.daySequence(): FastList<ITimePeriod> {
  val days = fastListOf<ITimePeriod>()

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

fun ITimePeriod.hourSequence(): FastList<ITimePeriod> {
  val hours = fastListOf<ITimePeriod>()

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

fun ITimePeriod.minuteSequence(): FastList<ITimePeriod> {
  val minutes = fastListOf<ITimePeriod>()

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

fun ITimePeriod.permutations(unit: PeriodUnit): Permutation<ITimePeriod> {
  return when (unit) {
    PeriodUnit.YEAR -> yearPermutation()
    PeriodUnit.HALFYEAR -> halfyearPermutation()
    PeriodUnit.QUARTER -> quarterPermutation()
    PeriodUnit.MONTH -> monthPermutation()
    PeriodUnit.WEEK -> weekPermutation()
    PeriodUnit.DAY -> dayPermutation()
    PeriodUnit.HOUR -> hourPermutation()
    PeriodUnit.MINUTE -> minutePermutation()
    else -> throw NotSupportedException("지원하지 않는 PeriodUnit 입니다. unit=[$unit]")
  }
}

fun ITimePeriod.yearPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime()) {
    return emptyPermutation()
  }

  if (start.isSameYear(end)) {
    return permutationOf(TimeRange(this))
  }

  val head = TimeRange(start, start.endTimeOfYear())
  val current = start.startTimeOfYear() + 1.years()
  val endYear = end.year
  val calendar = DefaultTimeCalendar

  fun nextYears(current: DateTime): Permutation<ITimePeriod> {
    return if (current.year < endYear) {
      permutationOf<ITimePeriod>(YearRange(current, calendar)) { nextYears(current.plusYears(1)) }
    } else if (current < end) {
      permutationOf<ITimePeriod>(TimeRange(current.startTimeOfYear(), end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextYears(current) }
}

fun ITimePeriod.halfyearPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime()) {
    return emptyPermutation()
  }

  assertHasPeriod()

  if (start.isSameHalfyear(end)) {
    return permutationOf(TimeRange(this))
  }

  val current = start.endTimeOfHalfyear()
  val head = TimeRange(start, current)
  val endHashcode = end.year * 10 + end.halfyearOf().value
  val calendar = DefaultTimeCalendar

  fun nextHalfyears(current: DateTime): Permutation<ITimePeriod> {
    return if (current.year * 10 + current.halfyearOf().value < endHashcode) {
      permutationOf<ITimePeriod>(HalfyearRange(current, calendar)) { nextHalfyears(current.plusMonths(MonthsPerHalfyear)) }
    } else if (current < end) {
      permutationOf<ITimePeriod>(TimeRange(current.startTimeOfHalfyear(), end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextHalfyears(current.withTimeAtStartOfDay() + 1.days()) }
}

fun ITimePeriod.quarterPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime())
    return emptyPermutation()

  assertHasPeriod()

  if (start.isSameQuarter(end)) {
    return permutationOf(TimeRange(this))
  }

  val current = start.endTimeOfQuarter()
  val head = TimeRange(start, current)
  val endHashcode = end.year * 10 + end.quarterOf().value
  val calendar = DefaultTimeCalendar

  fun nextQuarters(current: DateTime): Permutation<ITimePeriod> {
    return if (current.year * 10 + current.quarterOf().value < endHashcode) {
      permutationOf<ITimePeriod>(QuarterRange(current, calendar)) { nextQuarters(current.plusMonths(MonthsPerQuarter)) }
    } else if (current < end) {
      permutationOf<ITimePeriod>(TimeRange(current.startTimeOfQuarter(), end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextQuarters(current.withTimeAtStartOfDay() + 1.days()) }
}

fun ITimePeriod.monthPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime())
    return emptyPermutation()

  assertHasPeriod()

  if (start.isSameMonth(end)) {
    return permutationOf(TimeRange(this))
  }

  val current = start.endTimeOfMonth()
  val head = TimeRange(start, current)
  val monthEnd = end.startTimeOfMonth()
  val calendar = DefaultTimeCalendar

  fun nextMonths(current: DateTime): Permutation<ITimePeriod> {
    return if (current < monthEnd) {
      permutationOf<ITimePeriod>(MonthRange(current, calendar)) { nextMonths(current.plusMonths(1)) }
    } else if (current < end) {
      permutationOf<ITimePeriod>(TimeRange(current, end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextMonths(current.withTimeAtStartOfDay() + 1.days()) }
}

fun ITimePeriod.weekPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime())
    return emptyPermutation()

  assertHasPeriod()

  if (start.isSameWeek(end)) {
    return permutationOf(TimeRange(this))
  }

  val current = start.endTimeOfWeek()
  if (current >= end) {
    return permutationOf(TimeRange(this))
  }

  val head = TimeRange(start, current)
  val calendar = DefaultTimeCalendar

  fun nextWeeks(current: DateTime): Permutation<ITimePeriod> {
    return if (current < end) {
      permutationOf<ITimePeriod>(WeekRange(current, calendar)) { nextWeeks(current.plusWeeks(1)) }
    } else if (current < end) {
      permutationOf<ITimePeriod>(TimeRange(current, end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextWeeks(current.withTimeAtStartOfDay() + 1.days()) }
}

fun ITimePeriod.dayPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime())
    return emptyPermutation()

  assertHasPeriod()

  if (start.isSameDay(end)) {
    return permutationOf(TimeRange(this))
  }

  val endDay = end.startTimeOfDay()
  val current = start.startTimeOfDay()
  val head = TimeRange(start, start.endTimeOfDay())

  fun nextDays(current: DateTime): Permutation<ITimePeriod> {
    return if (current < endDay) {
      permutationOf<ITimePeriod>(DayRange(current, DefaultTimeCalendar)) { nextDays(current.plusDays(1)) }
    } else if (end.millisOfDay > 0) {
      permutationOf<ITimePeriod>(TimeRange(endDay, end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextDays(current.plusDays(1)) }
}

fun ITimePeriod.hourPermutation(): Permutation<ITimePeriod> {
  if (isAnyTime())
    return emptyPermutation()

  assertHasPeriod()

  if (start.isSameHour(end)) {
    return permutationOf(TimeRange(this))
  }

  val endHour = end.startTimeOfHour()
  val current = start.startTimeOfHour()
  val head = TimeRange(start, start.endTimeOfHour())

  fun nextHours(current: DateTime): Permutation<ITimePeriod> {
    return if (current < endHour) {
      permutationOf<ITimePeriod>(HourRange(current, DefaultTimeCalendar)) { nextHours(current.plusHours(1)) }
    } else if (end.minusHours(endHour.hourOfDay).millisOfDay > 0) {
      permutationOf<ITimePeriod>(TimeRange(current, end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextHours(current.plusHours(1)) }
}

fun ITimePeriod.minutePermutation(): Permutation<ITimePeriod> {
  if (isAnyTime())
    return emptyPermutation()

  assertHasPeriod()

  if (start.isSameMinute(end))
    return permutationOf(TimeRange(this))

  val endMin = end.startTimeOfMinute()
  val current = start.startTimeOfHour()
  val head = TimeRange(start, start.endTimeOfMinute())

  fun nextMinutes(current: DateTime): Permutation<ITimePeriod> {
    return if (current < endMin) {
      permutationOf<ITimePeriod>(MinuteRange(current, DefaultTimeCalendar)) { nextMinutes(current.plusMinutes(1)) }
    } else if (end.minusMinutes(endMin.minuteOfHour).millisOfDay > 0) {
      permutationOf<ITimePeriod>(TimeRange(current, end))
    } else {
      emptyPermutation()
    }
  }

  return cons(head) { nextMinutes(current.plusMinutes(1)) }
}

fun ITimePeriod?.assertHasPeriod(): Unit {
  assert(this != null && this.hasPeriod()) { "기간이 설정되지 않았습니다. period=$this" }
}

fun <R> ITimePeriod.mapPeriod(unit: PeriodUnit, func: (ITimePeriod) -> R): FastList<R> {
  return this.periodSequence(unit).collect(func)
}

fun <R> ITimePeriod.parMapPeriod(unit: PeriodUnit, func: (ITimePeriod) -> R): List<R> {
  return this.periodSequence(unit).parMap(mapper = func).toList()
}

fun ITimePeriod?.asString(): String = this?.toString() ?: NULL_STRING

fun Collection<ITimePeriod>.toTimePeriodCollection(): TimePeriodCollection
    = TimePeriodCollection.of(this)