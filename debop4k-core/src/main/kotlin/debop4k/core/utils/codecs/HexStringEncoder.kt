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

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.loggerOf
import debop4k.core.utils.*
import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Hex

/**
 * 문자열을 16진법 (Hex Decimal) 문자로 인코딩/디코딩 합니다
 * @author sunghyouk.bae@gmail.com
 */
class HexStringEncoder : StringEncoder {

  private val log = loggerOf(javaClass)

  private val hex = Hex(Charsets.UTF_8)

  /** {@inheritDoc}  */
  override fun encode(bytes: ByteArray?): String {
    if (bytes.isNullOrEmpty)
      return EMPTY_STRING

    return hex.encode(bytes).toUtf8String()
  }

  /** {@inheritDoc}  */
  override fun decode(str: String?): ByteArray {
    if (str.isNullOrBlank())
      return emptyByteArray

    try {
      return hex.decode(str!!.toUtf8Bytes())
    } catch (e: DecoderException) {
      log.error("Decoding 시 예외가 발생했습니다.", e)
      return emptyByteArray
    }
  }
}