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

@file:JvmName("Collectionx")

package debop4k.core.collections

import debop4k.core.areEquals
import debop4k.core.utils.*
import org.eclipse.collections.api.map.sorted.MutableSortedMap
import org.eclipse.collections.api.set.sorted.MutableSortedSet
import org.eclipse.collections.impl.factory.SortedMaps
import org.eclipse.collections.impl.factory.SortedSets
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.map.mutable.UnifiedMap
import org.eclipse.collections.impl.set.mutable.UnifiedSet
import java.util.*
import java.util.concurrent.atomic.*

fun <T> Iterator<T>.toList(): List<T> {
  return ArrayList<T>().apply {
    this@toList.forEach { this@apply.add(it) }
  }
}

fun <T> Iterator<T>.toMutableList(): MutableList<T> {
  return ArrayList<T>().apply {
    this@toMutableList.forEach { this@apply.add(it) }
  }
}

fun <T> Iterable<T>.size(): Int = when (this) {
  is Collection<*> -> this.size
  else             -> this.count()
}

fun <T> Iterable<T>.headOrNull(): T? = when (this) {
  is List ->
    if (this.isNotEmpty()) this[0] else null
  else    ->
    if (!none()) this.iterator().next() else null
}

fun <T> Iterable<T>.lastOrNull(): T? = when (this) {
  is List -> if (this.isNotEmpty()) this[this.lastIndex] else null
  else    -> if (!none()) this.last() else null
}


fun Iterable<*>.asByteArray(): ByteArray {
  val array = ByteArray(this.size())

  this.forEachIndexed { i, item -> array[i] = item.asByte() }
  return array
}

fun Iterable<*>.asShortArray(): ShortArray {
  val array = ShortArray(this.size())

  this.forEachIndexed { i, item -> array[i] = item.asShort() }
  return array
}

fun Iterable<*>.asIntArray(): IntArray {
  val array = IntArray(this.size())

  this.forEachIndexed { i, item -> array[i] = item.asInt() }
  return array
}

fun Iterable<*>.asLongArray(): LongArray {
  val array = LongArray(this.size())

  this.forEachIndexed { i, item -> array[i] = item.asLong() }
  return array
}

fun Iterable<*>.asFloatArray(): FloatArray {
  val array = FloatArray(this.size())
  this.forEachIndexed { i, item -> array[i] = item.asFloat() }
  return array
}

fun Iterable<*>.asDoubleArray(): DoubleArray {
  val array = DoubleArray(this.size())
  this.forEachIndexed { i, item -> array[i] = item.asDouble() }
  return array
}

fun <T> Collection<T>.head(): T? = if (this.size > 0) this.elementAt(0) else null
fun <T> Collection<T>.tail(): List<T> = this.drop(1)

infix fun <T> T.prependTo(list: List<T>): List<T> = listOf(this) + list

infix fun <T> MutableList<T>.prepend(element: T): List<T> = listOf(element) + this

fun <T> MutableList<T>.plus(vararg elements: T): List<T> = this.plus(listOf(*elements))

fun <T> emptyIterator(): Iterator<T> = emptyList<T>().iterator()


// Batch

fun <T> Sequence<T>.batch(batchSize: Int = 1): Sequence<List<T>> {
  return BatchSequence(this.asIterable(), batchSize)
}

fun <T> Sequence<T>.batch(batchSize: Int, forEachDo: (List<T>) -> Unit): Unit {
  BatchSequence(this.asIterable(), batchSize).forEach { forEachDo(it) }
}

fun <T> Iterable<T>.batch(batchSize: Int = 1): Sequence<List<T>> {
  return BatchSequence(this, batchSize)
}

fun <T> Iterable<T>.batch(batchSize: Int, forEachDo: (List<T>) -> Unit): Unit {
  BatchSequence(this, batchSize).forEach { forEachDo(it) }
}

class BatchSequence<out T>(val source: Iterable<T>, val batchSize: Int = 1) : Sequence<List<T>> {
  override fun iterator(): Iterator<List<T>> {
    return object : AbstractIterator<List<T>>() {
      private val iter = if (batchSize > 0) source.iterator() else emptyIterator()
      override fun computeNext() {
        if (iter.hasNext()) setNext(iter.asSequence().take(batchSize).toList())
        else done()
      }
    }
  }
}

fun <T> Sequence<T>.lazyBatch(batchSize: Int = 1): Sequence<Sequence<T>> {
  return LazyBatchSequence(this.asIterable(), batchSize)
}

fun <T> Sequence<T>.lazyBatch(batchSize: Int = 1, forEachDo: (Sequence<T>) -> Unit): Unit {
  LazyBatchSequence(this.asIterable(), batchSize).forEach { forEachDo(it) }
}

fun <T> Iterable<T>.lazyBatch(batchSize: Int = 1): Sequence<Sequence<T>> {
  return LazyBatchSequence(this, batchSize)
}

fun <T> Iterable<T>.lazyBatch(batchSize: Int = 1, forEachDo: (Sequence<T>) -> Unit): Unit {
  LazyBatchSequence(this, batchSize).forEach { forEachDo(it) }
}

open class LazyBatchSequence<out T>(val stream: Iterable<T>, val groupSize: Int = 1) : Sequence<Sequence<T>> {
  override fun iterator(): Iterator<Sequence<T>> {
    return object : AbstractIterator<Sequence<T>>() {
      private val iter = if (groupSize > 0) stream.iterator() else emptyIterator()
      private var prevBatch: LimitIteratorByCountStream<T>? = null

      override fun computeNext() {
        prevBatch?.consumeToLimit()
        if (iter.hasNext()) {
          val newBatch = LimitIteratorByCountStream(iter, groupSize)
          setNext(newBatch)
          prevBatch = newBatch
        } else {
          done()
        }
      }
    }
  }

  class LimitIteratorByCountStream<out T>(val iterator: Iterator<T>, val limit: Int) : Sequence<T> {
    private var count: Int = 0

    private val iteratorRef = AtomicReference<Iterator<T>>(object : AbstractIterator<T>() {
      override fun computeNext() {
        if (!iterator.hasNext() || count >= limit) {
          done()
        } else {
          count++
          setNext(iterator.next())
        }
      }
    })

    override fun iterator(): Iterator<T> {
      return iteratorRef.getAndSet(null) ?: throw IllegalStateException("이미 사용한 Iterator 입니다.")
    }

    fun consumeToLimit() {
      while (iterator.hasNext() && count < limit) {
        count++
        iterator.next()
      }
    }
  }
}


@JvmOverloads
fun Sequence<*>.asCharArray(dv: Char = 0.toChar()): CharArray = map { it.asChar(dv) }.toList().toCharArray()

@JvmOverloads
fun Collection<*>.asCharArray(dv: Char = 0.toChar()): CharArray = map { it.asChar(dv) }.toCharArray()

@JvmOverloads
fun Sequence<*>.asByteArray(dv: Byte = 0): ByteArray = map { it.asByte(dv) }.toList().toByteArray()

@JvmOverloads
fun Collection<*>.asByteArray(dv: Byte = 0): ByteArray = map { it.asByte(dv) }.toByteArray()

@JvmOverloads
fun Sequence<*>.asShortArray(dv: Short = 0): ShortArray = map { it.asShort(dv) }.toList().toShortArray()

@JvmOverloads
fun Collection<*>.asShortArray(dv: Short = 0): ShortArray = map { it.asShort(dv) }.toShortArray()

@JvmOverloads
fun Sequence<*>.asIntArray(dv: Int = 0): IntArray = map { it.asInt(dv) }.toList().toIntArray()

@JvmOverloads
fun Collection<*>.asIntArray(dv: Int = 0): IntArray = map { it.asInt(dv) }.toIntArray()

@JvmOverloads
fun Sequence<*>.asLongArray(dv: Long = 0L): LongArray = map { it.asLong(dv) }.toList().toLongArray()

@JvmOverloads
fun Collection<*>.asLongArray(dv: Long = 0L): LongArray = map { it.asLong(dv) }.toLongArray()

@JvmOverloads
fun Sequence<*>.asFloatArray(dv: Float = 0F): FloatArray = map { it.asFloat(dv) }.toList().toFloatArray()

@JvmOverloads
fun Collection<*>.asFloatArray(dv: Float = 0F): FloatArray = map { it.asFloat(dv) }.toFloatArray()

@JvmOverloads
fun Sequence<*>.asDoubleArray(dv: Double = 0.0): DoubleArray = map { it.asDouble(dv) }.toList().toDoubleArray()

@JvmOverloads
fun Collection<*>.asDoubleArray(dv: Double = 0.0): DoubleArray = map { it.asDouble(dv) }.toDoubleArray()

@JvmOverloads
fun Sequence<*>.asStringArray(dv: String = ""): Array<String> = map { it.asString(dv) }.toList().toTypedArray()

@JvmOverloads
fun Collection<*>.asStringArray(dv: String = ""): Array<String> = map { it.asString(dv) }.toTypedArray()


@Suppress("UNCHECKED_CAST")
fun <T> Collection<*>.asArray(clazz: Class<T>): Array<T> {
  val array: Array<T> = java.lang.reflect.Array.newInstance(clazz, this.size) as Array<T>
  this.forEachIndexed { i, item -> array[i] = item as T }
  return array
}


/**
 * 현 컬렉션의 항목수가 지정한 항목 수보다 작다면, `item` 값을 추가합니다.
 */
fun <T> Collection<T>.padTo(itemCount: Int, item: T): Collection<T> {
  val list = this.toMutableList()
  val count = itemCount - this.size
  (0 until count).forEach {
    list.add(item)
  }
  return list
}

fun <T> isSameElements(left: Iterable<T>,
                       right: Iterable<T>): Boolean {
  if (left is List<T> && right is List<T>) {
    if (left.size == right.size) {
      return left.indices.none { left[it] != right[it] }
    }
    return false
  }
  val l = left.iterator()
  val r = right.iterator()

  while (l.hasNext() && r.hasNext()) {
    if (!areEquals(l.next(), r.next()))
      return false
  }
  return !l.hasNext() && !r.hasNext()
}

fun <K, V> Map<K, V>.sortWith(comparator: Comparator<in Pair<K, V>>): MutableSortedMap<K, V>
    = this.toList().sortedWith(comparator).toMap().toSortedMap()

fun <K : Comparable<K>, V> Map<K, V>.sortByKey(): Map<K, V>
    = this.toList().sortedBy { it.first }.toMap()

fun <K : Comparable<K>, V> Map<K, V>.sortByKeyDesc(): Map<K, V>
    = this.toList().sortedByDescending { it.first }.toMap()

fun <K, V : Comparable<V>> Map<K, V>.sortByValue(): Map<K, V>
    = this.toList().sortedBy { it.second }.toMap()

fun <K, V : Comparable<V>> Map<K, V>.sortByValueDesc(): Map<K, V>
    = this.toList().sortedByDescending { it.second }.toMap()


fun <K : Comparable<K>, V> Map<K, V>.maxByKey(): Pair<K, V>
    = this.maxBy { it.key }!!.toPair()

fun <K : Comparable<K>, V> Map<K, V>.minByKey(): Pair<K, V>
    = this.minBy { it.key }!!.toPair()

fun <K, V : Comparable<V>> Map<K, V>.maxByValue(): Pair<K, V>
    = this.maxBy { it.value }!!.toPair()

fun <K, V : Comparable<V>> Map<K, V>.minByValue(): Pair<K, V>
    = this.minBy { it.value }!!.toPair()


fun <K, V> Map<K, V>.toSortedMap(): MutableSortedMap<K, V>
    = SortedMaps.mutable.ofSortedMap(this)


fun <T> emptyFastList(): FastList<T> = FastList.newList<T>()
fun <T> fastListOf(): FastList<T> = FastList.newList<T>()

/**
 * Build [FastList]
 */
fun <T> fastListOf(vararg elements: T): FastList<T> {
  return if (elements.isEmpty()) FastList.newList()
  else FastList.newListWith(*elements)
}

/**
 * Build [FastList]
 */
fun <T> fastListOf(iterable: Iterable<T>): FastList<T> = FastList.newList(iterable)

/**
 * Build [FastList]
 */
fun <T> fastListOf(iterator: Iterator<T>): FastList<T> {
  val list = fastListOf<T>()
  while (iterator.hasNext()) {
    list.add(iterator.next())
  }
  return list
}

fun <T> Sequence<T>.toFastList(): FastList<T> {
  return FastList.newList<T>().apply {
    this@toFastList.forEach { this@apply.add(it) }
  }
}

/**
 * Convert Iterable to [FastList]
 */
fun <T> Iterable<T>.toFastList(): FastList<T> {
  return FastList.newList(this)
}

fun <T> Iterator<T>.toFastList(): FastList<T> {
  return FastList.newList<T>().apply {
    this@toFastList.forEach { this@apply.add(it) }
  }
}

/**
 * Convert [Array] to [FastList]
 */
fun <T> Array<out T>.toFastList(): FastList<T> = FastList.newListWith(*this)

/**
 * Convert [Array] to [UnifiedSet]
 */
fun <T> Array<out T>.toUnifiedSet(): UnifiedSet<T>
    = UnifiedSet.newSetWith(*this)

/**
 * Build [UnifiedSet]
 */
fun <T> emptyUnifiedSet(): UnifiedSet<T> = UnifiedSet.newSet()

/**
 * Build [UnifiedSet]
 */
fun <T> unifiedSetOf(): UnifiedSet<T> = UnifiedSet.newSet()

/**
 * Build [UnifiedSet]
 */
fun <T> unifiedSetOf(vararg values: T): UnifiedSet<T> = UnifiedSet.newSetWith(*values)

/**
 * Convert [Array] to [MutableSortedSet]
 */
fun <T> Array<out T>.toSortedSet(): MutableSortedSet<T> = sortedSetOf(*this)

/**
 * Build [MutableSortedSet]
 */
fun <T> sortedSetOf(vararg values: T): MutableSortedSet<T>
    = SortedSets.mutable.of(*values)

/**
 * Build [MutableSortedSet] with Iterable
 */
fun <T> sortedSetOf(iterable: Iterable<T>): MutableSortedSet<T>
    = SortedSets.mutable.ofAll(iterable)

/**
 * Map to [UnifiedMap]
 */
fun <K, V> Map<K, V>.toUnifiedMap(): UnifiedMap<K, V> {
  if (this is UnifiedMap<K, V>)
    return this

  val map = UnifiedMap.newMap<K, V>()
  if (this.size > 0) {
    for ((k, v) in this) {
      map.put(k, v)
    }
  }
  return map
}

/**
 * build [UnifiedMap]
 */
fun <K, V> unifiedMapOf(vararg pairs: Pair<K, V>): UnifiedMap<K, V> {
  val map = UnifiedMap.newMap<K, V>(pairs.size)
  for ((k, v) in pairs) {
    map.put(k, v)
  }
  return map
}

/**
 * Convert to [UnifiedMap]
 */
fun <K, V> Iterable<Pair<K, V>>.toUnifiedMap(): UnifiedMap<K, V> {
  val map = UnifiedMap.newMap<K, V>()
  this.forEach { pair -> map.put(pair.first, pair.second) }
  return map
}
