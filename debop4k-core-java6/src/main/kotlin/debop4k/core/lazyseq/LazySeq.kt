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

package debop4k.core.lazyseq

import debop4k.core.collections.eclipseCollections.fastListOf
import debop4k.core.functional.Option
import debop4k.core.functional.Option.Some
import debop4k.core.utils.hashOf
import java.util.*
import java.util.function.*

/**
 * LazySeq
 * @author sunghyouk.bae@gmail.com
 */
abstract class LazySeq<E> : AbstractList<E>() {

  abstract val head: E

  open val headOption: Option<E> get() = Some(head)

  abstract val tail: LazySeq<E>

  protected abstract val isTailDefined: Boolean

  override operator fun get(index: Int): E {
    require(index >= 0)

    var curr = this
    (index downTo 1).forEach {
      if (curr.tail.isEmpty()) {
        throw IndexOutOfBoundsException(index.toString())
      }
      curr = curr.tail
    }
    return curr.head
  }

  abstract fun <R : Any?> map(mapper: (E) -> R): LazySeq<R>

  open fun sequence(): Sequence<E> = Sequence { this.iterator() }

  open fun parallelSequence(): Sequence<E> = Sequence { this.iterator() }

  override fun toString(): String {
    return this.joinToString(", ", "[", "]")
  }

  @JvmOverloads
  open fun mkString(separator: CharSequence = ", ",
                    prefix: CharSequence = "",
                    postfix: CharSequence = "",
                    lazy: Boolean = false): String {
    val sb = StringBuilder(prefix)

    var curr = this
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

  abstract fun filter(predicate: (E) -> Boolean): LazySeq<E>

  abstract fun <R> flatMap(mapper: (E) -> Iterable<R>): LazySeq<R>

  fun limit(maxSize: Long): LazySeq<E> = take(maxSize)

  open fun toList(): List<E> = fastListOf<E>(this.force())

  open fun take(maxSize: Long): LazySeq<E> {
    require(maxSize >= 0)
    return if (maxSize == 0L) emptyLazySeq() else takeUnsafe(maxSize)
  }

  abstract fun takeUnsafe(maxSize: Long): LazySeq<E>

  open fun drop(startInclusive: Long): LazySeq<E> {
    require(startInclusive >= 0L)
    return dropUnsafe(startInclusive)
  }

  open fun dropUnsafe(startInclusive: Long): LazySeq<E> {
    return if (startInclusive > 0) tail.drop(startInclusive - 1) else this
  }

  override fun subList(fromIndex: Int, toIndex: Int): LazySeq<E> = slice(fromIndex.toLong(), toIndex.toLong())

  open fun slice(startInclusive: Long, endExclusive: Long): LazySeq<E> {
    require(startInclusive >= 0 && startInclusive < endExclusive)
    return dropUnsafe(startInclusive).takeUnsafe(endExclusive - startInclusive)
  }

  open fun forEach(action: (E) -> Unit): Unit {
    action(head)
    tail.forEach(action)
//    tailrec fun traverse(seq: LazySeq<E>, action: (E) -> Unit): Unit {
//      action(seq.head)
//      traverse(seq.tail, action)
//    }
//    traverse(this, action)
  }

  open fun <C : Comparable<C>> maxBy(propertyFunc: (E) -> C): E? = max(propertyFunToComparator(propertyFunc))
  open fun <C : Comparable<C>> minBy(propertyFunc: (E) -> C): E? = min(propertyFunToComparator(propertyFunc))

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
    if (tail.isEmpty())
      return head

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
    return LazySeqIterator<E>(this)
  }

  open fun anyMatch(predicate: (E) -> Boolean): Boolean = predicate(head) || tail.anyMatch(predicate)
  open fun allMatch(predicate: (E) -> Boolean): Boolean = predicate(head) && tail.allMatch(predicate)
  open fun noneMatch(predicate: (E) -> Boolean): Boolean = !predicate(head) && tail.noneMatch(predicate)

  open fun takeWhile(predicate: (E) -> Boolean): LazySeq<E> {
    return if (predicate(head)) {
      cons(head) { tail.takeWhile(predicate) }
    } else {
      emptyLazySeq()
    }
  }

  open fun dropWhile(predicate: (E) -> Boolean): LazySeq<E> {
    return if (predicate(head)) {
      tail.dropWhile(predicate)
    } else {
      this
    }
  }

  open fun sliding(size: Int): LazySeq<List<E>> {
    require(size > 0)
    return slidingUnsafe(size)
  }

  open fun slidingUnsafe(size: Int): LazySeq<List<E>> {
    val window = take(size).toList()
    return cons(window) { tail.slidingFullOnly(size) }
  }

  open fun slidingFullOnly(size: Int): LazySeq<List<E>> {
    val window = take(size).toList()
    return if (window.size < size) {
      emptyLazySeq()
    } else {
      cons(window) { tail.slidingFullOnly(size) }
    }
  }

  open fun grouped(size: Int): LazySeq<List<E>> {
    require(size > 0)
    return groupedUnsafe(size)
  }

  open fun groupedUnsafe(size: Int): LazySeq<List<E>> {
    val window = take(size).toList()
    return cons(window) { drop(size.toLong()).groupedUnsafe(size) }
  }

  // TODO: 이건 Java 8에서 지원하는 것이라 LazySeqStream 에 정의해야 한다
  open fun scan(initial: E, binFunc: BinaryOperator<E>): LazySeq<E> {
    return cons(initial) { tail.scan(binFunc.apply(initial, head), binFunc) }
  }

  open fun distinct(): LazySeq<E> = filterOutSeen(hashSetOf<E>())
  open fun filterOutSeen(exclude: MutableSet<E>): LazySeq<E> {
    val moreDistinct = filter { !exclude.contains(it) }
    if (moreDistinct.isEmpty())
      return emptyLazySeq()

    val next = moreDistinct.head
    exclude.add(next)
    return cons(next) { moreDistinct.tail.filterOutSeen(exclude) }
  }

  @Suppress("UNCHECKED_CAST")
  open fun sorted(): LazySeq<E> {
    return sorted(Comparator { o1, o2 -> (o1 as Comparable<E>).compareTo(o2) })
  }

  open fun sorted(comparator: Comparator<in E>): LazySeq<E> {
    val list = fastListOf<E>(this)
    list.sortThis(comparator)
    return lazySeqOf(list)
  }

  open fun startsWith(prefix: Iterable<E>): Boolean = startsWith(prefix.iterator())
  open fun startsWith(iterator: Iterator<E>): Boolean {
    return !iterator.hasNext() ||
           head?.equals(iterator.next()) ?: false &&
           tail.startsWith(iterator)
  }

  open fun force(): LazySeq<E> {
    tail.force()
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (this == other) return true
    if (other is LazySeq<*>) {
      return !other.isEmpty() && hashCode() == other.hashCode()
    }
    return false
  }

  override fun hashCode(): Int {
    return hashOf(head, tail)
  }
}

fun <E> emptyLazySeq(): LazySeq<E> = Nil.instance()

fun <E> lazySeqOf(element: E): LazySeq<E>
    = cons(element, emptyLazySeq())

fun <E> lazySeqOf(element: E, tailFunc: () -> LazySeq<E>): LazySeq<E>
    = cons(element, tailFunc)

fun <E> lazySeqOf(element1: E, element2: E): LazySeq<E>
    = cons(element1, lazySeqOf(element2))

fun <E> lazySeqOf(element1: E, element2: E, tailFunc: () -> LazySeq<E>): LazySeq<E>
    = cons(element1, lazySeqOf(element2, tailFunc))

fun <E> lazySeqOf(element1: E, element2: E, element3: E): LazySeq<E>
    = cons(element1, lazySeqOf(element2, element3))

fun <E> lazySeqOf(element1: E, element2: E, element3: E, tailFunc: () -> LazySeq<E>): LazySeq<E>
    = cons(element1, lazySeqOf(element2, element3, tailFunc))

fun <E> lazySeqOf(vararg elements: E): LazySeq<E> = lazySeqOf(elements.iterator())
fun <E> lazySeqOf(elements: Iterable<E>): LazySeq<E> = lazySeqOf(elements.iterator())
fun <E> lazySeqOf(iterator: Iterator<E>): LazySeq<E> {
  return if (iterator.hasNext()) {
    cons(iterator.next(), { lazySeqOf(iterator) })
  } else {
    emptyLazySeq()
  }
}

fun <E> concat(elements: Iterable<E>, tailFunc: () -> LazySeq<E>): LazySeq<E>
    = concat(elements.iterator(), tailFunc)

fun <E> concat(elements: Iterable<E>, tail: LazySeq<E>): LazySeq<E>
    = concat(elements.iterator(), tail)

fun <E> concat(iterator: Iterator<E>, tailFunc: () -> LazySeq<E>): LazySeq<E> {
  return if (iterator.hasNext()) {
    concatNonEmptyIterator(iterator, tailFunc)
  } else {
    tailFunc()
  }
}

fun <E> concat(iterator: Iterator<E>, tail: LazySeq<E>): LazySeq<E> {
  return if (iterator.hasNext()) {
    concatNonEmptyIterator(iterator, tail)
  } else {
    tail
  }
}

private fun <E> concatNonEmptyIterator(iterator: Iterator<E>, tail: LazySeq<E>): LazySeq<E> {
  val next = iterator.next()
  return if (iterator.hasNext()) {
    cons(next, concatNonEmptyIterator(iterator, tail))
  } else {
    cons(next, tail)
  }
}

private fun <E> concatNonEmptyIterator(iterator: Iterator<E>, tailFunc: () -> LazySeq<E>): LazySeq<E> {
  val next = iterator.next()
  return if (iterator.hasNext()) {
    cons(next, concatNonEmptyIterator(iterator, tailFunc))
  } else {
    cons(next, tailFunc)
  }
}

fun <E> cons(head: E, tailFunc: () -> LazySeq<E>): LazySeq<E> = Cons(head, tailFunc)
fun <E> cons(head: E, tail: LazySeq<E>): LazySeq<E> = FixedCons(head, tail)

fun <E> iterate(initial: E, func: (E) -> E): LazySeq<E> {
  return Cons(initial, { iterate(func.invoke(initial), func) })
}

fun <E> tabulate(start: Int, generator: (Int) -> E): LazySeq<E> {
  return cons(generator(start)) { tabulate(start + 1, generator) }
}

fun <E> continually(generator: () -> E): LazySeq<E> = cons(generator.invoke()) { continually(generator) }
fun <E> continually(cycle: Iterable<E>): LazySeq<E> {
  return if (!cycle.iterator().hasNext()) {
    emptyLazySeq()
  } else {
    continuallyUnsafe(cycle)
  }
}

fun <E> continuallyUnsafe(cycle: Iterable<E>): LazySeq<E> = concat(cycle) { continually(cycle) }
fun <E> continually(value: E): LazySeq<E> = cons(value) { continually(value) }

@JvmOverloads
fun numbers(start: Int, step: Int = 1): LazySeq<Int>
    = cons(start) { numbers(start + step, step) }

@JvmOverloads
fun numbers(start: Long, step: Long = 1L): LazySeq<Long>
    = cons(start) { numbers(start + step, step) }


@JvmOverloads
fun numbers(start: Float, step: Float = 1.0F): LazySeq<Float>
    = cons(start) { numbers(start + step, step) }

@JvmOverloads
fun numbers(start: Double, step: Double = 1.0): LazySeq<Double>
    = cons(start) { numbers(start + step, step) }


private fun <E> maxByComparator(first: E, second: E, comparator: Comparator<in E>): E {
  return if (comparator.compare(first, second) >= 0) first else second
}