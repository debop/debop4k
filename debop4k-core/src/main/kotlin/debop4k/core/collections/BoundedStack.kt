/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.core.collections

import org.eclipse.collections.impl.list.mutable.FastList
import java.lang.IndexOutOfBoundsException
import java.lang.UnsupportedOperationException
import java.util.*

/**
 * 크기에 제한이 있는 스택 (Stack)
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Suppress("UNCHECKED_CAST")
class BoundedStack<E>(val maxSize: Int) : Stack<E>() {

  val array: Array<E?> = arrayOfNulls<Any?>(maxSize) as Array<E?>

  private var top = 0
  private var count = 0

  override val size: Int get() = count

  fun length(): Int = count

  override fun get(index: Int): E {
    if (index >= count)
      throw IndexOutOfBoundsException(index.toString())

    return array[(top + index) % maxSize] as E
  }

  @Synchronized
  override fun add(element: E): Boolean {
    push(element)
    return true
  }

  @Synchronized
  override fun addElement(elem: E): Unit {
    throw UnsupportedOperationException()
  }

  @Synchronized
  override fun add(index: Int, element: E) {
    insert(index, element)
  }

  @Synchronized
  override fun addAll(elements: Collection<E>): Boolean {
    elements.forEach { push(it) }
    return true
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
    throw UnsupportedOperationException()
  }

  @Synchronized
  override fun push(item: E): E {
    top = if (top == 0) maxSize - 1 else top - 1
    array[top] = item
    if (count < maxSize)
      count++
    return item
  }

  @Synchronized
  fun pushAll(vararg items: E): Unit {
    items.forEach { push(it) }
  }

  @Synchronized
  override fun pop(): E {
    if (count == 0)
      throw NoSuchElementException()

    val item = array[top]
    top = (++top) % maxSize
    count--
    return item!!
  }

  @Synchronized
  override fun peek(): E {
    if (count == 0)
      throw NoSuchElementException()

    return array[top]!!
  }

  @Synchronized
  fun insert(index: Int, elem: E): E {
    if (index == 0)
      return push(elem)
    else if (index > count)
      throw IndexOutOfBoundsException(index.toString())
    else if (index == count) {
      array[(top + index) % maxSize] = elem
      count++
    } else {
      val swapped = array[index]!!
      array[index] = elem
      insert(index - 1, swapped)
    }

    return elem
  }

  @Synchronized
  fun update(index: Int, elem: E): Unit {
    if (index > count)
      throw IndexOutOfBoundsException(index.toString())
    array[(top + index) % maxSize] = elem
  }

  @Synchronized
  fun toList(): FastList<E> {
    val list = fastListOf<E>()
    this.forEach { list.add(it) }
    return list
  }

  @Synchronized
  override fun iterator(): MutableIterator<E> {
    return object : MutableIterator<E> {

      private var index = 0
      override fun hasNext(): Boolean = index != count
      override fun next(): E = get(index++)
      override fun remove() { /* Nothing to do. */
      }
    }
  }
}