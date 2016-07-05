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

package debop4k.core.io.serializers.java6

import de.ruedigermoeller.serialization.FSTConfiguration
import debop4k.core.io.emptyByteArray
import debop4k.core.io.serializers.Serializer
import org.springframework.util.FastByteArrayOutputStream


/**
 * FST 라이브러리 for JDK 를 이용하여 직렬화/역직렬화를 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
class FstJava6Serializer(val conf: FSTConfiguration = FSTConfiguration.createDefaultConfiguration()) : Serializer {


  override fun serialize(graph: Any?): ByteArray {
    if (graph == null)
      return emptyByteArray()

    FastByteArrayOutputStream().use { bos ->
      val oos = conf.getObjectOutput(bos)
      oos.writeObject(graph)
      oos.flush()
      return bos.toByteArrayUnsafe()
    }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T> deserialize(bytes: ByteArray): T? {
    if (bytes.isEmpty())
      return null as T

    val ois = conf.getObjectInput(bytes)
    return ois.readObject() as T
  }

}