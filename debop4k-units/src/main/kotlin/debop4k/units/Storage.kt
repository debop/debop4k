@file:JvmName("storages")

package debop4k.units

import java.io.Serializable

fun Int.toBytes(): Storage = Storage(this.toDouble())
fun Int.toKBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.KBYTE)
fun Int.toMBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.MBYTE)
fun Int.toGBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.GBYTE)
fun Int.toTBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.TBYTE)
fun Int.toPBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.PBYTE)
fun Int.toXBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.XBYTE)
fun Int.toZBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.ZBYTE)
fun Int.toYBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.YBYTE)


fun Long.toBytes(): Storage = Storage(this.toDouble())
fun Long.toKBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.KBYTE)
fun Long.toMBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.MBYTE)
fun Long.toGBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.GBYTE)
fun Long.toTBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.TBYTE)
fun Long.toPBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.PBYTE)
fun Long.toXBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.XBYTE)
fun Long.toZBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.ZBYTE)
fun Long.toYBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.YBYTE)


fun Double.toBytes(): Storage = Storage(this)
fun Double.toKBytes(): Storage = Storage.of(this, StorageUnit.KBYTE)
fun Double.toMBytes(): Storage = Storage.of(this, StorageUnit.MBYTE)
fun Double.toGBytes(): Storage = Storage.of(this, StorageUnit.GBYTE)
fun Double.toTBytes(): Storage = Storage.of(this, StorageUnit.TBYTE)
fun Double.toPBytes(): Storage = Storage.of(this, StorageUnit.PBYTE)
fun Double.toXBytes(): Storage = Storage.of(this, StorageUnit.XBYTE)
fun Double.toZBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.ZBYTE)
fun Double.toYBytes(): Storage = Storage.of(this.toDouble(), StorageUnit.YBYTE)


/**
 * 저장 단위 (Bytes) 종류
 */
enum class StorageUnit(val unitName: String, val factor: Double) {

  BYTE("B", 1.0),
  KBYTE("KB", 1024.0),
  MBYTE("MB", Math.pow(1024.0, 2.0)),
  GBYTE("GB", Math.pow(1024.0, 3.0)),
  TBYTE("TB", Math.pow(1024.0, 4.0)),
  PBYTE("PB", Math.pow(1024.0, 5.0)),
  XBYTE("XB", Math.pow(1024.0, 6.0)),
  ZBYTE("ZB", Math.pow(1024.0, 7.0)),
  YBYTE("YB", Math.pow(1024.0, 8.0));

  companion object {

    @JvmStatic
    fun parse(unitStr: String): StorageUnit {
      var upper = unitStr.toUpperCase()
      if (upper.endsWith("s")) {
        upper = upper.dropLast(1)
      }
      return StorageUnit.values().find { it.unitName == upper }
          ?: throw NumberFormatException("Unknown Storage unit. unit=$unitStr")
    }
  }
}

/**
 * 저장 단위 (Bytes) 를 나타내는 클래스
 */
data class Storage(val bytes: Double = 0.0) : Comparable<Storage>, Serializable {

  final operator fun plus(that: Storage): Storage = Storage(bytes + that.bytes)
  final operator fun plus(scalar: Long): Storage = Storage(bytes + scalar)
  final operator fun minus(that: Storage): Storage = Storage(bytes - that.bytes)
  final operator fun minus(scalar: Long): Storage = Storage(bytes - scalar)

  final operator fun times(that: Storage): Storage = Storage(bytes * that.bytes)
  final operator fun times(scalar: Long): Storage = Storage(bytes * scalar)
  final operator fun times(scalar: Int): Storage = Storage(bytes * scalar)
  final operator fun times(scalar: Float): Storage = Storage(bytes * scalar)
  final operator fun times(scalar: Double): Storage = Storage(bytes * scalar)

  final operator fun div(that: Storage): Storage = Storage(bytes / that.bytes)
  final operator fun div(scalar: Long): Storage = Storage(bytes / scalar)
  final operator fun div(scalar: Int): Storage = Storage(bytes / scalar)
  final operator fun div(scalar: Float): Storage = Storage(bytes / scalar)
  final operator fun div(scalar: Double): Storage = Storage(bytes / scalar)

  final operator fun unaryMinus(): Storage = Storage(-bytes)

  fun inBytes() = bytes
  fun inKBytes() = bytes / StorageUnit.KBYTE.factor
  fun inMBytes() = bytes / StorageUnit.MBYTE.factor
  fun inGBytes() = bytes / StorageUnit.GBYTE.factor
  fun inTBytes() = bytes / StorageUnit.TBYTE.factor
  fun inPBytes() = bytes / StorageUnit.PBYTE.factor
  fun inXBytes() = bytes / StorageUnit.XBYTE.factor
  fun inZBytes() = bytes / StorageUnit.ZBYTE.factor
  fun inYBytes() = bytes / StorageUnit.YBYTE.factor

  override fun compareTo(other: Storage): Int = bytes.compareTo(other.bytes)
  override fun toString(): String = "$bytes ${StorageUnit.BYTE.unitName}"

  fun toHuman(): String {
    var display = Math.abs(bytes)
    var order = 0

    while (display >= 1024.0) {
      order++
      display /= StorageUnit.KBYTE.factor
    }

    return if (order == 0)
      "%d %s".format(bytes.toLong(), StorageUnit.BYTE.unitName)
    else
      "%.1f %s".format(Math.signum(bytes.toDouble()) * display, StorageUnit.values()[order].unitName)
  }

  companion object {

    final val ZERO = Storage(0.0)
    final val MAX_VALUE = Storage(Double.MAX_VALUE)
    final val MIN_VALUE = Storage(Double.MIN_VALUE)
    final val POSITIVE_INF = Storage(Double.POSITIVE_INFINITY)
    final val NEGATIVE_INF = Storage(Double.NEGATIVE_INFINITY)
    final val NaN = Storage(Double.NaN)

    @JvmStatic
    final fun of(value: Double = 0.0, unit: StorageUnit = StorageUnit.BYTE): Storage =
        Storage(value * unit.factor)

    @JvmStatic
    fun parse(storageStr: String): Storage {
      if (storageStr.isBlank())
        return Storage.ZERO

      try {
        val (bytes, unit) = storageStr.split(" ", limit = 2)
        return of(bytes.toDouble(), StorageUnit.parse(unit))

      } catch(e: Exception) {
        throw NumberFormatException("Invalid Storage string. storageStr=$storageStr")
      }
    }
  }
}
