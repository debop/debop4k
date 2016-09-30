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

@file:JvmName("Cryptographyx")

package debop4k.core.cryptography

import debop4k.core.cryptography.digesters.*
import debop4k.core.cryptography.encryptors.DESEncryptor
import debop4k.core.cryptography.encryptors.RC2Encryptor
import debop4k.core.cryptography.encryptors.TripleDESEncryptor
import org.jasypt.salt.ZeroSaltGenerator
import java.security.SecureRandom
import java.util.*

/** Zero Salt Genrator */
val ZERO_SALTGENERATOR by lazy { ZeroSaltGenerator() }

val MD5Byte by lazy { MD5ByteDigester() }
val SHA1Byte by lazy { SHA1ByteDigester() }
val SHA256Byte by lazy { SHA256ByteDigester() }
val SHA384Byte by lazy { SHA384ByteDigester() }
val SHA512Byte by lazy { SHA512ByteDigester() }


val MD5String by lazy { MD5StringDigester() }
val SHA1String by lazy { SHA1StringDigester() }
val SHA256String by lazy { SHA256StringDigester() }
val SHA384String by lazy { SHA384StringDigester() }
val SHA512String by lazy { SHA512StringDigester() }

/** RC2 알고리즘을 사용하는 대칭형 암호기  */
val RC2 by lazy { RC2Encryptor() }

/** DES 알고리즘을 사용하는 대칭형 암호기  */
val DES by lazy { DESEncryptor() }

/** 3DES 알고리즘을 사용하는 대칭형 암호기  */
val TRIPLE_DES by lazy { TripleDESEncryptor() }


const val RANDOM_NUMBER_GENERATION = "SHA1PRNG"

val secureRandom: Random by lazy { SecureRandom.getInstance(RANDOM_NUMBER_GENERATION) }

/**
 * 난수를 가지는 바이트 배열을 반환합니다.
 */
fun randomBytes(num: Int): ByteArray {
  require(num >= 0)

  val bytes = ByteArray(num)
  secureRandom.nextBytes(bytes)
  return bytes
}