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

package debop4k.timeperiod.calendars

import debop4k.timeperiod.timeranges.DayOfWeekHourRange
import debop4k.timeperiod.timeranges.DayRangeInMonth
import debop4k.timeperiod.timeranges.HourRangeInDay
import debop4k.timeperiod.timeranges.MonthRangeInYear
import org.eclipse.collections.impl.list.mutable.FastList

/**
 * Created by debop
 */
open class CalendarPeriodCollectorFilter : CalendarVisitorFilter(), ICalendarPeriodCollectorFilter {

  private val _collectingMonths: FastList<MonthRangeInYear> = FastList.newList()
  private val _collectingDays: FastList<DayRangeInMonth> = FastList.newList()
  private val _collectingHours: FastList<HourRangeInDay> = FastList.newList()
  private val _collectingDayHours: FastList<DayOfWeekHourRange> = FastList.newList()

  override val collectingMonths: FastList<MonthRangeInYear>
    get() = _collectingMonths
  override val collectingDays: FastList<DayRangeInMonth>
    get() = _collectingDays

  override val collectingHours: FastList<HourRangeInDay>
    get() = _collectingHours

  override val collectingDayHours: FastList<DayOfWeekHourRange>
    get() = _collectingDayHours

  override fun clear() {
    super.clear()
    _collectingMonths.clear()
    _collectingDays.clear()
    _collectingHours.clear()
    _collectingDayHours.clear()
  }
}