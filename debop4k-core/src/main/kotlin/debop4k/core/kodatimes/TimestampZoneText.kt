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

package debop4k.core.kodatimes

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * [DateTime] 의 TimeZone 정보를 제공합니다
 *
 * @author sunghyouk.bae@gmail.com
 */
open class TimestampZoneText(val datetime: DateTime?) {

  constructor(timestamp: Long, zone: DateTimeZone) : this(DateTime(timestamp, zone))

  constructor(timestamp: Long, zoneId: String) : this(DateTime(timestamp, DateTimeZone.forID(zoneId)))

  val timestamp: Long?
    get() = datetime?.millis

  val zoneId: String?
    get() = datetime?.zone?.id

  val timetext: String?
    get() = datetime?.toIsoFormatHMSString()

  override fun toString(): String {
    return "TimestampZoneText(timestamp=$timestamp, zoneId=$zoneId, timetext=$timetext)"
  }
}