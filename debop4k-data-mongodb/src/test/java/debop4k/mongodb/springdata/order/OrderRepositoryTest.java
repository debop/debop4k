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

package debop4k.mongodb.springdata.order;

import debop4k.mongodb.springdata.SpringDataMongoConfigurationTest;
import debop4k.mongodb.springdata.model.Customer;
import debop4k.mongodb.springdata.model.EmailAddress;
import debop4k.mongodb.springdata.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class OrderRepositoryTest extends SpringDataMongoConfigurationTest {

  @Before
  public void setup() {
    super.setup();
  }

  @Test
  public void findByCustomer() {
    Customer customer = customerRepo.findByEmailAddress(new EmailAddress("debop@kesti.co.kr"));
    assertThat(customer).isNotNull();

    // find order by customer
    List<Order> orders = orderRepo.findByCustomer(customer);

    assertThat(orders.size()).isGreaterThan(0);
    assertThat(orders.get(0).getCustomer()).isEqualTo(customer);

  }
}
