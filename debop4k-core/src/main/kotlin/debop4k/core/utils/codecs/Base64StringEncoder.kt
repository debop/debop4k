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

package debop4k.core.utils.codecs

import debop4k.core.collections.isNullOrEmpty
import debop4k.core.utils.EMPTY_STRING
import debop4k.core.utils.toUtf8Bytes
import org.apache.commons.codec.binary.Base64

/**
 * 문자열을 Base64 형태로 인코딩/디코딩 합니다
 * @author sunghyouk.bae@gmail.com
 */
class Base64StringEncoder : StringEncoder {

  private val base64 = Base64(true)

  /** {@inheritDoc}  */
  override fun encode(bytes: ByteArray?): String {
    if (bytes.isNullOrEmpty)
      return EMPTY_STRING

    return base64.encodeAsString(bytes)
  }

  /** {@inheritDoc}  */
  override fun decode(str: String?): ByteArray {
    if (str.isNullOrBlank())
      return ByteArray(0)

    return base64.decode(str.toUtf8Bytes())
  }
}