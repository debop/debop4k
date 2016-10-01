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

package debop4k.mongodb.springdata.core;

import debop4k.mongodb.springdata.SpringDataMongoConfigurationTest;
import debop4k.mongodb.springdata.model.Address;
import debop4k.mongodb.springdata.model.Customer;
import debop4k.mongodb.springdata.model.EmailAddress;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CustomerRepositoryTest extends SpringDataMongoConfigurationTest {

  @Before
  public void setup() {
    super.setup();
  }

  @Test
  public void configurationTest() {
    assertThat(customerRepo).isNotNull();
  }

  @Test
  public void saveCustomer() {
    EmailAddress email = new EmailAddress("sunghyouk.bae@gmail.com");
    Customer debop = new Customer("Sunghyouk", "Bae");
    debop.setEmailAddress(email);
    debop.add(new Address("성북구 정릉1동", "서울", "한국"));

    Customer saved = customerRepo.save(debop);
    log.debug("saved customer={}", saved);
    assertThat(saved.getId()).isNotNull();
  }

  @Test
  public void loadByEmail() {
    EmailAddress email = new EmailAddress("sunghyouk.bae@gmail.com");
    Customer debop = new Customer("Sunghyouk", "Bae");
    debop.setEmailAddress(email);
    customerRepo.save(debop);

    Customer loaded = customerRepo.findByEmailAddress(email);
    assertThat(loaded).isEqualTo(debop);
  }

  @Test(expected = DuplicateKeyException.class)
  public void preventDuplicatedEmail() {
    EmailAddress email = new EmailAddress("sunghyouk.bae@gmail.com");
    Customer debop = new Customer("Sunghyouk", "Bae");
    debop.setEmailAddress(email);
    customerRepo.save(debop);

    Customer dup = new Customer("Dup", "Email");
    dup.setEmailAddress(email);

    // NOTE: DuplicateKeyException 을 발생시켜야 합니다.
    customerRepo.save(dup);
  }
}
