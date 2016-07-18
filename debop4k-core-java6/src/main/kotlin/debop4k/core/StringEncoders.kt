/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
@file:JvmName("StringEncoders")

package debop4k.core

import org.apache.commons.codec.binary.Hex
import java.util.*
import java.util.Base64.*

val Base64Encoder: Encoder by lazy { Base64.getEncoder() }
val Base64Decoder: Decoder by lazy { Base64.getDecoder() }
val Base64UrlEncoder by lazy { Base64.getUrlEncoder() }
val Base64UrlDecoder by lazy { Base64.getUrlDecoder() }

fun String?.toBase64(): String {
  if (this == null)
    return EMPTY_STRING

  return Base64Encoder.encodeToString(this.toByteArray(Charsets.UTF_8))
}

fun String?.fromBase64(): String {
  if (this == null)
    return EMPTY_STRING

  return Base64Decoder.decode(this).toString(Charsets.UTF_8)
}

fun String?.toHex(): String {
  if (this == null)
    return EMPTY_STRING

  return Hex.encodeHexString(this.toByteArray(Charsets.UTF_8))
}

fun String?.fromHex(): String {
  if (this == null)
    return EMPTY_STRING
  return Hex.decodeHex(this.toCharArray()).toString(Charsets.UTF_8)
}


