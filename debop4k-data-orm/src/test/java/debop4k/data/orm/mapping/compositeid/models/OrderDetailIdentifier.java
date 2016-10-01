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

import debop4k.core.AbstractValueObject;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NonNull;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * {@link OrderDetail} 의 Identifier
 */
@Embeddable
@Getter
public class OrderDetailIdentifier extends AbstractValueObject {

  protected OrderDetailIdentifier() {}

  public OrderDetailIdentifier(@NonNull Order order, @NonNull Product product) {
    this.order = order;
    this.product = product;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "orderId", nullable = false)
  @LazyToOne(LazyToOneOption.PROXY)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "productId", nullable = false)
  @LazyToOne(LazyToOneOption.PROXY)
  private Product product;

  @Override
  public int hashCode() {
    return Hashx.compute(order, product);
  }

  private static final long serialVersionUID = -6216492245032975726L;
}
