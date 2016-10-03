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

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class SystemsTest {

  @Test
  public void loadSystemPropertiesFromJava() {
    Properties props = System.getProperties();
    for (Object key : props.keySet()) {
      log.debug("key={}, value={}", key, props.get(key));
    }
  }

  @Test
  public void loadSystemProperties() {
    Properties props = Systems.getSystemProps();
    assertThat(props).isNotNull();
    assertThat(props.size()).isGreaterThan(0);

    for (Object key : props.keySet()) {
      log.debug("key={}, value={}", key, props.get(key));
    }
  }

  @Test
  public void loadPropertiesByName() {
    log.debug("java home={}", Systems.getJavaHome());
    assertThat(Systems.getJavaHome()).isNotEmpty().containsIgnoringCase("Java");
    log.debug("temp dir={}", Systems.getTempDir());

    log.debug("file separator={}", Systems.getFileSeparator());
    assertThat(Systems.getFileSeparator()).isNotEmpty();
    assertThat(Systems.getPathSeparator()).isNotEmpty();
    assertThat(Systems.getLineSeparator()).isNotEmpty();

    log.debug("java class version={}", Systems.getJavaClassVersion());
    assertThat(Systems.getJavaClassVersion()).isNotEmpty().contains("5");
  }

}
