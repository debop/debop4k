/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

package debop4k.core.utils.codecs

import debop4k.core.cryptography.SymmetricEncryptor

/**
 * 바이트 배열을 암호화하고, 문자열로 인코딩 / 디코딩을 수행합니다.
 * @author sunghyouk.bae@gmail.com
 */
open class EncryptableStringEncoder(encoder: StringEncoder,
                                    val encryptor: SymmetricEncryptor) : StringEncoderDecorator(encoder) {

  /**
   * 바이트 배열을 암호화 한 후, 인코딩하여 문자열로 만든다.
   */
  override fun encode(bytes: ByteArray?): String {
    return super.encode(encryptor.encrypt(bytes))
  }

  /**
   * 문자열을 디코딩한 후, 암호 복원한다
   */
  override fun decode(str: String?): ByteArray {
    return encryptor.decrypt(super.decode(str))
  }

}