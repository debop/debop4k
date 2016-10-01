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

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.kodatimes.asIsoFormatDateTimeString
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * TimestampZoneText
 * @author sunghyouk.bae@gmail.com
 */
class TimestampZoneText(moment: DateTime?) : AbstractValueObject() {

  private var timestamp: Long = 0
  private var zoneId: String? = null
  private var timetext: String? = null

  init {
    if (moment != null) {
      this.timestamp = moment.millis
      this.zoneId = moment.zone.id
      this.timetext = moment.asIsoFormatDateTimeString()
    }
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("timestamp", timestamp)
        .add("zoneId", zoneId)
        .add("timetext", timetext)
  }

  companion object {

    @JvmStatic fun of(): TimestampZoneText = TimestampZoneText(null)
    @JvmStatic fun of(moment: DateTime?): TimestampZoneText = TimestampZoneText(moment)

    @JvmStatic fun of(timestamp: Long, zone: DateTimeZone): TimestampZoneText
        = TimestampZoneText(DateTime(timestamp, zone))

    @JvmStatic fun of(timestamp: Long, zoneId: String): TimestampZoneText
        = TimestampZoneText(DateTime(timestamp, DateTimeZone.forID(zoneId)))
  }
}