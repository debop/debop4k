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
@file:JvmName("Convertx")

package debop4k.core.utils

import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@JvmField val SIMPLE_DATE_FORMAT = SimpleDateFormat()

fun Boolean?.asBoolean(): Boolean = this ?: false

/**
 * Character 를 primitive char 로 변환한다.
 */
fun Char?.asChar(): Char = this ?: '\u0000'

/**
 * Byte 수형을 byte 수형으로 변환합니다.
 */
fun Byte?.asByte(): Byte = this ?: 0

/**
 * Short 수형을 short 수형으로 변환합니다.
 */
fun Short?.asShort(): Short = this ?: 0

/**
 * Integer 수형을 int 수형으로 변환합니다.
 */
fun Int?.asInt(): Int = this ?: 0

/**
 * Long 수형을 long 수형으로 변환합니다.
 */
fun Long?.asLong(): Long = this ?: 0L

/**
 * Float 수형을 float 수형으로 변환합니다.
 */
fun Float?.asFloat(): Float = this ?: 0.0f

/**
 * Double 수형을 double 수형으로 변환합니다.
 */
fun Double?.asDouble(): Double = this ?: 0.0

@JvmOverloads
fun Any?.asChar(dv: Char = 0.toChar()): Char = when (this) {
  null    -> dv
  is Char -> this
//  is Number -> this.toChar()
//  is String -> this[0]
  else    -> try {
    this.asByte().toChar()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asByte(dv: Byte = 0): Byte = when (this) {
  null      -> dv
  is Char   -> this.toByte()
  is Number -> this.toByte()
//  is String -> this.toByte()
  else      -> try {
    this.asDouble(dv.toDouble()).toByte()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asShort(dv: Short = 0): Short = when (this) {
  null      -> dv
  is Short  -> this
  is Char   -> this.toShort()
  is Number -> this.toShort()
//  is String -> this.toShort()
  else      -> try {
    this.asDouble(dv.toDouble()).toShort()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asInt(dv: Int = 0): Int = when (this) {
  null      -> dv
  is Int    -> this
  is Char   -> this.toInt()
  is Number -> this.toInt()
//  is String -> this.toInt()
  else      -> try {
    this.asDouble(dv.toDouble()).toInt()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asLong(dv: Long = 0L): Long {

  return when (this) {
    null      -> dv
    is Long   -> this
    is Char   -> this.toLong()
    is Number -> this.toLong()
//    is String -> this.toLong()
    else      -> try {
      this.asDouble(dv.toDouble()).toLong()
    } catch(ignored: Exception) {
      dv
    }
  }
}

@JvmOverloads
fun Any?.asFloat(dv: Float = 0.0F): Float = when (this) {
  null      -> dv
  is Number -> this.toFloat()
//  is String -> this.toFloat()
  else      -> try {
    this.asDouble(dv.toDouble()).toFloat()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asDouble(dv: Double = 0.0): Double = when (this) {
  is Number -> this.toDouble()
//  is String -> this.toDouble()
//  is Char -> this.toDouble()
  else      -> {
    try {
      this.toString().toDouble()
    } catch(ignored: Exception) {
      dv
    }
  }
}

@JvmOverloads
fun Any?.asBigDecimal(dv: BigDecimal = BigDecimal.ZERO): BigDecimal = when (this) {
  null          -> dv
  is BigDecimal -> this
  is Number     -> BigDecimal.valueOf(this.toDouble())
  else          -> try {
    BigDecimal.valueOf(this.asDouble(dv.toDouble()))
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asBigInt(dv: BigInteger = BigInteger.ZERO): BigInteger = when (this) {
  null          -> dv
  is BigInteger -> this
  is Number     -> BigInteger.valueOf(this.toLong())
  else          -> try {
    BigInteger.valueOf(this.asLong(dv.toLong()))
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asString(dv: String = ""): String = this?.toString() ?: dv

@JvmOverloads
fun Any?.asDateTime(dv: DateTime? = null): DateTime? = when (this) {
  null         -> dv
  is Number    -> DateTime(this.toLong())
  is Date      -> DateTime(this.time)
  is Timestamp -> DateTime(this.time)
  is DateTime  -> this
  else         -> try {
    DateTime.parse(this.toString())
  } catch(ignored: Exception) {
    dv
  }
}

fun Any?.asDateTime(pattern: String): DateTime? {
  if (this == null)
    return null

  try {
    val formatter = DateTimeFormat.forPattern(pattern)
    return formatter.parseDateTime(this.asString())
  } catch(e: Exception) {
    return null
  }
}

@JvmOverloads
fun Any?.asDate(dv: Date = Date(0L)): Date = when (this) {
  null         -> dv
  is Number    -> Date(this.toLong())
  is Date      -> this
  is Timestamp -> this
  is DateTime  -> this.toDate()
  else         -> try {
    SIMPLE_DATE_FORMAT.parse(this.asString())
  } catch(ignored: Exception) {
    dv
  }
}


fun Any?.asClass(): Class<*> = when (this) {
  null            -> throw NullPointerException()
  is Boolean      -> java.lang.Boolean.TYPE
  is Char         -> java.lang.Character.TYPE
  is Byte         -> java.lang.Byte.TYPE
  is Short        -> java.lang.Short.TYPE
  is Int          -> java.lang.Integer.TYPE
  is Long         -> java.lang.Long.TYPE
  is Float        -> java.lang.Float.TYPE
  is Double       -> java.lang.Double.TYPE
  is Number       -> this.javaClass
  is CharSequence -> this.javaClass
  else            -> this.javaClass
}

private val decimalFormats by lazy { ConcurrentHashMap<Int, DecimalFormat>() }

private fun decimalFormatOf(decimalCount: Int): DecimalFormat {
  return decimalFormats.getIfAbsentPut(decimalCount, { count ->
    val df = if (count > 0) DecimalFormat("." + "#".repeat(count))
    else DecimalFormat("#")
    return@getIfAbsentPut df
  })
}

fun Any?.asFloatFloor(decimalCount: Int): Float {
  require(decimalCount >= 0)
  val df = decimalFormatOf(decimalCount)
  return df.format(this.asFloat()).asFloat()

}

fun Any?.asDoubleFloor(decimalCount: Int): Double {
  require(decimalCount >= 0)

  val df = decimalFormatOf(decimalCount)
  return df.format(this.asDouble()).asDouble()
}

fun Any?.asFloatRound(decimalCount: Int): Float {
  require(decimalCount >= 0)
  if (decimalCount == 0)
    return this.asLong().toFloat()

  val decimal = Math.pow(10.0, decimalCount.toDouble())
  return (Math.round(this.asFloat() * decimal) / decimal).toFloat()
}

fun Any?.asDoubleRound(decimalCount: Int): Double {
  require(decimalCount >= 0)
  if (decimalCount == 0)
    return this.asLong().toDouble()

  val decimal = Math.pow(10.0, decimalCount.toDouble())
  return (Math.round(this.asDouble() * decimal) / decimal)
}


