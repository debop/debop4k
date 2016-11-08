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

package debop4k.core.collections.permutations

import java.lang.IndexOutOfBoundsException
import java.util.*

/**
 * Permutation 의 종료를 나타내는 클래스입니다.
 * @author sunghyouk.bae@gmail.com
 */
class Nil<T> : Permutation<T>() {

  companion object {
    @JvmField val NIL: Nil<Any> = Nil<Any>()

    @Suppress("UNCHECKED_CAST")
    @JvmStatic fun <T> instance(): Nil<T> = NIL as Nil<T>
  }

  override val head: T
    get() = throw NoSuchElementException("head of empty sequence")

//  override val headOption: Option<T>
//    get() = Option.None

  override val tail: Permutation<T>
    get() = throw NoSuchElementException("tail of empty sequence")

  override val isTailDefined: Boolean
    get() = false

  override fun get(index: Int): T = throw IndexOutOfBoundsException(index.toString())

  override fun <R> map(mapper: (T) -> R): Permutation<R> = instance()
  override fun filter(predicate: (T) -> Boolean): Permutation<T> = instance()
  override fun <R> flatMap(mapper: (T) -> Iterable<R>): Permutation<R> = instance()
  override fun takeUnsafe(maxSize: Long): Permutation<T> = instance()
  override fun dropUnsafe(startInclusive: Long): Permutation<T> = instance()
  override fun forEach(action: (T) -> Unit) {
    /* Nothing to do */
  }

  override fun max(comparator: Comparator<in T>): T? = null
  override fun min(comparator: Comparator<in T>): T? = null


  override val size: Int get() = 0

  override fun anyMatch(predicate: (T) -> Boolean): Boolean = false
  override fun allMatch(predicate: (T) -> Boolean): Boolean = true
  override fun noneMatch(predicate: (T) -> Boolean): Boolean = true

  override fun <S, R> zip(second: Permutation<S>, zipper: (T, S) -> R): Permutation<R> = instance()

  override fun takeWhile(predicate: (T) -> Boolean): Permutation<T> = instance()
  override fun dropWhile(predicate: (T) -> Boolean): Permutation<T> = instance()

  override fun slidingUnsafe(size: Long): Permutation<List<T>> = instance()
  override fun groupedUnsafe(size: Long): Permutation<List<T>> = instance()

  override fun scan(initial: T, binFunc: (T, T) -> T): Permutation<T> = permutationOf(initial)

  override fun distinct(): Permutation<T> = instance()
  override fun startsWith(iterator: Iterator<T>): Boolean = !iterator.hasNext()
  override fun force(): Permutation<T> = this

  override fun equals(other: Any?): Boolean = other is Nil<*>
  override fun hashCode(): Int = Nil.hashCode()
  override fun isEmpty(): Boolean = true
}