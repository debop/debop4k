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

package debop4k.data.orm.mapping.compositeid.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.collections.impl.factory.Lists;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity(name = "CompositeId_Order")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter
@Setter
public class Order extends IntEntity {

  public Order(String number) {
    this.number = number;
  }

  public Order(String number, Date orderDate) {
    this.number = number;
    this.orderDate = orderDate;
  }

  String number;

  @Temporal(TemporalType.DATE)
  private Date orderDate;

  @OneToMany(mappedBy = "id.order", cascade = {CascadeType.ALL}, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.EXTRA)
  List<OrderDetail> orderDetails = Lists.mutable.of();

  @Override
  public int hashCode() {
    return Hashx.compute(number);
  }

  private static final long serialVersionUID = -4205720116946041713L;
}
