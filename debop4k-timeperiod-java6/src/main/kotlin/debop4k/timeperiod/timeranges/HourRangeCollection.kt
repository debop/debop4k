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

import debop4k.core.kodatimes.asDate
import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.hourSequence

import org.joda.time.DateTime

/**
 * Created by debop
 */
open class HourRangeCollection(startTime: DateTime = now(),
                               hourCount: Int = 1,
                               calendar: ITimeCalendar = DefaultTimeCalendar)
: HourTimeRange(startTime, hourCount, calendar) {

  constructor(year: Int, monthOfYear: Int, hourCount: Int = 1, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(asDate(year, monthOfYear), hourCount, calendar)

  fun hourSequence(): Sequence<HourRange>
      = hourSequence(startHourOfStart, hourCount, calendar)
}