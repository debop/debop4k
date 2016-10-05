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
import java.io.Serializable

interface Product : Serializable {

  val productArity: Int

  operator fun get(index: Int): Any?

  fun toList(): List<Any?> = productIterator().toList()

  fun productIterator(): Iterator<Any?> = object : Iterator<Any?> {
    var currIndex = 0
    override fun hasNext(): Boolean = currIndex < productArity
    override fun next(): Any? = get(currIndex++)
  }

  fun productPrefix(): String = ""
}

interface Product1<T1> : Product {

  val first: T1
  override val productArity: Int get() = 1

  override operator fun get(index: Int): T1 = when (index) {
    0    -> first
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product2<T1, T2> : Product {

  val first: T1
  val second: T2

  override val productArity: Int get() = 2

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product3<T1, T2, T3> : Product {

  val first: T1
  val second: T2
  val third: T3

  override val productArity: Int get() = 3

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product4<T1, T2, T3, T4> : Product {

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4

  override val productArity: Int get() = 4

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product5<T1, T2, T3, T4, T5> : Product {

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5

  override val productArity: Int get() = 5

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product6<T1, T2, T3, T4, T5, T6> : Product {

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6

  override val productArity: Int get() = 6

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product7<T1, T2, T3, T4, T5, T6, T7> : Product {

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7

  override val productArity: Int get() = 7

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product8<T1, T2, T3, T4, T5, T6, T7, T8> : Product {

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8

  override val productArity: Int get() = 8

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product9<T1, T2, T3, T4, T5, T6, T7, T8, T9> : Product {

  val first: T1
  val second: T2
  val third: T3
  val fourth: T4
  val fifth: T5
  val sixth: T6
  val seventh: T7
  val eighth: T8
  val ninth: T9

  override val productArity: Int get() = 9

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}


interface Product10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> : Product {

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

  override val productArity: Int get() = 10

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    9    -> ten
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> : Product {

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

  override val productArity: Int get() = 11

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    9    -> ten
    10   -> eleven
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> : Product {

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

  override val productArity: Int get() = 12

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    9    -> ten
    10   -> eleven
    11   -> twelve
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> : Product {

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

  override val productArity: Int get() = 13

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    9    -> ten
    10   -> eleven
    11   -> twelve
    12   -> thirteen
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> : Product {

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

  override val productArity: Int get() = 14

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    9    -> ten
    10   -> eleven
    11   -> twelve
    12   -> thirteen
    13   -> fourteen
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}

interface Product15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> : Product {

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

  override val productArity: Int get() = 15

  override operator fun get(index: Int): Any? = when (index) {
    0    -> first
    1    -> second
    2    -> third
    3    -> fourth
    4    -> fifth
    5    -> sixth
    6    -> seventh
    7    -> eighth
    8    -> ninth
    9    -> ten
    10   -> eleven
    11   -> twelve
    12   -> thirteen
    13   -> fourteen
    14   -> fifteen
    else -> throw IndexOutOfBoundsException(index.toString())
  }
}