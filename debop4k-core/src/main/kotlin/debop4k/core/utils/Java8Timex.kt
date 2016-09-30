/*
 * Copyright (c) 2016. KESTI co, ltd
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

@file:JvmName("Java8Timex")

package debop4k.core.utils

import org.joda.time.DateTime
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.*

private val ISODateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

fun Temporal.toIsoString(): String = ISODateFormatter.format(this)

fun Temporal.toLocaleString(): String = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(this)
fun Temporal.toIsoDateString(): String = DateTimeFormatter.ISO_DATE.format(this)
fun Temporal.toIsoTimeString(): String = DateTimeFormatter.ISO_TIME.format(this)

fun java.time.Instant?.toDateTime(): DateTime = DateTime(this?.toEpochMilli() ?: 0)

fun Date?.toDateTime(): DateTime = DateTime(this?.time ?: 0)
fun Timestamp?.toDateTime(): DateTime = DateTime(this?.time ?: 0)