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

import debop4k.core.AbstractCoreKotlinTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 * LocaleKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class LocaleKotlinTest : AbstractCoreKotlinTest() {

  @Test
  fun systemDefaultLocale() {
    if (Locale.getDefault() == Locale.KOREA)
      assertThat(Locale.KOREA.isDefault()).isTrue()
  }

  @Test
  fun orDefaultWithNull() {
    val nullable: Locale? = null
    assertThat(nullable.orDefault()).isEqualTo(Locale.getDefault())
  }

  @Test
  fun parentLocale() {
    assertThat(Locale.KOREA.getParent()).isEqualTo(Locale.KOREAN)
    assertThat(Locale.KOREAN.getParent()).isNull()
    assertThat(Locale.US.getParent()).isEqualTo(Locale.ENGLISH)
  }

  @Test
  fun allParents() {
    assertThat(Locale.KOREA.getParents()).contains(Locale.KOREA, Locale.KOREAN).hasSize(2)
    assertThat(Locale.KOREAN.getParents()).contains(Locale.KOREAN).hasSize(1)

    assertThat(Locale.US.getParents()).contains(Locale.US, Locale.ENGLISH).hasSize(2)
    assertThat(Locale.ENGLISH.getParents()).contains(Locale.ENGLISH).hasSize(1)
  }

  @Test
  fun findLocaleFiles() {
    val filenames: List<String> = calculateFilenamesForLocale("msg", Locale.KOREA)
    assertThat(filenames).contains("msg_ko_KR", "msg_ko", "msg").hasSize(3)
  }
}