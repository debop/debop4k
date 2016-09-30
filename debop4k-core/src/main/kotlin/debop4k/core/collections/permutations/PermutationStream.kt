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

package debop4k.core.collections.permutations

import debop4k.core.collections.stream
import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*

/**
 * [LazySeq] 를 Java 8의 [Stream] 형태로 제공하기 위한 Wrapper 입니다
 * @author sunghyouk.bae@gmail.com
 */
class PermutationStream<T>(val underlying: Permutation<T>) : Stream<T> {

  private var closed: Boolean = false
  private var closeHandler: Runnable? = null

  override fun reduce(identity: T, accumulator: BinaryOperator<T>): T {
    return underlying.reduce(identity) { a, b -> accumulator.apply(a, b) }
  }

  override fun <U> reduce(identity: U, accumulator: BiFunction<U, in T, U>, combiner: BinaryOperator<U>): U {
    return underlying.reduce(identity) { a, b -> accumulator.apply(a, b) }
  }

  override fun reduce(accumulator: BinaryOperator<T>): Optional<T> {
    return Optional.ofNullable(underlying.reduce { a: T, b: T -> accumulator.apply(a, b) })
  }

  override fun iterator(): MutableIterator<T> {
    return underlying.iterator()
  }

  override fun count(): Long {
    return underlying.size.toLong()
  }

  override fun mapToLong(mapper: ToLongFunction<in T>?): LongStream {
    return underlying.stream().mapToLong(mapper)
  }

  override fun allMatch(predicate: Predicate<in T>): Boolean {
    return underlying.allMatch { predicate.test(it) }
  }

  override fun <R : Any?> map(mapper: Function<in T, out R>): Stream<R> {
    return underlying.map { mapper.apply(it) }.stream()

  }

  override fun findFirst(): Optional<T> {
    return Optional.ofNullable(underlying.head)
  }

  override fun findAny(): Optional<T> {
    return Optional.ofNullable(underlying.head)
  }

  override fun mapToInt(mapper: ToIntFunction<in T>): IntStream {
    return underlying.stream().mapToInt(mapper)
  }

  override fun forEach(action: Consumer<in T>) {
    underlying.forEach { action.accept(it) }
  }

  override fun <R : Any?> flatMap(mapper: Function<in T, out Stream<out R>>): Stream<R> {
    return underlying.flatMap { mapper.apply(it).collect(Collectors.toList<R>()) }.stream()
  }

  override fun flatMapToDouble(mapper: Function<in T, out DoubleStream>?): DoubleStream {
    return underlying.stream().flatMapToDouble(mapper)
  }

  override fun spliterator(): Spliterator<T> {
    return underlying.stream().spliterator()
  }

  override fun parallel(): Stream<T> = underlying.stream().parallel()

  override fun <R : Any?> collect(supplier: Supplier<R>?, accumulator: BiConsumer<R, in T>?, combiner: BiConsumer<R, R>?): R {
    return underlying.stream().collect(supplier, accumulator, combiner)
  }

  override fun <R : Any?, A : Any?> collect(collector: Collector<in T, A, R>?): R {
    return underlying.stream().collect(collector)
  }

  override fun min(comparator: Comparator<in T>): Optional<T> {
    return Optional.ofNullable(underlying.min(comparator))
  }

  override fun noneMatch(predicate: Predicate<in T>): Boolean {
    return underlying.noneMatch { predicate.test(it) }
  }

  override fun sorted(): Stream<T> {
    return underlying.sorted().stream()
  }

  override fun sorted(comparator: Comparator<in T>): Stream<T> {
    return underlying.sorted(comparator).stream()
  }

  override fun flatMapToLong(mapper: Function<in T, out LongStream>): LongStream {
    return underlying.stream().flatMapToLong(mapper)
  }

  override fun isParallel(): Boolean = false

  override fun peek(action: Consumer<in T>): Stream<T> {
    return underlying.map {
      action.accept(it)
      it
    }.stream()
  }

  override fun distinct(): Stream<T> {
    return underlying.distinct().stream()
  }

  override fun filter(predicate: Predicate<in T>): Stream<T> {
    return underlying.filter { predicate.test(it) }.stream()
  }

  override fun unordered(): Stream<T> = this

  override fun forEachOrdered(action: Consumer<in T>) {
    underlying.forEach { action.accept(it) }
  }

  override fun limit(maxSize: Long): Stream<T> {
    return underlying.limit(maxSize).stream()
  }

  override fun toArray(): Array<out Any?> {
    val array = arrayOfNulls<Any>(underlying.size)
    copyToArray(array)
    return array
  }

  @Suppress("UNCHECKED_CAST")
  override fun <A : Any?> toArray(generator: IntFunction<Array<A>>): Array<out A> {
    val array = generator.apply(underlying.size)
    copyToArray(array as Array<Any?>)
    return array
  }

  private fun copyToArray(array: Array<Any?>) {
    var cur = underlying
    for (i in 0 until array.size) {
      array[i] = cur.head as Any
      cur = cur.tail
    }
  }

  override fun sequential(): Stream<T> = this

  override fun anyMatch(predicate: Predicate<in T>): Boolean {
    return underlying.anyMatch { predicate.test(it) }
  }

  override fun flatMapToInt(mapper: Function<in T, out IntStream>?): IntStream {
    return underlying.stream().flatMapToInt(mapper)
  }

  override fun onClose(closeHandler: Runnable): Stream<T> {
    this.closeHandler = closeHandler
    return this
  }

  override fun close() {
    closeHandler?.run()
    closed = true
  }

  override fun max(comparator: Comparator<in T>): Optional<T> {
    return Optional.ofNullable(underlying.max(comparator))
  }

  override fun skip(n: Long): Stream<T> {
    return underlying.drop(n).stream()

  }

  override fun mapToDouble(mapper: ToDoubleFunction<in T>?): DoubleStream {
    return underlying.stream().mapToDouble(mapper)
  }
}