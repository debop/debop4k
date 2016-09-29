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
 * TimeCalendar
 * @author debop sunghyouk.bae@gmail.com
 */
class TimeCalendar(val cfg: TimeCalendarConfig = TimeCalendarConfig.DEFAULT) : ITimeCalendar {

  init {
    require(cfg.startOffset >= Duration.ZERO, { "startOffset must be greater than or equal zero." })
    require(cfg.endOffset <= Duration.ZERO, { "endOffset must be less than or equal zero." })
  }

  companion object {
    val DEFAULT: TimeCalendar by lazy { TimeCalendar() }
    val EMPTY_OFFSET: TimeCalendar by lazy { TimeCalendar(TimeCalendarConfig.EMPTY_OFFSET) }
  }

  override val locale: Locale get() = cfg.locale
  override val startOffset: Duration get() = cfg.startOffset
  override val endOffset: Duration get() = cfg.endOffset
  override val firstDayOfWeek: DayOfWeek get() = cfg.firstDayOfWeek

  override fun mapStart(moment: DateTime): DateTime
      = if (moment > MinPeriodTime) moment + startOffset else moment

  override fun mapEnd(moment: DateTime): DateTime
      = if (moment < MaxPeriodTime) moment + endOffset else moment

  override fun unmapStart(moment: DateTime): DateTime
      = if (moment > MinPeriodTime) moment - startOffset else moment

  override fun unmapEnd(moment: DateTime): DateTime
      = if (moment < MaxPeriodTime) moment - endOffset else moment

  override fun year(moment: DateTime): Int = moment.year

  override fun monthOfYear(moment: DateTime): Int = moment.monthOfYear

  override fun dayOfMonth(moment: DateTime): Int = moment.dayOfMonth

  override fun hourOfDay(moment: DateTime): Int = moment.hourOfDay

  override fun minuteOfHour(moment: DateTime): Int = moment.minuteOfHour

  override fun dayOfWeek(moment: DateTime): DayOfWeek = DayOfWeek.of(moment.dayOfWeek)

  override fun daysInMonth(year: Int, monthOfYear: Int): Int
      = debop4k.timeperiod.utils.daysInMonth(year, monthOfYear)

  override fun weekOfYear(moment: DateTime): YearWeek
      = weekOfYear(moment)

  override fun startOfYearWeek(weekyear: Int, weekOfWeekyear: Int): DateTime
      = startOfYearWeek(weekyear, weekOfWeekyear)
}