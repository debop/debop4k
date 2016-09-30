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

package debop4k.core.parallels

import debop4k.core.cryptography.secureRandom
import org.eclipse.collections.api.block.function.Function
import org.eclipse.collections.impl.parallel.ParallelIterate
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @see [ParallelIterates]
 */
@Deprecated(message = "Kotlin 으로 구현한 것으로 변환", replaceWith = ReplaceWith("ParallelIterates"))
object Parallels {

  private val log = LoggerFactory.getLogger(javaClass)

  val random: Random = secureRandom
  val processCount: Int by lazy { Runtime.getRuntime().availableProcessors() }
  val workerCount: Int by lazy { processCount * 2 }

  val DEFAULT_MIN_FORK_SIZE = 10000
  val AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors()
  val TASK_RATIO = 2
  val DEFAULT_PARALLEL_TASK_COUNT = ParallelIterate.getDefaultTaskCount()
  val EXECUTOR_SERVICE = ParallelIterate.newPooledExecutor(ParallelIterate::class.java.simpleName, true)


  fun <T : Comparable<T>, V> mapAsOrdered(items: Iterable<T>, mapper: (T) -> V): Collection<V>
      = ParallelIterate.collect(items, mapper, true)

  fun <T, V> map(items: Iterable<T>, mapper: (T) -> V): Collection<V>
      = ParallelIterate.collect(items, mapper)

  fun run(count: Int, action: () -> Unit): Unit {
    run(0, count - 1, action)
  }

  fun run(start: Int, endInclusive: Int, action: () -> Unit): Unit {
    run(IntRange(start, endInclusive), action)
  }

  fun run(range: IntProgression, action: () -> Unit): Unit {
    ParallelIterate.forEach(range.asIterable(), { action.invoke() })
  }

  fun runAction(count: Int, action1: (Int) -> Unit): Unit {
    runAction(0, count - 1, action1)
  }

  fun runAction(start: Int, endInclusive: Int, action1: (Int) -> Unit): Unit {
    runAction(IntRange(start, endInclusive), action1)
  }

  fun runAction(range: IntProgression, action1: (Int) -> Unit): Unit {
    ParallelIterate.forEach(range.asIterable(), { action1(it) })
  }

  fun <T> runEach(elements: Iterable<T>, block: (T) -> Unit): Unit {
    ParallelIterate.forEach(elements, { block(it) })
  }

  fun <T> runEach(elements: Iterable<T>, batchSize: Int, block: (T) -> Unit): Unit {
    ParallelIterate.forEach(elements,
                            { block(it) },
                            batchSize)
  }

  fun <T, V> call(count: Int, func: () -> V): Collection<V>
      = call<T, V>(0, count - 1, func)

  fun <T, V> call(start: Int, endInclusive: Int, func: () -> V): Collection<V>
      = call<T, V>(IntRange(start, endInclusive), func)

  fun <T, V> call(range: IntProgression, func: () -> V): Collection<V>
      = ParallelIterate.collect(range.asIterable(), { func() })


  fun <V> callFunction(count: Int, func: (Int) -> V): Collection<V>
      = callFunction(0, count - 1, func)

  fun <V> callFunction(start: Int, endInclusive: Int, func: (Int) -> V): Collection<V>
      = callFunction(IntRange(start, endInclusive), func)

  fun <V> callFunction(range: IntProgression, func: (Int) -> V): Collection<V>
      = ParallelIterate.collect(range.asIterable(), { func(it) })


  fun <T, V> callEach(elements: Iterable<T>, func: (T) -> V): Collection<V>
      = ParallelIterate.collect(elements, { func(it) })

  fun <T, V> callEach(elements: Iterable<T>, batchSize: Int, func: (T) -> V): Collection<V>
      = ParallelIterate.collect(elements,
                                Function { func(it) },
                                emptyList(),
                                batchSize,
                                EXECUTOR_SERVICE,
                                true)
}