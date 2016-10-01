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

package debop4k.data.orm.springdata.examples.simple;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SimpleConfiguration.class})
@Transactional
@Slf4j
public class SimpleTest {

  @Inject SimpleUserRepository repository;

  User user;

  @Before
  public void setup() {
    user = new User("foobar");
    user.setFirstname("firstname");
    user.setLastname("lastname");
  }

  @Test
  public void findById() {
    user = repository.save(user);

    assertThat(repository.findOne(user.getId())).isEqualTo(user);
  }

  @Test
  public void findUserByLastname() {
    user = repository.save(user);

    List<User> users = repository.findByLastname(user.getLastname());
    assertThat(users).hasSize(1).containsOnly(user);
  }

  @Test
  public void findByFirstnameOrLastname() {
    user = repository.save(user);

    List<User> users = repository.findByFirstnameOrLastname(user.getLastname());
    assertThat(users).hasSize(1).containsOnly(user);
  }

  @Test
  public void findByUsername() {
    assertThat(repository.findByUsername(user.getUsername())).isNull();

    repository.save(user);
    assertThat(repository.findByUsername(user.getUsername())).isNotNull();
  }

  @Test
  public void removeByLastname() {
    User user2 = new User("user2");
    user2.setLastname(user.getLastname());

    User user3 = new User("user3");
    user3.setLastname("no-positive-match");

    repository.save(asList(user, user2, user3));

    assertThat(repository.removeByLastname(user.getLastname())).isEqualTo(2);
    assertThat(repository.exists(user3.getId())).isTrue();
  }

  @Test
  public void sliceToLoadContent() {
    repository.deleteAll();

    List<User> sources = FastList.newList();
    for (int i = 0; i < 100; i++) {
      User user = new User(this.user.getLastname() + String.format("-%03d", i));
      user.setLastname(this.user.getLastname());
      sources.add(user);
    }

    repository.save(sources);
    Slice<User> users = repository.findByLastnameOrderByUsernameAsc(this.user.getLastname(), new PageRequest(1, 5));
    assertThat(users).containsOnlyElementsOf(sources.subList(5, 10));
  }

  @Test
  public void findFirst2ByOrderByLastnameAsc() {
    User user0 = new User("user0");
    user0.setLastname("lastname-0");

    User user1 = new User("user1");
    user1.setLastname("lastname-1");

    User user2 = new User("user2");
    user2.setLastname("lastname-2");

    repository.save(asList(user2, user1, user0));

    List<User> result = repository.findFirst2ByOrderByLastnameAsc();

    assertThat(result).hasSize(2).containsOnly(user0, user1);
  }

  @Test
  public void findTop2BySort() {
    User user0 = new User("user0");
    user0.setLastname("lastname-0");

    User user1 = new User("user1");
    user1.setLastname("lastname-1");

    User user2 = new User("user2");
    user2.setLastname("lastname-2");

    // save by reverse
    repository.save(asList(user2, user1, user0));

    List<User> resultAsc = repository.findTop2By(new Sort(ASC, "lastname"));
    assertThat(resultAsc).hasSize(2).containsOnly(user0, user1);

    List<User> resultDesc = repository.findTop2By(new Sort(DESC, "lastname"));
    assertThat(resultDesc).hasSize(2).containsOnly(user2, user1);
  }

  @Test
  public void findByFirstnameOrLastnameUsingSpringEL() {
    User first = new User("userFirst");
    first.setLastname("lastname");

    User second = new User("userSecond");
    second.setFirstname("firstname");

    User third = new User("user3");

    repository.save(asList(first, second, third));

    User reference = new User("reference");
    reference.setFirstname("firstname");
    reference.setLastname("lastname");

    Iterable<User> users = repository.findByFirstnameOrLastname(reference);
    assertThat(users).hasSize(2).containsOnly(first, second);
  }
}
