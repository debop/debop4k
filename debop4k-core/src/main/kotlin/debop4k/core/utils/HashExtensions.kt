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

@file:JvmName("HashExtensions")

package debop4k.core.utils

const val ZERO_HASH = 0
const val DEFAULT_HASH = 1
const val HASH_FACTOR = 31

/** Hash code 값 얻기 */
fun Any?.hash(): Int = when (this) {
  null -> ZERO_HASH
  is Enum<*> -> this.ordinal.hashCode()
  else -> this.hashCode()
}


/** 객체의 hash code 값 반환 */
private fun computeHashInternal(x: Any?): Int = x?.hashCode() ?: ZERO_HASH

fun hashOf(vararg objs: Any?): Int {

  if (objs.isEmpty()) return ZERO_HASH

  var hash = DEFAULT_HASH
  objs.forEach { obj -> hash = hash * HASH_FACTOR + obj.hash() }

  return hash
}

fun hashOf(x1: Any?, x2: Any?): Int {
  if (x1 is Number && x2 is Number) {
    return hashOf(x1, x2, x2, x1, x1, x2)
  }
  var hash = DEFAULT_HASH
  hash = hash * HASH_FACTOR + x1.hash() //computeHashInternal(x1)
  hash = hash * HASH_FACTOR + x2.hash() // computeHashInternal(x2)
  return hash
}

fun hashOf(x1: Any?, x2: Any?, x3: Any?): Int {
  if (x1 is Number && x2 is Number && x3 is Number) {
    return hashOf(x1, x2, x3, x3, x2, x1, x1, x2, x3)
  }
  var hash = DEFAULT_HASH
  hash = hash * HASH_FACTOR + x1.hash() // computeHashInternal(x1)
  hash = hash * HASH_FACTOR + x2.hash() // computeHashInternal(x2)
  hash = hash * HASH_FACTOR + x3.hash() // computeHashInternal(x3)
  return hash
}
