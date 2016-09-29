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

package debop4k.core.io.serializers

import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.uninitialized
import org.nustaq.serialization.FSTConfiguration

/**
 * FST-Serialization (https://github.com/RuedigerMoeller/fast-serialization) 을 사용한 Serializer 입니다.
 * Kyro 라이브러리와 막상막하의 성능을 나타낸다.
 *
 * @author sunghyouk.bae@gmail.com
 */
class FstSerializer
@JvmOverloads constructor(val conf: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()) : Serializer {

  override fun serialize(graph: Any?): ByteArray {
    if (graph == null)
      return emptyByteArray

    val oos = conf.objectOutput
    oos.writeObject(graph)
    return oos.copyOfWrittenBuffer
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> deserialize(bytes: ByteArray?): T {
    if (bytes.isNullOrEmpty)
      return uninitialized()

    return conf.getObjectInput(bytes).readObject() as T
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(conf: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()): FstSerializer
        = FstSerializer(conf)
  }
}