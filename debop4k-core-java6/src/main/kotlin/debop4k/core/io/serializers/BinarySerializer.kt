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

package debop4k.core.io.serializers


import debop4k.core.EmptyByteArray
import org.springframework.util.FastByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 이진 방식으로 직렬화/역직렬화를 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
class BinarySerializer : Serializer {

  override fun serialize(graph: Any?): ByteArray {
    if (graph == null)
      return EmptyByteArray

    FastByteArrayOutputStream().use { bos ->
      ObjectOutputStream(bos).use { oos ->
        oos.writeObject(graph)
      }
      return bos.toByteArrayUnsafe()
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> deserialize(bytes: ByteArray): T? {
    if (bytes.isEmpty())
      return null as T

    ByteArrayInputStream(bytes).use { bis ->
      ObjectInputStream(bis).use { ois ->
        return ois.readObject() as T
      }
    }
  }
}