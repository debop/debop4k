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

import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.mongodb.AbstractMongoDocument;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.eclipse.collections.impl.factory.Maps;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Document
@Getter
@Setter
public class Product extends AbstractMongoDocument {

  @Indexed(background = true)
  private String name;

  private String description;
  private BigDecimal price;
  private Map<String, String> attributes = Maps.mutable.of();

  public Product() {}

  public Product(String name, BigDecimal price) {
    this(name, price, "");
  }

  public Product(@NonNull String name, BigDecimal price, String description) {
    this.name = name;
    this.price = price;
    this.description = description;
  }

  public void setAttribute(String name, String value) {
    if (value == null) attributes.remove(name);
    else attributes.put(name, value);
  }

  @Override
  public int hashCode() {
    return Hashx.compute(name, price);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("name", name)
                .add("price", price)
                .add("description", description);
  }

  private static final long serialVersionUID = 6528586438981753409L;
}
