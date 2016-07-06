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

package debop4k.core.collections.graphs

import org.eclipse.collections.impl.factory.Sets
import org.eclipse.collections.impl.list.mutable.FastList
import org.slf4j.LoggerFactory
import java.util.*

private val log = LoggerFactory.getLogger("GraphFunctions")

/**
 * Depth First Search
 */
fun <T> depthFirstSearch(source: T, adjacents: (T) -> Iterable<T>): MutableList<T> {
  val toScan = Stack<T>()
  val scanned = Sets.mutable.of<T>() // new HashSet<>();

  toScan.add(source)
  while (toScan.size > 0) {
    val current = toScan.pop()
    log.trace("scanning... {}", current)
    scanned.add(current)

    for (item in adjacents(current)) {
      toScan.add(item)
    }
  }
  return FastList.newList(scanned)
}

/**
 * Breadth First Search
 */
fun <T> breadFirstSearch(source: T, adjacents: (T) -> Iterable<T>): MutableList<T> {
  val toScan = ArrayDeque<T>()
  val scanned = Sets.mutable.of<T>() // new HashSet<>();

  toScan.add(source)
  while (toScan.size > 0) {
    val current = toScan.peek()
    log.trace("scanning ... {}", current)
    scanned.add(current)

    for (item in adjacents(current)) {
      toScan.add(item)
    }
  }

  return FastList.newList(scanned)
}