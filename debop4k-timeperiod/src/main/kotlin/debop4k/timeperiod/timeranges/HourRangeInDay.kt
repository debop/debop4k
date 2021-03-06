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

package debop4k.timeperiod.timeranges

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.timeperiod.models.Timepart

/**
 * Created by debop
 */
open class HourRangeInDay @JvmOverloads constructor(val start: Timepart = Timepart(0),
                                                    val endExclusive: Timepart = Timepart(24)) :
    AbstractValueObject(), Comparable<HourRangeInDay> {

  @JvmOverloads constructor(startHour: Int, endHourExclusive: Int = startHour)
  : this(Timepart(startHour), Timepart(endHourExclusive))

  override fun compareTo(other: HourRangeInDay): Int {
    return start.compareTo(other.start)
  }

  override fun hashCode(): Int {
    return hashOf(start, endExclusive)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("start", start)
        .add("endExclusive", endExclusive)
  }
}