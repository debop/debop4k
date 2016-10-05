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

@file:JvmName("Streamx")

package debop4k.core.utils

import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.*
import java.util.*
import java.util.stream.*

fun <T> Stream<T>.toSequence(): Sequence<T> = Sequence { iterator() }
fun <T> Stream<T>.toIterable(): Iterable<T> = this.toSequence().asIterable()
fun <T> Stream<T>.toList(): List<T> = this.collect(Collectors.toList<T>())
fun <T> Stream<T>.toFastList(): FastList<T> = FastList.newList(this.toIterable())

fun <T> Array<T>.stream(): Stream<T> {
  val builder = Stream.builder<T>()
  this.forEach { builder.accept(it) }
  return builder.build()
}

fun <T> Iterable<T>.stream(): Stream<T> {
  return StreamSupport.stream({ Spliterators.spliteratorUnknownSize(this.iterator(), 0) }, 0, false)
}

fun <T> Iterable<T>.parallelStream(): Stream<T> {
  return StreamSupport.stream({ Spliterators.spliteratorUnknownSize(this.iterator(), 0) }, 0, true)
}

fun <T> Iterator<T>.stream(): Stream<T> {
  val builder = Stream.builder<T>()
  while (hasNext()) {
    builder.accept(next())
  }
  return builder.build()
}

fun <T> Iterator<T>.parallelStream(): Stream<T> = this.stream().parallel()

fun IntStream.toSequence(): Sequence<Int> = Sequence { iterator() }
fun IntStream.toIterable(): Iterable<Int> = this.toSequence().asIterable()
fun IntStream.toList(): List<Int> = this.toArray().asList()
fun IntStream.toIntArrayList(): IntArrayList = IntArrayList.newListWith(*this.toList().toIntArray())

fun LongStream.toSequence(): Sequence<Long> = Sequence { iterator() }
fun LongStream.toIterable(): Iterable<Long> = this.toSequence().asIterable()
fun LongStream.toList(): List<Long> = this.toArray().asList()
fun LongStream.toLongArrayList(): LongArrayList = LongArrayList.newListWith(*this.toList().toLongArray())

fun DoubleStream.toSequence(): Sequence<Double> = Sequence { iterator() }
fun DoubleStream.toIterable(): Iterable<Double> = this.toSequence().asIterable()
fun DoubleStream.toList(): List<Double> = this.toArray().asList()
fun DoubleStream.toDoubleArrayList(): DoubleArrayList = DoubleArrayList.newListWith(*(this.toList().toDoubleArray()))

fun IntArray.stream(): IntStream = Arrays.stream(this)
fun IntArrayList.stream(): IntStream = Arrays.stream(toArray())
fun IntArrayList.parallelStream(): IntStream = stream().parallel()

fun LongArray.stream(): LongStream = Arrays.stream(this)
fun LongArrayList.stream(): LongStream = Arrays.stream(toArray())
fun LongArrayList.parallelStream(): LongStream = stream().parallel()

fun FloatArray.stream(): DoubleStream {
  val builder = DoubleStream.builder()
  this.forEach { builder.accept(it.toDouble()) }
  return builder.build()
}

fun FloatArrayList.stream(): DoubleStream {
  val builder = DoubleStream.builder()
  this.forEach { builder.accept(it.toDouble()) }
  return builder.build()
}

fun FloatArrayList.parallelStream(): DoubleStream = stream().parallel()


fun DoubleArray.stream(): DoubleStream = Arrays.stream(this)
fun DoubleArrayList.stream(): DoubleStream = Arrays.stream(toArray())
fun DoubleArrayList.parallelStream(): DoubleStream = stream().parallel()





