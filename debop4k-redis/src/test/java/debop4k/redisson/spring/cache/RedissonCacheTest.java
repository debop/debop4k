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

package debop4k.redisson.spring.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedissonCacheConfiguration.class})
public class RedissonCacheTest {

  @Inject UserRepository userRepo;

  @Test
  public void testConfiguration() {
    assertThat(userRepo).isNotNull();
  }

  @Test
  public void testNull() {
    userRepo.save("user1", null);
    assertThat(userRepo.getNull("user1")).isNull();
    userRepo.remove("user1");
    assertThat(userRepo.getNull("user1")).isNull();
  }

  @Test
  public void testRemove() {
    userRepo.save("user1", UserRepository.UserObject.of("name1", "value1"));
    assertThat(userRepo.get("user1"))
        .isNotNull()
        .isInstanceOf(UserRepository.UserObject.class);
    userRepo.remove("user1");
    assertThat(userRepo.getNull("user1")).isNull();
  }

  @Test
  public void testPutGet() {
    userRepo.save("user1", UserRepository.UserObject.of("name1", "value1"));
    UserRepository.UserObject u = userRepo.get("user1");
    assertThat(u).isNotNull();
    assertThat(u.getName()).isEqualTo("name1");
    assertThat(u.getValue()).isEqualTo("value1");
  }

  @Test(expected = IllegalStateException.class)
  public void testGet() {
    userRepo.get("notExists");
  }
}
