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

package debop4k.timeperiod

import debop4k.core.Local

/**
 * 스레드별로 제공하는 Time Period Context
 *
 * @author debop sunghyouk.bae@gmail.com
 */
open class PeriodContext {

  companion object {
    val TIME_CALENDAR_KEY: String = PeriodContext::class.java.name + ".Current"
  }

  object Current {

    var calendar: ITimeCalendar
      get() = Local.getOrPut(TIME_CALENDAR_KEY, { DefaultTimeCalendar })!!
      set(calendar: ITimeCalendar):Unit {
        Local[TIME_CALENDAR_KEY] = calendar
      }
  }
}