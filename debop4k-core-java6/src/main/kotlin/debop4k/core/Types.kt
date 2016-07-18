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

import java.lang.reflect.*
import kotlin.reflect.KClass


inline fun <reified T : Any> typeRef(): TypeReference<T> = object : TypeReference<T>() {}
inline fun <reified T : Any> fullType(): TypeReference<T> = object : TypeReference<T>() {}

abstract class TypeReference<T> protected constructor() {

  public val type: Type by lazy {
    javaClass.genericSuperclass.let { superClass ->
      if (superClass is Class<*>) {
        throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")
      }
      (superClass as ParameterizedType).actualTypeArguments[0]
    }
  }

  public val forClass: Class<Any> by lazy { type.erasedType() }
}

@Suppress("UNCHECKED_CAST")
fun Type.erasedType(): Class<Any> = when (this) {
  is Class<*> -> this as Class<Any>
  is ParameterizedType -> this.rawType.erasedType()
  is GenericArrayType -> {
    val elementType = this.genericComponentType.erasedType()
    val testArray = java.lang.reflect.Array.newInstance(elementType, 0)
    testArray.javaClass
  }
  is TypeVariable<*> -> throw IllegalStateException("지원되지 않는 수형입니다.")
  is WildcardType -> this.upperBounds[0].erasedType()
  else -> throw IllegalStateException("지원되지 않는 수형힙니다.")
}

fun <T : Any> KClass<T>.isAssignableFrom(other: Type): Boolean =
    if (this.java == other) true
    else this.java.isAssignableFrom(other.erasedType())

fun Class<*>.isAssignableFrom(other: Type): Boolean =
    if (this == other) true
    else this.isAssignableFrom(other.erasedType())

fun <T : Any> Class<*>.isAssignableFrom(other: KClass<T>): Boolean =
    if (this == other.java) true
    else this.isAssignableFrom(other.java)

fun <T : Any> KClass<T>.isAssignableFrom(other: Class<*>): Boolean =
    if (this.java == other) true
    else this.java.isAssignableFrom(other)


fun <T : Any, O : Any> KClass<T>.isAssignableFrom(other: KClass<O>): Boolean =
    if (this.java == other.java) true
    else this.java.isAssignableFrom(other.java)


fun Type.isAssignableFrom(other: Type): Boolean =
    if (this == other) true
    else this.erasedType().isAssignableFrom(other.erasedType())

fun Type.isAssignableFrom(other: Class<*>): Boolean =
    if (this == other) true
    else this.erasedType().isAssignableFrom(other)

fun Type.isAssignableFrom(other: KClass<*>): Boolean =
    this.erasedType().isAssignableFrom(other.java)
