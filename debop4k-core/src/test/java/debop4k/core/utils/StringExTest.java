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

package debop4k.core.utils;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.eclipse.collections.impl.factory.SortedMaps;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static debop4k.core.collections.Arrayx.getEmptyCharArray;
import static debop4k.core.utils.Stringx.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class StringExTest extends AbstractCoreTest {

  static final String spaceTextt = "           ";
  static final String whitespaceText = "\n \r \t \f";

  @Test
  public void testIsNull() {
    assertThat(isNull(null)).isTrue();
    assertThat(isNull("")).isFalse();
    assertThat(isNotNull(null)).isFalse();
    assertThat(isNotNull("")).isTrue();
  }

  @Test
  public void testIsEmpty() {
    assertThat(isEmpty(null)).isTrue();
    assertThat(isEmpty("")).isTrue();
    assertThat(isEmpty(spaceTextt)).isTrue();
    assertThat(isEmpty(whitespaceText)).isTrue();
    assertThat(isEmpty("null")).isFalse();

    assertThat(isNotEmpty(null)).isFalse();
    assertThat(isNotEmpty("")).isFalse();
    assertThat(isNotEmpty(spaceTextt)).isFalse();
    assertThat(isNotEmpty(whitespaceText)).isFalse();
    assertThat(isNotEmpty("null")).isTrue();
  }

  @Test
  public void testEllipsis() {
    assertThat(needEllipsis(sampleText, sampleText.length() * 2)).isFalse();
    assertThat(needEllipsis(sampleText, sampleText.length() / 2)).isTrue();

    assertThat(ellipsisEnd(sampleText, 10)).endsWith(TRIMMING);
    assertThat(ellipsisStart(sampleText, 10)).startsWith(TRIMMING);
    assertThat(ellipsisMid(sampleText, 10))
        .is(new Condition<String>() {
          @Override
          public boolean matches(String value) {
            return !value.startsWith(TRIMMING);
          }
        })
        .is(new Condition<String>() {
          @Override
          public boolean matches(String value) {
            return !value.endsWith(TRIMMING);
          }
        })
        .contains(TRIMMING);
  }

  @Test
  public void covertToHexString() {
    String text = "123 123";
    String hexText = "31323320313233";

    assertThat(byteArrayToHexString(toUtf8Bytes(text))).isEqualTo(hexText);
    assertThat(toUtf8String(hexStringToByteArray(hexText))).isEqualTo(text);
  }

  @Test
  public void convertToBase64String() {
    String base64Str = byteArrayToBase64String(sampleBytes);
    byte[] bytes = base64StringToByteArray(base64Str);
    String converted = toUtf8String(bytes);

    assertThat(bytes).isEqualTo(sampleBytes);
    assertThat(converted).isEqualTo(sampleText);
  }

  @Test
  public void utf8StringToBytes() {
    byte[] bytes = toUtf8Bytes(sampleText);
    String converted = toUtf8String(bytes);

    assertThat(converted).isEqualTo(sampleText);
  }

  @Test
  public void testDeleteChar() {
    String text = "abcdefgh";
    assertThat(deleteChar(text, 'c', 'f')).isEqualTo("abdegh");
    assertThat(deleteChar(text, 'a', 'h')).isEqualTo("bcdefg");
  }

  @Test
  public void testConcat() {
    assertThat(concat(asList("a", "b", "c"), ",")).isEqualTo("a,b,c");
  }

  @Test
  public void stringSplitByCharacter() {
    String attlStr = "37|75|95|107|133|141|142|147|148|178";
    String atvlStr = "9 ||||2999999|||20091231|KR,KR,graph,c836|";

    List<String> attls = splits(attlStr, '|');
    List<String> atvls = splits(atvlStr, '|');

    log.debug("attls size={}, {}", attls.size(), attls);
    log.debug("atvls size={}, {}", atvls.size(), atvls);

  }

  @Test
  public void stringSplit() {
    String str = "동해,물 || 백두,산 a BaB";
    List<String> strList = splits(str, true, true, ",", "||", "A");
    assertThat(strList).contains("동해", "물", "백두", "산", "B", "B")
                       .hasSize(6);

    List<String> caseStrs = splits(str, false, true, ",", "||", "A");
    assertThat(caseStrs).contains("동해", "물", "백두", "산 a BaB")
                        .hasSize(4);
  }

  @Test
  public void stringSplitIgnoreCase() {
    String text = "Hello World! Hello java^^";
    List<String> result = splits(text, true, true, "!");
    assertThat(result).hasSize(2).contains("Hello World", "Hello java^^");

    result = splits(text, false, true, "hello");
    assertThat(result).hasSize(1).contains(text);

    result = splits(text, true, true, "hello");
    assertThat(result).hasSize(2).contains("World!", "java^^");

    result = splits(text, true, true, "hello", "java");
    assertThat(result).hasSize(2).contains("World!", "^^");

    result = splits(text, true, true, "||");
    assertThat(result).hasSize(1).contains(text);
  }

  @Test
  public void testWordCount() {
    String text = replicate(sampleText, 10);
    assertThat(wordCount(text, "동해")).isEqualTo(10);
  }

  @Test
  public void testFirstLine() {
    String text = join(asList(sampleText, sampleText, sampleText), getLINE_SEPARATOR());

    assertThat(firstLine(text)).isEqualTo(sampleText);
    assertThat(firstLine(text, getLINE_SEPARATOR())).isEqualTo(sampleText);
  }

  @Test
  public void testBetween() {
    String text = "서울특별시 성북구 정릉동 현대아파트 107-301 서울특별시 성북구 정릉동 현대아파트 107-301";
    assertThat(between(text, "", "")).isEmpty();
    assertThat(between(text, "별", "동")).isEqualTo("시 성북구 정릉");
    assertThat(between(text, "", "특")).isEqualTo("서울");
    assertThat(between(text, "3", "")).isEqualTo("01 서울특별시 성북구 정릉동 현대아파트 107-301");


    assertThat(between("abcdefg", "c", "g")).isEqualTo("def");
    assertThat(between("abcdefg", "", "c")).isEqualTo("ab");

    // NOTE: lastIndexOf 의 fromIndex 는 뒤에서부터 찾을 때이 index 값을 줘야 한다. (0 을 주면 항상 -1 이다)
    String sample = "Hello World! Sunghyouk Bae Hello World";
    assertThat(sample.lastIndexOf("Wor", 40)).isEqualTo(33);
    assertThat(sample.lastIndexOf("Wor", 0)).isEqualTo(-1);
  }

  @Test
  public void toCharArrayTest() {
    assertThat(toCharArray(null)).isEqualTo(getEmptyCharArray());
    assertThat(toCharArray("")).isEqualTo(getEmptyCharArray());
    assertThat(toCharArray("abc")).hasSize(3).contains('a', 'b', 'c');
  }

  @Test
  public void mkStringTest() {
    assertThat(mkString(new String[]{"a", "bc", "def"})).isEqualTo("a,bc,def");
    assertThat(mkString(new String[]{"a", "bc", "def"})).isEqualTo("a,bc,def");
    assertThat(mkString(new String[]{"a", "bc", "def"}, "|")).isEqualTo("a|bc|def");

    assertThat(mkString(FastList.newListWith("a", "bc", "def"))).isEqualTo("a,bc,def");
    assertThat(mkString(FastList.newListWith("a", "bc", "def"), "|")).isEqualTo("a|bc|def");

    Map<String, Integer> map = SortedMaps.mutable.of("a", 1, "b", 2, "c", 3);
    assertThat(mkString(map)).isEqualTo("a=1,b=2,c=3");
    assertThat(mkString(map, "|")).isEqualTo("a=1|b=2|c=3");
  }

  @Test
  public void joinString() {
    assertThat(join(new String[]{"a", "bc", "def"})).isEqualTo("a,bc,def");
    assertThat(join(new String[]{"a", "bc", "def"})).isEqualTo("a,bc,def");
    assertThat(join(new String[]{"a", "bc", "def"}, "|")).isEqualTo("a|bc|def");

    assertThat(join(FastList.newListWith("a", "bc", "def"))).isEqualTo("a,bc,def");
    assertThat(join(FastList.newListWith("a", "bc", "def"), "|")).isEqualTo("a|bc|def");

    Map<String, Integer> map = SortedMaps.mutable.of("a", 1, "b", 2, "c", 3);
    assertThat(join(map)).isEqualTo("a=1,b=2,c=3");
    assertThat(join(map, "|")).isEqualTo("a=1|b=2|c=3");
  }


}
