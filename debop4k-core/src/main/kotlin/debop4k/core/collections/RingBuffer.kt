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

package debop4k.core.collections

import org.eclipse.collections.api.block.predicate.Predicate
import org.eclipse.collections.impl.list.mutable.FastList
import java.lang.IndexOutOfBoundsException
import java.util.*

/**
 * 제한된 크기를 가지는 Ring Buffer 입니다.
 *
 * @author debop sunghyouk.bae@gmail.com
 */
class RingBuffer<E>(val maxSize: Int) : Iterable<E> {

  @Suppress("UNCHECKED_CAST")
  val array: Array<E?> = arrayOfNulls<Any?>(maxSize) as Array<E?> // Array<Any?>(maxSize, { null }) as Array<E?>

  var read: Int = 0
  var write: Int = 0
  var _count: Int = 0

  val length: Int get() = _count

  fun size(): Int = _count

  val isEmpty: Boolean get() = _count == 0

  fun add(item: E): Boolean {
    array[write] = item
    write = (++write) % maxSize

    if (_count == maxSize) read = (++read) % maxSize
    else ++_count

    return true
  }

  fun addAll(vararg elements: E): Boolean {
    elements.forEach { add(it) }
    return true
  }

  fun addAll(c: Collection<E>): Boolean {
    c.forEach { add(it) }
    return true
  }

  operator fun get(index: Int): E {
    if (index >= _count)
      throw IndexOutOfBoundsException(index.toString())

    return array[(read + index) % maxSize]!!
  }

  fun drop(n: Int): RingBuffer<E> {
    if (n >= maxSize)
      clear()

    read = (read + n) % maxSize
    return this
  }

  fun removeIf(predicate: Predicate<E>): Boolean {
    return removeIf { predicate.accept(it) }
  }

  fun removeIf(predicate: (E) -> Boolean): Boolean {
    var removeCount = 0
    var j = 0

    for (i in 0 until _count) {
      val elem = get(i)
      if (predicate(elem)) {
        removeCount++
      } else {
        if (j < i) set(j, elem)
        j++
      }
    }

    _count -= removeCount
    write = (read + _count) % maxSize
    return removeCount > 0
  }

  operator fun set(index: Int, elem: E): Unit {
    if (index >= _count)
      throw IndexOutOfBoundsException(index.toString())

    array[(read + index) % maxSize] = elem
  }

  fun next(): E? {
    if (read == write)
      throw NoSuchElementException()

    val result = array[read]
    read = (++read) % maxSize
    _count--
    return result
  }

  fun clear(): Unit {
    read = 0
    write = 0
    _count = 0
  }

  inline fun <reified E> toArray(): Array<E?> {
    val result = arrayOfNulls<E?>(_count)
    return toArray(result)
  }

  inline fun <reified E> toArray(a: Array<E?>): Array<E?> {
    var b = a
    if (a.size < length) {
      b = arrayOfNulls<E?>(length)
    }

    this.forEachIndexed { i, e -> b[i] = e as E }

    return b
  }

  fun toList(): FastList<E> = FastList.newList(this)


  override fun iterator(): MutableIterator<E> {

    return object : MutableIterator<E> {
      private var index = 0
      override fun hasNext(): Boolean = index != _count
      override fun next(): E = get(index++)
      override fun remove() {
        // Nothing to do.
      }
    }
  }
}