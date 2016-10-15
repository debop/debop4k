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

package debop4k.data.orm.springdata.multipleds.customer;///*
// * Copyright 2015-2020 KESTI
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.data.orm.springdata.multipleds.customer;
//
//import debop4k.data.orm.springdata.multipleds.Application;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.inject.Inject;
//
//import static org.assertj.core.api.Assertionx.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { Application.class })
//@Transactional(transactionManager = "customerTransactionManager")
//public class CustomerTest {
//
//  @Inject CustomerRepository customerRepo;
////  @Resource(name="customerEntityManagerFactory") EntityManager em;
//
//  @Before
//  public void setup() {
//    customerRepo.deleteAll();
//    customerRepo.save(new Customer("Sunghyouk", "Bae"));
//  }
//
//  @Test
//  public void findCustomerByLastname() {
//    Customer result = customerRepo.findByLastname("Bae");
//
//    assertThat(result).isNotNull();
//    assertThat(result.getFirstname()).isEqualTo("Sunghyouk");
//  }
//}
