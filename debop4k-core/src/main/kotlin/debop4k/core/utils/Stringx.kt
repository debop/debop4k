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

@file:JvmName("Stringx")

package debop4k.core.utils

import debop4k.core.BinaryStringFormat
import debop4k.core.BinaryStringFormat.Base64
import debop4k.core.BinaryStringFormat.HexDecimal
import debop4k.core.areEquals
import debop4k.core.collections.*
import debop4k.core.utils.codecs.*
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.list.mutable.primitive.CharArrayList
import java.nio.charset.Charset
import java.util.regex.*

const val EMPTY_STRING = ""
const val TRIMMING = "..."
const val NULL_STRING = "<null>"
const val COMMA: String = ","
const val TAB: String = "\t"
const val ElipsisLength = 80

@JvmField val LINE_SEPARATOR: String = Systems.lineSeparator
@JvmField val WHITESPACE_BLOCK: Pattern = Pattern.compile("\\s+")
@JvmField val UTF_8: Charset = Charsets.UTF_8

fun String?.asNullIfEmpty(): String? = if (this.isEmpty()) null else this

fun CharSequence?.isNull(): Boolean = this == null

fun CharSequence?.isNotNull(): Boolean = this != null

fun CharSequence?.isEmpty(): Boolean = this.isNullOrBlank()

fun CharSequence?.isNotEmpty(): Boolean = !isEmpty()

fun CharSequence?.isWhitespace(): Boolean = this.isNullOrBlank()

fun CharSequence?.isNotWhitespace(): Boolean = !isWhitespace()

fun CharSequence?.hasText(): Boolean = isNotEmpty()

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


/**
 * [ByteArray] 를 Hex Decimal Format 의 문자열로 변환
 * @see hexStringToByteArray
 */
fun byteArrayToHexString(bytes: ByteArray): String = bytes.encodeHexAsString()

/**
 * Hex Decimal Format의 문자열을 [ByteArray] 로 변환
 * @see byteArrayToHexString
 */
fun hexStringToByteArray(hexStr: String): ByteArray = hexStr.decodeHex()

fun byteArrayToBase64String(bytes: ByteArray): String = bytes.encodeBase64AsString()

fun base64StringToByteArray(base64Str: String): ByteArray = base64Str.decodeBase64()

fun String?.toUtf8Bytes(): ByteArray
    = this?.toByteArray(Charsets.UTF_8) ?: emptyByteArray

fun ByteArray?.toUtf8String(): String
    = this?.toString(Charsets.UTF_8) ?: EMPTY_STRING

fun String.convertEncoding(cs: Charset): String
    = if (this.isNullOrEmpty()) EMPTY_STRING else String(this.toByteArray(), cs)

@JvmOverloads
fun ByteArray?.toBinaryString(format: BinaryStringFormat = HexDecimal): String {
  if (this.isNullOrEmpty)
    return EMPTY_STRING

  return when (format) {
    HexDecimal -> byteArrayToHexString(this!!)
    Base64 -> byteArrayToBase64String(this!!)
    else -> throw UnsupportedOperationException("지원하지 않는 format 입니다. format=$format")
  }
}

@JvmOverloads
fun CharSequence?.fromBinaryString(format: BinaryStringFormat = HexDecimal): ByteArray {
  if (this.isNullOrBlank())
    return emptyByteArray

  return when (format) {
    HexDecimal -> hexStringToByteArray(this.toString())
    Base64 -> base64StringToByteArray(this.toString())
    else -> throw UnsupportedOperationException("지원하지 않는 format 입니다. format=$format")
  }
}


fun CharSequence?.deleteChar(vararg chars: Char): String {
  if (isNullOrBlank())
    return EMPTY_STRING

  if (chars.size == 0)
    return this.toString()

  val sb = StringBuilder(this!!.length)

  this.forEach { c ->
    if (!chars.contains(c)) {
      sb.append(c)
    }
  }
  return sb.toString()
}

@JvmOverloads
fun concat(items: Iterable<*>, separator: String = COMMA): String
    = items.toStringList().joinToString(separator = separator)


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

fun String?.splits(vararg separators: String): FastList<String> {
  return this.splits(true, true, *separators)
}

fun String?.splits(ignoreCase: Boolean, vararg separators: String): FastList<String> {
  return this.splits(ignoreCase, true, *separators)
}

fun String?.splits(ignoreCase: Boolean,
                   removeEmptyEntities: Boolean,
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

fun CharSequence?.toCharArray(): CharArray {
  if (this == null)
    return emptyCharArray

  val array = CharArray(this.length)
  this.forEachIndexed { i, c ->
    array[i] = c
  }
  return array
}

fun CharSequence?.toCharArrayList(): CharArrayList {
  return CharArrayList.newListWith(*this.toCharArray())
}

@JvmOverloads
fun <T> Iterable<T>.mkString(separator: CharSequence = COMMA): String {
  return this.toStringList().makeString(separator.toString())
}

@JvmOverloads
fun <T> Array<T>.mkString(separator: CharSequence = COMMA): String {
  return this.asIterable().toStringList().makeString(separator.toString())
}

@JvmOverloads
fun <K, V> Map<K, V>.mkString(separator: CharSequence = COMMA): String {
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

@JvmOverloads
fun <T> Iterable<T>.join(separator: CharSequence = COMMA): String {
  return this.mkString(separator.toString())
}

@JvmOverloads
fun <T> Array<T>.join(separator: CharSequence = COMMA): String {
  return this.asIterable().mkString(separator.toString())
}

@JvmOverloads
fun <K, V> Map<K, V>.join(separator: CharSequence = COMMA): String {
  return this.mkString(separator.toString())
}

fun String?.quotedStr(): String {
  if (isNullOrEmpty())
    return "''"
  return "'" + this!!.replace("\'", "\'\'") + "'"
}

fun CharSequence?.reverse(): CharSequence {
  if (this.isNullOrEmpty())
    return EMPTY_STRING

  return StringBuilder(this!!).reverse()
}

fun CharSequence?.replicate(n: Int): String {
  if (this.isNullOrEmpty())
    return EMPTY_STRING

  return this!!.repeat(n)
}


fun CharSequence?.wordCount(word: String): Int {
  // HINT: apache commons-lang3 에 StringUtils.countMatchs()
  if (this.isNullOrBlank() || word.isNullOrBlank()) {
    return 0
  }

  val pattern = Pattern.compile(word)
  val matcher = pattern.matcher(this)
  var count = 0
  while (matcher.find()) {
    count++
  }
  return count
}

@JvmOverloads
fun CharSequence?.firstLine(lineSeparator: String = LINE_SEPARATOR): String {
  if (this.isNullOrBlank())
    return EMPTY_STRING

  val index = this!!.indexOf(lineSeparator)
  return if (index > 0) substring(0, index) else this.toString()
}

fun CharSequence?.between(start: String, end: String): String {
  if (this.isNullOrBlank())
    return this?.toString() ?: EMPTY_STRING

  if (areEquals(start, end))
    return EMPTY_STRING

  var startIndex = 0
  if (start.isNotEmpty()) {
    val index = this!!.indexOf(start)
    if (index >= 0)
      startIndex = index + start.length
  }

  var endIndex = this!!.length
  if (end.isNotEmpty()) {
    // NOTE: lastIndexOf 의 fromIndex 는 뒤에서부터 찾을 때이 index 값을 줘야 한다. (0 을 주면 항상 -1 이다)
    val index = this.indexOf(end, startIndex)
    if (index >= 0)
      endIndex = index
  }

  return if (endIndex >= startIndex) this.substring(startIndex, endIndex) else EMPTY_STRING
}


val CharSequence.lastChar: Char
  get() = if (this.isEmpty()) 0.toChar() else this[length - 1]

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

fun String?.trim(): String {
  return if (this == null) EMPTY_STRING else (this as CharSequence).trim() as String
}


fun <T> Iterable<T>.toStringList(): FastList<String> {
  return FastList.newList(map { it.asString() })
}