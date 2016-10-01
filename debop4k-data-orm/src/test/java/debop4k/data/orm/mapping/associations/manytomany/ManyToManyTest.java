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

package debop4k.data.orm.mapping.associations.manytomany;

import debop4k.data.orm.mapping.AbstractMappingTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ManyToManyConfiguration.class})
public class ManyToManyTest extends AbstractMappingTest {

  @Test
  public void manyToMany() {
    AccountOwner owner = new AccountOwner("0123456");
    BankAccount account = new BankAccount("001-01-123456-01");

    owner.addBankAccounts(account);

    // mappedBy가 AccountOwner 로 설정되었다는 것은 AccountOwner를 기준으로 cascading 이 된다는 뜻이다.
    em.persist(owner); // account 는 자동으로 저장된다 (em.persist(account) 를 할 필요가 없다)
    em.flush();
    em.clear();

    BankAccount account2 = em.find(BankAccount.class, account.getId());
    assertThat(account2).isNotNull().isEqualTo(account);
    assertThat(account2.getOwners()).hasSize(1).containsOnly(owner);

    em.clear();

    AccountOwner owner2 = em.find(AccountOwner.class, owner.getId());
    assertThat(owner2.getBankAccounts()).hasSize(1).containsOnly(account);

    owner2.removeBankAccounts(account2);

    BankAccount barclays = new BankAccount("222-999");
    owner2.addBankAccounts(barclays);

    em.persist(owner2);
    em.flush();
    em.clear();

    AccountOwner owner3 = em.find(AccountOwner.class, owner.getId());
    assertThat(owner3.getBankAccounts()).hasSize(1).containsOnly(barclays);

    BankAccount barclays2 = owner3.getBankAccounts().iterator().next();
    barclays2.getOwners().clear();
    owner3.getBankAccounts().clear();

    em.persist(owner3);
    em.persist(barclays2);
    em.flush();
  }
}
