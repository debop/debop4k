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

import java.math.BigInteger

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
fun Any?.asByte(dv: Byte = 0): Byte {
  if (this == null) return dv

  return when (this) {
    is Number -> this.toByte()
    is String -> this.toByte()
    else -> try {
      this.asDouble(dv.toDouble()).toByte()
    } catch(ignored: Exception) {
      dv
    }
  }
}

@JvmOverloads
fun Any?.asInt(dv: Int = 0): Int {
  if (this == null) return dv

  return when (this) {
    is Number -> this.toInt()
    is String -> this.toInt()
    else -> try {
      this.asDouble(dv.toDouble()).toInt()
    } catch(ignored: Exception) {
      dv
    }
  }
}

@JvmOverloads
fun Any?.asLong(dv: Long = 0L): Long {
  if (this == null) return dv

  return when (this) {
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
fun Any?.asDouble(dv: Double = 0.0): Double {
  if (this == null) return dv

  return when (this) {
    is Number -> this.toDouble()
    is String -> this.toDouble()
    is Char -> this.toDouble()
    else -> {
      return try {
        this.toString().toDouble()
      } catch(ignored: Exception) {
        dv
      }
    }
  }
}

@JvmOverloads
fun Any?.asBigInt(dv: BigInteger = BigInteger.ZERO): BigInteger {
  if (this == null) return dv

  return when (this) {
    is BigInteger -> this
    is Number -> BigInteger.valueOf(this.toLong())
    else -> try {
      BigInteger.valueOf(this.asLong(dv.toLong()))
    } catch(ignored: Exception) {
      dv
    }
  }
}
