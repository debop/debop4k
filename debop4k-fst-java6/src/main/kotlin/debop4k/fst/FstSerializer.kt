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

package debop4k.fst

import de.ruedigermoeller.serialization.FSTConfiguration
import debop4k.core.io.serializers.Serializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * FST Library 를 이용한 Java 6용 Serializer
 * @author debop sunghyouk.bae@gmail.com
 */
class FstSerializer(val conf: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()) : Serializer {

  override fun serialize(graph: Any?): ByteArray {
    if (graph == null)
      return byteArrayOf()

    ByteArrayOutputStream().use { bos ->
      val oos = conf.getObjectOutput(bos)
      oos.writeObject(graph)
      oos.flush()

      return bos.toByteArray()
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> deserialize(bytes: ByteArray): T? {
    if (bytes.isEmpty())
      return null as T

    ByteArrayInputStream(bytes).use { bis ->
      val ois = conf.getObjectInput(bis)
      return ois.readObject() as T
    }
  }
}