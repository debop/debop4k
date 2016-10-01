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

package debop4k.data.orm.mapping.inheritance.joinedsubclass;


import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "JoinedSubclass_Customer")
@Table(indexes = {
    @Index(name = "ix_joined_subclass_customer_mobile", columnList = "mobile")
})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Customer extends Person {

  public Customer(String name, String ssn, String mobile) {
    super(name, ssn);
    this.mobile = mobile;
  }

  @Column(nullable = false, length = 64)
  private String mobile;

  @ManyToOne
  @JoinColumn(name = "contactEmployeeId", nullable = false)
  private Employee contactEmployee;

  @Override
  public int hashCode() {
    return Hashx.compute(super.hashCode(), mobile);
  }

  private static final long serialVersionUID = -7863964715939617205L;
}
