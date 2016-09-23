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
import java.util.*

///**
// * Cons
// * @author sunghyouk.bae@gmail.com
// */
//class Cons<T>(override val head: T, val tailFunc: () -> LazySeq<T>) : LazySeq<T>() {
//
//
//}
//
///**
// * Fixed Cons
// */
//class FixedCons<T>(override val head: T, override val tail: LazySeq<T>) : LazySeq<T>() {
//
//}

/**
 * Empty LazySeq
 */
class Nil<T> : LazySeq<T>() {

  companion object {
    val NIL: Nil<Any> = Nil<Any>()

    @SuppressWarnings("unchecked")
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

  override fun min(comparator: Comparator<in T>): T? = null
  override fun max(comparator: Comparator<in T>): T? = null

  override val size: Int get() = 0

  override fun anyMatch(predicate: (T) -> Boolean): Boolean = false
  override fun allMatch(predicate: (T) -> Boolean): Boolean = false
  override fun nonMatch(predicate: (T) -> Boolean): Boolean = true

  override fun takeWhile(predicate: (T) -> Boolean): LazySeq<T> = instance()
  override fun dropWhile(predicate: (T) -> Boolean): LazySeq<T> = instance()


}