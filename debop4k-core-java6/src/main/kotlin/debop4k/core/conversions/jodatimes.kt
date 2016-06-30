/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("jodatimes")

package debop4k.core.conversions

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

// yyyy-MM-dd'T'HH:mm:ss.SSSZZ
val JODA_DEFAULT_DATETIME_FORMATTER = ISODateTimeFormat.dateTime()


fun utcNow(): DateTime = DateTime.now(DateTimeZone.UTC)

fun DateTime.toISOString(): String = JODA_DEFAULT_DATETIME_FORMATTER.print(this)

fun String.toDateTime(formatter: DateTimeFormatter = JODA_DEFAULT_DATETIME_FORMATTER): DateTime {
  if (this.isNullOrBlank())
    return DateTime(0)
  return DateTime.parse(this, formatter)
}


fun DateTime.startOfDay(): DateTime = this.withTimeAtStartOfDay()
fun DateTime.endOfDay(): DateTime = this.startOfDay().plusDays(1).minusMillis(1)
fun DateTime.startOfWeek(): DateTime = this.minusDays(this.dayOfWeek - DateTimeConstants.MONDAY).withTimeAtStartOfDay()
fun DateTime.endOfWeek(): DateTime = this.startOfWeek().plusDays(7).minusMillis(1)
fun DateTime.startOfMonth(): DateTime = this.withDate(this.year, this.monthOfYear, 1)
fun DateTime.endOfMonth(): DateTime = this.startOfMonth().plusMonths(1).minusMillis(1)
fun DateTime.startOfYear(): DateTime = this.withDate(this.year, 1, 1)
fun DateTime.endOfYear(): DateTime = this.startOfYear().plusYears(1).minusMillis(1)

fun DateTime.getWeekyearAndWeekOfWeekyear(): Pair<Int, Int> {
  return Pair(this.weekyear, this.weekOfWeekyear)
}

fun DateTime.getMonthAndWeekOfMonth(): Pair<Int, Int> {
  val result = this.weekOfWeekyear - this.startOfMonth().weekOfWeekyear + 1
  if (result > 0)
    return Pair(this.monthOfYear, result)

  return Pair(this.plusMonths(1).monthOfYear, 1)
}
