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
import debop4k.timeperiod.utils.monthSequence
import debop4k.timeperiod.utils.startTimeOfMonth
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MonthRangeCollection @JvmOverloads constructor(startTime: DateTime = today(),
                                                          monthCount: Int = 1,
                                                          calendar: ITimeCalendar = DefaultTimeCalendar)
: MonthTimeRange(startTime.startTimeOfMonth(), monthCount, calendar) {

  constructor(year: Int,
              month: Int,
              monthCount: Int = 1,
              calendar: ITimeCalendar = DefaultTimeCalendar) :
  this(startTimeOfMonth(year, month), monthCount, calendar)


  fun monthSequence(): Sequence<MonthRange>
      = monthSequence(start, monthCount, calendar)
}