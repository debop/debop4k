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

package debop4k.core.utils

import debop4k.core.collections.charArrayListOf
import debop4k.core.collections.intArrayListOf
import org.eclipse.collections.impl.list.mutable.primitive.CharArrayList

/**
 * 한글 자소를 구분 지을 수 있도록 해주는 Utility class
 * @author sunghyouk.bae@gmail.com
 */
object KoreanString {

  private val CHO_SUNG = intArrayListOf(0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139,
                                        0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149,
                                        0x314a, 0x314b, 0x314c, 0x314d, 0x314e)
  private val JUNG_SUNG = intArrayListOf(0x314f, 0x3150, 0x3151, 0x3152,
                                         0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159,
                                         0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163)

  private val JONG_SUNG = intArrayListOf(0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135,
                                         0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e,
                                         0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146,
                                         0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e)

  private val KOREAN_RANGE = (0xAC00 .. 0xD7AF)

  /**
   * 문자열 내에서 할글 자소를 분해하여 초/중/종성 으로 제공합니다.
   * 예: 한국 -> ㅎㅏㄴㄱㅜㄱ
   */
  fun getJasoLetter(text: String): String {
    if (text.isNullOrBlank())
      return ""

    val letters = StringBuilder(text.length * 3)

    var i = 0
    val length = text.length
    while (i < length) {
      val char = text[i].toInt()
      if (char in KOREAN_RANGE) {
        val initIndex = char - 0xAC00

        val jongIndex = initIndex % 28
        val jungIndex = ((initIndex - jongIndex) / 28) % 21
        val choIndex = ((initIndex / 28) - jungIndex) / 21

        letters.append("%C".format(CHO_SUNG[choIndex]))
        letters.append("%C".format(JUNG_SUNG[jungIndex]))
        if (jongIndex != 0x0000) {
          letters.append("%C".format(JONG_SUNG[jongIndex]))
        }
      } else {
        letters.append("%C".format(char.toChar()))
      }
      i++
    }
    return letters.toString()
  }

  fun getChosung(text: String): CharArrayList {
    if (text.isNullOrBlank())
      return charArrayListOf()

    val chosungs = charArrayListOf()

    var i = 0
    val length = text.length
    while (i < length) {
      val char = text[i].toInt()
      if (char in KOREAN_RANGE) {
        val initIndex = char - 0xAC00

        val jongIndex = initIndex % 28
        val jungIndex = ((initIndex - jongIndex) / 28) % 21
        val choIndex = ((initIndex / 28) - jungIndex) / 21

        chosungs.add(CHO_SUNG[choIndex].toChar())
      }

      i++
    }
    return chosungs
  }

}