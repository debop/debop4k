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

import debop4k.core.SortDirection
import debop4k.core.SortDirection.ASC
import debop4k.core.SortDirection.DESC
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author sunghyouk.bae@gmail.com
 */
open class TimePeriodContainer : TimePeriod(), ITimePeriodContainer {

  private val _periods: MutableList<ITimePeriod> = mutableListOf<ITimePeriod>()

  override val periods: MutableList<ITimePeriod> get() = _periods

  override val size: Int get() = _periods.size

  override var start: DateTime
    get() {
      if (isEmpty()) return MinPeriodTime
      return _periods.map { period -> period.start }.min()!!
    }
    set(value) {
      if (!isEmpty()) move(Duration(start, value))
    }

  override var end: DateTime
    get() {
      if (isEmpty()) return MaxPeriodTime
      return _periods.map { period -> period.end }.max()!!
    }
    set(value) {
      if (!isEmpty()) move(Duration(end, value))
    }

  override var duration: Duration
    get() = if (hasPeriod()) Duration(start, end) else MaxDuration
    set(value) { /* noting to do */
    }

  override val readonly: Boolean get() = false

  override fun isEmpty(): Boolean = _periods.isEmpty()

  override fun setup(newStart: DateTime?, newEnd: DateTime?) {
    throw UnsupportedOperationException("TimePeriodContainer 는 setup 메소드를 지원하지 않습니다.")
  }

  override fun copy(offset: Duration): ITimePeriod {
    throw UnsupportedOperationException("TimePeriodContainer 는 copy 메소드를 지원하지 않습니다.")
  }

  override fun get(index: Int): ITimePeriod = _periods[index]

  override fun set(index: Int, element: ITimePeriod): ITimePeriod {
    val prev = _periods[index]
    _periods[index] = element
    return prev
  }

  override fun clear() {
    _periods.clear()
  }

  override fun containsPeriod(target: ITimePeriod): Boolean = _periods.contains(target)

  override fun add(element: ITimePeriod): Boolean {
    if (element is ITimePeriodContainer) {
      element.forEach { p -> add(p) }
      return true
    } else if (!containsPeriod(element)) {
      return _periods.add(element)
    }
    return false
  }

  override fun add(index: Int, element: ITimePeriod) {
    if (element is ITimePeriodContainer) {
      element.forEach { p -> add(index, p) }
    } else if (!containsPeriod(element)) {
      _periods.add(element)
    }
  }

  override fun addAll(elements: Collection<ITimePeriod>): Boolean {
    elements.forEach { e -> add(e) }
    return true
  }

  override fun addAll(index: Int, elements: Collection<ITimePeriod>): Boolean {
    elements.forEach { e -> add(index, e) }
    return true
  }

  override fun containsAll(elements: Collection<ITimePeriod>): Boolean {
    return _periods.containsAll(elements)
  }

  override fun remove(element: ITimePeriod): Boolean {
    return _periods.remove(element)
  }

  override fun removeAll(elements: Collection<ITimePeriod>): Boolean {
    return _periods.removeAll(elements)
  }

  override fun removeAt(index: Int): ITimePeriod {
    return _periods.removeAt(index)
  }

  override fun removeIf(filter: (ITimePeriod) -> Boolean): Boolean {
    return _periods.removeAll(filter)
  }

  override fun retainAll(elements: Collection<ITimePeriod>): Boolean {
    return _periods.retainAll(elements)
  }

  override fun indexOf(element: ITimePeriod): Int = _periods.indexOf(element)

  override fun lastIndexOf(element: ITimePeriod): Int = _periods.lastIndexOf(element)

  override fun iterator(): MutableIterator<ITimePeriod> = _periods.iterator()

  override fun sortByStart(sortDir: SortDirection) {
    when (sortDir) {
      ASC -> _periods.sortBy { x -> x.start }
      DESC -> _periods.sortByDescending { x -> x.start }
    }
  }

  override fun sortByEnd(sortDir: SortDirection) {
    when (sortDir) {
      ASC -> _periods.sortBy { x -> x.end }
      DESC -> _periods.sortByDescending { x -> x.end }
    }
  }

  override fun sortByDuration(sortDir: SortDirection) {
    when (sortDir) {
      ASC -> _periods.sortBy { x -> x.duration }
      DESC -> _periods.sortByDescending { x -> x.duration }
    }
  }

  override fun subList(fromIndex: Int, toIndex: Int): kotlin.collections.MutableList<ITimePeriod> {
    return _periods.subList(fromIndex, toIndex)
  }

  override fun compare(x: ITimePeriod, y: ITimePeriod): Int {
    return x.start.compareTo(y.start)
  }

  override fun move(offset: Duration) {
    if (offset != Duration.ZERO) {
      _periods.forEach { p -> p.move(offset) }
    }
  }

  override fun reset() {
    clear()
  }

  override fun contains(element: ITimePeriod): Boolean {
    return _periods.contains(element)
  }

  override fun listIterator(): MutableListIterator<ITimePeriod> {
    return _periods.listIterator()
  }

  override fun listIterator(index: Int): MutableListIterator<ITimePeriod> {
    return _periods.listIterator(index)
  }

  override fun toString(): String {
    return _periods.joinToString(separator = ",")
  }


  companion object {
    @JvmStatic
    fun of(c: Collection<ITimePeriod>): TimePeriodContainer {
      val container = TimePeriodContainer()
      container.addAll(c)
      return container
    }

    @JvmStatic
    fun of(vararg elements: ITimePeriod): TimePeriodContainer {
      val container = TimePeriodContainer()
      container.addAll(elements.toList())
      return container
    }
  }
}