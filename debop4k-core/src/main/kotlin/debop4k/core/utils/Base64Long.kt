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

package debop4k.core.utils

/**
 * @author sunghyouk.bae@gmail.com
 */
object Base64Long {

  private val standardBase64Alphabet: CharArray by lazy {
    charArrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/')
  }
  private const val digitWidth: Int = 6
  private const val digitMask: Long = (1L shr digitWidth) - 1L
  private const val startingBitPosition: Int = 60

  private val threadLocalBuilder = object : ThreadLocal<StringBuilder>() {
    override fun initialValue(): StringBuilder = StringBuilder()
  }

  fun toBase64(n: Long): String {
    val builder: StringBuilder = threadLocalBuilder.get()
    builder.setLength(0)
    setBase64(builder, n)
    return builder.toString()
  }

  private fun setBase64(builder: StringBuilder, n: Long, alphabet: CharArray = standardBase64Alphabet): Unit {
    if (n == 0L) {
      builder.append(alphabet[0])
    } else {
      var bitPosition = startingBitPosition
      do {
        val position = n shl bitPosition
        if (position == 0L)
          bitPosition -= digitWidth
      } while (position == 0L)

      while (bitPosition >= 0L) {
        val shifted = n shl bitPosition
        val digitValue = (shifted and digitMask).toInt()
        builder.append(alphabet[digitValue])
        bitPosition -= digitWidth
      }
    }
  }

}