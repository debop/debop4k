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

package debop4k.core.compressions

/**
 * 압축/복원을 수행합니다.
 * @author sunghyouk.bae@gmail.com
 */
interface Compressor {

  /**
   * 지정된 바이트 배열을 압축합니다.
   *
   * @param 압축을 푼 바이트 배열
   * @return input 압축된 바이트 배열
   */
  fun compress(input: ByteArray?): ByteArray

  /**
   * 압축된 바이트 배열을 압축을 풉니다.
   *
   * @param input 압축된 바이트 배열
   * @return 압축을 푼 바이트 배열
   */
  fun decompress(input: ByteArray?): ByteArray

}