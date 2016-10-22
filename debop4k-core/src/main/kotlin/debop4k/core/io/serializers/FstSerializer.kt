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
import org.nustaq.serialization.FSTConfiguration

/**
 * FST 라이브러리 for Java 7 or higher 를 이용한 Serializer
 *
 * 참고 : https://github.com/RuedigerMoeller/fast-serialization/wiki/Serialization
 * @author sunghyouk.bae@gmail.com
 */
open class FstSerializer
@JvmOverloads constructor(val conf: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()) : Serializer {

  override fun serialize(graph: Any?): ByteArray {
    if (graph == null)
      return emptyByteArray

    // Fst 2.x 이상에서 지원
    return conf.asByteArray(graph)
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> deserialize(bytes: ByteArray?): T? {
    if (bytes.isNullOrEmpty)
      return null

    // Fst 2.x 이상에서 지원
    return conf.asObject(bytes) as? T
  }

  companion object {

    @JvmStatic
    @JvmOverloads
    fun of(conf: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()): FstSerializer
        = FstSerializer(conf)

  }
}