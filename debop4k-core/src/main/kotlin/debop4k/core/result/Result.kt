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

package debop4k.core.result

import java.lang.Exception
import java.lang.UnsupportedOperationException

/**
 * kittinunf/Result 를 Porting 함 (Functional/Reactive/Asynchronous Programming 시 결과값 표현엥 응용할 수 있음)
 *
 * @see [kittinunf/Result](https://github.com/kittinunf/Result)
 * @see [Railway Oriented Programming](http://fsharpforfunandprofit.com/rop/#monads)
 */
sealed class Result<out V : Any, out E : Exception> {

  abstract operator fun component1(): V?
  abstract operator fun component2(): E?

  inline fun <X> fold(success: (V) -> X, failure: (E) -> X): X = when (this) {
    is Success -> success(this.value)
    is Failure -> failure(this.error)
  }

  abstract fun get(): V

  class Success<out V : Any, out E : Exception>(val value: V) : Result<V, E>() {
    override fun component1(): V? = value
    override fun component2(): E? = null
    override fun get(): V = value

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      return other is Success<*, *> && value == other.value
    }

    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "[Success: $value]"
  }

  class Failure<out V : Any, out E : Exception>(val error: E) : Result<V, E>() {
    override fun component1(): V? = null
    override fun component2(): E = error
    override fun get(): V = throw error

    fun getException(): E = error

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      return other is Failure<*, *> && error == other.error
    }

    override fun hashCode(): Int = error.hashCode()
    override fun toString(): String = "[Failure: $error]"
  }

  companion object {

    @JvmStatic
    fun <E : Exception> error(err: E) = Failure<Nothing, E>(err)

    @JvmStatic
    @JvmOverloads
    fun <V : Any> of(value: V?, fail: () -> Exception = { Exception() }): Result<V, Exception> {
      return value?.let { Success<V, Nothing>(it) } ?: error(fail())
    }

    fun <V : Any> of(factory: () -> V): Result<V, Exception> = try {
      Success(factory())
    } catch(e: Exception) {
      Failure(e)
    }
  }
}

inline fun <reified X> Result<*, *>.getAs() = when (this) {
  is Result.Success -> value as? X
  is Result.Failure -> error as? X
  else              -> throw UnsupportedOperationException("Unknown class type")
}

fun <V : Any> Result<V, *>.success(f: (V) -> Unit): Unit = fold(f, {})

fun <E : Exception> Result<*, E>.failure(f: (E) -> Unit): Unit = fold({}, f)

infix fun <V : Any, E : Exception> Result<V, E>.or(fallback: V): Result<V, E> = when (this) {
  is Result.Success -> this
  else              -> Result.Success<V, E>(fallback)
}

infix fun <V : Any, E : Exception> Result<V, E>.getOrElse(fallback: V): V = when (this) {
  is Result.Success -> this.value
  else              -> fallback
}

fun <V : Any, U : Any, E : Exception> Result<V, E>.map(transform: (V) -> U): Result<U, E> {
  return when (this) {
    is Result.Success -> Result.Success<U, E>(transform(value))
    is Result.Failure -> Result.Failure<U, E>(error)
  }
}

fun <V : Any, U : Any, E : Exception> Result<V, E>.flatMap(transform: (V) -> Result<U, E>): Result<U, E> {
  return when (this) {
    is Result.Success -> transform(value)
    is Result.Failure -> Result.Failure<U, E>(error)
  }
}

fun <V : Any, E : Exception, E2 : Exception> Result<V, E>.mapError(transform: (E) -> E2): Result<V, E2> {
  return when (this) {
    is Result.Success -> Result.Success<V, E2>(value)
    is Result.Failure -> Result.Failure<V, E2>(transform(error))
  }
}

fun <V : Any, E : Exception, E2 : Exception> Result<V, E>.flatMapError(transform: (E) -> Result<V, E2>): Result<V, E2> {
  return when (this) {
    is Result.Success -> Result.Success<V, E2>(value)
    is Result.Failure -> transform(error)
  }
}

fun <V : Any> Result<V, *>.any(predicate: (V) -> Boolean): Boolean {
  return when (this) {
    is Result.Success -> predicate(value)
    is Result.Failure -> false
  }
}