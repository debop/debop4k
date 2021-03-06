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

import debop4k.core.utils.toUtf8Bytes
import debop4k.core.utils.toUtf8String

/**
 * JsonSerializer
 * @author debop sunghyouk.bae@gmail.com
 */
interface JsonSerializer {

  /**
   * 객체를 JSON 직렬화를 수행하여 바이트 배열로 변환합니다.

   * @param graph 객체
   * *
   * @return 바이트 배열
   */
  fun <T : Any> toByteArray(graph: T?): ByteArray
      = toString(graph).toUtf8Bytes()

  /**
   * JSON 직렬화된 바이트 배열 정보를 역직렬화하여, 지정한 클래스 형식의 인스턴스로 빌드합니다.

   * @param jsonBytes JSON 직렬화된 바이트 배열
   * *
   * @param clazz     원하는 인스턴스의 클래스 형식
   * *
   * @param        인스턴스의 클래스 형식
   * *
   * @return 변환된 인스턴스
   */
  fun <T : Any> fromByteArray(jsonBytes: ByteArray?, clazz: Class<T>): T?
      = fromString(jsonBytes.toUtf8String(), clazz)

  /**
   * 객체를 JSON 직렬화를 통해 JSON 문자열로 변환합니다.

   * @param graph 직렬화할 객체
   * *
   * @return JSON 문자열
   */
  fun <T : Any> toString(graph: T?): String

  /**
   * JSON 문자열을 역직렬화를 통해 객체로 빌드합니다.

   * @param jsonText JSON 문자열
   * *
   * @param clazz    역직렬화하여 빌드할 인스턴스의 수형
   * *
   * @param       인스턴스의 수형
   * *
   * @return 역직렬화를 통해 빌드한 인스턴스
   */
  fun <T : Any> fromString(jsonText: String?, clazz: Class<T>): T?


}