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

package debop4k.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static debop4k.core.Guardx.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * GuardTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class GuardTest {

  @Test
  public void shouldBeTrue() {
    shouldBe(1 > 0, "always true");
  }

  @Test(expected = AssertionError.class)
  public void shouldBeFalse() {
    shouldBe(1 > 2, "always false");
  }

  @Test
  public void shouldNotBeNullWithNotNull() {
    assertThat(shouldNotBeNull("value", "always true")).isEqualTo("value");
  }

  @Test(expected = AssertionError.class)
  public void shouldNotBeNullWithNull() {
    final Object value = null;
    shouldNotBeNull(value, "oops! it's null");
  }

  @Test
  public void shouldBeInRangeWithTrue() {
    shouldBeInRange(3, 0, 5, "value");
  }

  @Test(expected = AssertionError.class)
  public void shouldBeInRangeWithFalse() {
    shouldBeInRange(5, 0, 5, "value");
  }
}
