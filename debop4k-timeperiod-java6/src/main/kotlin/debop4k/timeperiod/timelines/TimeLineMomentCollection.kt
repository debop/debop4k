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
import debop4k.timeperiod.ITimePeriod
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class TimeLineMomentCollection : ITimeLineMomentCollection {

  private val _moments = FastList.newList<ITimeLineMoment>()

  override val size: Int
    get() = _moments.size

  override val isEmpty: Boolean
    get() = _moments.isEmpty

  override fun min(): ITimeLineMoment = _moments.min()

  override fun max(): ITimeLineMoment = _moments.max()

  override fun get(index: Int): ITimeLineMoment {
    return _moments.get(index)
  }

  protected fun addPeriod(moment: DateTime, period: ITimePeriod): Unit {
    var item = find(moment)
    if (item == null) {
      item = TimeLineMoment(moment)
      _moments.add(item)

      _moments.sortThis()
    }
    item.periods.add(period)
  }

  protected fun removePeriod(moment: DateTime, period: ITimePeriod): Unit {
    val item = find(moment)
    if (item != null && item.periods.contains(period)) {
      item.periods.remove(period)
      if (item.periods.isEmpty()) {
        _moments.remove(item)
      }
    }
  }

  override fun add(period: ITimePeriod?) {
    if (period != null) {
      addPeriod(period.start, period)
      addPeriod(period.end, period)
    }
  }

  override fun addAll(periods: Collection<ITimePeriod?>) {
    periods.filter { it != null }.forEach { add(it) }
  }

  override fun remove(period: ITimePeriod?) {
    if (period != null) {
      removePeriod(period.start, period)
      removePeriod(period.end, period)
    }
  }

  override fun find(moment: DateTime): ITimeLineMoment? {
    return _moments.find { it.moment == moment }
  }

  override fun contains(moment: DateTime): Boolean {
    return _moments.anySatisfy { it.moment == moment }
  }

  override fun iterator(): Iterator<ITimeLineMoment> {
    return _moments.iterator()
  }

  override fun toString(): String {
    return ToStringHelper(this).add("moments", _moments).toString()
  }
}