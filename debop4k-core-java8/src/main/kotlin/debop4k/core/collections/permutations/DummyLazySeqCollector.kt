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

package debop4k.core.collections.permutations

import java.util.function.*
import java.util.function.Function
import java.util.stream.*
import java.util.stream.Collector.*


fun <E> toLazySeq(): Collector<E, Permutation<E>, Permutation<E>> = DummyLazySeqCollector()

/**
 * DummyLazySeqCollector
 * @author sunghyouk.bae@gmail.com
 */
class DummyLazySeqCollector<T> : Collector<T, Permutation<T>, Permutation<T>> {

  companion object {
    @JvmField val INSTANCE: DummyLazySeqCollector<Any> = DummyLazySeqCollector<Any>()

    @Suppress("UNCHECKED_CAST")
    @JvmStatic fun <E> instance(): DummyLazySeqCollector<E> {
      return INSTANCE as DummyLazySeqCollector<E>
    }
  }

  override fun accumulator(): BiConsumer<Permutation<T>, T> {
    throw IllegalStateException("Should never be called")
  }

  override fun characteristics(): MutableSet<Characteristics> {
    throw IllegalStateException("Should never be called")
  }

  override fun supplier(): Supplier<Permutation<T>> {
    throw IllegalStateException("Should never be called")
  }

  override fun combiner(): BinaryOperator<Permutation<T>> {
    throw IllegalStateException("Should never be called")
  }

  override fun finisher(): Function<Permutation<T>, Permutation<T>> {
    throw IllegalStateException("Should never be called")
  }
}