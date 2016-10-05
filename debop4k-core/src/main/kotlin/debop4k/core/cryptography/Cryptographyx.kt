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

@file:JvmName("Cryptographyx")

package debop4k.core.cryptography

import org.jasypt.salt.ZeroSaltGenerator
import java.security.SecureRandom
import java.util.*

/** Zero Salt Genrator */
val ZERO_SALTGENERATOR by lazy { ZeroSaltGenerator() }

/** MD5 Byte Digester */
val MD5Byte by lazy { MD5ByteDigester() }
/** SHA1 Byte Digester */
val SHA1Byte by lazy { SHA1ByteDigester() }
/** SHA256 Byte Digester */
val SHA256Byte by lazy { SHA256ByteDigester() }
/** SHA384 Byte Digester */
val SHA384Byte by lazy { SHA384ByteDigester() }
/** SHA512 Byte Digester */
val SHA512Byte by lazy { SHA512ByteDigester() }

/** MD5 String Digester */
val MD5String by lazy { MD5StringDigester() }
/** SHA1 String Digester */
val SHA1String by lazy { SHA1StringDigester() }
/** SHA256 String Digester */
val SHA256String by lazy { SHA256StringDigester() }
/** SHA384 String Digester */
val SHA384String by lazy { SHA384StringDigester() }
/** SHA512 String Digester */
val SHA512String by lazy { SHA512StringDigester() }

/** RC2 알고리즘을 사용하는 대칭형 암호기  */
val RC2 by lazy { RC2Encryptor() }

/** DES 알고리즘을 사용하는 대칭형 암호기  */
val DES by lazy { DESEncryptor() }

/** 3DES 알고리즘을 사용하는 대칭형 암호기  */
val TRIPLE_DES by lazy { TripleDESEncryptor() }

const val RANDOM_NUMBER_GENERATION = "SHA1PRNG"

@JvmField val SECURE_RANDOM: Random = SecureRandom.getInstance(RANDOM_NUMBER_GENERATION)

/**
 * 난수를 가지는 바이트 배열을 반환합니다.
 */
fun randomBytes(num: Int): ByteArray {
  require(num >= 0)

  val bytes = ByteArray(num)
  SECURE_RANDOM.nextBytes(bytes)
  return bytes
}