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

@file:JvmName("Durations")

package debop4k.timeperiod.utils

import debop4k.core.kodatimes.days
import debop4k.core.kodatimes.hours
import debop4k.core.kodatimes.minutes
import debop4k.core.kodatimes.seconds
import debop4k.timeperiod.DaysPerWeek
import debop4k.timeperiod.MonthsPerHalfyear
import debop4k.timeperiod.MonthsPerQuarter
import debop4k.timeperiod.models.*
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Utility class for [Duration]
 * @author debop sunghyouk.bae@gmail.com
 */
object Durations {

  fun of(start: DateTime, end: DateTime): Duration = Duration(start, end)

  fun yearDurationOf(year: Int): Duration {
    val start = startTimeOfYear(year)
    return of(start, start.plusYears(1))
  }

  fun halfyearDurationOf(year: Int, halfyear: Halfyear): Duration {
    val start = startTimeOfHalfyear(year, halfyear)
    return of(start, start.plusMonths(MonthsPerHalfyear))
  }

  fun halfyearOf(yearHalfyear: YearHalfyear): Duration = halfyearDurationOf(yearHalfyear.year, yearHalfyear.halfyear)

  fun quarterDurationOf(year: Int, quarter: Quarter): Duration {
    val start = startTimeOfQuarter(year, quarter)
    return of(start, start.plusMonths(MonthsPerQuarter))
  }

  fun quarterDurationOf(yq: YearQuarter): Duration = quarterDurationOf(yq.year, yq.quarter)

  fun monthDurationOf(year: Int, monthOfYear: Int): Duration {
    val start = startTimeOfMonth(year, monthOfYear)
    return of(start, start.plusMonths(1))
  }

  fun monthDurationOf(ym: YearMonth): Duration = monthDurationOf(ym.year, ym.monthOfYear)

  val Week = weekDurationOf(1)

  fun weekDurationOf(week: Int): Duration
      = if (week == 0) Duration.ZERO else dayDurationOf(week * DaysPerWeek)

  val Day = dayDurationOf(1)

  @JvmOverloads
  fun dayDurationOf(day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, milli: Int = 0): Duration {
    var duration = day.days().toStandardDuration()

    if (hour != 0)
      duration += hour.hours().duration
    if (minute != 0)
      duration += minute.minutes().duration
    if (second != 0)
      duration += second.seconds().duration
    if (milli != 0)
      duration += Duration.millis(milli.toLong())

    return duration
  }

  val Hour = hourDurationOf(1)

  @JvmOverloads
  fun hourDurationOf(hour: Int, minute: Int = 0, second: Int = 0, milli: Int = 0): Duration {
    return dayDurationOf(0, hour, minute, second, milli)
  }

  val Minute = minuteDurationOf(1)

  @JvmOverloads
  fun minuteDurationOf(minute: Int, second: Int = 0, milli: Int = 0): Duration {
    return hourDurationOf(0, minute, second, milli)
  }

  val Second = secondDurationOf(1)

  @JvmOverloads
  fun secondDurationOf(second: Int, milli: Int = 0): Duration {
    return minuteDurationOf(second, milli)
  }

  val MilliSecond = millisecondsOf(1)

  fun millisecondsOf(milli: Int): Duration = Duration.millis(milli.toLong())
}