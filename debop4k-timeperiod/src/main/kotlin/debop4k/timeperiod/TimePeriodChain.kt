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

import debop4k.core.loggerOf
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author sunghyouk.bae@gmail.com
 */
open class TimePeriodChain : TimePeriodContainer(), ITimePeriodChain {

  private val log = loggerOf(javaClass)

  override var start: DateTime
    get() = head?.start ?: MinPeriodTime
    set(value) {
      move(Duration(start, value))
    }
  override var end: DateTime
    get() = last?.end ?: MaxPeriodTime
    set(value) {
      move(Duration(end, value))
    }

  override fun set(index: Int, element: ITimePeriod): ITimePeriod {
    removeAt(index)
    add(index, element)
    return element
  }

  override fun add(element: ITimePeriod): Boolean {
    val last = this.last
    if (last != null) {
      assertSpaceAfter(last.end, element.duration)
      element.setup(last.end, last.end + element.duration)
    }
    log.trace("Period chain 마지막에 element 를 추가합니다 element={}", element)
    return periods.add(element)
  }

  override fun add(index: Int, element: ITimePeriod) {
    throw UnsupportedOperationException("지원하지 않습니다.")
  }

  override fun remove(element: ITimePeriod): Boolean {
    throw UnsupportedOperationException("지원하지 않습니다.")
  }

  override fun removeAt(index: Int): ITimePeriod {
    return periods.removeAt(index)
  }

  override fun assertSpaceBefore(moment: DateTime, duration: Duration) {
    var hasSpace = moment !== MinPeriodTime
    if (hasSpace) {
      hasSpace = duration <= Duration(MinPeriodTime, moment)
    }
    assert(hasSpace) { "duration[$duration] is out of range." }
  }

  override fun assertSpaceAfter(moment: DateTime, duration: Duration) {
    var hasSpace = moment !== MaxPeriodTime
    if (hasSpace) {
      hasSpace = duration <= Duration(moment, MaxPeriodTime)
    }
    assert(hasSpace) { "duration[$duration] is out of range." }
  }

  companion object {
    @JvmStatic fun of(c: Collection<ITimePeriod>): TimePeriodChain {
      val chain = TimePeriodChain()
      chain.addAll(c)
      return chain
    }

    @JvmStatic fun of(vararg elements: ITimePeriod): TimePeriodChain {
      val chain = TimePeriodChain()
      chain.addAll(elements.toList())
      return chain
    }
  }
}