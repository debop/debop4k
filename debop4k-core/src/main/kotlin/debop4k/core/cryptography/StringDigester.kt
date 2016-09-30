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
 * 문자열을 Hash 기반으로 암호화를 수행합니다. (복호화는 안됩니다)
 *
 * @author sunghyouk.bae@gmail.com
 */
interface StringDigester {

  /**
   * 문자열 암호화를 위한 알고리즘 명
   */
  val algorithm: String

  /**
   * 암호화 시에 사용할 Salt 를 생성하는 생성기
   */
  val saltGenerator: SaltGenerator

  /**
   * 문자열을 암호화 합니다.
   * @param message 바이트 배열
   * @return 암호화된 문자열
   */
  fun digest(message: String): String

  /**
   * 해당 메시지가 암호화된 내용과 일치하는지 확인합니다.
   * @param message 일반 메시지
   * @param digest  암호화된 메시지
   * @return 메시지 일치 여부
   */
  fun matches(message: String, digest: String): Boolean

//  companion object {
//    val MD5 by lazy { MD5StringDigester() }
//    val SHA1 by lazy { SHA1StringDigester() }
//    val SHA256 by lazy { SHA256StringDigester() }
//    val SHA384 by lazy { SHA384StringDigester() }
//    val SHA512 by lazy { SHA512StringDigester() }
//  }
}