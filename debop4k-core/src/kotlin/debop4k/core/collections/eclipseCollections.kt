/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("eclipsecollections")

package debop4k.core.collections

import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.*
import org.eclipse.collections.impl.map.mutable.UnifiedMap
import org.eclipse.collections.impl.set.mutable.UnifiedSet

//
// Eclipse Collections
//

// Primitive array

fun ByteArrayList.asList(): List<Byte> = this.toArray().asList()
fun ByteArray.toByteArrayList(): ByteArrayList = ByteArrayList.newListWith(*this)
fun byteArrayListOf(vararg values: Byte): ByteArrayList = ByteArrayList.newListWith(*values)

fun CharArrayList.asList(): List<Char> = this.toArray().asList()
fun CharArray.toCharArrayList(): CharArrayList = CharArrayList.newListWith(*this)
fun charArrayListOf(vararg values: Char): CharArrayList = CharArrayList.newListWith(*values)

fun ShortArrayList.asList(): List<Short> = this.toArray().asList()
fun ShortArray.toShortArrayList(): ShortArrayList = ShortArrayList.newListWith(*this)
fun shortArrayListOf(vararg values: Short): ShortArrayList = ShortArrayList.newListWith(*values)

fun IntArrayList.asList(): List<Int> = this.toArray().asList()
fun IntArray.toIntArrayList(): IntArrayList = IntArrayList.newListWith(*this)
fun intArrayListOf(vararg values: Int): IntArrayList = IntArrayList.newListWith(*values)

fun LongArrayList.asList(): List<Long> = this.toArray().asList()
fun LongArray.toLongArrayList(): LongArrayList = LongArrayList.newListWith(*this)
fun longArrayListOf(vararg values: Long): LongArrayList = LongArrayList.newListWith(*values)


fun FloatArrayList.asList(): List<Float> = this.toArray().asList()
fun FloatArray.toFloatArrayList(): FloatArrayList = FloatArrayList.newListWith(*this)
fun floatArrayListOf(vararg values: Float): FloatArrayList = FloatArrayList.newListWith(*values)


fun DoubleArrayList.asList(): List<Double> = this.toArray().asList()
fun DoubleArray.toDoubleArrayList(): DoubleArrayList = DoubleArrayList.newListWith(*this)
fun doubleArrayListOf(vararg values: Double): DoubleArrayList = DoubleArrayList.newListWith(*values)


// FastList
//
fun <T> Array<out T>.toFastList(): FastList<T> = FastList.newListWith(*this)

fun <T> emptyFastList() = FastList.newList<T>()
fun <T> fastListOf(): FastList<T> = emptyFastList()
fun <T> fastListOf(vararg elements: T): FastList<T> {
  return if (elements.size == 0) FastList.newList() else FastList.newListWith(*elements)
}

// Unified Set
fun <T> Array<out T>.toUnifiedSet(): UnifiedSet<T> = UnifiedSet.newSetWith(*this)

fun <T> emptyUnifiedSet(): UnifiedSet<T> = UnifiedSet.newSet()
fun <T> unifiedSetOf(): UnifiedSet<T> = emptyUnifiedSet()
fun <T> unifiedSetOf(vararg values: T): UnifiedSet<T> = UnifiedSet.newSetWith(*values)

// Unified Map
//
fun <K, V> Map<K, V>.toUnifiedMap(): UnifiedMap<K, V> {
  val map = UnifiedMap.newMap<K, V>()
  if (this.size > 0) {
    for ((k, v) in this) {
      map.put(k, v)
    }
  }
  return map
}

fun <K, V> unifiedMapOf(vararg pairs: Pair<K, V>): UnifiedMap<K, V> {
  val map = UnifiedMap.newMap<K, V>(pairs.size)
  for ((k, v) in pairs) {
    map.put(k, v)
  }
  return map
}
