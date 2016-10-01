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

package debop4k.mongodb.springdata;

import debop4k.mongodb.AbstractMongoTest;
import debop4k.mongodb.springdata.core.CustomerRepository;
import debop4k.mongodb.springdata.core.ProductRepository;
import debop4k.mongodb.springdata.model.*;
import debop4k.mongodb.springdata.order.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringDataMongoConfiguration.class})
public class SpringDataMongoConfigurationTest extends AbstractMongoTest {

  @Inject ApplicationContext context;

  @Inject protected OrderRepository orderRepo;
  @Inject protected CustomerRepository customerRepo;
  @Inject protected ProductRepository productRepo;

  @Inject protected MongoTemplate mongoTemplate;


  @Test
  public void bootstrap() {
    assertThat(context).isNotNull();
    assertThat(mongoTemplate).isNotNull();
  }

  protected void setup() {

    // Customer 삭제
    // NOTE: drop collection 을 수행하면 기존 index 도 다 삭제된다. 이 것은 되도록 하지 말아야 한다.
    // mongoTemplate.dropCollection(Customer.class);
    customerRepo.deleteAll();

    Address address = new Address("성북구 정릉1동", "서울", "한국");
    Customer customer = new Customer("성혁", "배");
    customer.setEmailAddress(new EmailAddress("debop@kesti.co.kr"));
    customer.add(address);
    customerRepo.save(customer);

    // Product
    // NOTE: drop collection 을 수행하면 기존 index 도 다 삭제된다. 이 것은 되도록 하지 말아야 한다.
    // mongoTemplate.dropCollection(Product.class);
    productRepo.deleteAll();

    Product ipad = new Product("iPad", new BigDecimal(499.0));
    ipad.setDescription("Apple tablet device");
    ipad.setAttribute("connector", "plug");

    Product macbook = new Product("MacBook Pro", new BigDecimal(1299.0));
    macbook.setDescription("Apple notebool");

    Product dock = new Product("Dock", new BigDecimal(49.0));
    dock.setDescription("Dock for iPhone/iPad");
    dock.setAttribute("connector", "plug");

    productRepo.save(Arrays.asList(ipad, macbook, dock));

    // Order / LineItem
    orderRepo.deleteAll();

    LineItem ipadLineItem = new LineItem(ipad, 2);
    LineItem macbookLineItem = new LineItem(macbook, 1);

    Order order = new Order(customer, address, address);
    order.addItem(ipadLineItem);
    order.addItem(macbookLineItem);

    orderRepo.save(order);

  }
}
