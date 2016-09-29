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
import debop4k.timeperiod.models.Halfyear
import debop4k.timeperiod.utils.addHalfyear
import debop4k.timeperiod.utils.startTimeOfHalfyear
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class HalfyearRange(moment: DateTime = today(),
                         calendar: ITimeCalendar = DefaultTimeCalendar)
: HalfyearTimeRange(moment, 1, calendar) {

  @JvmOverloads
  constructor(year: Int, halfyear: Halfyear, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(startTimeOfHalfyear(year, halfyear), calendar)

  @JvmOverloads
  constructor(year: Int, monthOfYear: Int, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(startTimeOfHalfyear(year, monthOfYear), calendar)


  fun nextHalfyear(): HalfyearRange = addHalfyears(1)
  fun prevHalfyear(): HalfyearRange = addHalfyears(-1)

  fun addHalfyears(count: Int): HalfyearRange {
    val yhy = addHalfyear(startYear, halfyearOfStart, count)
    return HalfyearRange(yhy.year, yhy.halfyear, calendar)
  }
}