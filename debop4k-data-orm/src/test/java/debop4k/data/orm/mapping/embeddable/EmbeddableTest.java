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

package debop4k.data.orm.mapping.embeddable;

import debop4k.data.orm.mapping.AbstractMappingTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmbeddableConfiguration.class})
public class EmbeddableTest extends AbstractMappingTest {

  @Test
  public void embeddableEntity() {
    User user = new User();

    user.setUsername("debop");
    user.setPassword("1234");
    user.setEmail("debop@gmail.com");

    user.getHomeAddress().setCity("서울");
    user.getHomeAddress().setStreet("정릉");
    user.getHomeAddress().setZipcode("100-200");

    user.getOfficeAddress().setCity("부산");
    user.getOfficeAddress().setStreet("센텀");
    user.getOfficeAddress().setZipcode("500-100");

    em.persist(user);
    em.flush();
    em.clear();

    User loaded = em.find(User.class, user.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getHomeAddress()).isEqualTo(user.getHomeAddress());
    assertThat(loaded.getOfficeAddress()).isEqualTo(user.getOfficeAddress());

    em.remove(loaded);
    em.flush();

    Assertions.assertThat(em.find(User.class, user.getId())).isNull();
  }
}
