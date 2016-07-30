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

import debop4k.core.collections.emptyByteArray
import org.eclipse.collections.impl.list.mutable.FastList
import org.springframework.util.StringUtils

const val EMPTY_STRING = ""
const val TRIMMING = "..."
const val NULL_STRING = "<null>"
const val TAB: String = "\t"
const val COMMA: String = ","
const val ElipsisLength = 80


fun CharSequence?.isEmpty(): Boolean = this == null || this.trim().length == 0

fun CharSequence?.isNull(): Boolean = this == null

fun CharSequence?.isNullOrEmpty(): Boolean = this == null || trim().length == 0

fun CharSequence?.nonEmpty(): Boolean = this != null && trim().length > 0

fun CharSequence?.hasText(): Boolean = this != null && trim().length > 0

val CharSequence.lastChar: Char
  get() = if (this.isEmpty()) 0.toChar() else this.get(length - 1)

fun ByteArray?.toUtf8String(): String
    = this?.toString(Charsets.UTF_8) ?: EMPTY_STRING

fun String?.toUtf8ByteArray(): ByteArray
    = this?.toByteArray(Charsets.UTF_8) ?: emptyByteArray


fun CharSequence?.words(): List<String> = this?.split(' ') ?: listOf<String>()

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

fun String?.trim(): String {
  return if (this == null) EMPTY_STRING else (this as CharSequence).trim() as String
}

fun String?.needEllipsis(maxLength: Int = ElipsisLength): Boolean {
  return this != null && this.isNotBlank() && this.length > maxLength
}

fun String?.ellipsisEnd(maxLength: Int = ElipsisLength): String {
  if (this == null) {
    return EMPTY_STRING
  }

  return if (!this.needEllipsis(maxLength)) this
  else this.substring(0, maxLength - TRIMMING.length) + TRIMMING
}

fun String?.ellipsisMid(maxLength: Int = ElipsisLength): String {
  if (this == null)
    return EMPTY_STRING

  if (!needEllipsis(maxLength))
    return this

  val length = maxLength / 2
  val sb = StringBuilder()
  sb.append(this.substring(0, length)).append(TRIMMING)

  val len = if (maxLength % 2 == 0) this.length - length
  else this.length - length - 1

  sb.append(this.substring(len))
  return sb.toString()
}

fun String?.ellipsisStart(maxLength: Int = ElipsisLength): String {
  if (this == null)
    return EMPTY_STRING

  return if (!this.needEllipsis(maxLength)) this
  else TRIMMING + this.substring(this.length - maxLength + TRIMMING.length)
}

fun Iterable<Any?>.toStringList(): FastList<String> {
  return FastList.newList(map { it.asString() })
}

@JvmOverloads
fun Iterable<Any?>.mkString(separator: String = COMMA): String {
  return toStringList().makeString(separator)
}

@JvmOverloads
fun Map<Any, Any?>.mkString(separator: String = COMMA): String {
  val sb = StringBuilder(this.size * 4)
  var i = 0

  this.entries.forEach {
    if (i > 0)
      sb.append(separator)
    sb.append(it.key.asString())
        .append("=")
        .append(it.value.asString())
    i++
  }

  return sb.toString()
}

fun String?.splitAt(index: Int): Pair<String, String> {
  if (this == null)
    return Pair<String, String>(EMPTY_STRING, EMPTY_STRING)

  if (this.isBlank() || index <= 0 || index > this.length) {
    return Pair(this, EMPTY_STRING)
  }

  return Pair(this.substring(0, index), this.substring(index + 1))
}

fun String?.splits(separator: Char): FastList<String> {

  val results = FastList.newList<String>()

  if (this == null || this.isEmpty())
    return results

  var startIdx = 0
//  val prevIdx = 0
  val length = this.length

  while (startIdx < length) {
    val idx = this.indexOf(separator, startIdx)
    if (idx < 0) {
      results.add(this.substring(startIdx).trim({ it <= ' ' }))
      break
    }

    results.add(this.substring(startIdx, idx).trim({ it <= ' ' }))
    startIdx = idx + 1
  }
  if (this[length - 1] == separator)
    results.add("")

  return results
}

@JvmOverloads
fun String?.splits(ignoreCase: Boolean = true,
                   removeEmptyEntities: Boolean = true,
                   vararg separators: String): FastList<String> {
  val results = FastList.newList<String>()

  if (this == null || this.isEmpty())
    return results

  if (separators.isEmpty())
    return FastList.newListWith(this)


  //    List<String> seps = Arrays.asList(separators).stream()
  //                              .map(sep -> ignoreCase ? sep.toLowerCase() : sep)
  //                              .collect(Collectors.toList());
  val seps = FastList.newListWith(*separators).collect { sep ->
    if (ignoreCase) sep.toLowerCase() else sep
  }

  val lowerStr = if (ignoreCase) this.toLowerCase() else this

  var startIndex = 0
  var prevIndex = 0
  val length = lowerStr.length

  while (startIndex < length) {
    var isContinue = true
    val iter = seps.iterator()
    while (iter.hasNext() && isContinue) {
      val sep = iter.next()
      val endIndex = Math.min(startIndex + sep.length, length)
      if (sep == lowerStr.substring(startIndex, endIndex)) {
        val item = this.substring(prevIndex, startIndex)
        if (item.isNotEmpty()) {
          if (removeEmptyEntities) {
            if (item.isNotBlank())
              results.add(item.trim { it <= ' ' })
          } else {
            results.add(item)
          }
        }
        prevIndex = startIndex + sep.length
        startIndex = prevIndex
        isContinue = false
      }
    }
    startIndex++
  }

  if (prevIndex < length) {
    val s =
        if (removeEmptyEntities) this.substring(prevIndex).trim { it <= ' ' }
        else this.substring(prevIndex)
    results.add(s)
  }

  return results
}