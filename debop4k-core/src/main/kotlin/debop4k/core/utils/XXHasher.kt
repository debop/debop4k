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

package debop4k.core.utils

import debop4k.core.collections.toByteArray
import net.jpountz.xxhash.StreamingXXHash32
import net.jpountz.xxhash.XXHashFactory

/**
 * LZ4 의 XXHash 를 이용하여, 빠르고 좋은 Hash Code 값을 생성합니다.
 * ref : https://github.com/jpountz/lz4-java
 *
 * @author sunghyouk.bae@gmail.com
 */
object XXHasher {

  val factory: XXHashFactory by lazy { XXHashFactory.fastestInstance() }
  val hash32: StreamingXXHash32 by lazy { factory.newStreamingHash32(0x19960522) }

  val ZERO_VALUE = 0.toByte()
  val DEFAULT_VALUE = 1.toByte()

  /** Hash 값을 생성합니다 */
  @JvmStatic
  fun compute(vararg objs: Any?): Int {
    if (objs.isEmpty()) return ZERO_VALUE.toInt()

    hash32.reset()

    objs.forEach {
      val bytes = it.hash().toByteArray()
      hash32.update(bytes, 0, bytes.size)
    }
    return hash32.value
  }

  fun Any?.hash(): Int = when (this) {
    null -> ZERO_HASH
    is Enum<*> -> ordinal.hashCode()
    else -> hashCode()
  }
}