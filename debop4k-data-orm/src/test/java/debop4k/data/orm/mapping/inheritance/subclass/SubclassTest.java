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

package debop4k.data.orm.mapping.inheritance.subclass;

import debop4k.data.orm.mapping.AbstractMappingTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SubclassConfiguration.class})
public class SubclassTest extends AbstractMappingTest {

  @Test
  public void subclass() {
    BankAccount bankAccount = new BankAccount("debop");
    bankAccount.setAccount("account");
    bankAccount.setBankname("국민");
    em.persist(bankAccount);

    CreditCard card = new CreditCard("debop");
    card.setNumber("1111-1111-1111-1111");
    card.setCompanyName("신한");
    card.setExpYear(2020);
    card.setExpMonth(12);
    em.persist(card);

    em.flush();
    em.clear();

    BankAccount account1 = em.find(BankAccount.class, bankAccount.getId());
    assertThat(account1).isNotNull().isEqualTo(bankAccount);

    CreditCard card1 = em.find(CreditCard.class, card.getId());
    assertThat(card1).isNotNull().isEqualTo(card);

    em.remove(account1);
    em.remove(card1);
    em.flush();
    em.clear();

    assertThat(em.find(BankAccount.class, bankAccount.getId())).isNull();
    assertThat(em.find(CreditCard.class, card.getId())).isNull();

  }
}
