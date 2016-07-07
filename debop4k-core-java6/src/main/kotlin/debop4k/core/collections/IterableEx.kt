package debop4k.core.collections

import debop4k.core.asByte
import debop4k.core.asInt
import debop4k.core.asLong

fun Iterable<*>.size(): Int {
  if (this is Collection<*>) {
    return this.size
  } else {
    var count = 0
    this.forEach { count++ }
    return count
  }
}

fun Iterable<*>.asByteArray(): ByteArray {
  val array = ByteArray(this.size())
  for ((index, item) in this.withIndex()) {
    array[index] = item.asByte()
  }
  return array
}

fun Iterable<*>.asIntArray(): IntArray {
  val array = IntArray(this.size())
  for ((index, item) in this.withIndex()) {
    array[index] = item.asInt()
  }
  return array
}

fun Iterable<*>.asLongArray(): LongArray {
  val array = LongArray(this.size())
  for ((index, item) in this.withIndex()) {
    array[index] = item.asLong()
  }
  return array
}