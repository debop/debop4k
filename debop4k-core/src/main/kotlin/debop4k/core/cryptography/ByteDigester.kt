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
 * 바이트 배열을 Digest (Hash 암호화)를 수행하는 interface
 *
 * @author sunghyouk.bae@gmail.com
 */
interface ByteDigester {

  /**
   * Digest 암호화를 위한 알고리즘 명
   */
  val algorithm: String

  /**
   * 암호화 시에 사용하는 Salt 값 생성기를 반환합니다.
   */
  val saltGenerator: SaltGenerator

  /**
   * 바이트 배열 정보를 암호화 합니다.
   * @param message 바이트 배열
   * @return 암호화된 바이트 배열
   */
  fun digest(message: ByteArray): ByteArray

  /**
   * Message 를 암호화하면, digest 와 같은 값이 되는지 확인한다.
   * @param message 암호화된 바이트 배열과 비교할 message
   * @param digest  암호화된 바이트 배열
   * @return 같은 값이 되는지 여부
   */
  fun matches(message: ByteArray, digest: ByteArray): Boolean

}