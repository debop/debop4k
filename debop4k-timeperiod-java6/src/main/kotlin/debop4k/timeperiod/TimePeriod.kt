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

package debop4k.timeperiod

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

  companion object {

    @JvmStatic
    fun of(src: ITimePeriod): TimePeriod
        = TimePeriod(src.start, src.end, src.readOnly)

    @JvmStatic
    @JvmOverloads
    fun of(start: DateTime = MinPeriodTime,
           end: DateTime = MaxPeriodTime,
           readOnly: Boolean = false): TimePeriod
        = TimePeriod(start, end, readOnly)

    @JvmStatic
    fun of(moment: DateTime, readOnly: Boolean = false): TimePeriod
        = TimePeriod(moment, moment, readOnly)

    @JvmStatic
    @JvmOverloads
    fun of(start: DateTime, duration: Duration, readOnly: Boolean = false): TimePeriod
        = TimePeriod(start, start.plus(duration), readOnly)
  }


  override var duration: Duration
    get() = Duration(start, end)
    set(d: Duration) {
      assertMutable()
      require(d.millis > 0, { "Duration 은 0보다 커야 합니다." })

      if (hasStart()) {
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
      return TimePeriod.of(this)
    }
    val s = if (hasStart()) start + offset else start
    val e = if (hasEnd()) end + offset else end

    return TimePeriod(s, e, readOnly)
  }

  override fun move(offset: Duration) {
    if (offset.millis == 0L) return
    assertMutable()

    if (hasStart()) start += offset
    if (hasEnd()) end += offset
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

  operator fun rangeTo(endIncluded: ITimePeriod): ClosedRange<ITimePeriod> = TODO()

}
