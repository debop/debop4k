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

@file:JvmName("encryptors")

package debop4k.core.cryptography

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.utils.*
import debop4k.core.utils.codecs.decodeHex
import debop4k.core.utils.codecs.encodeHexAsString
import org.jasypt.encryption.pbe.PooledPBEByteEncryptor
import org.jasypt.salt.SaltGenerator

abstract class AbstractSymmetricEncryptor
@JvmOverloads
protected constructor(override val algorithm: String,
                      override val saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : Encryptor {

  override var password = DEFAULT_PASSWORD
  val encryptor: PooledPBEByteEncryptor = newEncryptor()

  private fun newEncryptor(): PooledPBEByteEncryptor {
    val encryptor = PooledPBEByteEncryptor()
    encryptor.setPoolSize(4)
    encryptor.setAlgorithm(algorithm)
    encryptor.setSaltGenerator(saltGenerator)
    encryptor.setPassword(password)

    return encryptor
  }

  override fun encrypt(message: ByteArray?): ByteArray {
    if (message.isNullOrEmpty)
      return emptyByteArray

    return encryptor.encrypt(message)
  }

  override fun encryptString(message: String?): String {
    if (message.isNullOrBlank())
      return EMPTY_STRING

    return encryptor.encrypt(message.toUtf8Bytes()).encodeHexAsString()
  }

  override fun decrypt(encrypted: ByteArray?): ByteArray {
    if (encrypted.isNullOrEmpty)
      return emptyByteArray

    return encryptor.decrypt(encrypted)
  }

  override fun decryptString(encrypted: String?): String {
    if (encrypted.isNullOrBlank())
      return EMPTY_STRING

    return encryptor.decrypt(encrypted.decodeHex()).toUtf8String()
  }

  companion object {
    @JvmStatic private val DEFAULT_PASSWORD = "@kesti21"
  }
}

/**
 * RC2 대칭형 알고리즘을 이용한 암호기
 */
class RC2Encryptor
@JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) :
    AbstractSymmetricEncryptor("PBEwithSHA1andRC2_40", saltGenerator) {
}

/**
 * DES 대칭형 알고리즘을 이용한 암호기
 */
class DESEncryptor
@JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) :
    AbstractSymmetricEncryptor("PBEwithMD5andDES", saltGenerator) {
}

/**
 * TripleDES 대칭형 알고리즘을 이용한 암호기
 */
class TripleDESEncryptor
@JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) :
    AbstractSymmetricEncryptor("PBEwithSHA1andDESEDE", saltGenerator) {
  // NOTE: "PBEWithMD5AndTripleDES" 는 JCE 를 설치해야만 가능합니다. (Oracle 사이트에서 따로 다운받아 JRE에 추가해야 합니다.)
}



