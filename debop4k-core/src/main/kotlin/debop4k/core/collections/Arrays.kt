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

import debop4k.core.cryptography.randomBytes
import org.eclipse.collections.api.block.function.primitive.*
import org.eclipse.collections.impl.list.mutable.primitive.*
import java.nio.ByteBuffer

val emptyCharArray: CharArray get() = CharArray(0)
val emptyByteArray: ByteArray get() = ByteArray(0)
val emptyShortArray: ShortArray get() = ShortArray(0)
val emptyIntArray: IntArray get() = IntArray(0)
val emptyLongArray: LongArray get() = LongArray(0)
val emptyFloatArray: FloatArray get() = FloatArray(0)
val emptyDoubleArray: DoubleArray get() = DoubleArray(0)
val emptyStringArray: Array<String> get() = arrayOf<String>()

val CharArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val ByteArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val ShortArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val IntArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val LongArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val FloatArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val DoubleArray?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()
val Array<*>?.isNullOrEmpty: Boolean get() = this == null || this.isEmpty()

val CharArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val ByteArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val ShortArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val IntArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val LongArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val FloatArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val DoubleArray?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()
val Array<*>?.nonEmpty: Boolean get() = this != null && this.isNotEmpty()


fun Sequence<Char>.toCharArrayList(): CharArrayList = CharArrayList.newListWith(*this.toList().toCharArray())
fun Iterable<Char>.toCharArrayList(): CharArrayList = this.asSequence().toCharArrayList()
fun CharArray.toCharArrayList(): CharArrayList = CharArrayList.newListWith(*this)
fun CharArrayList.asSequence(): Sequence<Char> = toArray().asSequence()
fun CharArrayList.asList(): List<Char> = this.toArray().asList()
fun charArrayListOf(vararg values: Char): CharArrayList = CharArrayList.newListWith(*values)
fun charArrayListOf(iterable: Iterable<Char>): CharArrayList = iterable.toCharArrayList()

fun Sequence<Byte>.toByteArrayList(): ByteArrayList = ByteArrayList.newListWith(*this.toList().toByteArray())
fun Iterable<Byte>.toByteArrayList(): ByteArrayList = this.asSequence().toByteArrayList()
fun ByteArray.toByteArrayList(): ByteArrayList = ByteArrayList.newListWith(*this)
fun ByteArrayList.asSequence(): Sequence<Byte> = toArray().asSequence()
fun ByteArrayList.asList(): List<Byte> = this.toArray().asList()
fun byteArrayListOf(vararg values: Byte): ByteArrayList = ByteArrayList.newListWith(*values)
fun byteArrayListOf(iterable: Iterable<Byte>): ByteArrayList = iterable.toByteArrayList()

fun Int.toByteArray(): ByteArray {
  return ByteBuffer.allocate(4).putInt(this).array()
//  return byteArrayOf((this shr 24).toByte(),
//                     (this shr 16).toByte(),
//                     (this shr 8).toByte(),
//                     this.toByte())
}

fun Sequence<Short>.toShortArrayList(): ShortArrayList = ShortArrayList.newListWith(*this.toList().toShortArray())
fun Iterable<Short>.toShortArrayList(): ShortArrayList = this.asSequence().toShortArrayList()
fun ShortArray.toShortArrayList(): ShortArrayList = ShortArrayList.newListWith(*this)
fun ShortArrayList.asSequence(): Sequence<Short> = toArray().asSequence()
fun ShortArrayList.asList(): List<Short> = this.toArray().asList()
fun shortArrayListOf(vararg values: Short): ShortArrayList = ShortArrayList.newListWith(*values)
fun shortArrayListOf(iterable: Iterable<Short>): ShortArrayList = iterable.toShortArrayList()

fun Sequence<Int>.toIntArrayList(): IntArrayList = IntArrayList.newListWith(*this.toList().toIntArray())
fun Iterable<Int>.toIntArrayList(): IntArrayList = this.asSequence().toIntArrayList()
fun IntArray.toIntArrayList(): IntArrayList = IntArrayList.newListWith(*this)
fun IntArrayList.asSequence(): Sequence<Int> = toArray().asSequence()
fun IntArrayList.asList(): List<Int> = this.toArray().asList()
fun intArrayListOf(vararg values: Int): IntArrayList = IntArrayList(*values)
fun intArrayListOf(iterable: Iterable<Int>): IntArrayList = iterable.toIntArrayList()

fun Sequence<Long>.toLongArrayList(): LongArrayList = LongArrayList.newListWith(*this.toList().toLongArray())
fun Iterable<Long>.toLongArrayList(): LongArrayList = this.asSequence().toLongArrayList()
fun LongArray.toLongArrayList(): LongArrayList = LongArrayList.newListWith(*this)
fun LongArrayList.asList(): List<Long> = this.toArray().asList()
fun longArrayListOf(vararg values: Long): LongArrayList = LongArrayList.newListWith(*values)
fun longArrayListOf(iterable: Iterable<Long>): LongArrayList = iterable.toLongArrayList()

fun Sequence<Float>.toFloatArrayList(): FloatArrayList = FloatArrayList.newListWith(*this.toList().toFloatArray())
fun Iterable<Float>.toFloatArrayList(): FloatArrayList = this.asSequence().toFloatArrayList()
fun FloatArray.toFloatArrayList(): FloatArrayList = FloatArrayList.newListWith(*this)
fun FloatArrayList.asSequence(): Sequence<Float> = toArray().asSequence()
fun FloatArrayList.asList(): List<Float> = this.toArray().asList()
fun floatArrayListOf(vararg values: Float): FloatArrayList = FloatArrayList.newListWith(*values)
fun floatArrayListOf(iterable: Iterable<Float>): FloatArrayList = iterable.toFloatArrayList()

fun Sequence<Double>.toDoubleArrayList(): DoubleArrayList = DoubleArrayList.newListWith(*this.toList().toDoubleArray())
fun Iterable<Double>.toDoubleArrayList(): DoubleArrayList = this.asSequence().toDoubleArrayList()
fun DoubleArray.toDoubleArrayList(): DoubleArrayList = DoubleArrayList.newListWith(*this)
fun DoubleArrayList.asSequence(): Sequence<Double> = toArray().asSequence()
fun DoubleArrayList.asList(): List<Double> = this.toArray().asList()
fun doubleArrayListOf(vararg values: Double): DoubleArrayList = DoubleArrayList.newListWith(*values)
fun doubleArrayListOf(iterable: Iterable<Double>): DoubleArrayList = iterable.toDoubleArrayList()

inline fun CharArray?.setAll(initializer: (Int) -> Char): CharArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

inline fun ByteArray?.setAll(initializer: (Int) -> Byte): ByteArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

inline fun ShortArray?.setAll(initializer: (Int) -> Short): ShortArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

inline fun IntArray?.setAll(initializer: (Int) -> Int): IntArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

inline fun LongArray?.setAll(initializer: (Int) -> Long): LongArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

inline fun FloatArray?.setAll(initializer: (Int) -> Float): FloatArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

inline fun DoubleArray?.setAll(initializer: (Int) -> Double): DoubleArray? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}


inline fun <T> Array<T>?.setAll(initializer: (Int) -> T): Array<T>? {
  return this?.apply {
    for (i in this.indices) {
      this[i] = initializer(i)
    }
  }
}

fun <T> generateByteArray(length: Int, generator: ByteFunction<Int>): ByteArray
    = generateByteArray<Int>(length) { generator.byteValueOf(it) }

inline fun <T> generateByteArray(length: Int, generator: (Int) -> Byte): ByteArray {
  return (0 until length).map { i -> generator(i) }.asByteArray()
}

fun generateIntArray(length: Int, generator: IntFunction<Int>): IntArray
    = generateIntArray(length) { generator.intValueOf(it) }

inline fun generateIntArray(length: Int, generator: (Int) -> Int): IntArray
    = (0 until length).map { i -> generator(i) }.asIntArray()

fun <T> generateLongArray(length: Int, generator: LongFunction<Int>): LongArray
    = generateLongArray<Int>(length) { generator.longValueOf(it) }

inline fun <T> generateLongArray(length: Int, generator: (Int) -> Long): LongArray {
  return (0 until length).map { i -> generator(i) }.asLongArray()
}

fun <T> generateFloatArray(length: Int, generator: FloatFunction<Int>): FloatArray
    = generateFloatArray<Int>(length) { generator.floatValueOf(it) }

inline fun <T> generateFloatArray(length: Int, generator: (Int) -> Float): FloatArray {
  return (0 until length).map { i -> generator(i) }.asFloatArray()
}

fun <T> generateDoubleArray(length: Int, generator: DoubleFunction<Int>): DoubleArray
    = generateDoubleArray<Int>(length) { generator.doubleValueOf(it) }

inline fun <T> generateDoubleArray(length: Int, generator: (Int) -> Double): DoubleArray {
  return (0 until length).map { i -> generator(i) }.asDoubleArray()
}

fun getRandomBytes(count: Int): ByteArray = randomBytes(count)



