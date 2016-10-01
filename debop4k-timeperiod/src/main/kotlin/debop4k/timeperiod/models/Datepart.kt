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
import debop4k.core.kodatimes.asDate
import debop4k.core.utils.hashOf
import debop4k.timeperiod.MillisPerDay
import org.joda.time.DateTime

/**
 * Datepart
 * @author sunghyouk.bae@gmail.com
 */
class Datepart(_value: DateTime) : AbstractValueObject(), Comparable<Datepart> {
  val value: DateTime

  init {
    value = _value.withTimeAtStartOfDay()
  }

  val year: Int get() = value.year
  val month: Int get() = value.monthOfYear
  val dayOfMonth: Int get() = value.dayOfMonth

  operator fun plus(timepart: Timepart): DateTime {
    return value + timepart.totalMillis
  }

  @JvmOverloads
  fun toDateTime(hour: Int = 0, minute: Int = 0, second: Int = 0, millis: Int = 0): DateTime {
    return this + Timepart(hour, minute, second, millis)
  }

  fun toDateTime(timepart: Timepart): DateTime {
    return value + timepart.totalMillis
  }

  fun toDateTime(millis: Long): DateTime {
    return value.plus(millis % MillisPerDay)
  }


  override fun compareTo(other: Datepart): Int {
    return value.compareTo(other.value)
  }

  override fun hashCode(): Int {
    return hashOf(value)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper().add("value", value)
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(value: DateTime = debop4k.core.kodatimes.today()): Datepart = Datepart(value)

    @JvmStatic
    @JvmOverloads
    fun of(year: Int, monthOfYear: Int = 1, dayOfMonth: Int = 1): Datepart {
      return Datepart(asDate(year, monthOfYear, dayOfMonth))
    }
  }
}