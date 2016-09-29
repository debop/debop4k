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

package debop4k.core.cryptography.encryptors

import debop4k.core.fromHexString
import debop4k.core.toHexString
import debop4k.core.toUtf8String
import org.jasypt.salt.SaltGenerator

/**
 * @author sunghyouk.bae@gmail.com
 */
interface Encryptor {

  val algorithm: String
  val saltGenerator: SaltGenerator
  var password: String

  fun encrypt(message: ByteArray): ByteArray
  fun decrypt(encrypted: ByteArray): ByteArray

  fun encryptAsString(message: String): String {
    return encrypt(message.toByteArray()).toHexString()
  }

  fun decryptAsString(encrypted: String): String {
    return decrypt(encrypted.fromHexString()).toUtf8String()
  }
}