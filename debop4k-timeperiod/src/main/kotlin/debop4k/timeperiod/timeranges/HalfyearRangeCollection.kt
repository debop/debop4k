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

import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.models.Halfyear
import debop4k.timeperiod.utils.halfyearSequence
import debop4k.timeperiod.utils.relativeHalfyearPeriod
import org.joda.time.DateTime

/**
 * @author sunghyouk.bae@gmail.com
 */
open class HalfyearRangeCollection @JvmOverloads constructor(moment: DateTime,
                                                             val halfyearCount: Int = 1,
                                                             calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(moment.relativeHalfyearPeriod(halfyearCount), calendar) {

  @JvmOverloads
  constructor(year: Int,
              halfyear: Halfyear,
              halfyearCount: Int = 1,
              calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(debop4k.timeperiod.utils.startTimeOfHalfyear(year, halfyear), halfyearCount, calendar)

  fun halfyearSequence(): Sequence<HalfyearRange> {
    return halfyearSequence(start, halfyearCount, calendar)
  }
}