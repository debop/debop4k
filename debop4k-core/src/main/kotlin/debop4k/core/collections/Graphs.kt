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

@file:JvmName("Graphs")

package debop4k.core.collections

import debop4k.core.loggerOf
import org.eclipse.collections.impl.list.mutable.FastList
import java.util.*

private val log = loggerOf("GraphFunctions")

/**
 * Depth First Search
 */
inline fun <T> depthFirstSearch(source: T, adjacents: (T) -> Iterable<T>): FastList<T> {
  val toScan = Stack<T>()
  val scanned = fastListOf<T>()

  toScan.add(source)
  while (toScan.size > 0) {
    val current = toScan.pop()
    scanned.add(current)

    for (item in adjacents(current)) {
      toScan.add(item)
    }
  }
  return scanned
}

/**
 * Breadth First Search
 */
inline fun <T> breadFirstSearch(source: T, adjacents: (T) -> Iterable<T>): FastList<T> {
  val toScan = ArrayDeque<T>()
  val scanned = fastListOf<T>()

  toScan.add(source)
  while (toScan.size > 0) {
    val current = toScan.pop()
    scanned.add(current)

    for (item in adjacents(current)) {
      toScan.add(item)
    }
  }

  return scanned
}

/**
 * Filter by Depth First Search
 */
inline fun <T> filterByDepthFirstSearch(source: T,
                                        adjacents: (T) -> Iterable<T>,
                                        predicate: (T) -> Boolean): FastList<T> {
  val toScan = Stack<T>()
  val filtered = fastListOf<T>()

  toScan.add(source)
  while (toScan.size > 0) {
    val current = toScan.pop()

    if (predicate(current)) {
      filtered.add(current)
    }

    for (item in adjacents(current)) {
      toScan.add(item)
    }
  }
  return filtered
}

/**
 * Filter by Breadth First Search
 */
inline fun <T> filterByBreadFirstSearch(source: T,
                                        adjacents: (T) -> Iterable<T>,
                                        predicate: (T) -> Boolean): FastList<T> {
  val toScan = ArrayDeque<T>()
  val filtered = fastListOf<T>()

  toScan.add(source)
  while (toScan.size > 0) {
    val current = toScan.pop()
    if (predicate(current))
      filtered.add(current)

    for (item in adjacents(current)) {
      toScan.add(item)
    }
  }

  return filtered
}
