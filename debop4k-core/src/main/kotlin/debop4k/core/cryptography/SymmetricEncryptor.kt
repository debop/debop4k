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

package debop4k.core.cryptography

import org.jasypt.salt.SaltGenerator

/**
 * 대칭형 암호 (Symmetric Encryption) 을 수행하는 암호기의 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
interface SymmetricEncryptor {

  /**
   * 대칭형 암호화를 위한 알고리즘 명
   */
  val algorithm: String

  /**
   * 암호화 시 사용할 Salt를 생성하는 생성기
   */
  val saltGenerator: SaltGenerator

  /**
   * 비밀번호
   */
  var password: String

  /**
   * 지정된 일반 바이트 배열 정보를 암호화하여 바이트 배열로 반환합니다.
   * @param message 일반 바이트 배열
   * @return 암호화된 바이트 배열
   */
  fun encrypt(message: ByteArray?): ByteArray

  /**
   * 지정된 문자열을 암호화하여 반환합니다.
   * @param message 암호화할 일반 문자열
   */
  fun encryptString(message: String?): String

  /**
   * 암호화된 바이트 배열을 복호화하여, 일반 바이트 배열로 반환합니다.
   * @param encrypted 암호화된 바이트 배열
   * @return 복호화한 바이트 배열
   */
  fun decrypt(encrypted: ByteArray?): ByteArray

  /**
   * 암호화된 문자열을 복호화하여 일반 문자열로 반환합니다.
   * @param encrypted 암호화된 문자열
   * @return 복호화된 일반 문자열
   */
  fun decryptString(encrypted: String?): String
}