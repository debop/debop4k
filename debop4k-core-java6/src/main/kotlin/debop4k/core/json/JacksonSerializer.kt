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

package debop4k.core.json

import com.fasterxml.jackson.databind.ObjectMapper
import debop4k.core.EMPTY_STRING
import debop4k.core.java.collections.emptyByteArray

/**
 * JacksonSerializer
 * @author debop sunghyouk.bae@gmail.com
 */
class JacksonSerializer(val objectMapper: ObjectMapper = ObjectMapperFactory.newMapper()) : AbstractJsonSerializer() {

  override fun toByteArray(graph: Any?): ByteArray {
    if (graph == null)
      return emptyByteArray

    return objectMapper.writeValueAsBytes(graph)
  }

  override fun <T> fromByteArray(jsonBytes: ByteArray?, clazz: Class<T>): T? {
    if (jsonBytes == null)
      return null as T

    return objectMapper.readValue(jsonBytes, clazz)
  }

  override fun toString(graph: Any?): String {
    if (graph == null)
      return EMPTY_STRING

    return objectMapper.writeValueAsString(graph)
  }

  override fun <T> fromString(jsonText: String?, clazz: Class<T>): T? {
    if (jsonText.isNullOrBlank()) {
      return null as T
    }

    return objectMapper.readValue(jsonText, clazz)
  }
}