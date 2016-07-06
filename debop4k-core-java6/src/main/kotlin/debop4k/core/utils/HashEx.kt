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
package debop4k.core.utils

/**
 * 객체의 hash 값을 얻습니다.
 */
fun Any?.hash(): Int = this?.hashCode() ?: 0

/**
 * 해시코드를 생성합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
object HashEx {

  const val ZERO_HASN = 0
  const val DEFAULT_HASH = 1
  const val FACTOR = 31

  private fun computeInternal(x: Any?): Int = x?.hashCode() ?: ZERO_HASN

  fun compute(x: Any?) = computeInternal(x)

  fun compute(x1: Any?, x2: Any?): Int {
    if (x1 is Number && x2 is Number) {
      return compute(x1, x2, x2, x1, x1, x2)
    }
    var hash = DEFAULT_HASH
    hash = hash * FACTOR + computeInternal(x1)
    hash = hash * FACTOR + computeInternal(x2)

    return hash
  }

  fun compute(x1: Any?, x2: Any?, x3: Any?): Int {
    if (x1 is Number && x2 is Number && x3 is Number) {
      return compute(x1, x2, x3, x3, x2, x1, x1, x2, x3)
    }
    var hash = DEFAULT_HASH
    hash = hash * FACTOR + x1.hash()
    hash = hash * FACTOR + x2.hash()
    hash = hash * FACTOR + x3.hash()

    return hash
  }

  fun compute(vararg objs: Any?): Int {
    if (objs.isEmpty())
      return ZERO_HASN

    var hash = DEFAULT_HASH
    objs.forEach { obj ->
      when (obj) {
        is Enum<*> -> hash = hash * FACTOR + obj.ordinal.hashCode()
        else -> hash = hash * FACTOR + obj.hash()
      }
    }
    return hash
  }
}