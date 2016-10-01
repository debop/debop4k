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

import debop4k.core.kodatimes.asDate
import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.models.YearMonth
import debop4k.timeperiod.utils.startTimeOfMonth
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MonthRange @JvmOverloads constructor(startTime: DateTime = today(),
                                                calendar: ITimeCalendar = DefaultTimeCalendar) :
    MonthTimeRange(startTime, 1, calendar) {

  @JvmOverloads
  constructor(year: Int, monthOfYear: Int, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(startTimeOfMonth(year, monthOfYear), calendar)

  constructor(startYm: YearMonth) : this(asDate(startYm.year, startYm.monthOfYear))

  val year: Int get() = startYear
  val monthOfYear: Int get() = startMonthOfYear

  fun addMonths(months: Int): MonthRange {
    return MonthRange(start.plusMonths(months), calendar)
  }

  val nextMonth: MonthRange get() = addMonths(1)
  val prevMonth: MonthRange get() = addMonths(-1)
}