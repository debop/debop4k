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

import debop4k.core.io.toByteArray
import debop4k.core.io.toInputStream
import java.io.InputStream

/**
 * Serializer
 * @author sunghyouk.bae@gmail.com
 */
interface Serializer {

  /**
   * 객체를 직렬화하여 바이트 배열로 변환합니다.
   * @param graph 대상 객체
   * @return 바이트 배열
   */
  fun serialize(graph: Any?): ByteArray

  /**
   * 직렬화된 바이트 배열을 역직렬화 하여 객체로 변환합니다.
   * @param bytes 바이트 배열
   * @return 역직렬화된 객체의 Optional
   */
  fun <T> deserialize(bytes: ByteArray?): T?

  /**
   * 원본 객체를 복사합니다.
   * @param src 원본 객체
   * @param  원본 객체의 수형
   * @return 복사된 객체
   */
  fun <T> copy(src: T): T?
      = fromInputStream<T>(toInputStream(src))

  /**
   * 객체를 직렬화하여 [InputStream] 으로 변환합니다.
   * @param src 원본 객체
   * @return 원본 객체의 직렬화 정보를 담은 input stream
   */
  fun toInputStream(src: Any?): InputStream
      = serialize(src).toInputStream()

  /**
   * input stream 을 읽어, 역직렬화를 수행하여 객체로 변한합니다.

   * @param input input stream
   * @param    변환할 객체의 수형
   * @return 변환된 객체, 데이터가 없거나, 역직렬화 실패시에는 null 을 반환합니다.
   */
  fun <T> fromInputStream(input: InputStream): T?
      = deserialize(input.toByteArray())

}