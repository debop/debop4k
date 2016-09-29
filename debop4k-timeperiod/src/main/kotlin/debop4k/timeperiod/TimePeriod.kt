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

import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.timeperiod.models.PeriodRelation
import debop4k.timeperiod.utils.*
import org.joda.time.DateTime
import org.joda.time.Duration


/**
 * Time Period
 */
open class TimePeriod(override var start: DateTime = MinPeriodTime,
                      override var end: DateTime = MaxPeriodTime,
                      override val readOnly: Boolean = false) : ITimePeriod {

  constructor(src: ITimePeriod) : this(src.start, src.end, src.readOnly)

  @JvmOverloads
  constructor(moment: DateTime, readOnly: Boolean = false)
  : this(moment, moment, readOnly)

  @JvmOverloads
  constructor(start: DateTime, duration: Duration, readOnly: Boolean = false)
  : this(start, start + duration, readOnly)

  companion object {
    @JvmField val AnyTime: TimePeriod = TimePeriod(readOnly = true)
  }

  override var duration: Duration
    get() = Duration(start, end)
    set(d) {
      assertMutable()
      require(d.millis > 0, { "Duration 은 0보다 커야 합니다." })

      if (hasStart) {
        end = start + d
      }
    }

  override fun setup(newStart: DateTime, newEnd: DateTime) {
    if (newStart < newEnd) {
      start = newStart
      end = newEnd
    } else {
      start = newEnd
      end = newStart
    }
  }

  override fun copy(offset: Duration): ITimePeriod {
    if (offset.millis == 0L) {
      return TimePeriod(this)
    }
    val s = if (hasStart) start + offset else start
    val e = if (hasEnd) end + offset else end

    return TimePeriod(s, e, readOnly)
  }

  override fun move(offset: Duration) {
    if (offset.millis == 0L) return
    assertMutable()

    if (hasStart) start += offset
    if (hasEnd) end += offset
  }

  override fun isSamePeriod(other: ITimePeriod): Boolean {
    return (start == other.start) && (end == other.end)
  }

  override fun hasInside(moment: DateTime): Boolean {
    return hasInsideWith(moment)
  }

  override fun hasInside(other: ITimePeriod): Boolean {
    return hasInsideWith(other)
  }

  override fun intersectsWith(other: ITimePeriod): Boolean {
    return intersectWith(other)
  }

  override fun overlapsWith(other: ITimePeriod): Boolean {
    return overlapWith(other)
  }

  override fun reset() {
    assertMutable()
    start = MinPeriodTime
    end = MaxPeriodTime
  }

  override fun relation(other: ITimePeriod): PeriodRelation {
    return this.relation(other)
  }

  override fun intersection(other: ITimePeriod): ITimePeriod? {
    return this.intersectRange(other)
  }

  override fun union(other: ITimePeriod): ITimePeriod? {
    return this.unionRange(other)
  }

  override fun compareTo(other: ITimePeriod): Int {
    return start.compareTo(other.start)
  }

  override fun equals(other: Any?): Boolean {
    return other != null && other is ITimePeriod && hashCode() == other.hashCode()
  }

  override fun hashCode(): Int {
    return hashOf(start, end, readOnly)
  }

  override fun toString(): String {
    return ToStringHelper(this)
        .add("start", start)
        .add("end", end)
        .add("readOnly", readOnly)
        .toString()
  }
}
