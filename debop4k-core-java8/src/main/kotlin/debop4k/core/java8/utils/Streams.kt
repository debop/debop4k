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

package debop4k.core.java8.utils

import debop4k.core.java.collections.eclipseCollections.toDoubleArrayList
import debop4k.core.java.collections.eclipseCollections.toIntArrayList
import debop4k.core.java.collections.eclipseCollections.toLongArrayList
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList
import java.util.*
import java.util.stream.*


fun <T> Stream<T>.asSequence(): Sequence<T> = this.iterator().asSequence()
fun <T> Stream<T>.toList(): List<T> = this.collect(Collectors.toList<T>())
fun <T> Stream<T>.toFastList(): FastList<T> = FastList.newList(this.toList())


fun IntStream.asSequence(): Sequence<Int> = this.iterator().asSequence()
fun IntStream.toList(): List<Int> = this.toArray().asList()
fun IntStream.toIntArrayList(): IntArrayList = this.asSequence().toIntArrayList()


fun LongStream.asSequence(): Sequence<Long> = this.iterator().asSequence()
fun LongStream.toList(): List<Long> = this.toArray().asList()
fun LongStream.toLongArrayList(): LongArrayList = this.asSequence().toLongArrayList()


fun DoubleStream.asSequence(): Sequence<Double> = this.iterator().asSequence()
fun DoubleStream.toList(): List<Double> = this.toArray().asList()
fun DoubleStream.toDoubleArrayList(): DoubleArrayList = this.asSequence().toDoubleArrayList()


fun <T> Sequence<T>.asStream(): Stream<T> =
    StreamSupport.stream({ Spliterators.spliteratorUnknownSize(this.iterator(), 0) }, 0, false)

fun IntArrayList.asStream(): IntStream = Arrays.stream(this.toArray())
fun LongArrayList.asStream(): LongStream = Arrays.stream(this.toArray())
fun DoubleArrayList.asStream(): DoubleStream = Arrays.stream(this.toArray())