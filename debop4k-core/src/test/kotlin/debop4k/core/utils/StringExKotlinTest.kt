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

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.collections.emptyCharArray
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.eclipse.collections.impl.factory.SortedMaps
import org.eclipse.collections.impl.list.mutable.FastList
import org.junit.Test

class StringExKotlinTest : AbstractCoreKotlinTest() {

  internal val spaceTextt = "           "
  internal val whitespaceText = "\n \r \t "

  @Test
  fun testIsNull() {
    assertThat(null.isNull()).isTrue()
    assertThat("".isNull()).isFalse()
    assertThat(null.isNotNull()).isFalse()
    assertThat("".isNotNull()).isTrue()
  }

  @Test
  fun testIsEmpty() {
    assertThat(null.isEmpty()).isTrue()
    assertThat("".isEmpty()).isTrue()
    assertThat(spaceTextt.isEmpty()).isTrue()
    assertThat(whitespaceText.isEmpty()).isTrue()
    assertThat("null".isEmpty()).isFalse()

    assertThat(null.isNotEmpty()).isFalse()
    assertThat("".isNotEmpty()).isFalse()
    assertThat(spaceTextt.isNotEmpty()).isFalse()
    assertThat(whitespaceText.isNotEmpty()).isFalse()
    assertThat("null".isNotEmpty()).isTrue()
  }

  @Test
  fun testEllipsis() {
    assertThat(sampleText.needEllipsis(sampleText.length * 2)).isFalse()
    assertThat(sampleText.needEllipsis(sampleText.length / 2)).isTrue()

    assertThat(sampleText.ellipsisEnd(10)).endsWith(TRIMMING)
    assertThat(sampleText.ellipsisStart(10)).startsWith(TRIMMING)
    assertThat(sampleText.ellipsisMid(10))
        .`is`(object : Condition<CharSequence>() {
          override fun matches(value: CharSequence): Boolean {
            return !value.startsWith(TRIMMING)
          }
        })
        .`is`(object : Condition<CharSequence>() {
          override fun matches(value: CharSequence): Boolean {
            return !value.endsWith(TRIMMING)
          }
        }).contains(TRIMMING)
  }

  @Test
  fun covertToHexString() {
    val text = "123 123"
    val hexText = "31323320313233"

    assertThat(byteArrayToHexString(text.toUtf8Bytes())).isEqualTo(hexText)
    assertThat(hexStringToByteArray(hexText).toUtf8String()).isEqualTo(text)
  }

  @Test
  fun convertToBase64String() {
    val base64Str = byteArrayToBase64String(sampleBytes)
    val bytes = base64StringToByteArray(base64Str)
    val converted = bytes.toUtf8String()

    assertThat(bytes).isEqualTo(sampleBytes)
    assertThat(converted).isEqualTo(sampleText)
  }

  @Test
  fun utf8StringToBytes() {
    val bytes = sampleText.toUtf8Bytes()
    val converted = bytes.toUtf8String()

    assertThat(converted).isEqualTo(sampleText)
  }

  @Test
  fun testDeleteChar() {
    val text = "abcdefgh"
    assertThat(text.deleteChar('c', 'f')).isEqualTo("abdegh")
    assertThat(text.deleteChar('a', 'h')).isEqualTo("bcdefg")
  }

  @Test
  fun testConcat() {
    assertThat(concat(listOf("a", "b", "c"), ",")).isEqualTo("a,b,c")
  }

  @Test
  fun stringSplitByCharacter() {
    val attlStr = "37|75|95|107|133|141|142|147|148|178"
    val atvlStr = "9 ||||2999999|||20091231|KR,KR,graph,c836|"

    val attls = attlStr.splits('|')
    val atvls = atvlStr.splits('|')

    log.debug("attls size={}, {}", attls.size, attls)
    log.debug("atvls size={}, {}", atvls.size, atvls)

  }

  @Test
  fun stringSplit() {
    val str = "동해,물 || 백두,산 a BaB"
    val strList = str.splits(true, true, ",", "||", "A")
    assertThat<String>(strList).contains("동해", "물", "백두", "산", "B", "B").hasSize(6)

    val caseStrs = str.splits(false, true, ",", "||", "A")
    assertThat<String>(caseStrs).contains("동해", "물", "백두", "산 a BaB").hasSize(4)
  }

  @Test
  fun stringSplitIgnoreCase() {
    val text = "Hello World! Hello java^^"
    var result: List<String> = text.splits(true, true, "!")
    assertThat<String>(result).contains("Hello World", "Hello java^^").hasSize(2)

    result = text.splits(false, true, "hello")
    assertThat<String>(result).contains(text).hasSize(1)

    result = text.splits(true, true, "hello")
    assertThat<String>(result).contains("World!", "java^^").hasSize(2)

    result = text.splits(true, true, "hello", "java")
    assertThat<String>(result).contains("World!", "^^").hasSize(2)

    result = text.splits(true, true, "||")
    assertThat<String>(result).contains(text).hasSize(1)
  }

  @Test
  fun testWordCount() {
    val text = sampleText.replicate(10)
    assertThat(text.wordCount("동해")).isEqualTo(10)
  }

  @Test
  fun testFirstLine() {
    val text = listOf(sampleText, sampleText, sampleText).join(LINE_SEPARATOR)

    assertThat(text.firstLine()).isEqualTo(sampleText)
    assertThat(text.firstLine(LINE_SEPARATOR)).isEqualTo(sampleText)
  }

  @Test
  fun testBetween() {
    val text = "서울특별시 성북구 정릉동 현대아파트 107-301 서울특별시 성북구 정릉동 현대아파트 107-301"
    assertThat(text.between("", "")).isEmpty()
    assertThat(text.between("별", "동")).isEqualTo("시 성북구 정릉")
    assertThat(text.between("", "특")).isEqualTo("서울")
    assertThat(text.between("3", "")).isEqualTo("01 서울특별시 성북구 정릉동 현대아파트 107-301")


    assertThat("abcdefg".between("c", "g")).isEqualTo("def")
    assertThat("abcdefg".between("", "c")).isEqualTo("ab")

    // NOTE: lastIndexOf 의 fromIndex 는 뒤에서부터 찾을 때이 index 값을 줘야 한다. (0 을 주면 항상 -1 이다)
    val sample = "Hello World! Sunghyouk Bae Hello World"
    assertThat(sample.lastIndexOf("Wor", 40)).isEqualTo(33)
    assertThat(sample.lastIndexOf("Wor", 0)).isEqualTo(-1)
  }

  @Test
  fun toCharArrayTest() {
    assertThat(null.toCharArray()).isEqualTo(emptyCharArray)
    assertThat("".toCharArray()).isEqualTo(emptyCharArray)
    assertThat("abc".toCharArray()).hasSize(3).contains('a', 'b', 'c')
  }

  @Test
  fun mkStringTest() {
    assertThat(arrayOf("a", "bc", "def").mkString()).isEqualTo("a,bc,def")
    assertThat(arrayOf("a", "bc", "def").mkString()).isEqualTo("a,bc,def")
    assertThat(arrayOf("a", "bc", "def").mkString("|")).isEqualTo("a|bc|def")

    assertThat(FastList.newListWith("a", "bc", "def").mkString()).isEqualTo("a,bc,def")
    assertThat(FastList.newListWith("a", "bc", "def").mkString("|")).isEqualTo("a|bc|def")

    val map = SortedMaps.mutable.of("a", 1, "b", 2, "c", 3)
    assertThat(map.mkString<String, Int>()).isEqualTo("a=1,b=2,c=3")
    assertThat(map.mkString<String, Int>("|")).isEqualTo("a=1|b=2|c=3")
  }

  @Test
  fun joinString() {
    assertThat(arrayOf("a", "bc", "def").join()).isEqualTo("a,bc,def")
    assertThat(arrayOf("a", "bc", "def").join()).isEqualTo("a,bc,def")
    assertThat(arrayOf("a", "bc", "def").join("|")).isEqualTo("a|bc|def")

    assertThat(FastList.newListWith("a", "bc", "def").join()).isEqualTo("a,bc,def")
    assertThat(FastList.newListWith("a", "bc", "def").join("|")).isEqualTo("a|bc|def")

    val map = SortedMaps.mutable.of("a", 1, "b", 2, "c", 3)
    assertThat(map.join<String, Int>()).isEqualTo("a=1,b=2,c=3")
    assertThat(map.join<String, Int>("|")).isEqualTo("a=1|b=2|c=3")
  }

}
