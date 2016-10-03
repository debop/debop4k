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

/**
 * SystemsKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class SystemsKotlinTest : AbstractCoreKotlinTest() {

  @Test fun retrieveJavaSpecificationVersion() {
    log.debug("java version={}, implementation={}", Systems.javaVersion, Systems.javaImplementationVersion)
    log.debug("java vendor={}, implementation={}", Systems.javaVendor, Systems.javaImplementationVendor)

    log.debug("Java6={}", Systems.isJava6)
    log.debug("Java7={}", Systems.isJava7)
    log.debug("Java8={}", Systems.isJava8)
  }

  @Test
  fun loadSystemPropertiesFromJava() {
    val props = System.getProperties()
    for (key in props.keys) {
      log.debug("key={}, value={}", key, props[key])
    }
  }

  @Test
  fun loadSystemProperties() {
    val props = Systems.systemProps
    assertThat(props).isNotNull()
    assertThat(props.size).isGreaterThan(0)

    for (key in props.keys) {
      log.debug("key={}, value={}", key, props[key])
    }
  }

  @Test
  fun loadPropertiesByName() {
    log.debug("java home={}", Systems.javaHome)
    assertThat(Systems.javaHome).isNotEmpty().containsIgnoringCase("Java")
    log.debug("temp dir={}", Systems.tempDir)

    log.debug("file separator={}", Systems.fileSeparator)
    assertThat(Systems.fileSeparator).isNotEmpty()
    assertThat(Systems.pathSeparator).isNotEmpty()
    assertThat(Systems.lineSeparator).isNotEmpty()

    log.debug("java class version={}", Systems.javaClassVersion)
    assertThat(Systems.javaClassVersion).isNotEmpty().contains("5")
  }

}