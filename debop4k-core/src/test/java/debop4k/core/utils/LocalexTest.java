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

package debop4k.core.utils;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static debop4k.core.utils.Localex.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * LocalexTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 15.
 */
@Slf4j
public class LocalexTest extends AbstractCoreTest {

  @Test
  public void systemDefaultLocale() {
    if (Locale.getDefault() == Locale.KOREA)
      assertThat(isDefault(Locale.KOREA)).isTrue();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void orDefaultWithNull() {
    Locale nullable = null;
    assertThat(orDefault(nullable)).isEqualTo(Locale.getDefault());
  }

  @Test
  public void parentLocale() {
    assertThat(getParent(Locale.KOREA)).isEqualTo(Locale.KOREAN);
    assertThat(getParent(Locale.KOREAN)).isNull();
    assertThat(getParent(Locale.US)).isEqualTo(Locale.ENGLISH);
  }

  @Test
  public void allParents() {
    assertThat(getParents(Locale.KOREA))
        .hasSize(2)
        .contains(Locale.KOREA, Locale.KOREAN);
    assertThat(getParents(Locale.KOREAN))
        .hasSize(1)
        .contains(Locale.KOREAN);

    assertThat(getParents(Locale.US))
        .hasSize(2)
        .contains(Locale.US, Locale.ENGLISH);
    assertThat(getParents(Locale.ENGLISH))
        .hasSize(1)
        .contains(Locale.ENGLISH);
  }

  @Test
  public void findLocaleFiles() {
    List<String> filenames = calculateFilenamesForLocale("msg", Locale.KOREA);
    assertThat(filenames)
        .hasSize(3)
        .contains("msg_ko_KR", "msg_ko", "msg");
  }
}
