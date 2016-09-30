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

package debop4k.core.kodatimes

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * Timestamp 와 TimeZone 을 가집니다.
 *
 * @author Sunghyouk Bae sunghyouk.bae@gmail.com
 */
class TimeAndZone(val millis: Long = 0L, val zoneId: String = DateTimeZone.UTC.id) : AbstractValueObject() {

  override fun hashCode(): Int {
    return hashOf(millis, zoneId)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("millis", millis)
        .add("zoneId", zoneId)
  }

  companion object {

    @JvmStatic
    fun toDateTime(tz: TimeAndZone): DateTime
        = DateTime(tz.millis, DateTimeZone.forID(tz.zoneId))

    @JvmStatic
    fun fromDateTime(dt: DateTime): TimeAndZone
        = TimeAndZone(dt.millis, dt.zone.id)

  }

}