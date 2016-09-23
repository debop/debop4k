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

import debop4k.core.functional.Option
import debop4k.core.utils.hashOf
import org.eclipse.collections.api.list.MutableList
import java.util.*
import java.util.function.*
import java.util.stream.*

/**
 * LazySeq
 * @author sunghyouk.bae@gmail.com
 */
abstract class LazySeq<E> : AbstractList<E>() {

  abstract val head: E
  abstract val headOption: Option<E>

  abstract val tail: LazySeq<E>

  protected abstract val isTailDefined: Boolean

  override operator fun get(index: Int): E = TODO()

  abstract fun <R> map(mapper: (E) -> R): LazySeq<R>

  open fun sequence(): Sequence<E> = TODO()

  open fun parallelSequence(): Sequence<E> = TODO()

  override fun toString(): String {
    return this.joinToString(", ", "[", "]")
  }

  @JvmOverloads
  open fun mkString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", lazy: Boolean = false): String {
    TODO()
  }


  open fun concat(elements: Iterable<E>, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
  open fun concat(elements: Iterable<E>, tail: LazySeq<E>): LazySeq<E> = TODO()

  open fun concat(iterator: Iterator<E>, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
  open fun concat(iterator: Iterator<E>, tail: LazySeq<E>): LazySeq<E> = TODO()

  open fun createNonEmptyIterator(iterator: Iterator<E>, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
  open fun createNonEmptyIterator(iterator: Iterator<E>, tail: LazySeq<E>): LazySeq<E> = TODO()


  open fun filter(predicate: (E) -> Boolean): LazySeq<E> = TODO()

  open fun <R> flatMap(mapper: (E) -> Iterable<R>): LazySeq<R> = TODO()

  open fun forEach(action: (E) -> Unit): Unit {
    tailrec fun traverse(seq: LazySeq<E>, action: (E) -> Unit): Unit {
      action(seq.head)
      traverse(seq.tail, action)
    }
    traverse(this, action)
  }

  fun limit(maxSize: Long): LazySeq<E> = take(maxSize)

  open fun toList(): MutableList<E> = TODO()

  override fun subList(fromIndex: Int, toIndex: Int): LazySeq<E> = TODO()

  open fun slice(startInclusive: Long, endExclusive: Long): LazySeq<E> = TODO()


  open fun <C : Comparable<C>> maxBy(propertyFunc: (E) -> C): E = TODO()
  open fun <C : Comparable<C>> minBy(propertyFunc: (E) -> C): E = TODO()

  open fun max(comparator: Comparator<in E>): E? = TODO()
  open fun min(comparator: Comparator<in E>): E? = TODO()

  override val size: Int
    get() = 1 + tail.size

  override fun iterator(): MutableIterator<E> {
    return LazySeqIterator<E>(this)
  }

  open fun anyMatch(predicate: (E) -> Boolean): Boolean = predicate(head) || tail.anyMatch(predicate)
  open fun allMatch(predicate: (E) -> Boolean): Boolean = predicate(head) && tail.allMatch(predicate)
  open fun nonMatch(predicate: (E) -> Boolean): Boolean = !predicate(head) && tail.nonMatch(predicate)

  open fun take(maxSize: Long): LazySeq<E> = TODO()
  open fun takeUnsafe(maxSize: Long): LazySeq<E> = TODO()
  open fun takeWhile(predicate: (E) -> Boolean): LazySeq<E> = TODO()
  open fun drop(startInclusive: Long): LazySeq<E> = TODO()
  open fun dropUnsafe(startInclusive: Long): LazySeq<E> = TODO()
  open fun dropWhile(predicate: (E) -> Boolean): LazySeq<E> = TODO()

  open fun sliding(size: Int): LazySeq<List<E>> = TODO()
  open fun slidingUnsafe(size: Int): LazySeq<List<E>> = TODO()
  open fun slidingFullOnly(size: Int): LazySeq<List<E>> = TODO()

  open fun grouped(size: Int): LazySeq<List<E>> = TODO()
  open fun groupedUnsafe(size: Int): LazySeq<List<E>> = TODO()

  open fun scan(initial: E, binFunc: BinaryOperator<E>): LazySeq<E> = TODO()
  open fun distinct(): LazySeq<E> = TODO()
  open fun filterOutSeen(exclude: Set<E>): LazySeq<E> = TODO()

  @SuppressWarnings("unchecked")
  open fun sorted(): LazySeq<E> = TODO()

  open fun sorted(comparator: Comparator<E>): LazySeq<E> = TODO()

  open fun startsWith(prefix: Iterable<E>): Boolean = TODO()
  open fun startsWith(iterator: Iterator<E>): Boolean = TODO()

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

  companion object {
    @JvmStatic fun <E> of(element: E): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(element: E, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(element1: E, element2: E): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(element1: E, element2: E, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(element1: E, element2: E, element3: E): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(element1: E, element2: E, element3: E, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()

    @JvmStatic fun <E> of(vararg elements: E): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(elements: Iterable<E>): LazySeq<E> = TODO()
    @JvmStatic fun <E> of(iterator: Iterator<E>): LazySeq<E> = TODO()
  }
}

fun <E> cons(head: E, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
fun <E> cons(head: E, tail: LazySeq<E>): LazySeq<E> = TODO()

fun <E> iterate(initial: E, func: (E) -> E): LazySeq<E> = TODO()

fun <E> toLazySeq(): Collector<E, LazySeq<E>, LazySeq<E>> = TODO()

fun <E> tabulate(start: Int, generator: (Int) -> E): Collector<E, LazySeq<E>, LazySeq<E>> = TODO()

fun <E> continually(generator: () -> E): LazySeq<E> = TODO()
fun <E> continually(cycle: Iterable<E>): LazySeq<E> = TODO()
fun <E> continuallyUnsafe(cycle: Iterable<E>): LazySeq<E> = TODO()
fun <E> continually(value: E): LazySeq<E> = TODO()

fun numbers(start: Int, step: Int): LazySeq<Int> = TODO()
fun numbers(start: Long, step: Long): LazySeq<Long> = TODO()
fun numbers(start: Double, step: Double): LazySeq<Double> = TODO()


