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

package debop4k.core.compress

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.io.fastByteArrayOutputStreamOf
import debop4k.core.io.toByteArray
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.util.zip.*

/**
 * Zip 압축 알고리즘을 사용한 압축기
 */
class ZipCompressor : Compressor {

  /** 압축 */
  override fun compress(input: ByteArray?): ByteArray {
    if (input.isNullOrEmpty)
      return emptyByteArray

    fastByteArrayOutputStreamOf().use { bos ->
      ZipOutputStream(bos).use { zip ->
        zip.write(input)
      }
      return bos.toByteArrayUnsafe()
    }
  }

  /** 압축 복원 */
  override fun decompress(input: ByteArray?): ByteArray {
    if (input.isNullOrEmpty)
      return emptyByteArray

    BufferedInputStream(ByteArrayInputStream(input)).use { bis ->
      ZipInputStream(bis).use { zip ->
        return zip.toByteArray()
      }
    }
  }
}