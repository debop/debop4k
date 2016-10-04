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
import debop4k.timeperiod.*
import debop4k.timeperiod.utils.*
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class HourTimeRange @JvmOverloads constructor(startTime: DateTime = now().startTimeOfHour(),
                                                   val hourCount: Int = 1,
                                                   calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(startTime.relativeHourPeriod(hourCount), calendar) {

  val hourOfDayOfEnd: Int get() = end.hourOfDay

  fun minuteSequence(): Sequence<MinuteRange> {
    return minuteRangeSequence(startMinuteOfStart, hourCount * MinutesPerHour, calendar)
  }

  fun minutes(): List<MinuteRange> {
    return minuteSequence().toList()
  }
}