/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

package debop4k.core.compressions

import debop4k.core.emptyByteArray
import debop4k.core.io.stream.toByteArray
import org.springframework.util.FastByteArrayOutputStream
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.util.zip.*

/**
 * Deflater 알고리즘을 이용하는 압축기
 * @author sunghyouk.bae@gmail.com
 */
class DeflateCompressor : Compressor {

  override fun compress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return emptyByteArray()

    FastByteArrayOutputStream().use { bos ->
      DeflaterOutputStream(bos).use { deflater ->
        deflater.write(input)
      }
      return bos.toByteArrayUnsafe()
    }
  }

  override fun decompress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return emptyByteArray()

    BufferedInputStream(ByteArrayInputStream(input)).use { bis ->
      InflaterInputStream(bis).use { inflater ->
        return inflater.toByteArray()
      }
    }
  }
}