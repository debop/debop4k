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

package debop4k.core

/**
 * 문자열을 인코딩하는 방식
 *
 * @author debop sunghyouk.bae@gmail.com
 */
enum class BinaryStringFormat {

  /** Base 64 Encoding */
  Base64,

  /** Hex decimal encoding */
  HexDecimal;

  companion object {

    fun valueOf(v: Int): BinaryStringFormat = when (v) {
      0 -> Base64
      1 -> HexDecimal
      else -> throw IllegalArgumentException("지원하지 않는 ordinal 값입니다. v = $v")
    }
  }
}