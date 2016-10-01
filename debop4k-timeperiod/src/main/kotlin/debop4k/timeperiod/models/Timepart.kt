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
import debop4k.core.utils.hashOf
import debop4k.timeperiod.MillisPerHour
import debop4k.timeperiod.MillisPerMinute
import debop4k.timeperiod.MillisPerSecond
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Timepart
 * @author sunghyouk.bae@gmail.com
 */
class Timepart @JvmOverloads constructor(hour: Int = 0,
                                         minute: Int = 0,
                                         second: Int = 0,
                                         millis: Int = 0) : AbstractValueObject(), Comparable<Timepart> {

  constructor(time: DateTime) : this(time.hourOfDay,
                                     time.minuteOfHour,
                                     time.secondOfMinute,
                                     time.millisOfSecond)

  val value: DateTime

  init {
    value = DateTime(0).withTime(hour, minute, second, millis)
  }

  val hourOfDay: Int get() = value.hourOfDay
  val minuteOfHour: Int get() = value.minuteOfHour
  val secondOfMinute: Int get() = value.secondOfMinute
  val millisOfSecond: Int get() = value.millisOfSecond

  val totalMillis: Long get() = value.millis
  val totalSeconds: Double get() = totalMillis / MillisPerSecond.toDouble()
  val totalMinutes: Double get() = totalMillis / MillisPerMinute.toDouble()
  val totalHours: Double get() = totalMillis / MillisPerHour.toDouble()

  operator fun plus(datepart: Datepart): DateTime {
    return datepart.value.plus(totalMillis)
  }

  fun toDateTime(moment: DateTime): DateTime {
    return moment.withTimeAtStartOfDay().plus(totalMillis)
  }

  override fun compareTo(other: Timepart): Int {
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
    fun of(hour: Int = 0, minute: Int = 0, second: Int = 0, millis: Int = 0): Timepart {
      return Timepart(hour, minute, second, millis)
    }

    @JvmStatic
    fun of(time: DateTime): Timepart = Timepart(time)

    @JvmStatic
    fun of(duration: Duration): Timepart = Timepart(DateTime(0).plus(duration))
  }
}