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

import debop4k.timeperiod.utils.assertMutable
import debop4k.timeperiod.utils.intersectBlock
import debop4k.timeperiod.utils.unionBlock
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author sunghyouk.bae@gmail.com
 */
class TimeBlock @JvmOverloads constructor(start: DateTime? = MinPeriodTime,
                                          end: DateTime? = MaxPeriodTime,
                                          readOnly: Boolean = false) : TimePeriod(start ?: MinPeriodTime,
                                                                                  end ?: MaxPeriodTime,
                                                                                  readOnly), ITimeBlock {

  constructor(src: ITimePeriod) : this(src.start, src.end, src.readonly)
  constructor(src: ITimePeriod, readOnly: Boolean) : this(src.start, src.end, readOnly)
  constructor(moment: DateTime?, readOnly: Boolean = false) : this(moment, moment, readOnly)

  @JvmOverloads
  constructor(start: DateTime, duration: Duration, readOnly: Boolean = false)
  : this(start, start + duration, readOnly)

  @JvmOverloads
  constructor(duration: Duration, end: DateTime, readOnly: Boolean = false)
  : this(end - duration, end, readOnly)

  override var duration: Duration
    get() = super.duration
    set(value) {
      assertMutable()
      assertValidDuartion(value)
      durationFromStart(value)
    }

  override fun setup(ns: DateTime, nd: Duration) {
    assertMutable()
    assertValidDuartion(nd)

    start = ns
    duration = nd
  }

  override fun durationFromStart(nd: Duration) {
    assertMutable()
    assertValidDuartion(nd)

    if (nd == MaxDuration) {
      this.duration = nd
      end = MaxPeriodTime
    } else {
      this.duration = nd
      end = start + nd
    }
  }

  override fun durationFromEnd(nd: Duration) {
    assertMutable()
    assertValidDuartion(nd)

    if (nd == MaxDuration) {
      this.duration = nd
      start = MinPeriodTime
    } else {
      this.duration = nd
      start = end - nd
    }
  }

  override fun intersection(other: ITimePeriod): ITimePeriod? {
    return this.intersectBlock(other)
  }

  override fun union(other: ITimePeriod): ITimePeriod? {
    return this.unionBlock(other)
  }

  private fun assertValidDuartion(v: Duration): Unit {
    assert(v.millis >= 0, { "nd 은 0 이상의 값을 가져야 합니다." })
  }

}