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

package debop4k.core

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal

/**
 * Current time UTC
 * @return now as Instant in timezone UTC
 */
fun utcNowTime(): Instant = Instant.now()

/**
 * Make a date formatter for ISO Date Time 'yyyy-MM-dd`T`hh:mm:ss.SSSZ'
 * @return DateTimeFormatter configured for ISO Date Time format
 */
val ISODateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

/**
 * Convert the Instant into a String in the ISO Date Time format 'yyyy-MM-dd`T`hh:mm:ss.SSSZ'
 * @return String representing the Instant in ISO Date Time format
 */
fun Temporal.toIsoString(): String = ISODateFormatter.format(this)

fun Temporal.toLocaleString(): String = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(this)

fun Temporal.toIsoDateString(): String = DateTimeFormatter.ISO_DATE.format(this)

fun Temporal.toIsoTimeString(): String = DateTimeFormatter.ISO_TIME.format(this)