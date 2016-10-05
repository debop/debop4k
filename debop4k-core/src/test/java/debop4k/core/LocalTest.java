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

import debop4k.core.cryptography.Cryptographyx;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * LocalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 15.
 */
@Slf4j
public class LocalTest extends AbstractCoreTest {

  int COUNT = 100;
  Random random = Cryptographyx.SECURE_RANDOM;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class User implements Serializable {
    private static final long serialVersionUID = -3738537974658047578L;
    private String name;
    private String password;
    private int age;
  }

  @Before
  public void setup() {
    Local.clearAll();
  }

  @Test
  @SneakyThrows
  public void handlePrimitives() {
    String key = "Local.Value.Key";
    for (int i = 0; i < COUNT; i++) {
      Local.set(key, i);
      Thread.sleep(1);

      assertThat(Local.<Integer>get(key)).isNotNull().isInstanceOf(Integer.class);
      int stored = Local.get(key);
      assertThat(stored).isEqualTo(i);
      log.trace("put and get primitive type. stored={}", stored);
    }
  }

  @Test
  @SneakyThrows
  public void handleReferenceValue() {
    String key = "Local.Reference.Key";
    for (int i = 0; i < COUNT; i++) {
      User user = new User("user", "P" + Thread.currentThread().getId(), random.nextInt());
      Local.set(key, user);
      Thread.sleep(1);

      try {
        User stored = Local.get(key);
        assertThat(stored).isNotNull().isEqualTo(user);
        log.trace("put and get reference object. stored={}", stored);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        fail("Not found user");
      }
    }
  }
}
