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

package debop4k.data.orm.mapping.associations.onetomany.list;

import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.data.orm.mapping.associations.onetomany.list.models.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {OneToManyConfiguration.class})
public class OneToManyListTests extends AbstractMappingTest {

  @Test
  public void simpleOneToMany() {
    Order order = new Order("123456");
    OrderItem item1 = new OrderItem("Item1");
    OrderItem item2 = new OrderItem("Item2");
    order.addItems(item1, item2);

    em.persist(order);
    em.flush();
    em.clear();

    Order loaded = em.find(Order.class, order.getId());
    Assertions.assertThat(loaded).isNotNull();
    Assertions.assertThat(loaded.getItems()).hasSize(2);

    OrderItem item = loaded.getItems().get(0);
    loaded.removeItems(item);
    em.persist(loaded);
    em.flush();
    em.clear();

    Order order3 = em.find(Order.class, order.getId());
    Assertions.assertThat(order3).isNotNull();
    Assertions.assertThat(order3.getItems()).hasSize(1);

    em.remove(order3);
    em.flush();

    assertThat(em.find(Order.class, order.getId())).isNull();
  }

  @Test
  public void testUser() {
    User user = new User("성혁");
    user.getNicknames().add("debop");
    user.getNicknames().add("debop68");
    user.getAddresses().put("집", new Address("서울"));
    user.getAddresses().put("회사", new Address("부산"));

    em.persist(user);
    em.flush();
    em.clear();

    User loaded = em.find(User.class, user.getId());
    Assertions.assertThat(loaded).isNotNull();
    Assertions.assertThat(loaded.getNicknames()).hasSize(2).containsOnly("debop", "debop68");
    Assertions.assertThat(loaded.getAddresses()).hasSize(2).containsKeys("집", "회사");

    em.remove(loaded);
    em.flush();
    em.clear();

    assertThat(em.find(User.class, user.getId())).isNull();
  }

  @Test
  public void unidirectional() {
    Father father = new Father("이성계");
    Child child1 = new Child("1.방원");
    Child child2 = new Child("2.방석");
    father.getOrderedChildren().addAll(Arrays.asList(child1, child2));

    em.persist(father);
    em.flush();
    em.clear();

    Father loaded = em.find(Father.class, father.getId());
    Assertions.assertThat(loaded).isNotNull().isEqualTo(father);
    Assertions.assertThat(loaded.getOrderedChildren()).hasSize(2);
    Assertions.assertThat(loaded.getOrderedChildren().get(0)).isEqualTo(child1);
    Assertions.assertThat(loaded.getOrderedChildren().get(1)).isEqualTo(child2);

    em.remove(loaded);
    em.flush();
    em.clear();

    assertThat(em.find(Father.class, father.getId())).isNull();
  }
}
