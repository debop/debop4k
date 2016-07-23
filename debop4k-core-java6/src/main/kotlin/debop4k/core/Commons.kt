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

package debop4k.core

import debop4k.core.kodatimes.UnixEpoch
import org.joda.time.DateTime
import java.math.BigInteger
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

private val SIMPLE_DATE_FORMAT by lazy { SimpleDateFormat() }

/**
 * var 로 선언된 필드 중 non null 수형에 대해 초기화 값을 지정하고자 할 때 사용합니다.
 * 특히 ```@Autowired```, ```@Inject``` var 수형에 사용하기 좋다.
 * @see lateinit
 * @see Delegates.nonNull
 */
fun <T> uninitialized(): T = null as T

fun areEquals(a: Any?, b: Any?): Boolean {
  return (a === b) || (a != null && a == b)
}

infix inline fun <T> T.initializedBy(initializer: (T) -> Unit): T {
  initializer(this)
  return this
}

infix inline fun <T> T.initializeWith(initialize: T.() -> Unit): T {
  this.initialize()
  return this
}

infix inline fun <T> T.with(block: T.() -> Unit): T {
  this.block()
  return this
}

infix inline fun <T : Any, R : Any> T?.whenNotNull(thenDo: (T) -> R?): R?
    = if (this == null) null else thenDo(this)

infix inline fun <T : Any, R : Any> T?.withNotNull(thenDo: T.() -> R?): R?
    = if (this == null) null else this.thenDo()

fun <T : Any, R : Any> Collection<T?>.whenAllNotNull(block: (Collection<T>) -> R) {
  if (this.all { it != null }) {
    block(this.filterNotNull())
  }
}

fun <T : Any, R : Any> Collection<T?>.whenAnyNotNull(block: (Collection<T>) -> R) {
  if (this.any { it != null }) {
    block(this.filterNotNull())
  }
}


@JvmOverloads
fun Any?.asChar(dv: Char = 0.toChar()): Char = when (this) {
  null -> dv
  is Number -> this.toChar()
  is String -> this[0]
  else -> try {
    this.asByte().toChar()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asByte(dv: Byte = 0): Byte = when (this) {
  null -> dv
  is Number -> this.toByte()
  is String -> this.toByte()
  else -> try {
    this.asDouble(dv.toDouble()).toByte()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asShort(dv: Short = 0): Short = when (this) {
  null -> dv
  is Number -> this.toShort()
  is String -> this.toShort()
  else -> try {
    this.asDouble(dv.toDouble()).toShort()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asInt(dv: Int = 0): Int = when (this) {
  null -> dv
  is Number -> this.toInt()
  is String -> this.toInt()
  else -> try {
    this.asDouble(dv.toDouble()).toInt()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asLong(dv: Long = 0L): Long {

  return when (this) {
    null -> dv
    is Number -> this.toLong()
    is String -> this.toLong()
    else -> try {
      this.asDouble(dv.toDouble()).toLong()
    } catch(ignored: Exception) {
      dv
    }
  }
}

@JvmOverloads
fun Any?.asFloat(dv: Float = 0.0F): Float = when (this) {
  null -> dv
  is Number -> this.toFloat()
  is String -> this.toFloat()
  else -> try {
    this.asDouble(dv.toDouble()).toFloat()
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asDouble(dv: Double = 0.0): Double = when (this) {
  is Number -> this.toDouble()
  is String -> this.toDouble()
  is Char -> this.toDouble()
  else -> {
    try {
      this.toString().toDouble()
    } catch(ignored: Exception) {
      dv
    }
  }
}

@JvmOverloads
fun Any?.asBigInt(dv: BigInteger = BigInteger.ZERO): BigInteger = when (this) {
  is BigInteger -> this
  is Number -> BigInteger.valueOf(this.toLong())
  else -> try {
    BigInteger.valueOf(this.asLong(dv.toLong()))
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asString(dv: String = ""): String = this?.toString() ?: dv

@JvmOverloads
fun Any?.asDateTime(dv: DateTime = UnixEpoch): DateTime = when (this) {
  null -> dv
  is Number -> DateTime(this.toLong())
  is Date -> DateTime(this.time)
  is Timestamp -> DateTime(this.time)
  is DateTime -> this
  is CharSequence -> DateTime.parse(this.toString())
  else -> try {
    DateTime.parse(this.toString())
  } catch(ignored: Exception) {
    dv
  }
}

@JvmOverloads
fun Any?.asDate(dv: Date = Date(0L)): Date = when (this) {
  null -> dv
  is Number -> Date(this.toLong())
  is Date -> this
  is Timestamp -> this
  is DateTime -> this.toDate()
  else -> try {
    SIMPLE_DATE_FORMAT.parse(this.asString())
  } catch(ignored: Exception) {
    dv
  }
}


fun Any?.asClass(): Class<*> = when (this) {
  null -> throw NullPointerException()
  is Boolean -> java.lang.Boolean.TYPE
  is Char -> java.lang.Character.TYPE
  is Byte -> java.lang.Byte.TYPE
  is Short -> java.lang.Short.TYPE
  is Int -> java.lang.Integer.TYPE
  is Long -> java.lang.Long.TYPE
  is Float -> java.lang.Float.TYPE
  is Double -> java.lang.Double.TYPE
  is Number -> this.javaClass
  is CharSequence -> this.javaClass
  else -> this.javaClass
}
