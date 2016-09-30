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
import debop4k.core.io.toStringList
import lombok.SneakyThrows
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException
import kotlin.test.fail

/**
 * ResourcexKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class ResourcexKotlinTest : AbstractCoreKotlinTest() {

  @Test
  @SneakyThrows(IOException::class)
  fun loadResources() {
    Resourcex.getClassPathResourceStream("logback-test.xml")?.use { inputStream ->
      assertThat(inputStream).isNotNull()
      val lines = inputStream.toStringList()
      assertThat(lines.size).isGreaterThan(0)
    } ?: fail("not found")
  }

  @Test
  @SneakyThrows(IOException::class)
  fun loadResourcesByClassLoader() {
    Resourcex.getClassPathResourceStream("logback-test.xml", javaClass.classLoader)?.use { inputStream ->
      assertThat(inputStream).isNotNull()
      val lines = inputStream.toStringList()
      assertThat(lines.size).isGreaterThan(0)
    } ?: fail("not found")
  }

  @Test
  fun readString() {
    val xml = Resourcex.getString("logback-test.xml")
    assertThat(xml).isNotEmpty()
    log.debug("xml={}", xml)
  }
}