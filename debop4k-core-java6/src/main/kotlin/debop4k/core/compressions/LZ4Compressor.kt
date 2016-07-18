/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.core.compressions

import debop4k.core.EmptyByteArray
import net.jpountz.lz4.LZ4Factory

/**
 * LZ4 알고리즘으로 압축/복원
 * @author sunghyouk.bae@gmail.com
 */
class LZ4Compressor : Compressor {

  override fun compress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return EmptyByteArray
    return compressor.compress(input)
  }

  override fun decompress(input: ByteArray?): ByteArray {
    if (input == null || input.isEmpty())
      return EmptyByteArray

    return decompressor.decompress(input, input.size * 1000)
  }

  companion object {
    val factory by lazy { LZ4Factory.fastestInstance() }
    val compressor by lazy { factory.fastCompressor() }
    val decompressor by lazy { factory.safeDecompressor() }
  }
}