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

@file:JvmName("Durations")

package debop4k.timeperiod.utils

import debop4k.core.kodatimes.*
import debop4k.timeperiod.*
import debop4k.timeperiod.models.*
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Utility class for [Duration]
 *
 * @author debop sunghyouk.bae@gmail.com
 */
object Durations {

  @JvmField val Zero: Duration = Duration.ZERO

  @JvmStatic
  fun negate(duration: Duration) = duration.negated()

  @JvmStatic
  fun of(start: DateTime, end: DateTime): Duration = Duration(start, end)

  @JvmStatic
  fun yearOf(year: Int): Duration {
    val start = startTimeOfYear(year)
    return of(start, start.plusYears(1))
  }

  @JvmStatic
  fun halfyearOf(year: Int, halfyear: Halfyear): Duration {
    val start = startTimeOfHalfyear(year, halfyear)
    return of(start, start.plusMonths(MonthsPerHalfyear))
  }

  @JvmStatic
  fun halfyearOf(yearHalfyear: YearHalfyear): Duration
      = halfyearOf(yearHalfyear.year, yearHalfyear.halfyear)

  @JvmStatic
  fun quarterOf(year: Int, quarter: Quarter): Duration {
    val start = startTimeOfQuarter(year, quarter)
    return of(start, start.plusMonths(MonthsPerQuarter))
  }

  @JvmStatic
  fun quarterOf(yq: YearQuarter): Duration = quarterOf(yq.year, yq.quarter)

  @JvmStatic
  fun monthOf(year: Int, monthOfYear: Int): Duration {
    val start = startTimeOfMonth(year, monthOfYear)
    return of(start, start.plusMonths(1))
  }

  @JvmStatic
  fun monthOf(ym: YearMonth): Duration = monthOf(ym.year, ym.monthOfYear)

  @JvmField val Week = weekOf(1)

  @JvmStatic
  fun weekOf(week: Int): Duration
      = if (week == 0) Duration.ZERO else dayOf(week * DaysPerWeek)

  @JvmField val Day = dayOf(1)

  @JvmStatic
  @JvmOverloads
  fun dayOf(day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, milli: Int = 0): Duration {
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

  @JvmField val Hour: Duration = hourOf(1)

  @JvmStatic
  @JvmOverloads
  fun hourOf(hour: Int, minute: Int = 0, second: Int = 0, milli: Int = 0): Duration {
    return dayOf(0, hour, minute, second, milli)
  }

  @JvmField val Minute: Duration = minuteOf(1)

  @JvmStatic
  @JvmOverloads
  fun minuteOf(minute: Int, second: Int = 0, milli: Int = 0): Duration {
    return hourOf(0, minute, second, milli)
  }

  @JvmField val Second: Duration = secondOf(1)

  @JvmStatic
  @JvmOverloads
  fun secondOf(second: Int, milli: Int = 0): Duration {
    return minuteOf(0, second, milli)
  }

  @JvmField val Millisecond: Duration = millisOf(1)

  @JvmStatic
  fun millisOf(milli: Int): Duration = Duration.millis(milli.toLong())
}