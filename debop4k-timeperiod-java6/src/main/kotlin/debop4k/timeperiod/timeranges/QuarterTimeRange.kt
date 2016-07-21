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

import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.MonthsPerQuarter
import debop4k.timeperiod.models.Quarter
import debop4k.timeperiod.utils.monthSequence
import debop4k.timeperiod.utils.quarterOf
import debop4k.timeperiod.utils.relativeQuarterPeriod
import debop4k.timeperiod.utils.startTimeOfQuarter
import org.joda.time.DateTime

open class QuarterTimeRange(moment: DateTime,
                            val quarterCount: Int = 1,
                            calendar: ITimeCalendar = DefaultTimeCalendar) :
    CalendarTimeRange(moment.relativeQuarterPeriod(quarterCount), calendar) {

  @JvmOverloads
  constructor(year: Int, quarter: Quarter, quarterCount: Int = 1, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(startTimeOfQuarter(year, quarter), quarterCount, calendar)

  val quarterOfStart: Quarter get() = start.quarterOf()

  val quarterOfEnd: Quarter get() = end.quarterOf()

  val isMultipleCalendarYears: Boolean
    get() = startYear != endYear

  val isMultipleCalendarQuarters: Boolean
    get() = isMultipleCalendarYears && start.quarterOf() != end.quarterOf()


  fun monthSequence(): Sequence<MonthRange> {
    return monthSequence(start, quarterCount * MonthsPerQuarter, calendar)
  }

}