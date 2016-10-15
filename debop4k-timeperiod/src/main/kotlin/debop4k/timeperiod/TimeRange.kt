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

package debop4k.timeperiod

import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author sunghyouk.bae@gmail.com
 */
open class TimeRange(start: DateTime? = MinPeriodTime,
                     end: DateTime? = MaxPeriodTime,
                     readOnly: Boolean = false) : TimePeriod(start ?: MinPeriodTime,
                                                             end ?: MaxPeriodTime,
                                                             readOnly), ITimeRange {
  constructor() : this(MinPeriodTime, MaxPeriodTime, false)
  constructor(readOnly: Boolean) : this(MinPeriodTime, MaxPeriodTime, readOnly)
  constructor(start: DateTime?, end: DateTime?) : this(start, end, false)
  constructor(src: ITimePeriod) : this(src.start, src.end, src.readonly)
  constructor(src: ITimePeriod, readOnly: Boolean) : this(src.start, src.end, readOnly)
  constructor(moment: DateTime?) : this(moment, moment, false)
  constructor(moment: DateTime?, readOnly: Boolean) : this(moment, moment, readOnly)
  constructor(start: DateTime, duration: Duration) : this(start, start + duration, false)
  constructor(start: DateTime, duration: Duration, readOnly: Boolean) : this(start, start + duration, readOnly)
  constructor(duration: Duration, end: DateTime) : this(end - duration, end, false)
  constructor(duration: Duration, end: DateTime, readOnly: Boolean) : this(end - duration, end, readOnly)

  companion object {
    @JvmField val AnyTime: TimeRange = TimeRange(readOnly = true)
  }

  override var start: DateTime
    get() = super.start
    set(value) {
      assert(value <= end) { "start 는 $end 보다 이전이어야 합니다. value=$value" }
      super.start = value
    }

  override var end: DateTime
    get() = super.end
    set(value) {
      assert(value >= start) { "end 는 $start 보다 이후이어야 합니다. value=$value" }
      super.end = value
    }

  override fun copy(offset: Duration): ITimePeriod {
    if (offset == Duration.ZERO)
      return TimeRange(this)

    val ns = if (hasStart) start + offset else start
    val ne = if (hasEnd) end + offset else end
    return TimeRange(ns, ne, readonly)
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
    if (period.hasStart)
      expandStartTo(period.start)

    if (period.hasEnd)
      expandEndTo(period.end)
  }

  override fun shrinkStartTo(moment: DateTime) {
    if (hasInside(moment) && start < moment) {
      start = moment
    }
  }

  override fun shrinkEndTo(moment: DateTime) {
    if (hasInside(moment) && end > moment) {
      end = moment
    }
  }

  override fun shrinkTo(moment: DateTime) {
    shrinkStartTo(moment)
    shrinkEndTo(moment)
  }

  override fun shrinkTo(period: ITimePeriod) {
    if (period.hasStart) shrinkStartTo(period.start)
    if (period.hasEnd) shrinkEndTo(period.end)
  }
}