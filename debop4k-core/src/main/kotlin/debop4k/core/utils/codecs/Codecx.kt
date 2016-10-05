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

@file:JvmName("Codecx")

package debop4k.core.utils.codecs

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.compress.*
import debop4k.core.utils.*
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex

private val _base64: Base64 = Base64()
private val _hex: Hex = Hex(Charsets.UTF_8)

/** Base64 String Encoder */
val base64 by lazy { Base64StringEncoder() }

/** Hex Decimal String Encoder */
val hexDecimal by lazy { HexStringEncoder() }

/** GZip Base64 String Encoder */
val gzipBase64 by lazy { CompressableStringEncoder(base64, GZIP) }

/** GZip Hex Decimal String Encoder */
val gzipHexDecimal by lazy { CompressableStringEncoder(hexDecimal, GZIP) }

/** Deflate Base64 String Encoder */
val deflaterBase64 by lazy { CompressableStringEncoder(base64, DEFLATER) }

/** Deflate Hex Decimal String Encoder */
val deflaterHexDecimal by lazy { CompressableStringEncoder(hexDecimal, DEFLATER) }

/** Snappy Base64 String Encoder */
val snappyBase64 by lazy { CompressableStringEncoder(base64, SNAPPY) }

/** Snappy Hex Decimal String Encoder */
val snappyHexDecimal by lazy { CompressableStringEncoder(hexDecimal, SNAPPY) }


fun ByteArray?.encodeBase64(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return base64.encode(this!!).toUtf8Bytes()
}

fun ByteArray?.encodeBase64AsString(): String {
  if (this.isNullOrEmpty)
    return EMPTY_STRING

  return base64.encode(this!!)
}

fun String?.encodeBase64AsString(): String {
  if (this.isNullOrEmpty())
    return EMPTY_STRING

  return base64.encode(this.toUtf8Bytes())
}

fun ByteArray?.decodeBase64(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return base64.decode(this.toUtf8String())
}

fun String?.decodeBase64(): ByteArray {
  if (this.isNullOrBlank())
    return emptyByteArray

  return base64.decode(this!!)
}

fun String?.decodeBase64AsString(): String {
  if (this.isNullOrBlank())
    return EMPTY_STRING

  return base64.decode(this!!).toUtf8String()
}

fun ByteArray?.hexEncode(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return hexDecimal.encode(this!!).toUtf8Bytes()
}

fun String?.hexEncodeAsString(): String {
  if (this == null)
    return EMPTY_STRING

  return hexDecimal.encode(this.toUtf8Bytes())
}

fun ByteArray?.encodeHexAsString(): String {
  if (this.isNullOrEmpty)
    return EMPTY_STRING

  return hexDecimal.encode(this!!)
}

fun ByteArray?.hexDecode(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return hexDecimal.decode(this.toUtf8String())
}

fun String?.decodeHex(): ByteArray {
  if (this.isNullOrBlank())
    return emptyByteArray

  return hexDecimal.decode(this)
}

fun String?.decodeHexAsString(): String {
  if (this.isNullOrBlank())
    return EMPTY_STRING

  return hexDecimal.decode(this).toUtf8String()
}

// Hex String
fun ByteArray.toHexString(): String = hexDecimal.encode(this)

fun String.fromHexString(): ByteArray = hexDecimal.decode(this)

