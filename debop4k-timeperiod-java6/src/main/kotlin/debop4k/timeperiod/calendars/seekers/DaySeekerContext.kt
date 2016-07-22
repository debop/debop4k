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

package debop4k.timeperiod.calendars.seekers

import debop4k.timeperiod.calendars.ICalendarVisitorContext
import debop4k.timeperiod.timeranges.DayRange

/**
 * @author sunghyouk.bae@gmail.com
 */
open class DaySeekerContext(val startDay: DayRange,
                            val dayCount: Int) : ICalendarVisitorContext {

  var remainingDays: Int = 0
  var foundDay: DayRange? = null

  init {
    remainingDays = this.dayCount
  }

  val isFinished: Boolean get() = remainingDays == 0

  fun processDay(day: DayRange): Unit {
    if (!isFinished) {
      remainingDays--

      if (isFinished)
        foundDay = day
    }
  }
}