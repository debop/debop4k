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

package debop4k.core.json

import com.fasterxml.jackson.databind.ObjectMapper
import debop4k.core.collections.emptyByteArray
import debop4k.core.collections.isNullOrEmpty
import debop4k.core.utils.EMPTY_STRING


/**
 * Jackson Json Serializer
 * @author debop sunghyouk.bae@gmail.com
 */
class JacksonSerializer
@JvmOverloads constructor(val mapper: ObjectMapper = DefaultObjectMapper) : JsonSerializer {

  /** 객체를 Json 직렬화를 수행하여 바이트 배열로 반환한다 */
  override fun <T : Any> toByteArray(graph: T?): ByteArray {
    if (graph == null)
      return emptyByteArray

    return mapper.writeValueAsBytes(graph)
  }

  /** Json 데이터를 가진 바이트 배열을 읽어 지정된 수형의 인스턴스를 생성한다 */
  override fun <T : Any> fromByteArray(jsonBytes: ByteArray?, clazz: Class<T>): T? {
    if (jsonBytes.isNullOrEmpty)
      return null

    return mapper.readValue(jsonBytes, clazz)
  }

  /** 객체를 Json 직렬화를 통해 문자열로 변환한다 */
  override fun <T : Any> toString(graph: T?): String {
    if (graph == null)
      return EMPTY_STRING

    return mapper.writeValueAsString(graph)
  }

  /** Json 문자열을 역직렬화해서 지정된 수형의 인스턴스를 생성한다 */
  override fun <T : Any> fromString(jsonText: String?, clazz: Class<T>): T? {
    if (jsonText.isNullOrEmpty())
      return null

    return mapper.readValue(jsonText, clazz)
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(mapper: ObjectMapper = DefaultObjectMapper) = JacksonSerializer(mapper)
  }
}