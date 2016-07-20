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

import org.apache.commons.codec.binary.Hex
import org.springframework.util.StringUtils

const val EMPTY_STRING = ""
const val NULL_STRING = "<null>"
const val TAB: String = "\t"
const val COMMA: String = ","
val LINE_SEPARATOR: String by lazy { System.getProperty("line.separator") }


public fun CharSequence.isEmpty(): Boolean = trim().length == 0


fun CharSequence?.isNull(): Boolean = this == null

fun CharSequence?.isNullOrEmpty(): Boolean = this == null || trim().length == 0

fun CharSequence?.nonEmpty(): Boolean = this != null && trim().length > 0

fun CharSequence?.hasText(): Boolean = this != null && trim().length > 0

val CharSequence.lastChar: Char
  get() = if (this.isEmpty()) 0.toChar() else this.get(length - 1)

fun ByteArray.toUtf8String(): String = this.toString(Charsets.UTF_8)

// Hex String
fun ByteArray.toHexString(): String = Hex.encodeHexString(this)

fun String.fromHexString(): ByteArray = Hex.decodeHex(this.toCharArray())

fun String.dropLast(count: Int = 1): String = this.substring(0, this.length - count)
fun String.dropFirst(count: Int = 1): String = this.substring(count)
fun String.takeLast(length: Int): String = this.substring(this.length - length)
fun String.takeFirst(length: Int): String = this.substring(0, length - 1)


fun String.prefixWith(prefix: String): String
    = if (this.startsWith(prefix)) this else prefix + this

fun String.prefixWith(prefix: Char): String
    = if (this.startsWith(prefix)) this else prefix + this

fun String.notPrefixWith(prefix: String): String
    = if (!this.startsWith(prefix)) this else this.dropFirst(prefix.length)

fun String.notPrefixWith(prefix: Char): String
    = if (!this.startsWith(prefix)) this else this.dropFirst(1)

fun String.postfixWith(postfix: String): String
    = if (this.endsWith(postfix)) this else this + postfix

fun String.postfixWith(postfix: Char): String
    = if (this.endsWith(postfix)) this else this + postfix

fun String.notPostfixWith(postfix: String): String
    = if (!this.endsWith(postfix)) this else this.dropLast(postfix.length)

fun String.notPostfixWith(postfix: Char): String
    = if (this.endsWith(postfix)) this else this.dropLast(1)

inline fun String.whenStartsWith(prefix: String, doWithRest: (String) -> Unit): Boolean {
  if (this.startsWith(prefix)) {
    doWithRest(this.dropFirst(prefix.length))
    return true
  }
  return false
}

inline fun String.whenStartsWith(prefixes: List<String>, doWithRest: (String) -> Unit): Boolean {
  prefixes.forEach { prefix ->
    if (this.whenStartsWith(prefix, doWithRest)) {
      return@whenStartsWith true
    }
  }
  return false
}

inline fun String.whenEndsWith(postfix: String, doWithRest: (String) -> Unit): Boolean {
  if (this.endsWith(postfix)) {
    doWithRest(this.dropLast(postfix.length))
    return true
  }
  return false
}

inline fun String.whenEndsWith(postfixes: List<String>, doWithRest: (String) -> Unit): Boolean {
  postfixes.forEach { postfix ->
    if (this.whenEndsWith(postfix, doWithRest)) {
      return@whenEndsWith true
    }
  }
  return false
}

/**
 * 문자열의 문자들 중 유니크한 문자열로만 필터링해서 문자열로 반환합니다.
 */
fun String.unique(): String {
  val sb = StringBuilder()
  sb.append("a")
  sb.contains('a')

  for (char in this) {
    if (char != ' ' && !sb.contains(char)) {
      sb.append(char)
    }
  }
  return sb.toString()
}

// Spring Core 의 StringUtils

fun String.replace(oldPattern: String, newPattern: String): String
    = StringUtils.replace(this, oldPattern, newPattern)

fun String.tokenizeToStringArray(delimiters: String,
                                 trimToken: Boolean = true,
                                 ignoreEmptyTokens: Boolean = true): Array<String>
    = StringUtils.tokenizeToStringArray(this, delimiters, trimToken, ignoreEmptyTokens)