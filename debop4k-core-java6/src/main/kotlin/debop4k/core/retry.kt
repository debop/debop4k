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

@file:JvmName("retry")

package debop4k.core

import debop4k.core.functional.Option
import debop4k.core.functional.eithers.Either

const val DEFAULT_DELAY = 500

class Successful<T>(val predicate: (T) -> Boolean) {

  fun <R : T> or(that: Successful<R>): Successful<R> = Successful<R> { predicate(it) || that.predicate(it) }
  fun <R : T> or(that: () -> Boolean): Successful<R> = Successful<R> { predicate(it) || that() }
  fun <R : T> and(that: Successful<R>): Successful<R> = Successful<R> { predicate(it) && that.predicate(it) }
  fun <R : T> and(that: () -> Boolean): Successful<R> = Successful<R> { predicate(it) && that() }

  companion object {
    fun <A, B> either(): Successful<Either<A, B>> = Successful { it.isRight() }
    fun <A> option(): Successful<Option<A>> = Successful { it.isDefined() }

    @JvmStatic val always: Successful<Any> = Successful { true }
    @JvmStatic val never: Successful<Any> = Successful { false }
  }
}
