/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("storages")

package debop4k.units

import java.io.Serializable

enum class StorageUnit(val abbr: String, val factor: Long) {

  BYTE("B", 1),
  KILO_BYTE("KB", StorageUnit.KILO_FACTOR),
  MEGA_BYTE("MB", StorageUnit.MEGA_FACTOR),
  GIGA_BYTE("GB", StorageUnit.GIGA_FACTOR),
  TERA_BYTE("TB", StorageUnit.TERA_FACTOR),
  PETA_BYTE("PB", StorageUnit.PETA_FACTOR),
  EXA_BYTE("XB", StorageUnit.EXA_FACTOR);

  companion object {
    fun valueOf(str: String): StorageUnit = TODO("구현 중")

    val KILO_FACTOR: Long = 1L shl 10
    val MEGA_FACTOR: Long = 1L shl 20
    val GIGA_FACTOR: Long = 1L shl 30
    val TERA_FACTOR: Long = 1L shl 40
    val PETA_FACTOR: Long = 1L shl 50
    val EXA_FACTOR: Long = 1L shl 60
  }
}

//public data class Storage(val bytes: Long) : Comparable<Storage> {
//  override fun compareTo(other: Storage): Int = if (bytes > other.bytes) 1 else -1
//
//  public operator fun plus(that: Storage): Storage = copy(bytes + that.bytes)
//  public operator fun plus(scala: Long): Storage = copy(bytes + scala)
//}

open class Storage(val bytes: Long = 0) : Comparable<Storage>, Serializable {

  operator fun plus(that: Storage): Storage = Storage(bytes + that.bytes)
  operator fun plus(scala: Long): Storage = Storage(bytes + scala)
  operator fun minus(that: Storage): Storage = Storage(bytes - that.bytes)
  operator fun minus(scala: Long): Storage = Storage(bytes - scala)

  operator fun times(that: Storage): Storage = Storage(bytes * that.bytes)
  operator fun times(scala: Long): Storage = Storage(bytes * scala)

  operator fun div(that: Storage): Storage = Storage(bytes / that.bytes)
  operator fun div(scala: Long): Storage = Storage(bytes / scala)

  override fun compareTo(other: Storage): Int = if (bytes > other.bytes) 1 else -1

  override fun equals(other: Any?): Boolean {
    return other is Storage && bytes == other.bytes
  }

  override fun hashCode(): Int {
    return bytes.hashCode()
  }

  override fun toString(): String {
    return "$bytes.${StorageUnit.BYTE.abbr}"
  }

  fun toHuman(): String = TODO()

  companion object {

    val ZERO = Storage(0)
    val MAX_VALUE = Storage(Long.MAX_VALUE)
    val MIN_VALUE = Storage(Long.MIN_VALUE)

  }
}