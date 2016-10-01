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

package debop4k.data.orm.springdata.multipleds;

import debop4k.data.orm.springdata.multipleds.customer.Customer;
import debop4k.data.orm.springdata.multipleds.customer.CustomerRepository;
import debop4k.data.orm.springdata.multipleds.order.LineItem;
import debop4k.data.orm.springdata.multipleds.order.Order;
import debop4k.data.orm.springdata.multipleds.order.OrderRepository;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Component
public class DataInitializer {

  @Inject private CustomerRepository customerRepo;
  @Inject private OrderRepository orderRepo;

  @Transactional("customerTransactionManager")
  public Integer initializeCustomer() {
    return customerRepo.save(new Customer("Sunghyouk", "Bae")).getId();
  }


  @Transactional("orderTransactionManager")
  public Order initializeOrder(@NonNull Integer customerId) {
    Order order = new Order("order-1", customerId);

    order.addLineItem(new LineItem("Car"));
    order.addLineItem(new LineItem("TV"));

    return orderRepo.save(order);
  }
}
