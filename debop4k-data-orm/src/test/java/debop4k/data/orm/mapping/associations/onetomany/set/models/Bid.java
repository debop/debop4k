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

package debop4k.data.orm.mapping.associations.onetomany.set.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "OneToMany_Bid")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Bid extends IntEntity {

  public Bid(BigDecimal amount) {
    this.amount = amount;
  }

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "itemId", nullable = false)
  private BiddingItem item;

  @Column(nullable = false)
  private BigDecimal amount;

  @Transient
  private Timestamp timestamp;

  @Override
  public int hashCode() {
    return Hashx.compute(amount);
  }

  private static final long serialVersionUID = -4786809922793806427L;
}
