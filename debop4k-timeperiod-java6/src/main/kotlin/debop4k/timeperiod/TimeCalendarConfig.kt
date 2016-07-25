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

package debop4k.timeperiod

import debop4k.timeperiod.models.DayOfWeek
import org.joda.time.Duration
import java.util.*

/**
 * @author debop sunghyouk.bae@gmail.com
 */
data class TimeCalendarConfig(val locale: Locale = Locale.getDefault(),
                              val startOffset: Duration = DefaultStartOffset,
                              val endOffset: Duration = DefaultEndOffset,
                              val firstDayOfWeek: DayOfWeek = FirstDayOfWeek) {

  companion object {

    val DEFAULT: TimeCalendarConfig by lazy { TimeCalendarConfig() }

    val EMPTY_OFFSET: TimeCalendarConfig by lazy {
      TimeCalendarConfig(startOffset = EmptyDuration,
                         endOffset = EmptyDuration)
    }
  }
}