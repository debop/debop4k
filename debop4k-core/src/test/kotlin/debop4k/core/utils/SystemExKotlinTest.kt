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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * SystemExKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class SystemExKotlinTest : AbstractCoreKotlinTest() {

  @Test fun retrieveJavaSpecificationVersion() {
    log.debug("java version={}, implementation={}", SystemEx.javaVersion, SystemEx.javaImplementationVersion)
    log.debug("java vendor={}, implementation={}", SystemEx.javaVendor, SystemEx.javaImplementationVendor)

    log.debug("Java6={}", SystemEx.isJava6)
    log.debug("Java7={}", SystemEx.isJava7)
    log.debug("Java8={}", SystemEx.isJava8)
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
    val props = SystemEx.systemProps
    assertThat(props).isNotNull()
    assertThat(props.size).isGreaterThan(0)

    for (key in props.keys) {
      log.debug("key={}, value={}", key, props[key])
    }
  }

  @Test
  fun loadPropertiesByName() {
    log.debug("java home={}", SystemEx.javaHome)
    assertThat(SystemEx.javaHome).isNotEmpty().containsIgnoringCase("Java")
    log.debug("temp dir={}", SystemEx.tempDir)

    log.debug("file separator={}", SystemEx.fileSeparator)
    assertThat(SystemEx.fileSeparator).isNotEmpty()
    assertThat(SystemEx.pathSeparator).isNotEmpty()
    assertThat(SystemEx.lineSeparator).isNotEmpty()

    log.debug("java class version={}", SystemEx.javaClassVersion)
    assertThat(SystemEx.javaClassVersion).isNotEmpty().contains("5")
  }

}