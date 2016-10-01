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


import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "ManyToMany_AccountOwner")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class AccountOwner extends IntEntity {

  public AccountOwner(String ssn) {
    this.SSN = ssn;
  }

  private String SSN;

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name = "BankAccount_Owner",
             joinColumns = {@JoinColumn(name = "ownerId")},
             inverseJoinColumns = {@JoinColumn(name = "accountId")})
  @LazyCollection(LazyCollectionOption.EXTRA)
  private Set<BankAccount> bankAccounts = new HashSet<BankAccount>();


  public void addBankAccounts(BankAccount... accounts) {
    for (BankAccount account : accounts) {
      bankAccounts.add(account);
      account.getOwners().add(this);
    }
  }

  public void removeBankAccounts(BankAccount... accounts) {
    for (BankAccount account : accounts) {
      bankAccounts.remove(account);
      account.getOwners().remove(this);
    }
  }

  @Override
  public int hashCode() {
    return Hashx.compute(SSN);
  }

  private static final long serialVersionUID = -6770683863618343947L;
}
