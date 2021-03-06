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

package debop4k.timeperiod.calendars.seekers

import debop4k.timeperiod.calendars.ICalendarVisitorContext
import debop4k.timeperiod.timeranges.DayRange

/**
 * 일 단위로 탐색을 수행할 수 있도록 하는 Context 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
open class DaySeekerContext
@JvmOverloads constructor(val startDay: DayRange,
                          private val _dayCount: Int = 0) : ICalendarVisitorContext {

  val dayCount: Int
  var remainingDays: Int
  var foundDay: DayRange? = null

  init {
    dayCount = Math.abs(_dayCount)
    remainingDays = this.dayCount
  }

  val isFinished: Boolean get() = (remainingDays == 0)

  fun processDay(day: DayRange): Unit {
    if (!isFinished) {
      --remainingDays

      if (isFinished)
        foundDay = day
    }
  }
}