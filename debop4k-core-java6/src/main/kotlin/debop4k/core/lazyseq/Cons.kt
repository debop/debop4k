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
import java.util.*
import java.util.concurrent.locks.*
import java.util.function.*

/**
 * Cons
 * @author sunghyouk.bae@gmail.com
 */
class Cons<T>(override val head: T, val tailFunc: () -> LazySeq<T>) : LazySeq<T>() {

  @Volatile
  private var tailOrNull: LazySeq<T> = Nil.instance()

  private val lock = ReentrantLock()

  override val tail: LazySeq<T> by lazy {
    if (!isTailDefined) {
      synchronized(lock) {
        if (!isTailDefined) {
          tailOrNull = tailFunc()
        }
      }
    }
    tailOrNull
  }

  override val isTailDefined: Boolean get() = tailOrNull != Nil.instance<T>()

  override fun <R> map(mapper: (T) -> R): LazySeq<R> {
    return cons(mapper(head), { tail.map(mapper) })
  }

  override fun filter(predicate: (T) -> Boolean): LazySeq<T> {
    return if (predicate(head)) {
      cons(head, { tail.filter(predicate) })
    } else {
      tail.filter(predicate)
    }
  }

  override fun <R> flatMap(mapper: (T) -> Iterable<R>): LazySeq<R> {
    val result = fastListOf<R>()
    mapper(head).forEach { result.add(it) }
    return concat(result, { tail.flatMap(mapper) })
  }

  override fun takeUnsafe(maxSize: Long): LazySeq<T> {
    return if (maxSize > 1) {
      cons(head, { tail.takeUnsafe(maxSize - 1) })
    } else {
      lazySeqOf(head)
    }
  }

  override fun isEmpty(): Boolean = false
}


/**
 * Fixed Cons
 */
class FixedCons<T>(override val head: T, override val tail: LazySeq<T>) : LazySeq<T>() {

  override val isTailDefined: Boolean get() = true

  override fun <R> map(mapper: (T) -> R): LazySeq<R> {
    return cons(mapper(head), { tail.map(mapper) })
  }

  override fun filter(predicate: (T) -> Boolean): LazySeq<T> {
    return if (predicate(head)) {
      cons(head, tail.filter(predicate))
    } else {
      tail.filter(predicate)
    }
  }

  override fun <R> flatMap(mapper: (T) -> Iterable<R>): LazySeq<R> {
    val result = fastListOf<R>()
    mapper(head).forEach { result.add(it) }
    return concat(result, tail.flatMap(mapper))
  }

  override fun takeUnsafe(maxSize: Long): LazySeq<T> {
    return if (maxSize > 1) {
      cons(head, tail.takeUnsafe(maxSize - 1))
    } else {
      lazySeqOf(head)
    }
  }

  override fun isEmpty(): Boolean = false
}

/**
 * Empty LazySeq
 */
class Nil<T> : LazySeq<T>() {

  companion object {
    val NIL: Nil<Any> = Nil<Any>()

    @Suppress("UNCHECKED_CAST")
    @JvmStatic fun <T> instance(): Nil<T> = NIL as Nil<T>
  }

  override val head: T
    get() = throw NoSuchElementException("head of empty sequence")

  override val headOption: Option<T>
    get() = Option.None

  override val tail: LazySeq<T>
    get() = throw NoSuchElementException("tail of empty sequence")

  override val isTailDefined: Boolean
    get() = false

  override fun get(index: Int): T = throw IndexOutOfBoundsException(index.toString())

  override fun <R> map(mapper: (T) -> R): LazySeq<R> = instance()
  override fun filter(predicate: (T) -> Boolean): LazySeq<T> = instance()
  override fun <R> flatMap(mapper: (T) -> Iterable<R>): LazySeq<R> = instance()
  override fun takeUnsafe(maxSize: Long): LazySeq<T> = instance()
  override fun dropUnsafe(startInclusive: Long): LazySeq<T> = instance()
  override fun forEach(action: (T) -> Unit) {
    /* Nothing to do */
  }

  override fun max(comparator: Comparator<in T>): T? = null
  override fun min(comparator: Comparator<in T>): T? = null


  override val size: Int get() = 0

  override fun anyMatch(predicate: (T) -> Boolean): Boolean = false
  override fun allMatch(predicate: (T) -> Boolean): Boolean = true
  override fun noneMatch(predicate: (T) -> Boolean): Boolean = true

  override fun takeWhile(predicate: (T) -> Boolean): LazySeq<T> = instance()
  override fun dropWhile(predicate: (T) -> Boolean): LazySeq<T> = instance()

  override fun slidingUnsafe(size: Long): LazySeq<List<T>> = instance()
  override fun groupedUnsafe(size: Long): LazySeq<List<T>> = instance()

  override fun scan(initial: T, binFunc: BinaryOperator<T>): LazySeq<T> = lazySeqOf(initial)
  override fun distinct(): LazySeq<T> = instance()
  override fun startsWith(iterator: Iterator<T>): Boolean = !iterator.hasNext()
  override fun force(): LazySeq<T> = this

  override fun equals(other: Any?): Boolean = other is Nil<*>
  override fun hashCode(): Int = Nil.hashCode()
  override fun isEmpty(): Boolean = true
}