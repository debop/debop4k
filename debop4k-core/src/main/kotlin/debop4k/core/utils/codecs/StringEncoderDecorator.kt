/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.core.utils.codecs

/**
 * 문자열 인코더 기능에 Decorator 패턴을 적용하는 클래스입니다.

 * @author sunghyouk.bae@gmail.com
 */
abstract class StringEncoderDecorator(val encoder: StringEncoder) : StringEncoder {

  /** {@inheritDoc}  */
  override fun encode(bytes: ByteArray?): String {
    return encoder.encode(bytes)
  }

  /** {@inheritDoc}  */
  override fun decode(str: String?): ByteArray {
    return encoder.decode(str)
  }
}