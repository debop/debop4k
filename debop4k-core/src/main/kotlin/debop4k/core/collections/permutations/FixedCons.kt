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

import debop4k.core.collections.eclipseCollections.fastListOf

/**
 * FixedCons
 * @author sunghyouk.bae@gmail.com
 */
class FixedCons<T>(override val head: T, override val tail: Permutation<T>) : Permutation<T>() {

  override val isTailDefined: Boolean get() = true

  override fun <R> map(mapper: (T) -> R): Permutation<R> {
    return cons(mapper(head), { tail.map(mapper) })
  }

  override fun filter(predicate: (T) -> Boolean): Permutation<T> {
    return if (predicate(head)) {
      cons(head, tail.filter(predicate))
    } else {
      tail.filter(predicate)
    }
  }

  override fun <R> flatMap(mapper: (T) -> Iterable<R>): Permutation<R> {
    val result = fastListOf<R>()
    mapper(head).forEach { result.add(it) }
    return concat(result, tail.flatMap(mapper))
  }

  override fun takeUnsafe(maxSize: Long): Permutation<T> {
    return if (maxSize > 1) {
      cons(head, tail.takeUnsafe(maxSize - 1))
    } else {
      permutationOf(head)
    }
  }

  override fun isEmpty(): Boolean = false
}
