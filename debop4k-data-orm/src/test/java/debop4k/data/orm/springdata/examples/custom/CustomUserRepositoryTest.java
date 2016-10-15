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

package debop4k.data.orm.springdata.examples.custom;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomRepositoryConfiguration.class})
@Transactional
@Slf4j
public class CustomUserRepositoryTest {

  @Inject UserRepository repository;

  @Test
  public void testInsert() {
    User user = repository.save(new User("debop"));
    User loaded = repository.findOne(user.getId());
    assertThat(loaded).isEqualTo(user);
  }

  @Test
  public void saveAndFindByLastnameAndFindByUsername() {
    User user = new User("debop");
    user.setLastname("lastname");

    user = repository.save(user);

    List<User> users = repository.findByLastname("lastname");
    Assertions.assertThat(users).isNotNull().contains(user);

    User reference = repository.findByUsername("debop");
    assertThat(reference).isEqualTo(user);
  }

  /**
   * UserRepositoryImpl 에 사용자 정의 메소드를 테스트 합니다.
   */
  @Test
  public void testCustomMethod() {
    User user = new User("debop");
    user.setLastname("lastname");

    user = repository.saveAndFlush(user);

    List<User> users = repository.customBatchOperation();
    Assertions.assertThat(users).isNotNull().contains(user);
  }

}
