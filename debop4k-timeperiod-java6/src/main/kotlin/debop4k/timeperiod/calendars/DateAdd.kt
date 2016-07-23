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

package debop4k.timeperiod.calendars

import debop4k.timeperiod.*
import debop4k.timeperiod.timelines.TimeGapCalculator
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

/**
 * 특정 일자로부터 미래로(Forward)로 특정 기간을 이후의 일자를 계산합니다.
 * 특정 기간 산정을 탐색을 통해 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
open class DateAdd {

  private val log = LoggerFactory.getLogger(javaClass)

  open protected val includePeriods: TimePeriodCollection = TimePeriodCollection()
  open protected val excludePeriods: TimePeriodCollection = TimePeriodCollection()

  /**
   * 시작 일자로부터 offset 기간이 지난 일자를 계산합니다. (기간에 포함될 기간과 제외할 기간을 명시적으로 지정해 놓을 수 있습니다.)
   *
   * @param start        시작 일자
   * @param offset       기간
   * @param seekBoundary 마지막 일자 포함 여부
   * @return 시작 일자로부터 offset 기간 이후의 일자
   */
  @JvmOverloads
  open fun add(start: DateTime,
               offset: Duration,
               seekBoundary: SeekBoundaryMode = SeekBoundaryMode.Next): DateTime? {
    log.debug("Add start={}, offset={}, seekBoundary={}", start, offset, seekBoundary)

    // 예외 조건이 없으면 단순 계산으로 처리
    if (includePeriods.isEmpty() && excludePeriods.isEmpty()) {
      return start.plus(offset)
    }

    val end =
        if (offset < Duration.ZERO)
          calculateEnd(start, offset.negated(), SeekDirection.Backward, seekBoundary)
        else
          calculateEnd(start, offset, SeekDirection.Forward, seekBoundary)

    log.debug("Add Results. end={}, remaining={}", end.first, end.second)
    return end.first
  }

  /**
   * 시작 일자로부터 offset 기간 이전의 일자를 계산합니다. (기간에 포함될 기간과 제외할 기간을 명시적으로 지정해 놓을 수 있습니다.)
   *
   * @param start        시작 일자
   * @param offset       기간
   * @param seekBoundary 마지막 일자 포함 여부
   * @return 시작 일자로부터 offset 기간 이전의 일자
   */
  @JvmOverloads
  open fun substract(start: DateTime,
                     offset: Duration,
                     seekBoundary: SeekBoundaryMode = SeekBoundaryMode.Next): DateTime? {
    log.debug("Subtract start={}, offset={}, seekBoundary={}", start, offset, seekBoundary)

    // 예외 조건이 없으면 단순 계산으로 처리
    if (includePeriods.isEmpty() && excludePeriods.isEmpty()) {
      return start - offset
    }

    val end =
        if (offset < Duration.ZERO)
          calculateEnd(start, offset.negated(), SeekDirection.Forward, seekBoundary)
        else
          calculateEnd(start, offset, SeekDirection.Backward, seekBoundary)

    log.debug("Subtract Results. end={}, remaining={}", end.first, end.second)
    return end.first
  }

  @JvmOverloads
  open protected fun calculateEnd(start: DateTime,
                                  offset: Duration?,
                                  seekDir: SeekDirection,
                                  seekBoundary: SeekBoundaryMode = SeekBoundaryMode.Next): Pair<DateTime?, Duration?> {
    require((offset?.millis ?: 0L) >= 0L) { "offset 값은 0 이상이어야 합니다. offset=$offset" }

    log.debug("calculateEnd start={}, offset={}, seekDir={}, seekBoundary={}", start, offset, seekDir, seekBoundary)

    val searchPeriods = TimePeriodCollection.of(includePeriods.periods)
    if (searchPeriods.isEmpty()) {
      searchPeriods += TimeRange.AnyTime
    }

    // available periods
    var availablePeriods = getAvailablePeriods(searchPeriods)
    if (availablePeriods.isEmpty()) {
      return Pair(null, offset)
    }

    val periodCombiner = TimePeriodCombiner<TimeRange>()
    availablePeriods = periodCombiner.combinePeriods(availablePeriods)

    val startPeriod = if (seekDir === SeekDirection.Forward)
      findNextPeriod(start, availablePeriods)
    else
      findPrevPeriod(start, availablePeriods)

    // 첫 시작 기간이 없다면 중단합니다.
    if (startPeriod.first == null) {
      return Pair(null, offset)
    }

    if (offset == Duration.ZERO) {
      return Pair(startPeriod.second, offset)
    }

    log.debug("startPeriod={}, offset={}", startPeriod, offset)

    return if (seekDir === SeekDirection.Forward)
      findPeriodForward(availablePeriods,
                        offset,
                        startPeriod.first,
                        startPeriod.second,
                        seekBoundary)
    else
      findPeriodBackward(availablePeriods,
                         offset,
                         startPeriod.first,
                         startPeriod.second,
                         seekBoundary)
  }

  private fun getAvailablePeriods(searchPeriods: ITimePeriodCollection): ITimePeriodCollection {
    val availablePeriods = TimePeriodCollection()

    if (excludePeriods.isEmpty()) {
      availablePeriods.addAll(searchPeriods)
    } else {
      val gapCalculator = TimeGapCalculator<TimeRange>()

      for (p in searchPeriods) {
        if (excludePeriods.hasOverlapPeriods(p)) {
          for (gap in gapCalculator.gaps(excludePeriods, p)) {
            availablePeriods += gap
          }
        } else {
          availablePeriods += p
        }
      }
    }
    log.trace("availablePeriods={}", availablePeriods)
    return availablePeriods
  }

  private fun findPeriodForward(availablePeriods: ITimePeriodCollection,
                                remaining: Duration?,
                                startPeriod: ITimePeriod?,
                                seekMoment: DateTime,
                                seekBoundary: SeekBoundaryMode): Pair<DateTime?, Duration?> {
    log.trace("findPeriodForward remaining={}", remaining)

    var _seekMoment = seekMoment
    var _remaining = remaining

    // DateTime end = null;
    val startIndex = availablePeriods.indexOf(startPeriod)
    val length = availablePeriods.size

    for (i in startIndex..(length - 1)) {
      val gap = availablePeriods[i]
      val gapRemaining = Duration(_seekMoment, gap.end)

      log.trace("gap={}, gamRemaining={}, remaning={}, seekMoment={}", gap, gapRemaining, _remaining, _seekMoment)

      val isTargetPeriod =
          if (seekBoundary === SeekBoundaryMode.Fill)
            gapRemaining >= _remaining
          else
            gapRemaining > _remaining

      if (isTargetPeriod) {
        log.debug("find datetime={}", seekMoment.plus(_remaining))
        return Pair(seekMoment.plus(_remaining), null)
      }
      _remaining = _remaining?.minus(gapRemaining)

      if (i < length - 1)
        _seekMoment = availablePeriods[i + 1].start
    }

    log.debug("해당 일자를 찾지 못했습니다. remaining={}", _remaining)

    return Pair(null, _remaining)
  }

  private fun findPeriodBackward(availablePeriods: ITimePeriodCollection,
                                 remaining: Duration?,
                                 startPeriod: ITimePeriod?,
                                 seekMoment: DateTime,
                                 seekBoundary: SeekBoundaryMode): Pair<DateTime?, Duration?> {
    log.trace("findPeriodBackward remaining={}", remaining)
    // DateTime end = null;
    val startIndex = availablePeriods.indexOf(startPeriod)

    var _seekMoment = seekMoment
    var _remaining = remaining

    (startIndex downTo 0).forEach { i ->
      val gap = availablePeriods[i]
      val gapRemaining = Duration(gap.start, _seekMoment)

      log.trace("gap={}, gamRemaining={}, remaning={}, seekMoment={}",
                gap, gapRemaining, _remaining, _seekMoment)

      val isTargetPeriod =
          if (seekBoundary === SeekBoundaryMode.Fill)
            gapRemaining >= _remaining
          else
            gapRemaining > _remaining

      if (isTargetPeriod) {
        log.trace("find datetime={}", _seekMoment.minus(_remaining))
        return Pair(_seekMoment.minus(_remaining), null)
      }
      _remaining = _remaining?.minus(gapRemaining)

      if (i > 0) {
        _seekMoment = availablePeriods[i - 1].end
      }
    }

    log.debug("해당 일자를 찾지 못했습니다. remaining={}", _remaining)

    return Pair(null, _remaining)
  }

  private fun findNextPeriod(start: DateTime,
                             periods: Collection<ITimePeriod>): Pair<ITimePeriod?, DateTime> {
    var nearest: ITimePeriod? = null
    var moment = start
    var diff = MaxDuration

    log.trace("find next period. start={}, periods={}", start, periods)

    periods.forEach { period ->
      if (period.end >= start) {
        // start 가 기간와 속한다면
        if (period.hasInside(start)) {
          return Pair(period, start)
        }

        // 근처 값이 아니라면 포기
        val periodToMoment = Duration(start, period.end)
        if (periodToMoment < diff) {
          diff = periodToMoment
          nearest = period
          moment = period.start
        }
      }
    }
    return Pair(nearest, moment)
  }

  private fun findPrevPeriod(start: DateTime,
                             periods: Collection<ITimePeriod>): Pair<ITimePeriod?, DateTime> {
    var nearest: ITimePeriod? = null
    var moment = start
    var diff = MaxDuration

    log.trace("find prev period. start={}, periods={}", start, periods)

    periods.forEach { period ->
      if (period.start <= start) {
        // start 가 기간와 속한다면
        if (period.hasInside(start)) {
          return Pair(period, start)
        }

        // 근처 값이 아니라면 포기
        val periodToMoment = Duration(start, period.end)
        if (periodToMoment < diff) {
          diff = periodToMoment
          nearest = period
          moment = period.end
        }
      }
    }
    return Pair(nearest, moment)
  }
}