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

package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.timeperiod.*
import debop4k.timeperiod.utils.halfyearSequence
import debop4k.timeperiod.utils.monthSequence
import debop4k.timeperiod.utils.quarterSequence
import debop4k.timeperiod.utils.relativeYearPeriod
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class YearTimeRange(val year: Int,
                         val yearCount: Int = 1,
                         calendar: ITimeCalendar = DefaultTimeCalendar)
: YearCalendarTimeRange(relativeYearPeriod(year, yearCount),
                        calendar) {

  constructor() : this(now().year)

  @JvmOverloads
  constructor(m: DateTime,
              yearCount: Int = 1,
              calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(m.year, yearCount, calendar)


  fun halfyearSequence(): Sequence<HalfyearRange>
      = halfyearSequence(start, yearCount * HalfyearsPerYear, calendar)

  fun quarterSequence(): Sequence<QuarterRange>
      = quarterSequence(start, yearCount * QuartersPerYear, calendar)

  fun monthSequence(): Sequence<MonthRange>
      = monthSequence(start, yearCount * MonthsPerYear, calendar)

  fun daySequence(): Sequence<DayRange> {
    return monthSequence().flatMap { m ->
      m.dayStream()
    }.asSequence()
  }

  fun hourSequence(): Sequence<HourRange> {
    return daySequence().flatMap { d ->
      d.hourStream()
    }.asSequence()
  }

  fun minuteSequence(): Sequence<MinuteRange> {
    return hourSequence().flatMap { h ->
      h.minuteSequence()
    }.asSequence()
  }

}

