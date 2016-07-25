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

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.kodatimes.trimToDay
import debop4k.core.utils.hashOf
import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.MaxDaysPerMonth

/**
 * Created by debop
 */
@Suppress("EqualsOrHashCode")
open class DayRangeInMonth(val startDayOfMonth: Int = 1,
                           val endDayOfMonth: Int = MaxDaysPerMonth) :
    AbstractValueObject(), Comparable<DayRangeInMonth> {

  init {
    require(startDayOfMonth >= 1)
    require(endDayOfMonth <= MaxDaysPerMonth)
    require(startDayOfMonth <= endDayOfMonth)
  }

  companion object {

    fun of(period: ITimePeriod): DayRangeInMonth {
      require(period.hasPeriod())
      require(period.start.trimToDay() == period.end.trimToDay())
      return DayRangeInMonth(period.start.dayOfMonth, period.end.dayOfMonth)
    }
  }

  val isSingleDay: Boolean
    get() = startDayOfMonth == endDayOfMonth

  fun hasInside(dayOfMonth: Int): Boolean {
    return startDayOfMonth <= dayOfMonth && dayOfMonth <= endDayOfMonth
  }

  fun assertValidDayRange(dayOfMonth: Int): Unit {
    assert(dayOfMonth > 0 && dayOfMonth <= MaxDaysPerMonth,
           { "dayOfMonth[$dayOfMonth]는 1 ~ $MaxDaysPerMonth 사이 값이어야 합니다." })
  }

  override fun compareTo(other: DayRangeInMonth): Int {
    return startDayOfMonth - other.startDayOfMonth
  }

  override fun hashCode(): Int {
    return hashOf(startDayOfMonth, endDayOfMonth)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("startDayOfMonth", startDayOfMonth)
        .add("endDayOfMonth", endDayOfMonth)
  }
}