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

package debop4k.timeperiod.models

import debop4k.timeperiod.WeekdayList
import debop4k.timeperiod.WeekendList

/**
 * @author debop sunghyouk.bae@gmail.com
 */
enum class DayOfWeek(val value: Int, val description: String) {

  MONDAY(1, "월"),

  TUESDAY(2, "화"),

  WEDNESDAY(3, "수"),

  THURSDAY(4, "목"),

  FRIDAY(5, "금"),

  SATURDAY(6, "토"),

  SUNDAY(7, "일");

  operator fun plus(delta: Int): DayOfWeek {
    val amount = delta % 7
    return of((value - 1 + (amount + 7)) % 7 + 1)
  }

  operator fun minus(delta: Int): DayOfWeek = plus(-delta)

  fun isWeekday(): Boolean = WeekdayList.contains(this)
  fun isWeekend(): Boolean = WeekendList.contains(this)

  companion object {

    val dayOfWeekArray: Array<DayOfWeek> get() = DayOfWeek.values()

    fun of(dayOfWeek: Int): DayOfWeek = dayOfWeekArray[dayOfWeek - 1]
  }

}