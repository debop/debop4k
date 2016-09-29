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

package debop4k.timeperiod.models

import debop4k.timeperiod.utils.startTimeOfMonth
import org.joda.time.DateTime

/**
 * @author sunghyouk.bae@gmail.com
 */
data class MonthWeek(val month: Int, val week: Int) {

  companion object {
    @JvmStatic fun of(m: DateTime): MonthWeek {
      val result = m.weekOfWeekyear - m.startTimeOfMonth().weekOfWeekyear + 1

      if (result > 0)
        return MonthWeek(m.monthOfYear, result)
      return MonthWeek(m.plusMonths(1).monthOfYear, result)
    }
  }
}