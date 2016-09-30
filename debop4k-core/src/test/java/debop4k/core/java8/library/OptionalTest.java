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

package debop4k.core.java8.library;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * OptionalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 10.
 */
public class OptionalTest {

  @Test
  public void empty() {
    Optional empty = Optional.empty();
    Optional<Object> alsoEmpty = Optional.ofNullable(null);

    assertFalse(empty.isPresent());
  }

  @Test
  public void isPresent() {
    Optional<Integer> a = Optional.of(1);
    assertTrue(a.isPresent());
    assertSame(1, a.get());
  }

  @Test
  public void orElse() {
    Optional<String> empty = Optional.empty();
    assertEquals("b", empty.orElse("b"));
    assertEquals("c", empty.orElseGet(() -> "c"));
  }
}
