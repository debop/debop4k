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
open class TimePeriod(var _start: DateTime? = MinPeriodTime,
                      var _end: DateTime? = MaxPeriodTime,
                      override val readonly: Boolean = false) : ITimePeriod {

  constructor(src: ITimePeriod) : this(src.start, src.end, src.readonly)
  constructor(src: ITimePeriod, readonly: Boolean) : this(src.start, src.end, readonly)
  constructor(start: DateTime, duration: Duration) : this(start, start + duration, false)
  constructor(start: DateTime, duration: Duration, readOnly: Boolean) : this(start, start + duration, readOnly)

  init {
    val pair = adjustPeriod(_start, _end)
    _start = pair.first ?: MinPeriodTime
    _end = pair.second ?: MaxPeriodTime
  }

  override var start: DateTime
    get() = _start!!
    set(value) {
      assertMutable()
//      assert(value <= _end) { "start 는 $_end 보다 이전이어야 합니다. value=$value" }
      _start = value
    }

  override var end: DateTime
    get() = _end!!
    set(value) {
      assertMutable()
//      assert(value >= _start) { "end 는 $_start 보다 이후이어야 합니다. value=$value" }
      _end = value
    }

  companion object {
    @JvmField val AnyTime: TimePeriod = TimePeriod(readonly = true)
  }


//  override var duration: Duration
//    get() = Duration(start, end)
//    set(d) {
//      assertMutable()
//      require(d.millis >= 0, { "Duration 은 0보다 커야 합니다." })
//
//      if (hasStart()) {
//        end = start + d
//      }
//    }

  override fun setup(newStart: DateTime?, newEnd: DateTime?) {
    val ns = newStart ?: MinPeriodTime
    val ne = newEnd ?: MaxPeriodTime
    if (ns < ne) {
      _start = ns
      _end = ne
    } else {
      _start = ne
      _end = ns
    }
  }

  override fun copy(offset: Duration): ITimePeriod {
    if (offset.millis == 0L) {
      return TimeBlock(this)
    }
    val s = if (hasStart()) start + offset else start
    val e = if (hasEnd()) end + offset else end

    return TimeBlock(s, e, readonly)
  }

  override fun move(offset: Duration) {
    if (offset.millis == 0L) return
    assertMutable()

    if (offset > Duration.ZERO) {
      if (hasEnd()) end += offset
      if (hasStart()) start += offset
    } else {
      if (hasStart()) start += offset
      if (hasEnd()) end += offset
    }
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
    return relationWith(other)
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
    return hashOf(start, end, readonly)
  }

  override fun toString(): String {
    return ToStringHelper(this)
        .add("start", start)
        .add("end", end)
        .add("readonly", readonly)
        .toString()
  }
}
