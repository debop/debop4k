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

package debop4k.core.collections

import debop4k.core.asByte
import debop4k.core.asInt
import debop4k.core.asLong

fun Iterable<*>.size(): Int {
  if (this is Collection<*>) {
    return this.size
  } else {
    return this.count()
  }
}

fun Iterable<*>.asByteArray(): ByteArray {
  val array = ByteArray(this.size())
  for ((index, item) in this.withIndex()) {
    array[index] = item.asByte()
  }
  return array
}

fun Iterable<*>.asIntArray(): IntArray {
  val array = IntArray(this.size())
  for ((index, item) in this.withIndex()) {
    array[index] = item.asInt()
  }
  return array
}

fun Iterable<*>.asLongArray(): LongArray {
  val array = LongArray(this.size())
  for ((index, item) in this.withIndex()) {
    array[index] = item.asLong()
  }
  return array
}