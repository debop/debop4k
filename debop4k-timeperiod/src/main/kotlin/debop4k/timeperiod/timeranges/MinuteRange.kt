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

import debop4k.core.kodatimes.now
import debop4k.core.kodatimes.trimToSecond
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MinuteRange(moment: DateTime,
                       calendar: ITimeCalendar = DefaultTimeCalendar)
: MinuteTimeRange(moment, 1, calendar) {

  constructor(calendar: ITimeCalendar = DefaultTimeCalendar) : this(now(), calendar)

  val year: Int get() = startYear
  val monthOfYear: Int get() = startMonthOfYear
  val dayOfMonth: Int get() = startDayOfMonth
  val hourOfDay: Int get() = startHourOfDay
  val minuteOfHour: Int get() = startMinuteOfHour
  val secondOfMinute: Int get() = startSecondOfMinute

  fun addMinutes(minutes: Int): MinuteRange {
    return MinuteRange(start.trimToSecond().plusMinutes(minutes), calendar)
  }

  val prevMinute: MinuteRange get() = addMinutes(-1)
  val nextMinute: MinuteRange get() = addMinutes(1)

}

