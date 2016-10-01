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
import debop4k.data.orm.model.AbstractHibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;


/**
 * EmbeddedId 형태의 Composite Id 를 가지는 엔티티입니다.
 */
@Entity(name = "CompositeId_OrderDetail")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail extends AbstractHibernateEntity<OrderDetailIdentifier> {

  public OrderDetail(Order order, Product product) {
    this(new OrderDetailIdentifier(order, product));
  }

  public OrderDetail(OrderDetailIdentifier identifier) {
    this.id = identifier;
    identifier.getOrder().getOrderDetails().add(this);
  }

  @EmbeddedId
  OrderDetailIdentifier id;

  private BigDecimal unitPrice;
  private Integer quantity;
  private Float discount;

  @Override
  public int hashCode() {
    return Hashx.compute(unitPrice, quantity, discount);
  }

  private static final long serialVersionUID = 3812828508031242597L;
}
