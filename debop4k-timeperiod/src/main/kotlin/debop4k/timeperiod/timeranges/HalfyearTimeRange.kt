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
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.MonthsPerHalfyear
import debop4k.timeperiod.QuartersPerHalfyear
import debop4k.timeperiod.models.Halfyear
import debop4k.timeperiod.utils.halfyearOf
import debop4k.timeperiod.utils.monthSequence
import debop4k.timeperiod.utils.quarterSequence
import debop4k.timeperiod.utils.relativeHalfyearPeriod
import org.joda.time.DateTime

open class HalfyearTimeRange @JvmOverloads constructor(moment: DateTime = today(),
                                                       val halfyearCount: Int = 1,
                                                       calendar: ITimeCalendar = DefaultTimeCalendar) :
    CalendarTimeRange(moment.relativeHalfyearPeriod(halfyearCount), calendar) {

  val halfyearOfStart: Halfyear get() = start.halfyearOf()
  val halfyearOfEnd: Halfyear get() = end.halfyearOf()

  val halfyear: Halfyear get() = halfyearOfStart

  val isMultipleCalendarYears: Boolean
    get() = startYear != endYear

  fun quarterSequence(): Sequence<QuarterRange> {
    return quarterSequence(start, halfyearCount * QuartersPerHalfyear, calendar)
  }

  fun monthSequence(): Sequence<MonthRange> {
    return monthSequence(start, halfyearCount * MonthsPerHalfyear, calendar)
  }

}