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

@file:JvmName("Storages")

package debop4k.units

import java.io.Serializable

fun Int.bytes(): Storage = Storage(this.toDouble())
fun Int.kilobytes(): Storage = Storage.of(this.toDouble(), StorageUnit.KBYTE)
fun Int.megabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.MBYTE)
fun Int.gigabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.GBYTE)
fun Int.terabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.TBYTE)
fun Int.petabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.PBYTE)
fun Int.exabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.XBYTE)
fun Int.zetabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.ZBYTE)
fun Int.yotabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.YBYTE)

operator fun Int.times(storage: Storage): Storage = storage.times(this)

fun Long.bytes(): Storage = Storage(this.toDouble())
fun Long.kilobytes(): Storage = Storage.of(this.toDouble(), StorageUnit.KBYTE)
fun Long.megabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.MBYTE)
fun Long.gigabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.GBYTE)
fun Long.terabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.TBYTE)
fun Long.petabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.PBYTE)
fun Long.exabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.XBYTE)
fun Long.zetabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.ZBYTE)
fun Long.yotabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.YBYTE)

operator fun Long.times(storage: Storage): Storage = storage.times(this)

fun Double.bytes(): Storage = Storage(this)
fun Double.kilobytes(): Storage = Storage.of(this, StorageUnit.KBYTE)
fun Double.megabytes(): Storage = Storage.of(this, StorageUnit.MBYTE)
fun Double.gigabytes(): Storage = Storage.of(this, StorageUnit.GBYTE)
fun Double.terabytes(): Storage = Storage.of(this, StorageUnit.TBYTE)
fun Double.petabytes(): Storage = Storage.of(this, StorageUnit.PBYTE)
fun Double.exabytes(): Storage = Storage.of(this, StorageUnit.XBYTE)
fun Double.zetabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.ZBYTE)
fun Double.yotabytes(): Storage = Storage.of(this.toDouble(), StorageUnit.YBYTE)

operator fun Double.times(storage: Storage): Storage = storage.times(this)


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

  operator fun plus(that: Storage): Storage = Storage(bytes + that.bytes)
  operator fun plus(scalar: Long): Storage = Storage(bytes + scalar)
  operator fun minus(that: Storage): Storage = Storage(bytes - that.bytes)
  operator fun minus(scalar: Long): Storage = Storage(bytes - scalar)

  operator fun times(that: Storage): Storage = Storage(bytes * that.bytes)
  operator fun times(scalar: Long): Storage = Storage(bytes * scalar)
  operator fun times(scalar: Int): Storage = Storage(bytes * scalar)
  operator fun times(scalar: Float): Storage = Storage(bytes * scalar)
  operator fun times(scalar: Double): Storage = Storage(bytes * scalar)

  operator fun div(that: Storage): Storage = Storage(bytes / that.bytes)
  operator fun div(scalar: Long): Storage = Storage(bytes / scalar)
  operator fun div(scalar: Int): Storage = Storage(bytes / scalar)
  operator fun div(scalar: Float): Storage = Storage(bytes / scalar)
  operator fun div(scalar: Double): Storage = Storage(bytes / scalar)

  operator fun unaryMinus(): Storage = Storage(-bytes)

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

    @JvmField val ZERO = Storage(0.0)
    @JvmField val MAX_VALUE = Storage(Double.MAX_VALUE)
    @JvmField val MIN_VALUE = Storage(Double.MIN_VALUE)
    @JvmField val POSITIVE_INF = Storage(Double.POSITIVE_INFINITY)
    @JvmField val NEGATIVE_INF = Storage(Double.NEGATIVE_INFINITY)
    @JvmField val NaN = Storage(Double.NaN)

    @JvmOverloads
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
