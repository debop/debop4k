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
@file:JvmName("ByteBufferx")

package debop4k.core.io

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import java.nio.ByteBuffer

//private val log = loggerOf("ByteBufferx")

/**
 * [ByteBuffer] 가 null 이거나 일을 수 있는 바이트가 없다면 true 를 반환한다
 */
val ByteBuffer?.isNullOrEmpty: Boolean
  get() = (this == null || this.remaining() == 0)

/**
 * ByteBuffer를 읽어 바이트 배열을 빌드합니다
 */
fun ByteBuffer?.toByteArray(): ByteArray {
  if (this.isNullOrEmpty)
    return emptyByteArray

  val bytes = ByteArray(this!!.remaining())
  this.duplicate().get(bytes)
  return bytes
}

/**
 * 바이트 배열을 읽어 [ByteBuffer]를 빌드합니다
 */
fun ByteArray?.toByteBuffer(): ByteBuffer {
  if (this.isNullOrEmpty)
    return ByteBuffer.allocate(0)

  return ByteBuffer.wrap(this!!)
}

/**
 * 바이트 배열을 읽어 [ByteBuffer]를 빌드합니다 (Heap 이 아닌 Direct Memory Access 가 가능한)
 */
fun ByteArray?.toByteBufferDirectly(): ByteBuffer {
  if (this.isNullOrEmpty)
    return ByteBuffer.allocate(0)

  val buffer = ByteBuffer.allocateDirect(this!!.size)
  buffer.put(this)
  buffer.flip()

  return buffer
}

