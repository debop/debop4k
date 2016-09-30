/*
 * Copyright (c) 2016. KESTI co, ltd
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

@file:JvmName("tuples")

package debop4k.core

import java.io.Serializable

fun <T1> tupleOf(t1: T1) = Tuple1(t1)
fun <T1, T2> tupleOf(t1: T1, t2: T2) = Tuple2(t1, t2)
fun <T1, T2, T3> tupleOf(t1: T1, t2: T2, t3: T3) = Tuple3(t1, t2, t3)
fun <T1, T2, T3, T4> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4) = Tuple4(t1, t2, t3, t4)
fun <T1, T2, T3, T4, T5> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5) = Tuple5(t1, t2, t3, t4, t5)
fun <T1, T2, T3, T4, T5, T6> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6) = Tuple6(t1, t2, t3, t4, t5, t6)
fun <T1, T2, T3, T4, T5, T6, T7> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7)
    = Tuple7(t1, t2, t3, t4, t5, t6, t7)

fun <T1, T2, T3, T4, T5, T6, T7, T8> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8)
    = Tuple8(t1, t2, t3, t4, t5, t6, t7, t8)

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8, t9: T9)
    = Tuple9(t1, t2, t3, t4, t5, t6, t7, t8, t9)

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8, t9: T9, t10: T10)
    = Tuple10(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)

fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> tupleOf(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7, t8: T8, t9: T9, t10: T10, t11: T11)
    = Tuple11(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)


data class Tuple1<T1>(override val first: T1) : Product1<T1>, Serializable {

  override fun toString(): String {
    return "($first)"
  }
}

data class Tuple2<T1, T2>(
    override val first: T1,
    override val second: T2) : Product2<T1, T2>, Serializable {

  override fun toString(): String {
    return "($first, $second)"
  }
}

data class Tuple3<T1, T2, T3>(
    override val first: T1,
    override val second: T2,
    override val third: T3) : Product3<T1, T2, T3>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third)"
  }
}

data class Tuple4<T1, T2, T3, T4>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4) : Product4<T1, T2, T3, T4>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth)"
  }
}

data class Tuple5<T1, T2, T3, T4, T5>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5) : Product5<T1, T2, T3, T4, T5>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth)"
  }
}

data class Tuple6<T1, T2, T3, T4, T5, T6>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5,
    override val sixth: T6) : Product6<T1, T2, T3, T4, T5, T6>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth, $sixth)"
  }
}

data class Tuple7<T1, T2, T3, T4, T5, T6, T7>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5,
    override val sixth: T6,
    override val seventh: T7) : Product7<T1, T2, T3, T4, T5, T6, T7>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth, $sixth, $seventh)"
  }
}

data class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5,
    override val sixth: T6,
    override val seventh: T7,
    override val eighth: T8) : Product8<T1, T2, T3, T4, T5, T6, T7, T8>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth, $sixth, $seventh, $eighth)"
  }
}

data class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5,
    override val sixth: T6,
    override val seventh: T7,
    override val eighth: T8,
    override val ninth: T9) : Product9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth, $sixth, $seventh, $eighth, $ninth)"
  }
}

data class Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5,
    override val sixth: T6,
    override val seventh: T7,
    override val eighth: T8,
    override val ninth: T9,
    override val ten: T10) : Product10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth, $sixth, $seventh, $eighth, $ninth, $ten)"
  }
}


data class Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>(
    override val first: T1,
    override val second: T2,
    override val third: T3,
    override val fourth: T4,
    override val fifth: T5,
    override val sixth: T6,
    override val seventh: T7,
    override val eighth: T8,
    override val ninth: T9,
    override val ten: T10,
    override val eleven: T11) : Product11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, Serializable {

  override fun toString(): String {
    return "($first, $second, $third, $fourth, $fifth, $sixth, $seventh, $eighth, $ninth, $ten, $eleven)"
  }
}

// TODO: Tuple22 까지 만들기 ^^