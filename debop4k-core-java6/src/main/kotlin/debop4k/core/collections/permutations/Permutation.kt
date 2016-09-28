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

package debop4k.core.collections.permutations

import debop4k.core.collections.eclipseCollections.fastListOf
import debop4k.core.utils.hashOf
import java.util.*

/**
 * 동적으로 계산하는 순열
 * @author sunghyouk.bae@gmail.com
 */
abstract class Permutation<E> : AbstractList<E>(), Sequence<E> {

  abstract val head: E

//  open val headOption: Option<E> get() = Some(head)

  abstract val tail: Permutation<E>

  protected abstract val isTailDefined: Boolean

  operator fun plus(other: Permutation<E>): Permutation<E> {
    return concat(this, other)
  }

  override operator fun get(index: Int): E {
    if (index < 0) {
      throw IndexOutOfBoundsException(index.toString())
    }

    var curr = this
    (index downTo 1).forEach {
      if (curr.tail.isEmpty()) {
        throw IndexOutOfBoundsException(index.toString())
      }
      curr = curr.tail
    }
    return curr.head
  }

  abstract fun <R : Any?> map(mapper: (E) -> R): Permutation<R>

  open fun sequence(): Sequence<E> = Sequence { this.iterator() }

  open fun parallelSequence(): Sequence<E> = Sequence { this.iterator() }

  override fun toString(): String {
    return mkString(", ", "[", "]", true)
  }

  @JvmOverloads
  open fun mkString(separator: CharSequence = ", ",
                    prefix: CharSequence = "",
                    postfix: CharSequence = "",
                    lazy: Boolean = false): String {
    val sb = StringBuilder(prefix)

    var curr: Permutation<E> = this
    while (!curr.isEmpty()) {
      sb.append(curr.head)
      if (!lazy || curr.isTailDefined) {
        if (!curr.tail.isEmpty()) {
          sb.append(separator)
        }
        curr = curr.tail
      } else {
        sb.append(separator).append("?")
        break
      }
    }
    return sb.append(postfix).toString()
  }

  abstract fun filter(predicate: (E) -> Boolean): Permutation<E>

  abstract fun <R> flatMap(mapper: (E) -> Iterable<R>): Permutation<R>

  fun limit(maxSize: Long): Permutation<E> = take(maxSize)

  open fun toList(): List<E> = fastListOf<E>(this.force())

  open fun take(maxSize: Long): Permutation<E> {
    require(maxSize >= 0L)
    return if (maxSize == 0L) emptyPermutation() else takeUnsafe(maxSize)
  }

  abstract fun takeUnsafe(maxSize: Long): Permutation<E>

  open fun drop(startInclusive: Long): Permutation<E> {
    require(startInclusive >= 0)
    return dropUnsafe(startInclusive)
  }

  open fun dropUnsafe(startInclusive: Long): Permutation<E> {
    return if (startInclusive > 0) tail.drop(startInclusive - 1) else this
  }

  override fun subList(fromIndex: Int, toIndex: Int): Permutation<E> = slice(fromIndex.toLong(), toIndex.toLong())

  open fun slice(startInclusive: Long, endExclusive: Long): Permutation<E> {
    require(startInclusive >= 0 && startInclusive <= endExclusive)
    return dropUnsafe(startInclusive).takeUnsafe(endExclusive - startInclusive)
  }

  open fun forEach(action: (E) -> Unit): Unit {
    //    action(head)
//    tail.forEach(action)
    tailrec fun traverse(seq: Permutation<E>, action: (E) -> Unit): Unit {
      action(seq.head)
      traverse(seq.tail, action)
    }
    traverse(this, action)
  }

  @Suppress("UNCHECKED_CAST")
  fun <R : E> reduce(operation: (R, E) -> R): R? {
    if (isEmpty() || tail.isEmpty()) {
      return null
    }
    var result = head as R
    var curr = tail
    while (!curr.isEmpty()) {
      result = operation(result, curr.head)
      curr = curr.tail
    }
    return result

//    var acc = head as R
//    val iter = tail.iterator()
//
//    while (iter.hasNext()) {
//      acc = operation(acc, iter.next())
//    }
//
//    return acc
  }

  fun <R> reduce(identity: R, operation: (R, E) -> R): R {
    var result = identity
    var curr = this
    while (!curr.isEmpty()) {
      result = operation(result, curr.head)
      curr = curr.tail
    }
    return result
  }

  fun <C : Comparable<C>> maxBy(propertyFunc: (E) -> C): E? = max(propertyFunToComparator(propertyFunc))
  fun <C : Comparable<C>> minBy(propertyFunc: (E) -> C): E? = min(propertyFunToComparator(propertyFunc))

  fun max(comparator: (a: E, b: E) -> Int): E? = max(Comparator(comparator))
  fun min(comparator: (a: E, b: E) -> Int): E? = min(Comparator(comparator))

  open fun max(comparator: Comparator<in E>): E? = greatestByComparator(comparator)
  open fun min(comparator: Comparator<in E>): E? = greatestByComparator(comparator.reversed())

  private fun <C : Comparable<C>> propertyFunToComparator(propertyFunc: (E) -> C): Comparator<in E> {
    return Comparator { a, b ->
      val aProperty = propertyFunc(a)
      val bProperty = propertyFunc(b)
      aProperty.compareTo(bProperty)
    }
  }

  private fun greatestByComparator(comparator: Comparator<in E>): E? {
    if (isEmpty()) {
      return null as E
    }
    if (tail.isEmpty()) {
      return head
    }

    var minSoFar = head
    var curr = this.tail
    while (!curr.isEmpty()) {
      minSoFar = maxByComparator(minSoFar, curr.head, comparator)
      curr = curr.tail
    }
    return minSoFar
  }

  override val size: Int
    get() = 1 + tail.size

  override fun iterator(): MutableIterator<E> {
    return PermutationIterator<E>(this)
  }

  open fun anyMatch(predicate: (E) -> Boolean): Boolean = predicate(head) || tail.anyMatch(predicate)
  open fun allMatch(predicate: (E) -> Boolean): Boolean = predicate(head) && tail.allMatch(predicate)
  open fun noneMatch(predicate: (E) -> Boolean): Boolean = !predicate(head) && tail.noneMatch(predicate)

  open fun <S, R> zip(second: Permutation<S>, zipper: (E, S) -> R): Permutation<R> {
    if (second.isEmpty()) {
      return empty<R>()
    } else {
      val headsZipped = zipper.invoke(head, second.head)
      return cons(headsZipped) { tail.zip(second.tail, zipper) }
    }
  }

  open fun takeWhile(predicate: (E) -> Boolean): Permutation<E> {
    return if (predicate(head)) {
      cons(head) { tail.takeWhile(predicate) }
    } else {
      emptyPermutation()
    }
  }

  open fun dropWhile(predicate: (E) -> Boolean): Permutation<E> {
    return if (predicate(head)) {
      tail.dropWhile(predicate)
    } else {
      this
    }
  }

  open fun sliding(size: Long): Permutation<List<E>> {
    require(size > 0)
    return slidingUnsafe(size)
  }

  open fun slidingUnsafe(size: Long): Permutation<List<E>> {
    val window = take(size).toList()
    return cons(window) { tail.slidingFullOnly(size) }
  }

  open fun slidingFullOnly(size: Long): Permutation<List<E>> {
    val window = take(size).toList()
    return if (window.size < size) {
      emptyPermutation()
    } else {
      cons(window) { tail.slidingFullOnly(size) }
    }
  }

  open fun grouped(size: Long): Permutation<List<E>> {
    require(size > 0)
    return groupedUnsafe(size)
  }

  open fun groupedUnsafe(size: Long): Permutation<List<E>> {
    val window = take(size).toList()
    return cons(window) { drop(size).groupedUnsafe(size) }
  }

  open fun scan(initial: E, binFunc: (E, E) -> E): Permutation<E> {
    return cons(initial) { tail.scan(binFunc(initial, head), binFunc) }
  }

  open fun distinct(): Permutation<E> = filterOutSeen(hashSetOf<E>())

  open fun filterOutSeen(exclude: MutableSet<E>): Permutation<E> {
    val moreDistinct = filter { !exclude.contains(it) }
    if (moreDistinct.isEmpty())
      return emptyPermutation()

    val next = moreDistinct.head
    exclude.add(next)
    return cons(next) { moreDistinct.tail.filterOutSeen(exclude) }
  }

  @Suppress("UNCHECKED_CAST")
  open fun sorted(): Permutation<E> {
    return sorted(Comparator { o1, o2 -> (o1 as Comparable<E>).compareTo(o2) })
  }

  fun sorted(comparator: (E1: E, E2: E) -> Int): Permutation<E> {
    return sorted(Comparator(comparator))
  }

  open fun sorted(comparator: Comparator<in E>): Permutation<E> {
    val list = fastListOf<E>(this)
    list.sortThis(comparator)
    return permutationOf(list)
  }

  open fun startsWith(prefix: Iterable<E>): Boolean = startsWith(prefix.iterator())
  open fun startsWith(iterator: Iterator<E>): Boolean {
    return !iterator.hasNext() ||
           head?.equals(iterator.next()) ?: false &&
           tail.startsWith(iterator)
  }

  open fun force(): Permutation<E> {
    tail.force()
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other is Permutation<*>) {
      return !other.isEmpty() && head == other.head && tail == other.tail
    }
    return false
  }

  override fun hashCode(): Int {
    return hashOf(head, tail)
  }

  companion object {

    @JvmStatic
    fun <E> empty(): Permutation<E> = Nil.instance()


    private fun <E> maxByComparator(first: E, second: E, comparator: Comparator<in E>): E {
      return if (comparator.compare(first, second) >= 0) first else second
    }
  }
}

