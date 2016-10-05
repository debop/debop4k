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
import debop4k.core.io.IOStreamx;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ResourceExTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 15.
 */
@Slf4j
public class ResourceExTest extends AbstractCoreTest {

  @Test
  @SneakyThrows({IOException.class})
  public void loadResources() {
    InputStream is = Resources.getClassPathResourceStream("logback-test.xml");
    try {
      assertThat(is).isNotNull();
      List<String> lines = IOStreamx.toStringList(is);
      assertThat(lines.size()).isGreaterThan(0);
    } finally {
      is.close();
    }
  }

  @Test
  @SneakyThrows({IOException.class})
  public void loadResourcesByClassLoader() {
    InputStream is = Resources.getClassPathResourceStream("logback-test.xml", getClass().getClassLoader());
    try {
      assertThat(is).isNotNull();
      List<String> lines = IOStreamx.toStringList(is);
      assertThat(lines.size()).isGreaterThan(0);
    } finally {
      is.close();
    }
  }

  @Test
  public void readString() {
    String xml = Resources.getString("logback-test.xml");
    assertThat(xml).isNotEmpty();
    log.debug("xml={}", xml);
  }
}
