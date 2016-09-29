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
import debop4k.timeperiod.utils.startTimeOfWeek
import org.joda.time.DateTime

/**
 * @author sunghyouk.bae@gmail.com
 */
open class WeekRange(startTime: DateTime = today().startTimeOfWeek(),
                     calendar: ITimeCalendar = DefaultTimeCalendar) :
    WeekTimeRange(startTime, 1, calendar) {

  constructor(weekyear: Int, weekOfWeekyear: Int, calendar: ITimeCalendar = DefaultTimeCalendar) :
  this(startTimeOfWeek(weekyear, weekOfWeekyear), calendar)

  val firstDayOfWeek: DateTime get() = start
  val lastDayOfWeek: DateTime get() = end

  fun isMultipleCanedarYears(): Boolean
      = calendar.year(firstDayOfWeek) != calendar.year(lastDayOfWeek)

  fun addWeeks(weeks: Int): WeekRange {
    return WeekRange(start.plusWeeks(weeks), calendar)
  }

  val nextWeek: WeekRange get() = addWeeks(1)
  val prevWeek: WeekRange get() = addWeeks(-1)
}