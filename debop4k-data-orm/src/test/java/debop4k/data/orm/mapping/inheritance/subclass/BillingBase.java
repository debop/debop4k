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

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * 한 테이블에 Super-Sub class 들 모두 저장됩니다. (subclass)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "Subclass_Billing",
       indexes = {
           @Index(name = "ix_subclass_billing_owner", columnList = "owner")
       })
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public abstract class BillingBase extends IntEntity {

  public BillingBase(@NonNull String owner) {
    this.owner = owner;
  }

  @Column(nullable = false)
  protected String owner;

  @Override
  public int hashCode() {
    return Hashx.compute(owner);
  }

  private static final long serialVersionUID = 136009748117080899L;
}
