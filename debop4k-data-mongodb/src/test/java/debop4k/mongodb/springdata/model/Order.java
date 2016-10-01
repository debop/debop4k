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

package debop4k.mongodb.springdata.model;

import debop4k.mongodb.AbstractMongoDocument;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Document
@Getter
@Setter
public class Order extends AbstractMongoDocument {

  @DBRef
  private Customer customer;

  private Address shippingAddress;
  private Address billingAddress;

  private Set<LineItem> lineItems = new HashSet<LineItem>();

  public Order() {}

  public Order(@NonNull Customer customer, Address shippingAddress, Address billingAddress) {
    this.customer = customer;
    this.shippingAddress = shippingAddress;
    this.billingAddress = billingAddress;
  }

  public void addItem(LineItem item) {
    lineItems.add(item);
  }

  public BigDecimal getTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (LineItem item : lineItems) {
      total.add(item.getTotal());
    }
    return total;
  }

  private static final long serialVersionUID = 562668807953484829L;
}
