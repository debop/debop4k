/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package debop4k.core.compressions

import debop4k.core.io.toByteArray
import java.nio.ByteBuffer


fun Compressor.compress(buffer: ByteBuffer): ByteBuffer
    = ByteBuffer.wrap(this.compress(buffer.toByteArray()))

fun Compressor.compressAsString(input: String): String {
  return this.compress(input.toByteArray()).toString(Charsets.UTF_8)
}

fun Compressor.decompress(buffer: ByteBuffer): ByteBuffer
    = ByteBuffer.wrap(this.decompress(buffer.toByteArray()))

fun Compressor.decompressAsString(input: String): String {
  return this.decompress(input.toByteArray()).toString(Charsets.UTF_8)
}


