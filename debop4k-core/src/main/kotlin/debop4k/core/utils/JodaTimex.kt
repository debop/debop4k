/*
 *  Copyright (c) 2016. KESTI co, ltd
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
@file:JvmName("JodaTimex")

package debop4k.core.utils

//
// NOTE: Use debop4k.core.kodatimes.KodaTimes.Kt
//

//import org.joda.time.DateTime
//import org.joda.time.DateTimeConstants
//import org.joda.time.format.DateTimeFormatter
//import org.joda.time.format.ISODateTimeFormat
//
///** 기본 [DateTimeFormatter] */
//private val defaultFormatter: DateTimeFormatter = ISODateTimeFormat.dateTime()
//
///** Timestamp 값을 [DateTime] 으로 변환합니다 */
//fun Long.toDateTime(): DateTime = DateTime(this)
//
//@JvmOverloads
//fun String?.parse(formatter: DateTimeFormatter = defaultFormatter): DateTime
//    = if (this.isNullOrBlank()) DateTime(0) else DateTime.parse(this, formatter)
//
//fun DateTime.startOfDay(): DateTime = this.withTimeAtStartOfDay()
//fun DateTime.endOfDay(): DateTime = this.withTimeAtStartOfDay().plusDays(1).minusMillis(1)
//
//fun DateTime.startOfWeek(): DateTime =
//    this.minusDays(this.dayOfWeek - DateTimeConstants.MONDAY).withTimeAtStartOfDay()
//
//fun DateTime.endOfWeek(): DateTime =
//    this.startOfWeek().plusDays(7).minusMillis(1)
//
//fun DateTime.startOfMonth(): DateTime = withDate(year, dayOfMonth, 1)
//fun DateTime.endOfMonth(): DateTime = startOfMonth().plusMonths(1).minusMillis(1)
//
//fun DateTime.startOfYear(): DateTime = withDate(year, 1, 1)
//fun DateTime.endOfYear(): DateTime = startOfYear().plusYears(1).minusMillis(1)
//
//fun DateTime.weekyearAndWeekOfWeekyear(): Pair<Int, Int>
//    = Pair(this.weekyear, this.weekOfWeekyear)
//
//fun DateTime.monthAndWeekOfMonth(): Pair<Int, Int> {
//  val result = weekOfWeekyear - startOfMonth().weekOfWeekyear + 1
//  if (result > 0)
//    return Pair(monthOfYear, result)
//
//  return Pair(this.plusMonths(1).monthOfYear, 1)
//}



