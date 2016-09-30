/*
 * Copyright (c) 2016. KESTI co, ltd
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

@file:JvmName("CodecEx")

package debop4k.core.utils.codecs

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.utils.EMPTY_STRING
import debop4k.core.utils.toUtf8Bytes
import debop4k.core.utils.toUtf8String
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex

private val _base64: Base64 get() = Base64()
private val _hex: Hex get() = Hex(Charsets.UTF_8)

fun ByteArray?.encodeBase64(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return _base64.encode(this!!)
}

fun ByteArray?.encodeBase64AsString(): String {
  if (this.isNullOrEmpty)
    return EMPTY_STRING

  return _base64.encodeAsString(this!!)
}

fun ByteArray?.decodeBase64(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return _base64.decode(this!!)
}

fun String?.decodeBase64(): ByteArray {
  if (this.isNullOrBlank())
    return emptyByteArray

  return _base64.decode(this!!)
}

fun String?.decodeBase64AsString(): String {
  if (this.isNullOrBlank())
    return EMPTY_STRING

  return _base64.decode(this!!).toUtf8String()
}

fun ByteArray?.encodeHex(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return _hex.encode(this!!)
}

fun ByteArray?.encodeHexAsString(): String {
  if (this.isNullOrEmpty)
    return EMPTY_STRING

  return _hex.encode(this!!).toUtf8String()
}

fun ByteArray?.decodeHex(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  return _hex.decode(this!!)
}

fun String?.decodeHex(): ByteArray {
  if (this.isNullOrBlank())
    return emptyByteArray

  return _hex.decode(this!!.toUtf8Bytes())
}

fun String?.decodeHexAsString(): String {
  if (this.isNullOrBlank())
    return EMPTY_STRING

  return _hex.decode(this!!.toUtf8Bytes()).toUtf8String()
}