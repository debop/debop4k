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
import org.eclipse.collections.api.list.MutableList
import org.eclipse.collections.impl.list.mutable.AbstractMutableList
import java.util.*

/**
 * LazySeq
 * @author sunghyouk.bae@gmail.com
 */
abstract class LazySeq<E> : AbstractMutableList<E>() {

  abstract val head: E
  abstract val headOption: Option<E>

  abstract val tail: LazySeq<E>

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

  open fun filter(predicate: (E) -> Boolean): LazySeq<E> = TODO()

  open fun <R> flatMap(mapper: (E) -> Iterable<R>): LazySeq<R> = TODO()


  fun limit(maxSize: Long): LazySeq<E> = take(maxSize)


  override fun toList(): MutableList<E> = TODO()

  override fun subList(fromIndex: Int, toIndex: Int): LazySeq<E> = TODO()

  fun slice(startInclusive: Long, endExclusive: Long): LazySeq<E> = TODO()

  override val size: Int
    get() = 1 + tail.size

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


fun <E> Iterable<E>.concat(tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
fun <E> Iterable<E>.concat(tail: LazySeq<E>): LazySeq<E> = TODO()

fun <E> Iterator<E>.concat(tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
fun <E> Iterator<E>.concat(tail: LazySeq<E>): LazySeq<E> = TODO()

fun <E> Iterator<E>.createNonEmptyIterator(tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
fun <E> Iterator<E>.createNonEmptyIterator(tail: LazySeq<E>): LazySeq<E> = TODO()

fun <E> cons(head: E, tailFun: () -> LazySeq<E>): LazySeq<E> = TODO()
fun <E> cons(head: E, tail: LazySeq<E>): LazySeq<E> = TODO()

fun <E> iterate(initial: E, func: (E) -> E): LazySeq<E> = TODO()

fun <E> continually(generator: () -> E): LazySeq<E> = TODO()
fun <E> continually(cycle: Iterable<E>): LazySeq<E> = TODO()
fun <E> continuallyUnsafe(cycle: Iterable<E>): LazySeq<E> = TODO()
fun <E> continually(value: E): LazySeq<E> = TODO()

fun numbers(start: Int, step: Int): LazySeq<Int> = TODO()
fun numbers(start: Long, step: Long): LazySeq<Long> = TODO()
fun numbers(start: Double, step: Double): LazySeq<Double> = TODO()


fun <E> LazySeq<E>.forEach(action: (E) -> Unit): Unit {
  tailrec fun traverse(seq: LazySeq<E>, action: (E) -> Unit): Unit {
    action(seq.head)
    traverse(seq.tail, action)
  }
  traverse(this, action)
}

fun <E, C : Comparable<C>> LazySeq<E>.maxBy(propertyFunc: (E) -> C): E = TODO()
fun <E, C : Comparable<C>> LazySeq<E>.minBy(propertyFunc: (E) -> C): E = TODO()

fun <E : Comparable<E>> LazySeq<E>.max(comparator: Comparator<E>): E = TODO()
fun <E : Comparable<E>> LazySeq<E>.min(comparator: Comparator<E>): E = TODO()

fun <E> LazySeq<E>.take(maxSize: Long): LazySeq<E> = TODO()
fun <E> LazySeq<E>.takeUnsafe(maxSize: Long): LazySeq<E> = TODO()
fun <E> LazySeq<E>.takeWhile(predicate: (E) -> Boolean): LazySeq<E> = TODO()
fun <E> LazySeq<E>.drop(startInclusive: Long): LazySeq<E> = TODO()
fun <E> LazySeq<E>.dropUnsafe(startInclusive: Long): LazySeq<E> = TODO()
fun <E> LazySeq<E>.dropWhile(predicate: (E) -> Boolean): LazySeq<E> = TODO()

fun <E> LazySeq<E>.grouped(size: Int): LazySeq<List<E>> = TODO()
fun <E> LazySeq<E>.groupedUnsafe(size: Int): LazySeq<List<E>> = TODO()

fun <E> LazySeq<E>.scan(