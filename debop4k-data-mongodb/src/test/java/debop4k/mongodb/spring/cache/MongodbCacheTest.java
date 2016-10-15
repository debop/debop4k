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

package debop4k.mongodb.spring.cache;

import debop4k.core.utils.With;
import debop4k.mongodb.AbstractMongoTest;
import debop4k.mongodb.models.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MongodbCacheConfiguration.class})
public class MongodbCacheTest extends AbstractMongoTest {

  @Inject MongodbCacheManager cacheManager;
  @Inject UserRepository userRepo;
  @Inject MongoTemplate mongo;

  @Before
  public void setup() {
//    mongo.dropCollection("user");
  }

  @After
  public void cleanup() {
//    mongo.dropCollection("user");
  }

  @Test
  public void configuration() {
    assertThat(cacheManager).isNotNull();
    Cache cache = cacheManager.getCache("test");
    assertThat(cache).isNotNull();
    cache.clear();
  }

  @Test
  @SneakyThrows(Exception.class)
  public void userCache() {

    final String userId = UUID.randomUUID().toString();
    final List<User> users = FastList.newList();

    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        User user = userRepo.getUser(userId, 100);
        users.add(user);
      }
    });

    Thread.sleep(1000);

    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        User user = userRepo.getUser(userId, 200);
        users.add(user);
      }
    });

    User user1 = users.get(0);
    User user2 = users.get(1);

    assertThat(user1).isEqualTo(user2);
    assertThat(user1.getFavoriteMovies().size()).isEqualTo(user2.getFavoriteMovies().size());
  }

  @Test
  @SneakyThrows(Exception.class)
  public void userCacheEvict() {

    final String userId = UUID.randomUUID().toString();
    final List<User> users = FastList.newList();

    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        User user = userRepo.getUser(userId, 100);
        users.add(user);
      }
    });

    Thread.sleep(500);

    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        User user = userRepo.getUser(userId, 200);
        users.add(user);
      }
    });

    Thread.sleep(200);

    User user1 = users.get(0);
    User user2 = users.get(1);

    userRepo.updateUser(user1);

    Thread.sleep(200);

    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        User user = userRepo.getUser(userId, 200);
        users.add(user);
      }
    });

    User user3 = users.get(2);

    assertThat(user1).isEqualTo(user2);
    assertThat(user1.getFavoriteMovies().size()).isEqualTo(user2.getFavoriteMovies().size());
    assertThat(user3.getFavoriteMovies().size()).isNotEqualTo(user1.getFavoriteMovies().size());

  }
}
