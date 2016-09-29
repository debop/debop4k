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

package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DaysPerWeek
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.daySequence
import debop4k.timeperiod.utils.relativeWeekPeriod
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class WeekTimeRange(startTime: DateTime = today(),
                         val weekCount: Int,
                         calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(startTime.relativeWeekPeriod(weekCount), calendar) {

  val year: Int get() = startYear
  val weekyear: Int get() = start.weekyear
  val weekOfWeekyear: Int get() = start.weekOfWeekyear

  val startWeekyear: Int get() = start.weekyear
  val startWeekOfWeekyear: Int get() = start.weekOfWeekyear
  val endWeekyear: Int get() = end.weekyear
  val endWeekOfWeekyear: Int get() = end.weekOfWeekyear

  fun daySequence(): Sequence<DayRange>
      = daySequence(start, weekCount * DaysPerWeek, calendar)
}