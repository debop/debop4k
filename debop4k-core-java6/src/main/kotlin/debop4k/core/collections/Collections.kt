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

package debop4k.core.collections

import debop4k.core.*
import java.util.concurrent.atomic.*

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

class BatchSequence<T>(val source: Iterable<T>, val batchSize: Int = 1) : Sequence<List<T>> {
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

class LazyBatchSequence<T>(val stream: Iterable<T>, val groupSize: Int = 1) : Sequence<Sequence<T>> {
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

  class LimitIteratorByCountStream<T>(val iterator: Iterator<T>, val limit: Int) : Sequence<T> {
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
fun Sequence<Any?>.asCharArray(dv: Char = 0.toChar()): CharArray = map { it.asChar(dv) }.toList().toCharArray()

@JvmOverloads
fun Collection<Any?>.asCharArray(dv: Char = 0.toChar()): CharArray = map { it.asChar(dv) }.toCharArray()

@JvmOverloads
fun Sequence<Any?>.asByteArray(dv: Byte = 0): ByteArray = map { it.asByte(dv) }.toList().toByteArray()

@JvmOverloads
fun Collection<Any?>.asByteArray(dv: Byte = 0): ByteArray = map { it.asByte(dv) }.toByteArray()

@JvmOverloads
fun Sequence<Any?>.asShortArray(dv: Short = 0): ShortArray = map { it.asShort(dv) }.toList().toShortArray()

@JvmOverloads
fun Collection<Any?>.asShortArray(dv: Short = 0): ShortArray = map { it.asShort(dv) }.toShortArray()

@JvmOverloads
fun Sequence<Any?>.asIntArray(dv: Int = 0): IntArray = map { it.asInt(dv) }.toList().toIntArray()

@JvmOverloads
fun Collection<Any?>.asIntArray(dv: Int = 0): IntArray = map { it.asInt(dv) }.toIntArray()

@JvmOverloads
fun Sequence<Any?>.asLongArray(dv: Long = 0L): LongArray = map { it.asLong(dv) }.toList().toLongArray()

@JvmOverloads
fun Collection<Any?>.asLongArray(dv: Long = 0L): LongArray = map { it.asLong(dv) }.toLongArray()

@JvmOverloads
fun Sequence<Any?>.asFloatArray(dv: Float = 0F): FloatArray = map { it.asFloat(dv) }.toList().toFloatArray()

@JvmOverloads
fun Collection<Any?>.asFloatArray(dv: Float = 0F): FloatArray = map { it.asFloat(dv) }.toFloatArray()

@JvmOverloads
fun Sequence<Any?>.asDoubleArray(dv: Double = 0.0): DoubleArray = map { it.asDouble(dv) }.toList().toDoubleArray()

@JvmOverloads
fun Collection<Any?>.asDoubleArray(dv: Double = 0.0): DoubleArray = map { it.asDouble(dv) }.toDoubleArray()


@JvmOverloads
fun Sequence<Any?>.asStringArray(dv: String = ""): Array<String> = map { it.asString(dv) }.toList().toTypedArray()

@JvmOverloads
fun Collection<Any?>.asStringArray(dv: String = ""): Array<String> = map { it.asString(dv) }.toTypedArray()


fun <T> generateByteArray(length: Int, generator: (Int) -> Byte): ByteArray {
  return (0 until length).map { i -> generator(i) }.asByteArray()
}

fun <T> generateIntArray(length: Int, generator: (Int) -> Int): IntArray {
  return (0 until length).map { i -> generator(i) }.asIntArray()
}

fun <T> generateLongArray(length: Int, generator: (Int) -> Long): LongArray {
  return (0 until length).map { i -> generator(i) }.asLongArray()
}

fun <T> generateFloatArray(length: Int, generator: (Int) -> Float): FloatArray {
  return (0 until length).map { i -> generator(i) }.asFloatArray()
}

fun <T> generateDoubleArray(length: Int, generator: (Int) -> Double): DoubleArray {
  return (0 until length).map { i -> generator(i) }.asDoubleArray()
}