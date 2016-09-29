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

import debop4k.timeperiod.models.DayOfWeek
import debop4k.timeperiod.models.YearWeek
import org.joda.time.DateTime
import org.joda.time.Duration
import java.util.*

/**
 * @author debop sunghyouk.bae@gmail.com
 */
interface ITimeCalendar : ITimePeriodMapper {

  val locale: Locale

  val startOffset: Duration

  val endOffset: Duration

  val firstDayOfWeek: DayOfWeek

  fun year(moment: DateTime): Int

  fun monthOfYear(moment: DateTime): Int

  fun dayOfMonth(moment: DateTime): Int

  fun hourOfDay(moment: DateTime): Int

  fun minuteOfHour(moment: DateTime): Int

  fun dayOfWeek(moment: DateTime): DayOfWeek

  fun daysInMonth(year: Int, monthOfYear: Int): Int

  fun weekOfYear(moment: DateTime): YearWeek

  fun startOfYearWeek(weekyear: Int, weekOfWeekyear: Int): DateTime

}