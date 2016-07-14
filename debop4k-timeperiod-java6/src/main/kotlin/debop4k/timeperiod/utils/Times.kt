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

@file:JvmName("Times")

package debop4k.timeperiod.utils

import debop4k.core.kodatimes.asDate
import debop4k.core.kodatimes.months
import debop4k.timeperiod.models.YearMonth
import debop4k.timeperiod.models.YearWeek
import org.joda.time.DateTime

fun daysInMonth(year: Int, monthOfYear: Int): Int = TODO()

fun weekOfYear(moment: DateTime): YearWeek = TODO()

fun startOfYearWeek(weekyear: Int, weekOfWeekyear: Int): YearWeek = TODO()

fun DateTime.startTimeOfMonth(): DateTime = startTimeOfMonth(this.year, this.monthOfYear)
fun YearMonth.startTimeOfMonth(): DateTime = startTimeOfMonth(this.year, this.monthOfYear)
fun startTimeOfMonth(year: Int, monthOfYear: Int): DateTime = asDate(year, monthOfYear, 1)

fun DateTime.endTimeOfMonth(): DateTime = endTimeOfMonth(this.year, this.monthOfYear)
fun YearMonth.endTimeOfMonth(): DateTime = endTimeOfMonth(this.year, this.monthOfYear)
fun endTimeOfMonth(year: Int, monthOfYear: Int): DateTime {
  return startTimeOfMonth(year, monthOfYear).plusMonths(1).minusMillis(1)
}

fun nextMonth(year: Int, monthOfYear: Int): YearMonth {
  return addMonth(year, monthOfYear, 1)
}

fun YearMonth.nextMonth(): YearMonth = this.addMonth(1)

fun addMonth(year: Int, monthOfYear: Int, delta: Int): YearMonth {
  return YearMonth(year, monthOfYear).addMonth(delta)
}

fun YearMonth.addMonth(delta: Int): YearMonth {
  if (delta == 0) return this.copy()

  val dt = this.startTimeOfMonth() + delta.months()
  return YearMonth(dt.year, dt.monthOfYear)
}
