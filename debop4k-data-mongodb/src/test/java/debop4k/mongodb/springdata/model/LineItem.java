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

import debop4k.core.AbstractValueObject;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

@Getter
@Setter
public class LineItem extends AbstractValueObject {

  @DBRef
  private Product product;
  private int amount;
  private BigDecimal price;

  public LineItem(Product product, int amount) {
    this.product = product;
    this.amount = amount;
    this.price = product.getPrice();
  }

  public BigDecimal getTotal() {
    return price.multiply(BigDecimal.valueOf(amount));
  }

  @Override
  public int hashCode() {
    return Hashx.compute(product);
  }

  private static final long serialVersionUID = 2743787198238570294L;
}
