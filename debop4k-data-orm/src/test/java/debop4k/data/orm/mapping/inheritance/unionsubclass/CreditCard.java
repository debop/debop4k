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

package debop4k.data.orm.mapping.inheritance.unionsubclass;

import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "UnionSubclass_CreditCard")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class CreditCard extends BillingBase {

  public CreditCard(@NonNull String owner) {
    super(owner);
  }

  // Subclass 방식과는 달리, 고유의 속성에 대한 not null 정의가 가능하다.
  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String number;

  private Integer expMonth;
  private Integer expYear;

  @Temporal(TemporalType.DATE)
  private Date startDate;

  @Temporal(TemporalType.DATE)
  private Date endDate;

  private String swift;

  @Override
  public int hashCode() {
    return Hashx.compute(super.hashCode(), number);
  }

  private static final long serialVersionUID = -1490845170116016672L;
}
