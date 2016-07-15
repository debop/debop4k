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
import org.eclipse.collections.api.LazyIterable
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
fun DateTime.relativeYearPeriod(years: Int): TimeRange {
  val startYear = this.startOfYear()
  return startYear.timeRange(startYear + years.years())
}

fun DateTime.relativeHalfyearPeriod(halfyearCount: Int): TimeRange = TODO()

fun DateTime.relativeQuarterPeriod(quarterCount: Int): TimeRange = TODO()

fun DateTime.relativeMonthPeriod(monthCount: Int): TimeRange = TODO()

fun DateTime.relativeWeekPeriod(weekCount: Int): TimeRange = TODO()

fun DateTime.relativeDayPeriod(dayCount: Int): TimeRange = TODO()

fun DateTime.relativeHourPeriod(hourCount: Int): TimeRange = TODO()
fun DateTime.relativeMinutePeriod(minuteCount: Int): TimeRange = TODO()
fun DateTime.relativeSecondPeriod(secondCount: Int): TimeRange = TODO()
fun DateTime.relativeMillisPeriod(millisCount: Int): TimeRange = TODO()

fun DateTime.periodOf(unit: PeriodUnit, calendar: ITimeCalendar = DefaultTimeCalendar): ITimePeriod {
  return when (unit) {
    PeriodUnit.ALL -> TimeRange.AnyTime
    PeriodUnit.YEAR -> TODO()
    PeriodUnit.HALFYEAR -> TODO()
    PeriodUnit.QUARTER -> TODO()
    PeriodUnit.MONTH -> TODO()
    PeriodUnit.WEEK -> TODO()
    PeriodUnit.DAY -> TODO()
    PeriodUnit.HOUR -> TODO()
    PeriodUnit.MINUTE -> TODO()
    PeriodUnit.SECOND -> TODO()
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

fun ITimePeriod.periodStream(unit: PeriodUnit): LazyIterable<out ITimePeriod> = when (unit) {
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

fun ITimePeriod.yearStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.halfyearStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.quarterStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.monthStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.weekStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.dayStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.hourStream(): LazyIterable<out ITimePeriod> = TODO()
fun ITimePeriod.minuteStream(): LazyIterable<out ITimePeriod> = TODO()

fun ITimePeriod?.assertHasPeriod(): Unit {
  assert(this != null && this.hasPeriod(), { "기간이 설정되지 않았습니다. period=$this" })
}

fun <R> ITimePeriod.mapPeriod(unit: PeriodUnit, func: (ITimePeriod) -> R): LazyIterable<R> {
  return this.periodStream(unit).collect(func)
}

fun <R> ITimePeriod.mapPeriodAsParallel(unit: PeriodUnit, func: (ITimePeriod) -> R): List<R> {
  return this.periodStream(unit).parallelCollect(func).toList()
}

fun ITimePeriod?.asString(): String = this?.toString() ?: NULL_STRING

fun Collection<ITimePeriod>.toTimePeriodCollection(): TimePeriodCollection
    = TimePeriodCollection.of(this)