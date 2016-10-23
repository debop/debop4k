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
 */

package debop4k.spring.core

import debop4k.core.uninitialized
import debop4k.spring.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.core.env.*
import java.util.*

class PropertyResolverTest : AbstractSpringTest() {

  private var testProperties: Properties = uninitialized()
  private var propertySources: MutablePropertySources = uninitialized() // by Delegates.notNull()
  private var propertyResolver: PropertyResolver = uninitialized() //by Delegates.notNull()

  @Before
  fun setup() {
    testProperties = Properties()
    propertySources = MutablePropertySources().apply {
      addFirst(PropertiesPropertySource("testProperties", testProperties))
    }
    propertyResolver = PropertySourcesPropertyResolver(propertySources)
  }

  @Test
  fun getProperty() {

    assertThat(propertyResolver["foo"]).isNull()
    assertThat(propertyResolver["num"]).isNull()
    assertThat(propertyResolver["enabled"]).isNull()

    testProperties["foo"] = "bar"
    testProperties["num"] = 5
    testProperties["enabled"] = true

    assertThat(propertyResolver["foo"]).isEqualTo("bar")
    assertThat(propertyResolver["num", Int::class.java]).isEqualTo(5)
    assertThat(propertyResolver["enabled", Boolean::class.java]).isEqualTo(true)
  }

  @Test
  fun getPropertyWithDefaultValue() {
    assertThat(propertyResolver["foo", "myDefault"]).isEqualTo("myDefault")
    assertThat(propertyResolver["num", Int::class.java, 5]).isEqualTo(5)
    assertThat(propertyResolver["enabled", Boolean::class.java, true]).isEqualTo(true)

    testProperties["foo"] = "bar"
    testProperties["num"] = 42
    testProperties["enabled"] = true

    assertThat(propertyResolver["foo"]).isEqualTo("bar")
    assertThat(propertyResolver["num", Int::class.java]).isEqualTo(42)
    assertThat(propertyResolver["enabled", Boolean::class.java]).isEqualTo(true)
  }
}