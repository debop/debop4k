@file:JvmName("HashEx")

package debop4k.core.utils

const val ZERO_HASN = 0
const val DEFAULT_HASH = 1
const val HASH_FACTOR = 31

fun Any?.hash(): Int = this?.hashCode() ?: 0

private fun computeHashInternal(x: Any?): Int = x?.hashCode() ?: ZERO_HASN

fun hashOf(vararg objs: Any?): Int {

  if (objs.isEmpty()) return ZERO_HASN

  var hash = DEFAULT_HASH
  var i = 0
  objs.forEach { obj ->
    when (obj) {
      is Enum<*> -> hash = hash * HASH_FACTOR + obj.ordinal.hashCode()
      else -> hash = hash * HASH_FACTOR + computeHashInternal(obj)
    }
  }
  return hash
}

fun hashOf(x1: Any?, x2: Any?): Int {
  if (x1 is Number && x2 is Number) {
    return hashOf(x1, x2, x2, x1, x1, x2)
  }
  var hash = DEFAULT_HASH
  hash = hash * HASH_FACTOR + computeHashInternal(x1)
  hash = hash * HASH_FACTOR + computeHashInternal(x2)
  return hash
}

fun hashOf(x1: Any?, x2: Any?, x3: Any?): Int {
  if (x1 is Number && x2 is Number && x3 is Number) {
    return hashOf(x1, x2, x3, x3, x2, x1, x1, x2, x3)
  }
  var hash = DEFAULT_HASH
  hash = hash * HASH_FACTOR + computeHashInternal(x1)
  hash = hash * HASH_FACTOR + computeHashInternal(x2)
  hash = hash * HASH_FACTOR + computeHashInternal(x3)
  return hash
}