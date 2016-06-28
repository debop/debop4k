/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("either")

package debop4k.core.functional.either

import com.google.common.base.Objects
import debop4k.core.collections.prependTo
import debop4k.core.functional.Option
import debop4k.core.functional.Option.None
import debop4k.core.functional.Option.Some
import debop4k.core.functional.either.Either.Left
import debop4k.core.functional.either.Either.Right
import java.util.*

sealed class Either<out L, out R> {

  fun left(): LeftProjection<L, R> = LeftProjection(this)
  fun right(): RightProjection<L, R> = RightProjection(this)

  operator abstract fun component1(): L?
  operator abstract fun component2(): R?

  abstract fun isLeft(): Boolean
  abstract fun isRight(): Boolean

  fun<X> fold(fl: (L) -> X, fr: (R) -> X): X {
    return when (this) {
      is Left<L, R>  -> fl(this.l)
      is Right<L, R> -> fr(this.r)
    }
  }

  fun swap(): Either<R, L> {
    return when (this) {
      is Left<L, R>  -> Right(this.l)
      is Right<L, R> -> Left(this.r)
    }
  }


  class Left<out L, out R>(val l: L) : Either<L, R>() {
    override fun component1(): L? = l
    override fun component2(): R? = null
    override fun isLeft(): Boolean = true
    override fun isRight(): Boolean = false

    override fun equals(other: Any?): Boolean = when (other) {
      is Left<*, *> -> Objects.equal(l, other.l)
      else          -> false
    }

    override fun hashCode(): Int = Objects.hashCode(l)
    override fun toString(): String = "Left($l)"
  }

  class Right<out L, out R>(val r: R) : Either<L, R>() {
    override fun component1(): L? = null
    override fun component2(): R? = r
    override fun isLeft(): Boolean = false
    override fun isRight(): Boolean = true

    override fun equals(other: Any?): Boolean = when (other) {
      is Right<*, *> -> Objects.equal(r, other.r)
      else           -> false
    }

    override fun hashCode(): Int = Objects.hashCode(r)
    override fun toString(): String = "Right($r)"
  }
}

fun<T> Either<T, T>.merge(): T = when (this) {
  is Left<T, T>  -> this.l
  is Right<T, T> -> this.r
  else           -> throw UnsupportedOperationException()
}

fun<L, R> Pair<L, R>.toLeft(): Left<L, R> = Left(this.component1())
fun<L, R> Pair<L, R>.toRight(): Right<L, R> = Right(this.component2())

fun <T> eitherTry(body: () -> T): Either<Exception, T> =
    try {
      Right(body())
    } catch(e: Exception) {
      Left(e)
    }

fun <T, L, R> List<T>.traverse(f: (T) -> Either<L, R>): Either<L, List<R>> {
  return foldRight(Right(emptyList())) { i: T, accumulator: Either<L, List<R>> ->
    val either: Either<L, R> = f(i)
    when (either) {
      is Right -> either.right().map(accumulator) { head: R, tail: List<R> -> head prependTo tail }
      is Left  -> Left(either.l)
      else     -> throw UnsupportedOperationException()
    }
  }
}

fun <L, R> List<Either<L, R>>.sequantial(): Either<L, List<R>> = traverse { it }

/**
 * LeftProjection
 */
class LeftProjection<out L, out R>(val e: Either<L, R>) {

  fun get(): L = when (e) {
    is Left<L, R> -> e.l
    else          -> throw NoSuchElementException("Either.left.value on Right")
  }

  fun forEach(f: (L) -> Unit): Unit = when (e) {
    is Left<L, R> -> f(e.l)
    else          -> {
    }
  }

  fun exists(predicate: (L) -> Boolean): Boolean = when (e) {
    is Left<L, R> -> predicate(e.l)
    else          -> false
  }

  fun <X> map(f: (L) -> X): Either<X, R> = flatMap { Left<X, R>(f(it)) }

  fun filter(predicate: (L) -> Boolean): Option<Either<L, R>> = when (e) {
    is Left<L, R> -> if (predicate(e.l)) Some(e) else None
    else          -> None
  }

  fun toList(): List<L> = when (e) {
    is Left<L, R> -> listOf(e.l)
    else          -> listOf()
  }

  fun toOption(): Option<L> = when (e) {
    is Left<L, R> -> Some(e.l)
    else          -> None
  }
}

fun <L, R, X> LeftProjection<L, R>.flatMap(f: (L) -> Either<X, R>): Either<X, R> = when (e) {
  is Left<L, R>  -> f(e.l)
  is Right<L, R> -> Right<X, R>(e.r)
}

fun <L, R, X, Y> LeftProjection<L, R>.map(x: Either<X, R>, f: (L, X) -> Y): Either<Y, R> {
  return flatMap { l -> x.left().map { xx -> f(l, xx) } }
}

fun <R, L> LeftProjection<L, R>.getOrElse(default: () -> L): L = when (e) {
  is Left<L, R> -> e.l
  else          -> default()
}

/**
 * RightProjection
 */
class RightProjection<out L, out R>(val e: Either<L, R>) {

  fun get(): R = when (e) {
    is Right<L, R> -> e.r
    else           -> throw NoSuchElementException("Either.right.value on Left")
  }

  fun forEach(f: (R) -> Unit): Unit = when (e) {
    is Right<L, R> -> f(e.r)
    else           -> {
    }
  }

  fun exists(predicate: (R) -> Boolean): Boolean = when (e) {
    is Right<L, R> -> predicate(e.r)
    else           -> false
  }

  fun<X> map(f: (R) -> X): Either<L, X> = flatMap { Right<L, X>(f(it)) }

  fun filter(predicate: (R) -> Boolean): Option<Either<L, R>> = when (e) {
    is Right<L, R> -> if (predicate(e.r)) Some(e) else None
    else           -> None
  }

  fun toList(): List<R> = when (e) {
    is Right<L, R> -> listOf(e.r)
    else           -> listOf()
  }

  fun toOption(): Option<R> = when (e) {
    is Right<L, R> -> Some(e.r)
    else           -> None
  }
}

fun <X, L, R> RightProjection<L, R>.flatMap(f: (R) -> Either<L, X>): Either<L, X> = when (e) {
  is Left<L, R>  -> Left(e.l)
  is Right<L, R> -> f(e.r)
}

fun <L, R, X, Y> RightProjection<L, R>.map(x: Either<L, X>, f: (R, X) -> Y): Either<L, Y> {
  return flatMap { r -> x.right().map { xx -> f(r, xx) } }
}

fun <L, R> RightProjection<L, R>.getOrElse(default: () -> R): R = when (e) {
  is Right<L, R> -> e.r
  else           -> default()
}