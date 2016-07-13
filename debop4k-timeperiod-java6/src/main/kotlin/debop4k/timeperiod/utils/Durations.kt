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

import debop4k.core.kodatimes.*
import debop4k.timeperiod.DaysPerWeek
import debop4k.timeperiod.models.*
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Utility class for [Duration]
 * @author debop sunghyouk.bae@gmail.com
 */
object Durations {

  fun of(start: DateTime, end: DateTime): Duration = Duration(start, end)

  fun year(year: Int): Duration {
    val start = dateTimeOf(year, 1, 1)
    val end = start + 1.years()
    return of(start, end)
  }

  fun halfyear(year: Int, halfyear: Halfyear): Duration = TODO()
  fun halfyear(yearHalfyear: YearHalfyear): Duration = TODO()

  fun quarter(year: Int, quarter: Quarter): Duration = TODO()
  fun quarter(yq: YearQuarter): Duration = TODO()

  fun month(year: Int, monthOfYear: Int): Duration = TODO()
  fun month(ym: YearMonth): Duration = TODO()

  val Week = weeks(1)

  fun weeks(week: Int): Duration
      = if (week == 0) Duration.ZERO else days(week * DaysPerWeek)

  val Day = days(1)

  @JvmOverloads
  fun days(day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, milli: Int = 0): Duration {
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

  val Hour = hours(1)

  @JvmOverloads
  fun hours(hour: Int, minute: Int = 0, second: Int = 0, milli: Int = 0): Duration
      = days(0, hour, minute, second, milli)

  val Minute = minutes(1)

  @JvmOverloads
  fun minutes(minute: Int, second: Int = 0, milli: Int = 0): Duration
      = hours(0, minute, second, milli)

  val Second = seconds(1)

  @JvmOverloads
  fun seconds(second: Int, milli: Int = 0): Duration
      = minutes(second, milli)

  val MilliSecond = milliseconds(1)

  fun milliseconds(milli: Int): Duration = Duration.millis(milli.toLong())
}