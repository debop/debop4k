/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.timeperiod

import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author sunghyouk.bae@gmail.com
 */
open class TimeRange(start: DateTime = MinPeriodTime,
                     end: DateTime = MaxPeriodTime,
                     readOnly: Boolean = false) : TimePeriod(start, end, readOnly), ITimeRange {

  companion object {
    @JvmStatic val AnyTime: TimeRange = TimeRange(readOnly = true)

    @JvmStatic
    @JvmOverloads
    fun of(start: DateTime = MinPeriodTime, end: DateTime = MaxPeriodTime, readOnly: Boolean = false): TimeRange {
      return TimeRange(start, end, readOnly)
    }

    @JvmStatic
    fun of(moment: DateTime, readOnly: Boolean = false): TimeRange {
      return TimeRange(moment, moment, readOnly)
    }

    fun of(start: DateTime, offset: Duration, readOnly: Boolean = false): TimeRange {
      return TimeRange(start, start + offset, readOnly)
    }

    fun of(src: ITimePeriod, readOnly: Boolean = src.readOnly): TimeRange {
      return TimeRange(src.start, src.end, readOnly)
    }
  }

  override fun copy(offset: Duration): ITimePeriod {
    if (offset == Duration.ZERO)
      return TimeRange.of(this)

    val ns = if (hasStart()) start + offset else start
    val ne = if (hasEnd()) end + offset else end
    return TimeRange.of(ns, ne, readOnly)
  }

  override fun expandStartTo(moment: DateTime) {
    if (start > moment) {
      start = moment
    }
  }

  override fun expandEndTo(moment: DateTime) {
    if (end < moment) {
      end = moment
    }
  }

  override fun expandTo(moment: DateTime) {
    expandStartTo(moment)
    expandEndTo(moment)
  }

  override fun expandTo(period: ITimePeriod) {
    if (period.hasStart())
      expandStartTo(period.start)

    if (period.hasEnd())
      expandEndTo(period.end)
  }

  override fun shrinkStartTo(moment: DateTime) {
    if (!hasInside(moment) && start < moment) {
      start = moment
    }
  }

  override fun shrinkEndTo(moment: DateTime) {
    if (!hasInside(moment) && end > moment) {
      end = moment
    }
  }

  override fun shrinkTo(moment: DateTime) {
    shrinkStartTo(moment)
    shrinkEndTo(moment)
  }

  override fun shrinkTo(period: ITimePeriod) {
    if (period.hasStart()) shrinkStartTo(period.start)
    if (period.hasEnd()) shrinkEndTo(period.end)
  }
}