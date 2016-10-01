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

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ManyToMany_BankAccount")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class BankAccount extends IntEntity {

  public BankAccount(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  private String accountNumber;

  // NOTE: @ManyToMany 에서는 둘 중 하나는 mappedBy 를 지정해야 합니다.
  @ManyToMany(mappedBy = "bankAccounts")
  private Set<AccountOwner> owners = new HashSet<AccountOwner>();


  @Override
  public int hashCode() {
    return Hashx.compute(accountNumber);
  }

  private static final long serialVersionUID = 3314427190678030303L;
}
