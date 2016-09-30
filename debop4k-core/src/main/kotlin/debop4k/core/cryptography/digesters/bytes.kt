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

@file:JvmName("bytes")

package debop4k.core.cryptography.digesters

import debop4k.core.cryptography.ByteDigester
import debop4k.core.cryptography.ZERO_SALTGENERATOR
import org.jasypt.digest.PooledByteDigester
import org.jasypt.salt.SaltGenerator

/**
 * 바이트 배열을 Digest (Hash 암호화)를 수행하는 추상 클래스

 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractByteDigester
@JvmOverloads protected constructor(override val algorithm: String,
                                    override val saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : ByteDigester {

  private val digester: PooledByteDigester = newDigester()

  @Synchronized
  open protected fun newDigester(): PooledByteDigester {
    val digester = PooledByteDigester()
    digester.setPoolSize(4)
    digester.setAlgorithm(algorithm)
    digester.setSaltGenerator(saltGenerator)

    return digester
  }

  /** {@inheritDoc}  */
  override fun digest(message: ByteArray): ByteArray {
    return digester.digest(message)
  }

  /** {@inheritDoc}  */
  override fun matches(message: ByteArray, digest: ByteArray): Boolean {
    return digester.matches(message, digest)
  }
}

/**
 * MD5 알고리즘을 사용한 Byte 배열 암호기
 */
class MD5ByteDigester : AbstractByteDigester {
  @JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : super("MD5", saltGenerator)
}

/**
 * SHA-1 알고리즘을 사용한 Byte 배열 암호기
 */
class SHA1ByteDigester : AbstractByteDigester {
  @JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : super("SHA-1", saltGenerator)
}

/**
 * SHA-256 알고리즘을 사용한 Byte 배열 암호기
 */
class SHA256ByteDigester : AbstractByteDigester {
  @JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : super("SHA-256", saltGenerator)
}

/**
 * SHA-384 알고리즘을 사용한 Byte 배열 암호기
 */
class SHA384ByteDigester : AbstractByteDigester {
  @JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : super("SHA-384", saltGenerator)
}

/**
 * SHA-512 알고리즘을 사용한 Byte 배열 암호기
 */
class SHA512ByteDigester : AbstractByteDigester {
  @JvmOverloads constructor(saltGenerator: SaltGenerator = ZERO_SALTGENERATOR) : super("SHA-512", saltGenerator)
}