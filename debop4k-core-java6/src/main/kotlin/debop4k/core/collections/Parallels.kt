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

import com.google.common.collect.Iterables
import debop4k.core.Systems
import debop4k.core.asyncs.async
import debop4k.core.asyncs.readyAll
import debop4k.core.asyncs.resultAll
import nl.komponents.kovenant.Promise
import org.eclipse.collections.impl.list.mutable.FastList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import java.util.*

private val log: Logger by lazy { LoggerFactory.getLogger("Parallels") }
private val random: Random by lazy { SecureRandom.getInstanceStrong() }
private val workerCount: Int by lazy { Systems.ProcessCount * 2 }

fun calcPartitionSize(itemCount: Int, partitionCount: Int): Int {
  return (itemCount / partitionCount) + (if (itemCount % partitionCount > 0) 1 else 0)
}

fun <T, V> Iterable<T>.mapAsOrdered(mapper: (T) -> V): Iterable<V> {
  return this
      .mapIndexed { i, t -> Pair(i, mapper(t)) }
      .sortedBy { it.first }
      .map { it.second }
}

/**
 * [Sequence]를 병렬로 작업을 수행합니다.
 */
fun <T> Sequence<T>.parEach(action: (T) -> Unit): Unit {
  this.asIterable().parEach(action)
}

/**
 * [Iterable] 을 병렬로 작업을 수행합니다.
 */
fun <T> Iterable<T>.parEach(action: (T) -> Unit): Unit {
  log.trace("병렬로 forEach 구문을 수행합니다...")

  try {
    val partitions = Iterables.partition(this, calcPartitionSize(this.count(), workerCount))
    val tasks = FastList.newList<Promise<Unit, Exception>>()

    for (partition in partitions) {
      tasks += async() { partition.forEach(action) }
    }

    readyAll(tasks)
    log.debug("모든 작업을 병렬로 수행했습니다. task 수={}", tasks.size)
  } catch(e: Exception) {
    log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e)
    throw RuntimeException(e)
  }
}

/**
 * [Sequence]를 병렬로 mapping 작업을 수행합니다.
 */
fun <T, V> Sequence<T>.parMap(mapper: (T) -> V): Sequence<V> {
  return this.asIterable().parMap(mapper).asSequence()
}

/**
 * [Iterable]을 병렬로 mapping 작업을 수행합니다.
 */
fun <T, V> Iterable<T>.parMap(mapper: (T) -> V): Iterable<V> {
  log.trace("병렬로 forEach 구문을 수행합니다...")

  try {
    val partitions = Iterables.partition(this, calcPartitionSize(this.count(), workerCount))
    val tasks = FastList.newList<Promise<List<V>, Exception>>()

    for (partition in partitions) {
      tasks += async() { partition.map(mapper) }
    }

    // TODO: Index 순으로 정렬이 되는지 확인 필요
    val taskResults = resultAll(tasks)
    log.debug("모든 작업을 병렬로 수행했습니다. task 수={}", tasks.size)

    val results = FastList.newList<V>()
    for (r in taskResults) {
      results.addAll(r)
    }
    return results
  } catch(e: Exception) {
    log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e)
    throw RuntimeException(e)
  }
}
