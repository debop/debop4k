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

@file:JvmName("StringEncoders")

package debop4k.core

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex

private val base64 by lazy { Base64(true) }
private val hex by lazy { Hex(Charsets.UTF_8) }

fun String?.base64Encode(): String {
  if (this == null)
    return EMPTY_STRING

  return base64.encodeToString(this.toByteArray(Charsets.UTF_8))
}

fun String?.base64Decode(): String {
  if (this == null)
    return EMPTY_STRING

  return base64.decode(this).toString(Charsets.UTF_8)
}

fun String?.encodeHex(): String {
  if (this == null)
    return EMPTY_STRING

  return Hex.encodeHexString(this.toByteArray(Charsets.UTF_8))
}

fun String?.decodeHex(): String {
  if (this == null)
    return EMPTY_STRING

  return Hex.decodeHex(this.toCharArray()).toString(Charsets.UTF_8)
}


// Hex String
fun ByteArray.toHexString(): String = Hex.encodeHexString(this)

fun String.fromHexString(): ByteArray = Hex.decodeHex(this.toCharArray())

