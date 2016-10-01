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

package debop4k.data.orm.mapping.associations.onetomany.map;

import debop4k.data.orm.mapping.AbstractMappingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {OneToManyMapConfiguration.class})
public class OneToManyMapTest extends AbstractMappingTest {

  @Test
  public void oneToManyMap() {
    Car car = new Car("K5");
    CarOption option1 = new CarOption("option1", 1);
    CarOption option2 = new CarOption("option2", 2);

    car.getCarOptions().put("op1", option1);
    car.getCarOptions().put("op2", option2);

    car.getOptions().put("stringOp1", "Value1");
    car.getOptions().put("stringOp2", "Value2");
    car.getOptions().put("stringOp3", "Value3");

    em.persist(car);
    em.flush();
    em.clear();

    Car loaded = em.find(Car.class, car.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getCarOptions()).hasSize(2);
    assertThat(loaded.getOptions()).hasSize(3);

    loaded.getOptions().remove("stringOp1");
    em.persist(loaded);
    em.flush();
    em.clear();

    loaded = em.find(Car.class, car.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getCarOptions()).hasSize(2);
    assertThat(loaded.getOptions()).hasSize(2);

    em.remove(loaded);
    em.flush();

    assertThat(em.find(Car.class, car.getId())).isNull();
  }
}
