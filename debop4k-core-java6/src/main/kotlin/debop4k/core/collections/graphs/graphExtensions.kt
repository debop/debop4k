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