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

import debop4k.core.kodatimes.now
import debop4k.timeperiod.*
import debop4k.timeperiod.models.DayOfWeek
import debop4k.timeperiod.timelines.TimeGapCalculator
import debop4k.timeperiod.timeranges.DayOfWeekHourRange
import debop4k.timeperiod.timeranges.HourRangeInDay
import debop4k.timeperiod.utils.startTimeOfDay
import org.eclipse.collections.api.set.MutableSet
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

/**
 * 특정 기간의 [Duration] 을 Calendar의 예외 일자를 고려하여 계산합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
open class CalendarDateDiff(val calendar: ITimeCalendar = TimeCalendar.EMPTY_OFFSET) {

  init {
    require(calendar.startOffset.millis == 0L)
    require(calendar.endOffset.millis == 0L)
  }

  private val log = LoggerFactory.getLogger(javaClass)

  val collectorFilter = CalendarPeriodCollectorFilter()

  val weekDays: MutableSet<DayOfWeek>
    get() = collectorFilter.weekOfDays

  val workingHours: FastList<HourRangeInDay>
    get() = collectorFilter.collectingHours

  val workingDayHours: FastList<DayOfWeekHourRange>
    get() = collectorFilter.collectingDayHours

  fun addWorkingdays(): Unit {
    addWeekdays(*Weekdays)
  }

  fun addWeekenddays(): Unit {
    addWeekdays(*Weekends)
  }

  fun addWeekdays(vararg dayOfWeeks: DayOfWeek) {
    weekDays.addAll(dayOfWeeks.asList())
  }

  /**
   * 두 일자의 중간의 Working 시간을 고려한 Working Time을 구합니다.
   * 예를 들어 7월 1일 ~ 7월 31일의 시간은 31일 이지만, 업무일 기준의 월 근로시간을 구할 수 있습니다.

   * @param fromTime 시작 시각
   * *
   * @param toTime   완료 시각
   * *
   * @return 두 일자 사이에 실제 일하는 시간만을 집계한 기간
   */
  @JvmOverloads
  fun difference(fromTime: DateTime, toTime: DateTime = now()): Duration {
    if (fromTime == toTime)
      return Duration.ZERO

    val isEmpty = weekDays.isEmpty &&
        workingHours.isEmpty &&
        workingDayHours.isEmpty

    if (isEmpty) {
      return DateDiff(fromTime, toTime, calendar).difference
    }

    val diffRange = TimeRange(fromTime, toTime)
    val limits = TimeRange(diffRange.start.startTimeOfDay(),
                           diffRange.end.startTimeOfDay().plusDays(1))
    val collector = CalendarPeriodCollector(collectorFilter, limits, SeekDirection.Forward, calendar)

    // 갭을 계산합니다.
    val gapCalculator = TimeGapCalculator<TimeRange>(calendar)
    val gaps = gapCalculator.gaps(collector.periods, diffRange)
    var difference = Duration.ZERO

    for (gap in gaps) {
      difference += gap.duration
    }

    log.debug("시작={}, 완료={} 의 Duration={}", difference)

    if (fromTime.compareTo(toTime) <= 0)
      return difference
    return difference.negated()
  }


}