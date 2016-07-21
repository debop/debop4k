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

package debop4k.timeperiod.timelines

import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.TimePeriodCollection
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class TimeLineMoment(override val moment: DateTime) : ITimeLineMoment {

  private val _periods: TimePeriodCollection = TimePeriodCollection()

  override val periods: ITimePeriodCollection
    get() = _periods

  override val startCount: Long
    get() = _periods.periods.count { it.start == moment }.toLong()

  override val endCount: Long
    get() = _periods.periods.count { it.end == moment }.toLong()

  override fun compareTo(other: ITimeLineMoment): Int {
    return moment.compareTo(other.moment)
  }

  override fun equals(other: Any?): Boolean = when (other) {
    null -> false
    else -> javaClass == other.javaClass && hashCode() == other.hashCode()
  }

  override fun hashCode(): Int {
    return hashOf(moment)
  }

  override fun toString(): String {
    return ToStringHelper(this)
        .add("moment", moment)
        .add("startCount", startCount)
        .add("endCount", endCount)
        .add("periods", periods)
        .toString()
  }

}