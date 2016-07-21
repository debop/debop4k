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

import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.timeperiod.models.DayOfWeek

/**
 * 특정 요일의 하루 동안의 시간 간격
 * Created by debop
 */
open class DayOfWeekHourRange(val dayOfWeek: DayOfWeek,
                              startHourOfDay: Int = 0,
                              endHourOfDay: Int = 23) : HourRangeInDay(startHourOfDay, endHourOfDay) {

  override fun hashCode(): Int {
    return hashOf(dayOfWeek, startHourOfDay, endHourOfDay)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("dayOfWeek", dayOfWeek)
  }
}