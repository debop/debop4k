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

@file:JvmName("Productx")

package debop4k.core

import debop4k.core.collections.toList


interface Product {

  val productArity: Int
  operator fun get(index: Int): Any?

  fun toList(): List<*> = productIterator().toList()

  fun productIterator(): Iterator<*> = object : Iterator<Any?> {
    var currIndex = 0
    override fun hasNext(): Boolean = currIndex < productArity
    override fun next(): Any? = get(currIndex++)
  }

  fun productPrefix(): String = ""

}

interface Product1<T1> : Product {
  override val productArity: Int
    get() = 1

  override operator fun get(index: Int): T1 = when (index) {
    0 -> first
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
}

interface Product2<T1, T2> : Product {

  override val productArity: Int
    get() = 2

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
}

interface Product3<T1, T2, T3> : Product {

  override val productArity: Int
    get() = 3

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
}

interface Product4<T1, T2, T3, T4> : Product {

  override val productArity: Int
    get() = 4

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
}

interface Product5<T1, T2, T3, T4, T5> : Product {

  override val productArity: Int
    get() = 5

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
}

interface Product6<T1, T2, T3, T4, T5, T6> : Product {

  override val productArity: Int
    get() = 6

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
}

interface Product7<T1, T2, T3, T4, T5, T6, T7> : Product {

  override val productArity: Int
    get() = 7

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
}

interface Product8<T1, T2, T3, T4, T5, T6, T7, T8> : Product {

  override val productArity: Int
    get() = 8

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
}

interface Product9<T1, T2, T3, T4, T5, T6, T7, T8, T9> : Product {

  override val productArity: Int
    get() = 9

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
}


interface Product10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> : Product {

  override val productArity: Int
    get() = 10

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    9 -> ten
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
  val ten: T10
}

interface Product11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> : Product {

  override val productArity: Int
    get() = 11

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    9 -> ten
    10 -> eleven
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
  val ten: T10
  val eleven: T11
}

interface Product12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> : Product {

  override val productArity: Int
    get() = 12

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    9 -> ten
    10 -> eleven
    11 -> twelve
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
  val ten: T10
  val eleven: T11
  val twelve: T12
}

interface Product13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> : Product {

  override val productArity: Int
    get() = 13

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    9 -> ten
    10 -> eleven
    11 -> twelve
    12 -> thirteen
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
  val ten: T10
  val eleven: T11
  val twelve: T12
  val thirteen: T13
}

interface Product14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> : Product {

  override val productArity: Int
    get() = 14

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    9 -> ten
    10 -> eleven
    11 -> twelve
    12 -> thirteen
    13 -> fourteen
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
  val ten: T10
  val eleven: T11
  val twelve: T12
  val thirteen: T13
  val fourteen: T14
}

interface Product15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> : Product {

  override val productArity: Int
    get() = 15

  override operator fun get(index: Int): Any? = when (index) {
    0 -> first
    1 -> second
    2 -> third
    3 -> fourth
    4 -> fifth
    5 -> sixth
    6 -> seventh
    7 -> eighth
    8 -> ninth
    9 -> ten
    10 -> eleven
    11 -> twelve
    12 -> thirteen
    13 -> fourteen
    14 -> fifteen
    else -> throw IndexOutOfBoundsException(index.toString())
  }

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9
  val ten: T10
  val eleven: T11
  val twelve: T12
  val thirteen: T13
  val fourteen: T14
  val fifteen: T15
}