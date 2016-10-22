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
package debop4k.core.io

import debop4k.core.utils.min
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * {@link ByteBuffer}를 저장소로 사용하는 Input Stream 구현체입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
open class ByteBufferInputStream(val buffer: ByteBuffer) : InputStream() {

  @JvmOverloads
  constructor(bufferSize: Int = DEFAULT_BUFFER_SIZE) : this(ByteBuffer.allocateDirect(bufferSize))

  constructor(bytes: ByteArray) : this(bytes.toByteBufferDirectly())


  override fun read(): Int {
    return if (buffer.hasRemaining()) (buffer.get().toInt() and 0xFF) else -1
  }

  override fun read(b: ByteArray?, off: Int, len: Int): Int {
    if (len == 0)
      return 0

    val count = buffer.remaining() min len
    if (count == 0)
      return -1

    buffer.get(b, off, len)
    return count
  }

  override fun available(): Int {
    return buffer.remaining()
  }

  companion object {
    @JvmStatic fun of(src: ByteBuffer): ByteBufferInputStream {
      val buffer = src.duplicate().apply { flip() }
      return ByteBufferInputStream(buffer)
    }
  }
}