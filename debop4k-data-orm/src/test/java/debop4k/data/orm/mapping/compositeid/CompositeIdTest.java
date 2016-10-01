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

package debop4k.data.orm.mapping.compositeid;

import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.data.orm.mapping.compositeid.models.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {CompositeIdConfiguration.class})
public class CompositeIdTest extends AbstractMappingTest {

  @Test
  public void embeddedId() {
    EmbeddableIdCar car = new EmbeddableIdCar(new EmbeddableCarIdentifier("Kia", 2012));
    car.setSerialNo("6675");
    em.persist(car);
    em.flush();
    em.clear();

    EmbeddableIdCar loaded = em.find(EmbeddableIdCar.class, car.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded).isEqualTo(car);
    assertThat(loaded.getId().getBrand()).isEqualTo(car.getId().getBrand());

    loaded.setSerialNo("5081");
    em.persist(loaded);
    em.flush();
    em.clear();

    loaded = em.find(EmbeddableIdCar.class, car.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded).isEqualTo(car);
    assertThat(loaded.getId().getBrand()).isEqualTo(car.getId().getBrand());

    em.remove(loaded);
    em.flush();

    assertThat(em.find(EmbeddableIdCar.class, car.getId())).isNull();
  }

  @Test
  public void idClass() {
    CarIdentifier identifier = new CarIdentifier("KIA", 2012);
    IdClassCar car = new IdClassCar(identifier);
    car.setSerialNo("6675");
    em.persist(car);
    em.flush();
    em.clear();

    IdClassCar loaded = em.find(IdClassCar.class, identifier);
    assertThat(loaded).isNotNull();
    assertThat(loaded).isEqualTo(car);
    assertThat(loaded.getBrand()).isEqualTo(car.getBrand());

    loaded.setSerialNo("5081");
    em.persist(loaded);
    em.flush();
    em.clear();

    loaded = em.find(IdClassCar.class, identifier);
    assertThat(loaded).isNotNull();
    assertThat(loaded).isEqualTo(car);
    assertThat(loaded.getBrand()).isEqualTo(car.getBrand());

    em.remove(loaded);
    em.flush();

    assertThat(em.find(IdClassCar.class, identifier)).isNull();
  }

  @Test
  public void orderDetail() {
    Order order = new Order("ORDER-1", new Date());

    Product product1 = new Product("맥북프로");
    Product product2 = new Product("자동차");

    OrderDetail orderDetail1 = new OrderDetail(order, product1);
    OrderDetail orderDetail2 = new OrderDetail(order, product2);

    em.persist(product1);
    em.persist(product2);
    em.persist(order);
    em.persist(orderDetail1);
    em.persist(orderDetail2);
    em.flush();
    em.clear();

    OrderDetail loaded1 = em.find(OrderDetail.class, orderDetail1.getId());
    assertThat(loaded1).isNotNull();
    assertThat(loaded1.getId().getOrder()).isEqualTo(order);
    assertThat(loaded1.getId().getProduct()).isEqualTo(product1);

    em.remove(loaded1.getId().getOrder());
    em.remove(loaded1.getId().getProduct());
    em.flush();

    assertThat(em.find(Order.class, order.getId())).isNull();
  }
}
