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

@file:JvmName("Lambdax")
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST", "NOTHING_TO_INLINE")

package debop4k.core.collections

import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*

inline fun <T> Iterable<T>.forEach(action: Consumer<T>): Unit
    = (this as java.lang.Iterable<T>).forEach(action)

inline fun <T> Iterable<T>.spliterator(): Spliterator<T>
    = (this as java.lang.Iterable<T>).spliterator()

inline fun <T> Iterator<T>.forEachRemaining(action: Consumer<in T>): Unit
    = (this as java.util.Iterator<T>).forEachRemaining(action)

inline fun <T> Iterator<T>.forEachRemaining(crossinline action: (T) -> Unit): Unit
    = (this as java.util.Iterator<T>).forEachRemaining { action(it) }


inline fun <T> MutableCollection<T>.removeIf(filter: Predicate<in T>): Boolean
    = (this as java.util.Collection<T>).removeIf(filter)

inline fun <T> MutableCollection<T>.removeIf(crossinline filter: (T) -> Boolean): Boolean
    = (this as java.util.Collection<T>).removeIf { filter(it) }

inline fun <T> Collection<T>.stream(): Stream<T> = (this as java.util.Collection<T>).stream()

inline fun <T> Collection<T>.parallelStream(): Stream<T> = (this as java.util.Collection<T>).parallelStream()


inline fun <T> MutableList<T>.replaceAll(operator: UnaryOperator<T>): Unit
    = (this as java.util.List<T>).replaceAll(operator)

inline fun <T> MutableList<T>.replaceAll(crossinline operator: (T) -> T): Unit
    = (this as java.util.List<T>).replaceAll { operator(it) }


inline fun <T> MutableList<T>.sort(comparator: Comparator<in T>)
    = (this as java.util.List<T>).sort(comparator)

inline fun <K, V> Map<out K, V>.getOrDefault(key: K, defaultValue: () -> V): V {
  val v = this[key]

  return if (v != null && containsKey(key)) v
  else defaultValue()
}

inline fun <K, V> Map<K, V>.forEach(action: BiConsumer<in K, in V>): Unit
    = (this as java.util.Map<K, V>).forEach(action)

inline fun <K, V> Map<K, V>.forEach(crossinline action: (K, V) -> Unit): Unit
    = (this as java.util.Map<K, V>).forEach { k, v -> action(k, v) }

inline fun <K, V> MutableMap<K, V>.replaceAll(function: BiFunction<in K, in V, out V>): Unit
    = (this as java.util.Map<K, V>).replaceAll(function)

inline fun <K, V> MutableMap<K, V>.replaceAll(crossinline function: (K, V) -> V): Unit
    = (this as java.util.Map<K, V>).replaceAll { k, v -> function(k, v) }

inline fun <K, V> MutableMap<K, V>.putIfAbsent(key: K, value: V): V?
    = (this as java.util.Map<K, V>).putIfAbsent(key, value)

inline fun <K, V> MutableMap<out K, out V>.remove(key: K, value: V): Boolean
    = (this as java.util.Map<K, V>).remove(key, value)


inline fun <K, V> MutableMap<K, V>.replace(key: K, oldValue: V, newValue: V): Boolean
    = (this as java.util.Map<K, V>).replace(key, oldValue, newValue)

inline fun <K, V> MutableMap<K, V>.replace(key: K, value: V): V?
    = (this as java.util.Map<K, V>).replace(key, value)


inline fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, mappingFunction: Function<in K, out V?>): V?
    = (this as java.util.Map<K, V>).computeIfAbsent(key, mappingFunction)

inline fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, crossinline mappingFunction: (K) -> V?): V?
    = (this as java.util.Map<K, V>).computeIfAbsent(key, Function { mappingFunction(it) })

inline fun <K, V> MutableMap<K, V>.computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V, out V?>): V?
    = (this as java.util.Map<K, V>).computeIfPresent(key, remappingFunction)

inline fun <K, V> MutableMap<K, V>.computeIfPresent(key: K, crossinline remappingFunction: (K, V) -> V?): V?
    = (this as java.util.Map<K, V>).computeIfPresent(key, BiFunction { k, v -> remappingFunction(k, v) })


inline fun <K, V> MutableMap<K, V>.compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>): V?
    = (this as java.util.Map<K, V>).compute(key, remappingFunction)

inline fun <K, V> MutableMap<K, V>.compute(key: K, crossinline remappingFunction: (K, V?) -> V?): V?
    = (this as java.util.Map<K, V>).compute(key, BiFunction { k, v -> remappingFunction(k, v) })


inline fun <K, V> MutableMap<K, V>.merge(key: K, value: V, remappingFunction: BiFunction<in V, in V, out V?>): V?
    = (this as java.util.Map<K, V>).merge(key, value, remappingFunction)

inline fun <K, V> MutableMap<K, V>.merge(key: K, value: V, crossinline remappingFunction: (V, V) -> V?): V?
    = (this as java.util.Map<K, V>).merge(key, value, BiFunction { oldValue, newValue -> remappingFunction(oldValue, newValue) })
