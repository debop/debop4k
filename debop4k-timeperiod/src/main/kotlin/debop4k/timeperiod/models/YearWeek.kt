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

import debop4k.timeperiod.utils.startTimeOfWeek
import org.joda.time.DateTime

/**
 * 주차를 표현합니다.
 * @author debop sunghyouk.bae@gmail.com
 */
data class YearWeek(val weekyear: Int, val weekOfWeekyear: Int) {

  operator fun plus(weeks: Int): YearWeek {
    return of(this.startTimeOfWeek().plusWeeks(weeks))
  }

  operator fun minus(weeks: Int): YearWeek = this.plus(-weeks)

  companion object {

    @JvmStatic fun of(moment: DateTime): YearWeek
        = YearWeek(moment.weekyear, moment.weekOfWeekyear)

  }
}