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

import org.apache.commons.codec.binary.Hex
import java.util.*

val base64Encoder = Base64.getEncoder()
val base64Decoder = Base64.getDecoder()
val base64UrlEncoder = Base64.getUrlEncoder()
val base64UrlDecoder = Base64.getUrlDecoder()

fun String.toBase64(): String = base64Encoder.encodeToString(this.toByteArray(Charsets.UTF_8))
fun String.fromBase64(): String = base64Decoder.decode(this).toString(Charsets.UTF_8)

fun String.toHex(): String = Hex.encodeHexString(this.toByteArray(Charsets.UTF_8))
fun String.fromHex(): String = Hex.decodeHex(this.toCharArray()).toString(Charsets.UTF_8)


