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
@file:JvmName("converters")
@file:Suppress("CAST_NEVER_SUCCEEDS")

package debop4k.core.conversions

import debop4k.core.*
import debop4k.core.collections.fastListOf
import debop4k.core.collections.unifiedMapOf
import debop4k.core.kodatimes.toISOString
import org.joda.time.DateTime
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.util.*

object TypeConversionConfig {
  @Volatile var permiteEnumToEnum: Boolean = false

  @Volatile var defaultConverter = TypeConverters().with {
    register(primitiveConversionPredicate, primitiveConversion)
    val services = ServiceLoader.load(SelfRegisteringConverters::class.java)
    services.forEach { it.registerInfo(this) }
  }
}

interface SelfRegisteringConverters {
  fun registerInfo(conversion: TypeConverters)
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class TypeConverters(val parent: TypeConverters? = null) {

  private val specialConverters = fastListOf<AskToConverter>()
  private val exactConvertersMap = unifiedMapOf<Pair<Type, Type>, ExactConverter>()

  @Suppress("UNCHECKED_CAST")
  fun <T : Any, R : Any> register(fromType: Type, toType: Type, converter: ExactConverter.(T) -> R) {
    val ec = ExactConverter(fromType, toType, converter as ExactConverter.(Any) -> Any)
    exactConvertersMap.put(ec.key, ec)
  }

  fun <T : X, X : Any, R : Any> register(fromType: TypeReference<T>,
                                         toType: TypeReference<R>,
                                         converter: ExactConverter.(X) -> R) {
    register(fromType.type, toType.type, converter)
  }

  inline fun <reified T : Any, reified R : Any> register(noinline converter: ExactConverter.(T) -> R) {
    register(fullType<T>(), fullType<R>(), converter)
  }

  fun register(ask: (Type, Type) -> Boolean, converter: ExactConverter.(Any) -> Any) {
    specialConverters.add(AskToConverter(ask, converter))
  }

  fun hasConverter(fromType: Type, toType: Type): Boolean {
    val tempParent = parent ?: if (this != TypeConversionConfig.defaultConverter) TypeConversionConfig.defaultConverter else null
    return (tempParent?.findConverter(fromType, toType) ?: findConverter(fromType, toType)) != null
  }

  fun <T : Any, R : Any> hasConverter(fromType: TypeReference<T>, toType: TypeReference<R>): Boolean {
    return hasConverter(fromType.type, toType.type)
  }

  fun findConverter(fromType: Type, toType: Type): ExactConverter? {
    return try {
      val tempParent = parent ?: if (this != TypeConversionConfig.defaultConverter) TypeConversionConfig.defaultConverter else null
      tempParent?.findConverter(fromType, toType) ?: exactConvertersMap.getOrPut(Pair(fromType, toType)) {
        val askConverter = specialConverters.firstOrNull { it.ask(fromType, toType) } ?: throw IllegalStateException()
        ExactConverter(fromType, toType, askConverter.converter)
      }
    } catch(ex: IllegalStateException) {
      null
    }
  }

  data class ExactConverter(val fromType: Type,
                            val toType: Type,
                            val converter: ExactConverter.(Any) -> Any) {
    val key = Pair(fromType, toType)
  }

  data class AskToConverter(val ask: (Type, Type) -> Boolean,
                            val converter: ExactConverter.(Any) -> Any)
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST")
private val primitiveConversion = fun TypeConverters.ExactConverter.(value: Any): Any = when (value) {

  is String -> when (toType) {
    String::class.java, CharSequence::class.java -> value
    Char::class.java, java.lang.Character::class.java -> value.toShort().toChar()
    Byte::class.java, java.lang.Byte::class.java -> value.toShort().toByte()
    Short::class.java, java.lang.Short::class.java -> value.toShort()
    Int::class.java, java.lang.Integer::class.java -> value.toInt()
    Long::class.java, java.lang.Long::class.java -> value.toLong()
    Float::class.java, java.lang.Float::class.java -> value.toFloat()
    Double::class.java, java.lang.Double::class.java -> value.toDouble()
    Boolean::class.java, java.lang.Boolean::class.java -> value.toBoolean()
    BigDecimal::class.java -> BigDecimal(value)
    BigInteger::class.java -> BigInteger(value)
    ByteArray::class.java -> value.toByteArray()
    File::class.java -> File(value)
    URL::class.java -> URL(value)
    URI::class.java -> URI(value)
    else -> {
      val toErased = toType.erasedType()
      if (toErased.isEnum) {
        val ecls = toErased as Class<Enum<*>>
        ecls.enumConstants.filter { it.name == value }.first() ?: IllegalStateException("Unknown Enum conversion from ${fromType} to ${toType}, no matching value: $value")
      } else {
        throw IllegalStateException("Unknown conversion from $fromType to $toType")
      }
    }
  }
  is CharSequence -> when (toType) {
    CharSequence::class.java -> value
    String::class.java -> value.toString()
    ByteArray::class.java -> value.toString().toByteArray()
    else -> throw IllegalStateException("Unknown CharSequence conversion from $fromType to $toType")
  }

  is Number -> when (toType) {
    Char::class.java, java.lang.Character::class.java -> value.toShort().toChar()
    Byte::class.java, java.lang.Byte::class.java -> value.toShort().toByte()
    Short::class.java, java.lang.Short::class.java -> value.toShort()
    Int::class.java, java.lang.Integer::class.java -> value.toInt()
    Long::class.java, java.lang.Long::class.java -> value.toLong()
    Float::class.java, java.lang.Float::class.java -> value.toFloat()
    Double::class.java, java.lang.Double::class.java -> value.toDouble()
    BigDecimal::class.java -> BigDecimal(value.toDouble())
    BigInteger::class.java -> BigInteger(value.toString())
    String::class.java -> value.toString()
    Boolean::class.java, java.lang.Boolean::class.java -> value != 0
    Date::class.java -> Date(value.toLong())
    DateTime::class.java -> DateTime(value.toLong())
    else -> {
      val toErased = toType.erasedType()
      if (toErased.isEnum) {
        val ecls = toErased as Class<Enum<*>>
        ecls.enumConstants.filter { it.name == value }.first() ?: IllegalStateException("Unknown Enum conversion from ${fromType} to ${toType}, no matching value: $value")
      } else {
        throw IllegalStateException("Unknown conversion from $fromType to $toType")
      }
    }
  }

  is Char -> when (toType) {
    Char::class.java, java.lang.Character::class.java -> value.toShort().toChar()
    Byte::class.java, java.lang.Byte::class.java -> value.toShort().toByte()
    Short::class.java, java.lang.Short::class.java -> value.toShort()
    Int::class.java, java.lang.Integer::class.java -> value.toInt()
    Long::class.java, java.lang.Long::class.java -> value.toLong()
    Float::class.java, java.lang.Float::class.java -> value.toFloat()
    Double::class.java, java.lang.Double::class.java -> value.toDouble()
    BigDecimal::class.java -> BigDecimal(value.toDouble())
    BigInteger::class.java -> BigInteger(value.toString())
    String::class.java -> value.toString()
    Boolean::class.java, java.lang.Boolean::class.java -> value == '1' || value == 'T' || value == 't'
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  is Boolean -> when (toType) {
    Boolean::class.java, java.lang.Boolean::class.java -> value
    Char::class.java, java.lang.Character::class.java -> if (value) 'T' else 'F'
    Byte::class.java, java.lang.Byte::class.java -> (if (value) 1 else 0).toByte()
    Short::class.java, java.lang.Short::class.java -> (if (value) 1 else 0).toShort()
    Int::class.java, java.lang.Integer::class.java -> (if (value) 1 else 0).toInt()
    Long::class.java, java.lang.Long::class.java -> (if (value) 1 else 0).toLong()
    Float::class.java, java.lang.Float::class.java -> (if (value) 1 else 0).toFloat()
    Double::class.java, java.lang.Double::class.java -> (if (value) 1 else 0).toDouble()
    BigDecimal::class.java -> BigDecimal((if (value) 1 else 0))
    BigInteger::class.java -> BigInteger((if (value) 1 else 0).toString())
    String::class.java -> value.toString()

    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  is Date -> when (toType) {
    Date::class.java -> value
    DateTime::class.java -> DateTime(value.time)
    Int::class.java, java.lang.Integer::class.java -> value.time.toInt()
    Long::class.java, java.lang.Long::class.java -> value.time
    String::class.java -> DateTime(value.time).toISOString()
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }
  is DateTime -> when (toType) {
    DateTime::class.java -> value
    Date::class.java -> value.toDate()
    Int::class.java, java.lang.Integer::class.java -> value.millis.toInt()
    Long::class.java, java.lang.Long::class.java -> value.millis
    String::class.java -> value.toISOString()
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  is ByteArray -> when (toType) {
    ByteArray::class.java -> value
    String::class.java -> value.toString(Charsets.UTF_8)
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  is Enum<*> -> when (toType) {
    String::class.java -> value.name
    Byte::class.java, java.lang.Byte::class.java,
    Int::class.java, java.lang.Integer::class.java,
    Long::class.java, java.lang.Long::class.java -> value.ordinal.toByte()
    else -> {
      val toErased = toType.erasedType()
      if (toErased.isEnum) {
        val ecls = toErased as Class<Enum<*>>
        ecls.enumConstants.filter { it.name == value }.first() ?: IllegalStateException("Unknown Enum conversion from ${fromType} to ${toType}, no matching value: $value")
      } else {
        throw IllegalStateException("Unknown conversion from $fromType to $toType")
      }
    }
  }

  is File -> when (toType) {
    File::class.java -> value
    String::class.java -> value.absolutePath
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  is URL -> when (toType) {
    URL::class.java -> value
    String::class.java -> value.toString()
    URI::class.java -> value.toURI()
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  is URI -> when (toType) {
    URI::class.java -> value
    String::class.java -> value.toString()
    URL::class.java -> value.toURL()
    else -> throw IllegalStateException("Unknown conversion from $fromType to $toType")
  }

  else -> throw IllegalArgumentException("No primitive conversion for $fromType to $toType for value $value")

}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private val primitiveConversionPredicate = fun(fromType: Type, toType: Type): Boolean {

  val fromErased = fromType.erasedType()

  return when {
    String::class.isAssignableFrom(fromErased) -> when (toType) {
      String::class.java,
      Short::class.java, java.lang.Short::class.java,
      Byte::class.java, java.lang.Byte::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java,
      Float::class.java, java.lang.Float::class.java,
      Double::class.java, java.lang.Double::class.java,
      BigDecimal::class.java,
      BigInteger::class.java,
      CharSequence::class.java,
      ByteArray::class.java,
      Boolean::class.java, java.lang.Boolean::class.java,
      File::class.java,
      URL::class.java,
      URI::class.java -> true

      else -> toType.erasedType().isEnum
    }

    CharSequence::class.isAssignableFrom(fromErased) -> when (toType) {
      CharSequence::class.java,
      String::class.java,
      ByteArray::class.java -> true
      else -> false
    }

    Number::class.isAssignableFrom(fromErased) -> when (toType) {
      Char::class.java, java.lang.Character::class.java,
      Byte::class.java, java.lang.Byte::class.java,
      Short::class.java, java.lang.Short::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java,
      Float::class.java, java.lang.Float::class.java,
      Double::class.java, java.lang.Double::class.java,
      BigDecimal::class.java,
      BigInteger::class.java,
      String::class.java,
      Boolean::class.java, java.lang.Boolean::class.java,
      Date::class.java,
      DateTime::class.java -> true
      else -> toType.erasedType().isEnum
    }

    Char::class.isAssignableFrom(fromErased) || java.lang.Character::class.isAssignableFrom(fromErased) -> when (toType) {
      Char::class.java, java.lang.Character::class.java,
      Byte::class.java, java.lang.Byte::class.java,
      Short::class.java, java.lang.Short::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java,
      Float::class.java, java.lang.Float::class.java,
      Double::class.java, java.lang.Double::class.java,
      BigDecimal::class.java,
      BigInteger::class.java,
      String::class.java,
      Boolean::class.java, java.lang.Boolean::class.java -> true
      else -> false
    }

    Boolean::class.java.isAssignableFrom(fromErased) || java.lang.Boolean::class.isAssignableFrom(fromErased) -> when (toType) {
      Boolean::class.java, java.lang.Boolean::class.java,
      Char::class.java, java.lang.Character::class.java,
      Byte::class.java, java.lang.Byte::class.java,
      Short::class.java, java.lang.Short::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java,
      Float::class.java, java.lang.Float::class.java,
      Double::class.java, java.lang.Double::class.java,
      BigDecimal::class.java,
      BigInteger::class.java,
      String::class.java -> true
      else -> false
    }

    Date::class.java.isAssignableFrom(fromErased) -> when (toType) {
      Date::class.java,
      DateTime::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java,
      String::class.java -> true
      else -> false
    }

    DateTime::class.java.isAssignableFrom(fromErased) -> when (toType) {
      Date::class.java,
      DateTime::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java,
      String::class.java -> true
      else -> false
    }

    fromErased == ByteArray::class.java -> when (toType) {
      ByteArray::class.java, String::class.java -> true
      else -> false
    }

    Enum::class.isAssignableFrom(fromErased) -> when (toType) {
      String::class.java,
      Byte::class.java,
      Int::class.java, java.lang.Integer::class.java,
      Long::class.java, java.lang.Long::class.java -> true
      else -> {
        val toErased = toType.erasedType()
        if (toErased.isEnum && TypeConversionConfig.permiteEnumToEnum) {
          true
        } else {
          false
        }
      }
    }

    fromErased == File::class.java -> when (toType) {
      File::class.java,
      String::class.java -> true
      else -> false
    }

    fromErased == URI::class.java || fromErased == URL::class.java -> when (toType) {
      URI::class.java,
      URL::class.java,
      String::class.java -> true
      else -> false
    }

    else -> false

  }
}

