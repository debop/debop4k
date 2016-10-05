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

@file:JvmName("ParallelIteratex")

package debop4k.core.collections

import org.eclipse.collections.api.map.primitive.ObjectDoubleMap
import org.eclipse.collections.api.map.primitive.ObjectLongMap
import org.eclipse.collections.api.multimap.MutableMultimap
import org.eclipse.collections.impl.parallel.ParallelIterate
import org.eclipse.collections.impl.parallel.ParallelMapIterate
import java.math.BigDecimal
import java.math.BigInteger


val DEFAULT_MIN_FORK_SIZE: Int = 10000

/**
 * 병렬 방식으로 filtering 을 수행합니다.
 */
@JvmOverloads
inline fun <T> Iterable<T>.parFilter(reorder: Boolean = true,
                                     crossinline predicate: (T) -> Boolean): Collection<T> {
  return ParallelIterate.select(this,
                                { predicate(it) },
                                reorder)
}

/**
 * 병렬 방식으로 [predicate] 가 false 인 요소들만 filtering 합니다.
 */
@JvmOverloads
inline fun <T> Iterable<T>.parReject(reorder: Boolean = true,
                                     crossinline predicate: (T) -> Boolean): Collection<T> {
  return ParallelIterate.reject(this, { predicate(it) }, reorder)
}

/**
 * 병렬 방식으로 [predicate] 가 true 를 반환하는 요소의 수를 반환합니다.
 */
inline fun <T> Iterable<T>.parCount(crossinline predicate: (T) -> Boolean): Int {
  return ParallelIterate.count(this, { predicate(it) })
}

/**
 * Parallel forEach
 */
inline fun <T> Iterable<T>.parForEach(crossinline action: (T) -> Unit): Unit {
  ParallelIterate.forEach(this, { action(it) })
}

/**
 * Parallel forEach with index
 */
inline fun <T> Iterable<T>.parForEachWithIndex(crossinline action: (T, Int) -> Unit): Unit {
  ParallelIterate.forEachWithIndex(this, { t, i -> action(t, i) })
}

/**
 * Parallel Map
 */
@JvmOverloads
inline fun <T, V> Iterable<T>.parMap(reorder: Boolean = true, crossinline mapper: (T) -> V): Collection<V> {
  return ParallelIterate.collect(this, { mapper(it) }, reorder)
}

/**
 * Parallel Flat Map
 */
@JvmOverloads
inline fun <T, V> Iterable<T>.parFlatMap(reorder: Boolean = true,
                                         crossinline mapper: (T) -> Collection<V>): Collection<V> {
  return ParallelIterate.flatCollect(this, { mapper(it) }, reorder)
}

/**
 * Parallel Filter and Mapping
 */
@JvmOverloads
inline fun <T, V> Iterable<T>.parFilterMap(crossinline predicate: (T) -> Boolean,
                                           crossinline mapper: (T) -> V,
                                           reorder: Boolean = true): Collection<V> {
  return ParallelIterate.collectIf(this, { predicate(it) }, { mapper(it) }, reorder)
}

/**
 * Parallel group by
 */
@JvmOverloads
inline fun <K, V, R : MutableMultimap<K, V>>
    Iterable<V>.parGroupBy(batchSize: Int = DEFAULT_MIN_FORK_SIZE,
                           crossinline function: (V) -> K): MutableMultimap<K, V> {

  return ParallelIterate.groupBy(this,
                                 { function(it) },
                                 batchSize)

}

/**
 * Parallel aggregate by
 */
@JvmOverloads
inline fun <T, K, V> Iterable<T>.parAggregateBy(crossinline groupBy: (T) -> K,
                                                crossinline zeroValueFactory: () -> V,
                                                crossinline nonMutatingAggregator: (V, T) -> V,
                                                batchSize: Int = DEFAULT_MIN_FORK_SIZE): MutableMap<K, V> {
  return ParallelIterate.aggregateBy(this,
                                     { groupBy(it) },
                                     { zeroValueFactory() },
                                     { v, t -> nonMutatingAggregator(v, t) },
                                     batchSize)
}

/**
 * Parallel aggregate by
 */
@JvmOverloads
inline fun <T, K, V> Iterable<T>.parAggregateInPlaceBy(crossinline groupBy: (T) -> K,
                                                       crossinline zeroValueFactory: () -> V,
                                                       crossinline mutatingAggregator: (V, T) -> Unit,
                                                       batchSize: Int = DEFAULT_MIN_FORK_SIZE): MutableMap<K, V> {
  return ParallelIterate.aggregateInPlaceBy(this,
                                            { groupBy(it) },
                                            { zeroValueFactory() },
                                            { v, t -> mutatingAggregator(v, t) },
                                            batchSize)
}

/**
 * Parallel sum by Double
 */
inline fun <T, V> Iterable<T>.parSumByDouble(crossinline groupBy: (T) -> V,
                                             crossinline doubleFunc: (T) -> Double): ObjectDoubleMap<V> {
  return ParallelIterate.sumByDouble(this,
                                     { groupBy(it) },
                                     { doubleFunc(it) })
}

/**
 * Parallel sum by Float
 */
inline fun <T, V> Iterable<T>.parSumByFloat(crossinline groupBy: (T) -> V,
                                            crossinline floatFunc: (T) -> Float): ObjectDoubleMap<V> {
  return ParallelIterate.sumByFloat(this,
                                    { groupBy(it) },
                                    { floatFunc(it) })
}

/**
 * Parallel sum by Long
 */
inline fun <T, V> Iterable<T>.parSumByLong(crossinline groupBy: (T) -> V,
                                           crossinline longFunc: (T) -> Long): ObjectLongMap<V> {
  return ParallelIterate.sumByLong(this,
                                   { groupBy(it) },
                                   { longFunc(it) })
}

/**
 * Parallel sum by Int
 */
inline fun <T, V> Iterable<T>.parSumByInt(crossinline groupBy: (T) -> V,
                                          crossinline longFunc: (T) -> Int): ObjectLongMap<V> {
  return ParallelIterate.sumByInt(this,
                                  { groupBy(it) },
                                  { longFunc(it) })
}

/**
 * Parallel sum by BigDecimal
 */
inline fun <T, V> Iterable<T>.parSumByBigDecimal(crossinline groupBy: (T) -> V,
                                                 crossinline longFunc: (T) -> BigDecimal): MutableMap<V, BigDecimal> {
  return ParallelIterate.sumByBigDecimal(this,
                                         { groupBy(it) },
                                         { longFunc(it) })
}

/**
 * Parallel sum by BigInteger
 */
inline fun <T, V> Iterable<T>.parSumByBigInteger(crossinline groupBy: (T) -> V,
                                                 crossinline longFunc: (T) -> BigInteger): MutableMap<V, BigInteger> {
  return ParallelIterate.sumByBigInteger(this,
                                         { groupBy(it) },
                                         { longFunc(it) })
}

/**
 * Map을 병렬로 procedure를 호출하여 작업을 수행합니다.
 */
inline fun <K, V> Map<K, V>.parForEach(crossinline procedure: (K, V) -> Unit): Unit {
  ParallelMapIterate.forEachKeyValue(this, { k, v -> procedure(k, v) })
}

/**
 * Map을 병렬로 mapping 을 수행합니다
 */
@JvmOverloads
inline fun <K, V, R> Map<K, V>.parMap(reorder: Boolean = true,
                                      crossinline mapper: (K, V) -> R): Collection<R> {
  return this.toList().parMap(reorder) { mapper(it.first, it.second) }
}

/**
 * Map을 병렬로 flat mapping 을 수행합니다
 */
@JvmOverloads
inline fun <K, V, R> Map<K, V>.parFlatMap(reorder: Boolean = true,
                                          crossinline flatMapper: (K, V) -> Collection<R>): Collection<R> {
  return this.toList().parFlatMap(reorder) { flatMapper(it.first, it.second) }
}