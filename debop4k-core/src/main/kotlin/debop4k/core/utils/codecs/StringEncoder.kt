/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

package debop4k.core.utils.codecs

/**
 * 바이트 배열을 문자열로 인코딩/디코딩 하는 Interface
 */
interface StringEncoder {
  /**
   * 바이트 배열을 인코딩하여 문자열로 만든다.
   * @param bytes 인코딩할 바이트 배열
   * @return 인코딩된 문자열
   */
  fun encode(bytes: ByteArray?): String

  /**
   * 인코딩된 문자열을 분해하여 바이트 배열로 만든다.
   * @param str 인코딩된 문자열
   * @return 디코딩된 바이트 배열
   */
  fun decode(str: String?): ByteArray
}